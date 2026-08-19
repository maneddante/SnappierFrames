package dog.dante.snappierframes.entity;

import net.minecraft.core.BlockPos;

public interface BlockSupportedEntity {
    double snappierFrames$getOffset();
    void snappierFrames$setOffset(double offset);
    BlockPos snappierFrames$getBlockPos();
}
