package com.github.jummes.suprememob.wrapper.v1_14_R1;

import net.minecraft.server.v1_14_R1.EntityHuman;
import net.minecraft.server.v1_14_R1.EntityLiving;
import net.minecraft.server.v1_14_R1.PathfinderGoalWrapped;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;

import java.util.stream.Stream;

public class Utils_v1_14_R1 {

    static Class<? extends EntityLiving> getNMSEntityType(Mob mob, EntityType type) {
        Class<? extends EntityLiving> targetClass;
        if (type.equals(EntityType.PLAYER)) {
            targetClass = EntityHuman.class;
        } else {
            LivingEntity e = (LivingEntity) mob.getWorld().spawnEntity(new Location(mob.getWorld(), 0, -100, 0), type);
            targetClass = (Class<EntityLiving>) ((CraftEntity) e).getHandle().getClass();
            e.remove();
        }
        return targetClass;
    }

    public static int nextId(Stream<PathfinderGoalWrapped> goals) {
        return goals.map(PathfinderGoalWrapped::h).max(Integer::compareTo).
                orElse(0) + 1;
    }
}
