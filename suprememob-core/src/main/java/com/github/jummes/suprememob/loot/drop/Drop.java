package com.github.jummes.suprememob.loot.drop;

import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.condition.Condition;
import com.github.jummes.supremeitem.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.libs.annotation.Serializable;
import com.github.jummes.supremeitem.libs.model.Model;
import com.github.jummes.suprememob.utils.HeadUtils;
import com.google.common.collect.Lists;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootContext;

import java.util.Collection;
import java.util.Map;

@Enumerable.Parent(classArray = {ItemDrop.class})
public abstract class Drop implements Model {

    private static final String CONDITION_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmMyNzEwNTI3MTllZjY0MDc5ZWU4YzE0OTg5NTEyMzhhNzRkYWM0YzI3Yjk1NjQwZGI2ZmJkZGMyZDZiNWI2ZSJ9fX0=";

    @Serializable(headTexture = CONDITION_HEAD, description = "gui.loot.drop.condition",
            additionalDescription = {"gui.additional-tooltips.recreate"})
    private Condition condition;

    public Drop(Condition condition) {
        this.condition = condition;
    }

    public Drop(Map<String, Object> map) {
        this.condition = (Condition) map.get("condition");
    }

    public abstract Collection<ItemStack> getLoot(Source source, Target target, LootContext context);

    public Collection<ItemStack> populateLoot(Source source, Target target, LootContext context) {
        if (condition.checkCondition(target, source)) {
            return getLoot(source, target, context);
        }
        return Lists.newArrayList();
    }

}
