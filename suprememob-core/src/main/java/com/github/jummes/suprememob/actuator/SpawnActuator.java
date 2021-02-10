package com.github.jummes.suprememob.actuator;

import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.action.source.EntitySource;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.github.jummes.supremeitem.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.libs.annotation.Serializable;
import com.google.common.collect.Lists;
import org.bukkit.entity.LivingEntity;

import java.util.List;
import java.util.Map;

@Enumerable.Child
@Enumerable.Displayable(name = "&6&lSpawn &cactuator", description = "gui.actuator.spawn.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTNjOGFhM2ZkZTI5NWZhOWY5YzI3ZjczNGJkYmFiMTFkMzNhMmU0M2U4NTVhY2NkNzQ2NTM1MjM3NzQxM2IifX19")
public class SpawnActuator extends Actuator {

    @Serializable(headTexture = ENTITY_HEAD, description = "gui.actuator.entity-actions")
    @Serializable.Optional(defaultValue = "ACTIONS_DEFAULT")
    private List<Action> onEntityActions;

    public SpawnActuator() {
        this(Lists.newArrayList());
    }

    public SpawnActuator(Map<String, Object> map) {
        super(map);
        this.onEntityActions = (List<Action>) map.getOrDefault("onEntityActions", Lists.newArrayList());
    }

    public SpawnActuator(List<Action> onEntityActions) {
        this.onEntityActions = onEntityActions;
    }

    public void executeActuator(LivingEntity e, Map<String, Object> map) {
        onEntityActions.forEach(action -> action.execute(new EntityTarget(e), new EntitySource(e), map));
    }
}
