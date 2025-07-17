package com.awesoft.cclink.libs;

import dan200.computercraft.api.turtle.ITurtleAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class RaycastingUtil {

    public static Optional<Entity> raycastEntity(Player player, double distance) {
        Level world = player.level();
        Vec3 startVec = player.getEyePosition();
        Vec3 lookVec = player.getViewVector(1.0F).scale(distance);
        Vec3 endVec = startVec.add(lookVec);


        BlockHitResult blockHitResult = world.clip(new ClipContext(
                startVec,
                endVec,
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                player
        ));


        double blockHitDistance = blockHitResult.getLocation().distanceTo(startVec);
        if (blockHitDistance < distance) {
            endVec = blockHitResult.getLocation();
        }


        AABB boundingBox = new AABB(startVec, endVec).inflate(1.0D);
        Entity closestEntity = null;
        double closestDistance = distance;

        for (Entity entity : world.getEntities(player, boundingBox)) {
            if (entity == player || !entity.isPickable()) continue;
            AABB entityBoundingBox = entity.getBoundingBox().inflate(0.3D);
            Optional<Vec3> intersection = entityBoundingBox.clip(startVec, endVec);

            if (intersection.isPresent()) {
                double entityDistance = startVec.distanceTo(intersection.get());
                if (entityDistance < closestDistance) {
                    closestEntity = entity;
                    closestDistance = entityDistance;
                }
            }
        }

        return Optional.ofNullable(closestEntity);
    }

    public static Map<String, Object> raycast(double reach, ITurtleAccess turtle, Direction dir) {

        double limitedReach = Math.min(5, Math.max(1, reach));

        BlockPos pos = turtle.getPosition();

        Level level = turtle.getLevel();


        Vec3 direction = Vec3.atLowerCornerOf(dir.getNormal());

        Vec3 start = Vec3.atCenterOf(pos).add(direction.scale(0.5));

        Vec3 end = start.add(direction.scale(limitedReach));



        BlockHitResult blockHit = level.clip(new ClipContext(
                start,
                end,
                ClipContext.Block.OUTLINE,
                ClipContext.Fluid.NONE,
                null
        ));

        Map<String, Object> info = new HashMap<>();
        if (blockHit.getType() == HitResult.Type.BLOCK) {

            BlockPos hitPos = blockHit.getBlockPos();

            Block blk = level.getBlockState(hitPos).getBlock();

            info.put("id", ForgeRegistries.BLOCKS.getKey(blk).toString());
            info.put("x", hitPos.getX());
            info.put("y", hitPos.getY());
            info.put("z", hitPos.getZ());
            info.put("distance",pos.getCenter().distanceTo(hitPos.getCenter()));
        }
        return info;
    }

}
