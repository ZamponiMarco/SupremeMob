package com.github.jummes.suprememob.wrapper.v1_16_R1;

import com.github.jummes.suprememob.wrapper.TargetSelectorUtils;
import lombok.SneakyThrows;
import net.minecraft.server.v1_16_R1.*;
import org.apache.commons.lang.reflect.FieldUtils;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;

import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static com.github.jummes.suprememob.wrapper.v1_16_R1.Utils_v1_16_R1.getNMSEntityType;
import static com.github.jummes.suprememob.wrapper.v1_16_R1.Utils_v1_16_R1.nextId;

public class TargetSelectorUtils_v1_16_R1 implements TargetSelectorUtils {

    @Override
    public void setNearestEntityTarget(Mob mob, EntityType type, Predicate<LivingEntity> entityPredicate, int randomInterval, boolean mustSee, boolean mustReach) {
        EntityInsentient entity = (EntityInsentient) ((CraftEntity) mob).getHandle();
        Predicate<EntityLiving> p = entityLiving -> entityPredicate.test((LivingEntity) entityLiving.getBukkitEntity());
        entity.targetSelector.a(nextId(entity.targetSelector.d()), new PathfinderGoalNearestAttackableTarget<>(entity,
                getNMSEntityType(mob, type), randomInterval, mustSee, mustReach, p));
    }

    @SneakyThrows
    @Override
    public void clearEntityTargets(Mob mob) {
        EntityInsentient entity = (EntityInsentient) ((CraftEntity) mob).getHandle();
        Set<PathfinderGoalWrapped> targets = (Set<PathfinderGoalWrapped>) FieldUtils.readField(FieldUtils.
                        getDeclaredField(entity.targetSelector.getClass(), "d", true), entity.targetSelector,
                true);
        targets.clear();
    }
}
