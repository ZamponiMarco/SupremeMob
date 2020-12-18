package com.github.jummes.suprememob.goal;

import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.libs.annotation.Enumerable;
import com.github.jummes.suprememob.SupremeMob;
import org.bukkit.entity.Mob;

import java.util.Map;

@Enumerable.Child
@Enumerable.Displayable(name = "&6&lClear &cgoal selector", description = "gui.goal-selector.clear.description",
        headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2VkMWFiYTczZjYzOWY0YmM0MmJkNDgxOTZjNzE1MTk3YmUyNzEyYzNiOTYyYzk3ZWJmOWU5ZWQ4ZWZhMDI1In19fQ==")
public class ClearGoalSelector extends GoalSelector{

    public ClearGoalSelector() {
    }

    public ClearGoalSelector(Map<String, Object> map) {
        super(map);
    }

    @Override
    public void applyToEntity(Mob e, Source source, Target target) {
        SupremeMob.getInstance().getWrapper().getGoalSelector().clearEntityGoals(e);
    }

    @Override
    public String getName() {
        return "&6&lClear &cgoal selector";
    }
}
