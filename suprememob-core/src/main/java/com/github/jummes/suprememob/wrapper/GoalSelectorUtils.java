package com.github.jummes.suprememob.wrapper;

import com.github.jummes.supremeitem.condition.Condition;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;

public interface GoalSelectorUtils {

    void setMeleeGoal(Mob mob, double speed, boolean following, Condition condition);

    void setAvoidTarget(Mob mob, EntityType type, float maxDist, double walkSpeedModifier,
                        double sprintSpeedModifier, Condition condition);

    void setRandomStrollLand(Mob mob, double speed, float probability);

    void clearEntityGoals(Mob mob);
}
