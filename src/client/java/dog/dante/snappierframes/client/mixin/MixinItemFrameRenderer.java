package dog.dante.snappierframes.client.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import dog.dante.snappierframes.entity.BlockSupportedEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.ItemFrameRenderer;
import net.minecraft.client.renderer.entity.state.ItemFrameRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.entity.decoration.ItemFrame;
import dog.dante.snappierframes.client.state.BlockSupportedEntityState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemFrameRenderer.class)
public class MixinItemFrameRenderer<T extends ItemFrame> {
    @ModifyConstant(
            method = "submit(Lnet/minecraft/client/renderer/entity/state/ItemFrameRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V",
            slice = @Slice( // IDEK if we need this, I recently learned it and wanted to use lol
                    to = @At(value = "INVOKE", target = "Lnet/minecraft/core/Direction$Axis;isHorizontal()Z")
            ),
            constant = @Constant(doubleValue = 0.46875D) // 15/32
    )
    private double modifyOffset(double original, final ItemFrameRenderState state, final PoseStack poseStack, final SubmitNodeCollector submitNodeCollector, final CameraRenderState camera) {
        return original - ((BlockSupportedEntityState) state).snappierFrames$getOffset();
    }

    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/decoration/ItemFrame;Lnet/minecraft/client/renderer/entity/state/ItemFrameRenderState;F)V", at = @At("TAIL"))
    private void snappierFrames$extractRenderState(T entity, ItemFrameRenderState state, float partialTicks, CallbackInfo ci) {
        double offset = ((BlockSupportedEntity) entity).snappierFrames$getOffset();

        ((BlockSupportedEntityState) state).snappierFrames$setOffset(offset);

        if (state.nameTag != null && state.nameTagAttachment != null) {
            state.nameTagAttachment = state.nameTagAttachment.subtract(state.direction.getUnitVec3().scale(offset));
        }
    }
}
