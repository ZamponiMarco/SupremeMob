package com.github.jummes.suprememob.actuator;

import com.github.jummes.supremeitem.action.Action;
import com.github.jummes.supremeitem.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.libs.core.Libs;
import com.github.jummes.supremeitem.libs.model.Model;
import com.github.jummes.supremeitem.libs.util.ItemUtils;
import com.google.common.collect.Lists;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

@Enumerable.Parent(classArray = {CooldownActuator.class, SpawnActuator.class, DamageActuator.class, HitActuator.class,
        DeathActuator.class, InteractActuator.class})
public abstract class Actuator implements Model {

    protected static final String ENTITY_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjY4YjQzMTE1MmU4MmFmNWRlZjg4ZjkxYmI2MWM2MjNiM2I3YWMzYWJlODJkMjc2ZmFkMzQ3Nzc2NDBmOTU5MCJ9fX0=";

    protected static final List<Action> ACTIONS_DEFAULT = Lists.newArrayList();

    public Actuator() {
    }

    public Actuator(Map<String, Object> map) {

    }

    @Override
    public ItemStack getGUIItem() {
        return ItemUtils.getNamedItem(Libs.getWrapper().skullFromValue(getClass().getAnnotation(Enumerable.Displayable.class).headTexture()),
                getClass().getAnnotation(Enumerable.Displayable.class).name(), Lists.newArrayList());
    }
}
