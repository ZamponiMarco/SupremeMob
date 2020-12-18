package com.github.jummes.suprememob.loot.drop;

import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.condition.AlwaysTrueCondition;
import com.github.jummes.supremeitem.condition.Condition;
import com.github.jummes.supremeitem.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.libs.annotation.Serializable;
import com.github.jummes.supremeitem.libs.core.Libs;
import com.github.jummes.supremeitem.libs.model.wrapper.ItemStackWrapper;
import com.github.jummes.supremeitem.libs.util.ItemUtils;
import com.github.jummes.supremeitem.value.NumericValue;
import com.google.common.collect.Lists;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootContext;

import java.util.Collection;
import java.util.Map;

@Enumerable.Child
@Enumerable.Displayable(name = "&6&lItem &cdrop", description = "gui.loot.drop.item.description")
public class ItemDrop extends Drop {

    private static final String COUNT_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjdkYzNlMjlhMDkyM2U1MmVjZWU2YjRjOWQ1MzNhNzllNzRiYjZiZWQ1NDFiNDk1YTEzYWJkMzU5NjI3NjUzIn19fQ==";

    @Serializable(displayItem = "getDisplayItem", description = "gui.loot.drop.item.item")
    private ItemStackWrapper item;
    @Serializable(headTexture = COUNT_HEAD, description = "gui.loot.drop.item.count", additionalDescription = {"gui.additional-tooltips.value"})
    private NumericValue count;

    public ItemDrop() {
        this(new AlwaysTrueCondition(), new ItemStackWrapper(), new NumericValue(1));
    }

    public ItemDrop(Map<String, Object> map) {
        super(map);
        this.item = (ItemStackWrapper) map.getOrDefault("item", new ItemStackWrapper());
        this.count = (NumericValue) map.getOrDefault("count", new NumericValue(1));
    }

    public ItemDrop(Condition condition, ItemStackWrapper item, NumericValue count) {
        super(condition);
        this.item = item;
        this.count = count;
    }

    @Override
    public Collection<ItemStack> getLoot(Source source, Target target, LootContext context) {
        ItemStack toReturn = item.getWrapped().clone();
        toReturn.setAmount(count.getRealValue(target, source).intValue());
        return Lists.newArrayList(toReturn);
    }

    public ItemStack getDisplayItem() {
        return item.getWrapped().clone();
    }

    @Override
    public ItemStack getGUIItem() {
        return ItemUtils.getNamedItem(getDisplayItem(), "&6&lItem: &c" + item.getWrapped().getType().name() +
                        " x" + count.getName(), Libs.getLocale().getList("gui.additional-tooltips.delete"));
    }
}
