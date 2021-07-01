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
@Enumerable.Displayable(name = "&6&lDeath &cactuator", description = "gui.actuator.death.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODYzZmQ3ODIyMmQ1OTg4YzZiZTFjYzlmYTk2ZTg1Mjg5MTViYjY5NzQ2NDY0ZDIzOGY5MzZlYmViYjIzYzUyIn19fQ==")
public class DeathActuator extends Actuator {

    private static final String KILLER_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTBkZmM4YTM1NjNiZjk5NmY1YzFiNzRiMGIwMTViMmNjZWIyZDA0Zjk0YmJjZGFmYjIyOTlkOGE1OTc5ZmFjMSJ9fX0=";

    @Serializable(headTexture = ENTITY_HEAD, description = "gui.actuator.entity-actions")
    private List<Action> onEntityActions;
    @Serializable(headTexture = KILLER_HEAD, description = "gui.actuator.death.killer-actions")
    private List<Action> onKillerActions;

    public DeathActuator() {
        this(Lists.newArrayList(), Lists.newArrayList());
    }

    public DeathActuator(Map<String, Object> map) {
        super(map);
        this.onEntityActions = (List<Action>) map.get("onEntityActions");
        this.onKillerActions = (List<Action>) map.get("onKillerActions");
    }

    public DeathActuator(List<Action> onEntityActions, List<Action> onKillerActions) {
        this.onEntityActions = onEntityActions;
        this.onKillerActions = onKillerActions;
    }

    public void executeSkill(Map<String, Object> map, LivingEntity mob, LivingEntity killer) {
        onEntityActions.forEach(action -> action.execute(new EntityTarget(mob), new EntitySource(mob, new ItemStack(Material.CARROT)), map));

        if (killer != null) {
            onKillerActions.forEach(action -> action.execute(new EntityTarget(killer), new EntitySource(mob, new ItemStack(Material.CARROT)), map));
        }
    }

}
