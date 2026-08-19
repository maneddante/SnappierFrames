package dog.dante.snappierframes.client.mixin;

import net.minecraft.client.renderer.entity.state.ItemFrameRenderState;
import dog.dante.snappierframes.client.state.BlockSupportedEntityState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ItemFrameRenderState.class)
public class MixinItemFrameRenderState implements BlockSupportedEntityState {
    @Unique
    private double snappierFrames$offset;

    @Override
    public double snappierFrames$getOffset() {
        return snappierFrames$offset;
    }

    @Override
    public void snappierFrames$setOffset(double offset) {
        snappierFrames$offset = offset;
    }
}