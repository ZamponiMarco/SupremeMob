package com.github.jummes.suprememob.wrapper.v1_17_R1;

import com.github.jummes.suprememob.wrapper.TargetSelectorUtils;
import lombok.SneakyThrows;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.ai.goal.PathfinderGoalWrapped;
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalNearestAttackableTarget;
import org.apache.commons.lang.reflect.FieldUtils;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;

import java.util.Set;
import java.util.function.Predicate;

import static com.github.jummes.suprememob.wrapper.v1_17_R1.Utils_v1_17_R1.getNMSEntityType;
import static com.github.jummes.suprememob.wrapper.v1_17_R1.Utils_v1_17_R1.nextId;

public class TargetSelectorUtils_v1_17_R1 implements TargetSelectorUtils {

    @Override
    public void setNearestEntityTarget(Mob mob, EntityType type, Predicate<LivingEntity> entityPredicate, int randomInterval, boolean mustSee, boolean mustReach) {
        EntityInsentient entity = (EntityInsentient) ((CraftEntity) mob).getHandle();
        Predicate<EntityLiving> p = entityLiving -> entityPredicate.test((LivingEntity) entityLiving.getBukkitEntity());
        entity.bP.a(nextId(entity.bP.d()), new PathfinderGoalNearestAttackableTarget<>(entity,
                getNMSEntityType(mob, type), randomInterval, mustSee, mustReach, p));
    }

    @SneakyThrows
    @Override
    public void clearEntityTargets(Mob mob) {
        EntityInsentient entity = (EntityInsentient) ((CraftEntity) mob).getHandle();
        Set<PathfinderGoalWrapped> targets = (Set<PathfinderGoalWrapped>) FieldUtils.readField(FieldUtils.
                        getDeclaredField(entity.bP.getClass(), "d", true), entity.bP,
                true);
        targets.clear();
    }
}
