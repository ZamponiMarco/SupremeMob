package com.github.jummes.suprememob.actuator;

import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.source.EntitySource;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.github.jummes.supremeitem.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.libs.annotation.Serializable;
import com.google.common.collect.Lists;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

@Enumerable.Child
@Enumerable.Displayable(name = "&6&lInteract &cactuator", description = "gui.actuator.interact.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjc0ZmRhYzYyZjMyZjZmMWI4MjgzYTc3MTAyNDljMmJmMjJiNGRmZmRkMTkyNzMxOGNmMDlkODUzNGEwZTMxZCJ9fX0=")
public class InteractActuator extends CooldownActuator {

    private static final String INTERACTOR_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDI4NDNjM2MyMzU3MTZmM2ViNWNkOWMzYmRiZjIwODUzZjUwYTY1ZGMyMjMwNThiMWUxZWVmZmRlOTlmNjExMCJ9fX0=";

    @Serializable(headTexture = ENTITY_HEAD, description = "gui.actuator.entity-actions")
    private List<Action> onEntityActions;
    @Serializable(headTexture = INTERACTOR_HEAD, description = "gui.actuator.interact.interactor-actions")
    private List<Action> onInteractorActions;

    public InteractActuator() {
        this(COOLDOWN_DEFAULT, Lists.newArrayList(), Lists.newArrayList());
    }

    public InteractActuator(Map<String, Object> map) {
        super(map);
        this.onEntityActions = (List<Action>) map.get("onEntityActions");
        this.onInteractorActions = (List<Action>) map.get("onInteractorActions");
    }

    public InteractActuator(int cooldown, List<Action> onEntityActions, List<Action> onInteractorActions) {
        super(cooldown);
        this.onEntityActions = onEntityActions;
        this.onInteractorActions = onInteractorActions;
    }

    @Override
    protected void executeExactSkill(Map<String, Object> map, LivingEntity... e) {
        onEntityActions.forEach(action -> action.execute(new EntityTarget(e[0]), new EntitySource(e[0], new ItemStack(Material.CARROT)), map));
        onInteractorActions.forEach(action -> action.execute(new EntityTarget(e[1]), new EntitySource(e[0], new ItemStack(Material.CARROT)), map));
    }
}
