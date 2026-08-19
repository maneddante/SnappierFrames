package dog.dante.snappierframes.mixin;

import dog.dante.snappierframes.entity.BlockSupportedEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.decoration.ItemFrame;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemFrame.class)
public class MixinItemFrame implements BlockSupportedEntity {
    @Unique
    private double snappierFrames$offset;

    @Unique
    private BlockPos snappierFrames$blockPos;

    @Override
    public double snappierFrames$getOffset() {
        return snappierFrames$offset;
    }

    @Override
    public void snappierFrames$setOffset(double offset) {
        snappierFrames$offset = offset;
    }

    @Override
    public BlockPos snappierFrames$getBlockPos() {
        return snappierFrames$blockPos;
    }

    @Inject(method = "recalculateBoundingBox", at = @At("TAIL"))
    private void snappierFrames$recalculateBoundingBox(CallbackInfo ci) {
        final ItemFrame self = ((ItemFrame) (Object) this);
        Direction direction = self.getDirection();

        if (direction == null) {
            return; // unreachable in vanilla but mods/updates could expose and the bug is lwk nasty
        }

        snappierFrames$blockPos = self.getPos().relative(direction.getOpposite());

        if (snappierFrames$offset <= 0.0D) {
            return;
        }

        self.setBoundingBox(self.getBoundingBox().move(direction.getUnitVec3().scale(-snappierFrames$offset)));
    }
}