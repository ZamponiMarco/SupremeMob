package com.github.jummes.suprememob.loot;

import com.github.jummes.supremeitem.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.libs.annotation.Serializable;
import com.github.jummes.suprememob.SupremeMob;
import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootContext;
import org.bukkit.loot.LootTable;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;
import java.util.Random;

@Enumerable.Child
@Enumerable.Displayable(name = "&6&lMinecraft &cdrop table", description = "gui.loot.minecraft.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODQ0OWI5MzE4ZTMzMTU4ZTY0YTQ2YWIwZGUxMjFjM2Q0MDAwMGUzMzMyYzE1NzQ5MzJiM2M4NDlkOGZhMGRjMiJ9fX0=")
public class MinecraftDropTable extends DropTable {

    private static final String NAMESPACE_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjcxYjVjYTNhNjFiZWYyOTE2NWViMTI2NmI0MDVhYzI1OTE1NzJjMTZhNGIzOWNiMzZlZGFmNDZjODZjMDg4In19fQ==";
    private static final String NAME_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTdlZDY2ZjVhNzAyMDlkODIxMTY3ZDE1NmZkYmMwY2EzYmYxMWFkNTRlZDVkODZlNzVjMjY1ZjdlNTAyOWVjMSJ9fX0=";

    @Serializable(headTexture = NAMESPACE_HEAD, description = "gui.loot.minecraft.namespace")
    private String namespace;
    @Serializable(headTexture = NAME_HEAD, description = "gui.loot.minecraft.name")
    private String lootTableName;

    private LootTable lootTable;

    public MinecraftDropTable() {
        this(DROPS_DEFAULT, "minecraft", "example");
    }

    public MinecraftDropTable(Map<String, Object> map) {
        super(map);
        this.namespace = (String) map.get("namespace");
        this.lootTableName = (String) map.get("lootTableName");
    }

    public MinecraftDropTable(boolean defaultDropsEnabled, String namespace, String lootTableName) {
        super(defaultDropsEnabled);
        this.namespace = namespace;
        this.lootTableName = lootTableName;
    }

    @Override
    public void onModify(Field field) {
        try {
            lootTable = Bukkit.getServer().getLootTable(new NamespacedKey(namespace, lootTableName));
        } catch (IllegalArgumentException ignored) {

        }
    }

    @Override
    public Collection<ItemStack> populateLoot(Random random, LootContext context) {
        if (lootTable != null) {
            return lootTable.populateLoot(random, context);
        }
        return Lists.newArrayList();
    }

    @Override
    public void fillInventory(Inventory inventory, Random random, LootContext context) {
        throw new UnsupportedOperationException();
    }

    @Override
    public NamespacedKey getKey() {
        return new NamespacedKey(SupremeMob.getInstance(), lootTableName);
    }
}
