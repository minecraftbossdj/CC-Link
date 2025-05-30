package com.awesoft.cclink.libs;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

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
}
