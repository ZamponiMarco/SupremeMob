package com.github.jummes.suprememob.goal;

import com.destroystokyo.paper.entity.ai.Goal;
import com.destroystokyo.paper.entity.ai.GoalKey;
import com.destroystokyo.paper.entity.ai.GoalType;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.condition.Condition;
import com.github.jummes.supremeitem.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.libs.annotation.Serializable;
import com.github.jummes.supremeitem.value.NumericValue;
import com.github.jummes.suprememob.goal.impl.CustomAvoidTargetGoal;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.Map;

@Enumerable.Child
@Enumerable.Displayable(name = "&6&lAvoid target &cgoal selector", description = "gui.goal-selector.avoid.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjI2ZDk1YTBhY2I0MjI0YWY0ODE4ZGI2NzBiMzZlNWYyMDE5MmE4OWVmYjk2ZmE1YzJiZjBjN2U0M2YyZDdmIn19fQ==")
public class AvoidTargetGoalSelector extends ConditionalGoalSelector {

    private static final NumericValue WALK_DEFAULT = new NumericValue(1);
    private static final NumericValue SPRINT_DEFAULT = new NumericValue(1);

    private static final String SPRINT_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzJhN2RjYmY3ZWNhNmI2ZjYzODY1OTFkMjM3OTkxY2ExYjg4OGE0ZjBjNzUzZmY5YTMyMDJjZjBlOTIyMjllMyJ9fX0=";

    @Serializable(displayItem = "typeHead", description = "gui.goal-selector.avoid.entity", stringValue = true, fromList = "getSpawnableEntities", fromListMapper = "spawnableEntitiesMapper")
    private EntityType type;
    @Serializable(headTexture = SPEED_HEAD, description = "gui.goal-selector.speed")
    private NumericValue walkSpeedModifier;
    @Serializable(headTexture = SPRINT_HEAD, description = "gui.goal-selector.avoid.sprint")
    private NumericValue sprintSpeedModifier;

    public AvoidTargetGoalSelector() {
        this(CONDITION_DEFAULT.clone(), EntityType.PLAYER, WALK_DEFAULT.clone(),
                SPRINT_DEFAULT.clone());
    }

    public AvoidTargetGoalSelector(Map<String, Object> map) {
        super(map);
        this.type = EntityType.valueOf((String) map.getOrDefault("type", "PLAYER"));
        this.walkSpeedModifier = (NumericValue) map.getOrDefault("walkSpeedModifier", WALK_DEFAULT.clone());
        this.sprintSpeedModifier = (NumericValue) map.getOrDefault("sprintSpeedModifier", SPRINT_DEFAULT.clone());
    }

    public AvoidTargetGoalSelector(Condition condition, EntityType type,
                                   NumericValue walkSpeedModifier, NumericValue sprintSpeedModifier) {
        super(condition);
        this.type = type;
        this.walkSpeedModifier = walkSpeedModifier;
        this.sprintSpeedModifier = sprintSpeedModifier;
    }

    @Override
    public void applyToEntity(Mob e, Source source, Target target) {
        int currentSize = Bukkit.getMobGoals().getAllGoals(e).size();
        Bukkit.getMobGoals().addGoal(e, currentSize, new CustomAvoidTargetGoal(e, walkSpeedModifier.
                getRealValue(target, source), sprintSpeedModifier.getRealValue(target, source)));
    }

    @Override
    public String getName() {
        return "&6&lAvoid target &cgoal selector";
    }
}
