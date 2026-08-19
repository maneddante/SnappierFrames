package dog.dante.snappierframes.math;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.shapes.VoxelShape;

public final class OffsetHelper {
    private OffsetHelper() {}

    public static double calculateOffset(Level level, BlockPos blockPos, Direction direction) {
        if (!level.hasChunk(SectionPos.blockToSectionCoord(blockPos.getX()), SectionPos.blockToSectionCoord(blockPos.getZ()))) {
            return 0.0D;
        }

        VoxelShape supportShape = level.getBlockState(blockPos).getShape(level, blockPos);

        if (supportShape.isEmpty()) {
            return 0.0D;
        }

        Direction.Axis axis = direction.getAxis();
        double offset = supportShape.min(axis);

        if (direction.getAxisDirection() == Direction.AxisDirection.POSITIVE) {
            offset = 1.0D - supportShape.max(axis);
        }

        return Math.clamp(offset, 0.0D, 1.0D);
    }
}