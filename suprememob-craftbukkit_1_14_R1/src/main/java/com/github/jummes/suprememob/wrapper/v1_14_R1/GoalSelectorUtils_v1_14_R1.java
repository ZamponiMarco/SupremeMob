package com.github.jummes.suprememob.wrapper.v1_14_R1;

import com.github.jummes.supremeitem.condition.Condition;
import com.github.jummes.suprememob.wrapper.GoalSelectorUtils;
import com.github.jummes.suprememob.wrapper.v1_14_R1.goal.CustomPathfinderGoalAvoidTarget;
import com.github.jummes.suprememob.wrapper.v1_14_R1.goal.CustomPathfinderGoalMeleeAttack;
import com.github.jummes.suprememob.wrapper.v1_14_R1.goal.CustomPathfinderGoalRandomStrollLand;
import lombok.SneakyThrows;
import net.minecraft.server.v1_14_R1.EntityCreature;
import net.minecraft.server.v1_14_R1.EntityInsentient;
import net.minecraft.server.v1_14_R1.PathfinderGoalRandomStrollLand;
import net.minecraft.server.v1_14_R1.PathfinderGoalWrapped;
import org.apache.commons.lang.reflect.FieldUtils;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;

import java.util.Set;

import static com.github.jummes.suprememob.wrapper.v1_14_R1.Utils_v1_14_R1.getNMSEntityType;
import static com.github.jummes.suprememob.wrapper.v1_14_R1.Utils_v1_14_R1.nextId;

public class GoalSelectorUtils_v1_14_R1 implements GoalSelectorUtils {

    @Override
    public void setMeleeGoal(Mob mob, double speed, boolean following, Condition condition) {
        EntityCreature entity = (EntityCreature) ((CraftEntity) mob).getHandle();
        entity.goalSelector.a(nextId(entity.goalSelector.c()), new CustomPathfinderGoalMeleeAttack(entity, speed,
                following, condition));
    }

    @Override
    public void setAvoidTarget(Mob mob, EntityType type, float maxDist, double walkSpeedModifier,
                               double sprintSpeedModifier, Condition condition) {
        EntityCreature entity = (EntityCreature) ((CraftEntity) mob).getHandle();
        entity.goalSelector.a(nextId(entity.goalSelector.c()), new CustomPathfinderGoalAvoidTarget<>(entity,
                getNMSEntityType(mob, type), maxDist, walkSpeedModifier, sprintSpeedModifier, condition));
    }

    @Override
    public void setRandomStrollLand(Mob mob, double speed, float probability, Condition canUse) {
        EntityCreature entity = (EntityCreature) ((CraftEntity) mob).getHandle();
        entity.goalSelector.a(nextId(entity.goalSelector.c()), new CustomPathfinderGoalRandomStrollLand(entity, speed,
                probability, canUse));
    }

    @SneakyThrows
    @Override
    public void clearEntityGoals(Mob mob) {
        EntityInsentient entity = (EntityInsentient) ((CraftEntity) mob).getHandle();
        Set<PathfinderGoalWrapped> goals = (Set<PathfinderGoalWrapped>) FieldUtils.readField(FieldUtils.
                        getDeclaredField(entity.goalSelector.getClass(), "d", true), entity.goalSelector,
                true);
        goals.clear();
    }
}
