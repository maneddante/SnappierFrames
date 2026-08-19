package dog.dante.snappierframes.mixin;

import dog.dante.snappierframes.entity.BlockSupportedEntity;
import dog.dante.snappierframes.math.OffsetHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.entity.decoration.BlockAttachedEntity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockAttachedEntity.class)
public abstract class MixinBlockAttachedEntity {
    @Shadow
    protected abstract void recalculateBoundingBox();

    @Unique
    private BlockState snappierFrames$lastState = null;

    @Inject(method = "tick", at = @At("HEAD"))
    private void snappierFrames$tick(final CallbackInfo ci) { // null-gate immediately after instantiation for max performance
        if (!((Object) this instanceof ItemFrame self)) {
            return;
        }

        final BlockPos blockPos = ((BlockSupportedEntity) self).snappierFrames$getBlockPos();

        if (blockPos == null) {
            return;
        }

        final Level level = self.level();

        if (!level.hasChunk(SectionPos.blockToSectionCoord(blockPos.getX()), SectionPos.blockToSectionCoord(blockPos.getZ()))) {
            return;
        }

        final BlockState state = level.getBlockState(blockPos);

        if (state != snappierFrames$lastState) {
            snappierFrames$lastState = state;
            ((BlockSupportedEntity) self).snappierFrames$setOffset(OffsetHelper.calculateOffset(level, blockPos, self.getDirection()));
            recalculateBoundingBox();
        }
    }
}
