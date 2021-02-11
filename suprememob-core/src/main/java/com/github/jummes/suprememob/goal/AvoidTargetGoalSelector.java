package com.github.jummes.suprememob.goal;

import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.condition.Condition;
import com.github.jummes.supremeitem.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.libs.annotation.Serializable;
import com.github.jummes.supremeitem.libs.model.ModelPath;
import com.github.jummes.supremeitem.libs.util.MapperUtils;
import com.github.jummes.supremeitem.value.NumericValue;
import com.github.jummes.suprememob.SupremeMob;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Enumerable.Child
@Enumerable.Displayable(name = "&6&lAvoid target &cgoal selector", description = "gui.goal-selector.avoid.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjI2ZDk1YTBhY2I0MjI0YWY0ODE4ZGI2NzBiMzZlNWYyMDE5MmE4OWVmYjk2ZmE1YzJiZjBjN2U0M2YyZDdmIn19fQ==")
public class AvoidTargetGoalSelector extends ConditionalGoalSelector {

    private static final NumericValue DISTANCE_DEFAULT = new NumericValue(10);
    private static final NumericValue WALK_DEFAULT = new NumericValue(1);
    private static final NumericValue SPRINT_DEFAULT = new NumericValue(1);

    private static final String DISTANCE_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTI0MjMwMmViZDY1NWY2ZDQyOWMxZTRhZWRlMjFiN2Y1YzRkYjY4YTQwNDVlYmFlYzE3NjMzYTA1MGExYTEifX19";
    private static final String SPRINT_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzJhN2RjYmY3ZWNhNmI2ZjYzODY1OTFkMjM3OTkxY2ExYjg4OGE0ZjBjNzUzZmY5YTMyMDJjZjBlOTIyMjllMyJ9fX0=";

    @Serializable(displayItem = "typeHead", description = "gui.goal-selector.avoid.entity", stringValue = true, fromList = "getSpawnableEntities", fromListMapper = "spawnableEntitiesMapper")
    private EntityType type;
    @Serializable(headTexture = DISTANCE_HEAD, description = "gui.goal-selector.avoid.distance")
    @Serializable.Number(minValue = 0, scale = 1)
    private NumericValue maxDistance;
    @Serializable(headTexture = SPEED_HEAD, description = "gui.goal-selector.speed")
    private NumericValue walkSpeedModifier;
    @Serializable(headTexture = SPRINT_HEAD, description = "gui.goal-selector.avoid.sprint")
    private NumericValue sprintSpeedModifier;

    public AvoidTargetGoalSelector() {
        this(CONDITION_DEFAULT.clone(), EntityType.PLAYER, DISTANCE_DEFAULT.clone(), WALK_DEFAULT.clone(),
                SPRINT_DEFAULT.clone());
    }

    public AvoidTargetGoalSelector(Map<String, Object> map) {
        super(map);
        this.type = EntityType.valueOf((String) map.getOrDefault("type", "PLAYER"));
        this.maxDistance = (NumericValue) map.getOrDefault("maxDistance", DISTANCE_DEFAULT.clone());
        this.walkSpeedModifier = (NumericValue) map.getOrDefault("walkSpeedModifier", WALK_DEFAULT.clone());
        this.sprintSpeedModifier = (NumericValue) map.getOrDefault("sprintSpeedModifier", SPRINT_DEFAULT.clone());
    }

    public AvoidTargetGoalSelector(Condition condition, EntityType type, NumericValue maxDistance,
                                   NumericValue walkSpeedModifier, NumericValue sprintSpeedModifier) {
        super(condition);
        this.type = type;
        this.maxDistance = maxDistance;
        this.walkSpeedModifier = walkSpeedModifier;
        this.sprintSpeedModifier = sprintSpeedModifier;
    }

    public static List<Object> getSpawnableEntities(ModelPath path) {
        return Arrays.stream(EntityType.values()).filter(EntityType::isAlive).collect(Collectors.toList());
    }

    public static Function<Object, ItemStack> spawnableEntitiesMapper() {
        return MapperUtils.getEntityTypeMapper();
    }

    public ItemStack typeHead() {
        return MapperUtils.getEntityTypeMapper().apply(type);
    }

    @Override
    public void applyToEntity(Mob e, Source source, Target target) {
        SupremeMob.getInstance().getWrapper().getGoalSelector().setAvoidTarget(e, type, maxDistance.
                        getRealValue(target, source).floatValue(), walkSpeedModifier.getRealValue(target, source),
                sprintSpeedModifier.getRealValue(target, source), condition);
    }

    @Override
    public String getName() {
        return "&6&lAvoid target &cgoal selector";
    }
}
