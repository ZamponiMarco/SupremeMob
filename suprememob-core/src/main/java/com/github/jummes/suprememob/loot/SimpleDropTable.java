package com.github.jummes.suprememob.loot;

import com.github.jummes.supremeitem.action.source.EntitySource;
import com.github.jummes.supremeitem.action.source.Source;
import com.github.jummes.supremeitem.action.targeter.EntityTarget;
import com.github.jummes.supremeitem.action.targeter.Target;
import com.github.jummes.supremeitem.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.libs.annotation.Serializable;
import com.github.jummes.suprememob.loot.drop.Drop;
import com.github.jummes.suprememob.utils.HeadUtils;
import com.google.common.collect.Lists;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootContext;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Enumerable.Child
@Enumerable.Displayable(name = "&6&lSimple &cdrop table", description = "gui.loot.simple.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTliOTA2YjIxNTVmMTkzNzg3MDQyMzM4ZDA1Zjg0MDM5MWMwNWE2ZDNlODE2MjM5MDFiMjk2YmVlM2ZmZGQyIn19fQ")
public class SimpleDropTable extends DropTable {

    private static final String DROPS_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTliOTA2YjIxNTVmMTkzNzg3MDQyMzM4ZDA1Zjg0MDM5MWMwNWE2ZDNlODE2MjM5MDFiMjk2YmVlM2ZmZGQyIn19fQ";
    private static final String PLAYER_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDI4NDNjM2MyMzU3MTZmM2ViNWNkOWMzYmRiZjIwODUzZjUwYTY1ZGMyMjMwNThiMWUxZWVmZmRlOTlmNjExMCJ9fX0=";
    
    @Serializable(headTexture = PLAYER_HEAD, description = "gui.loot.simple.player")
    private boolean onlyPlayerKill;

    @Serializable(headTexture = DROPS_HEAD, description = "gui.loot.simple.drops")
    private List<Drop> drops;

    public SimpleDropTable() {
        this(DROPS_DEFAULT, false, Lists.newArrayList());
    }

    public SimpleDropTable(Map<String, Object> map) {
        super(map);
        this.onlyPlayerKill = (boolean) map.get("onlyPlayerKill");
        this.drops = (List<Drop>) map.get("drops");
    }

    public SimpleDropTable(boolean defaultDropsEnabled, boolean onlyPlayerKill, List<Drop> drops) {
        super(defaultDropsEnabled);
        this.onlyPlayerKill = onlyPlayerKill;
        this.drops = drops;
    }

    @Override
    public Collection<ItemStack> populateLoot(Random random, LootContext context) {

        if (context.getLootedEntity() == null || (onlyPlayerKill && context.getKiller() == null)) {
            return Lists.newArrayList();
        }

        Source source = context.getKiller() != null ? new EntitySource(context.getKiller()) :
                new EntitySource((LivingEntity) context.getLootedEntity());
        Target target = new EntityTarget((LivingEntity) context.getLootedEntity());

        return drops.stream().reduce(Lists.newArrayList(), (list, drop) -> {
            list.addAll(drop.populateLoot(source, target, context));
            return list;
        }, (list1, list2) -> {
            list1.addAll(list2);
            return list1;
        });
    }

    @Override
    public void fillInventory(Inventory inventory, Random random, LootContext context) {
        throw new UnsupportedOperationException();
    }

    @Override
    public NamespacedKey getKey() {
        return NamespacedKey.randomKey();
    }
}
