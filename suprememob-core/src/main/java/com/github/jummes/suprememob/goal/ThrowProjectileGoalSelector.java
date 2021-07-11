package com.github.jummes.suprememob.goal;

import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.condition.Condition;
import com.github.jummes.supremeitem.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.libs.annotation.Serializable;
import com.github.jummes.supremeitem.libs.model.ModelPath;
import com.github.jummes.supremeitem.libs.util.MapperUtils;
import com.github.jummes.suprememob.goal.impl.CustomThrowProjectileTargetGoal;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


@Enumerable.Child
@Enumerable.Displayable(name = "&6&lProjectile &cgoal selector", description = "gui.goal-selector.projectile.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWRmZDc3MjRjNjlhMDI0ZGNmYzYwYjE2ZTAwMzM0YWI1NzM4ZjRhOTJiYWZiOGZiYzc2Y2YxNTMyMmVhMDI5MyJ9fX0=")
public class ThrowProjectileGoalSelector extends ConditionalGoalSelector{

    @Serializable(headTexture = SPEED_HEAD, description = "gui.goal-selector.projectile.projectile", stringValue = true,
            fromList = "getSpawnableEntities", fromListMapper = "spawnableEntitiesMapper")
    private EntityType projectile;

    public ThrowProjectileGoalSelector() {
        this(CONDITION_DEFAULT.clone(), EntityType.ARROW);
    }

    public ThrowProjectileGoalSelector(Condition condition, EntityType projectile) {
        super(condition);
        this.projectile = projectile;
    }

    public ThrowProjectileGoalSelector(Map<String, Object> map) {
        super(map);
        this.projectile = EntityType.valueOf((String) map.getOrDefault("projectile", "ARROW"));
    }

    @Override
    public void applyToEntity(Mob e, Source source, Target target) {
        int currentSize = Bukkit.getMobGoals().getAllGoals(e).size();
        Bukkit.getMobGoals().addGoal(e, currentSize, new CustomThrowProjectileTargetGoal(e, projectile));
    }

    public static List<Object> getSpawnableEntities(ModelPath path) {
        return Arrays.stream(EntityType.values()).filter(type -> type.getEntityClass() != null &&
                Projectile.class.isAssignableFrom(type.getEntityClass())).collect(Collectors.toList());
    }

    public static Function<Object, ItemStack> spawnableEntitiesMapper() {
        return MapperUtils.getEntityTypeMapper();
    }

    @Override
    public String getName() {
        return "&6&lProjectile &cgoal selector";
    }
}
