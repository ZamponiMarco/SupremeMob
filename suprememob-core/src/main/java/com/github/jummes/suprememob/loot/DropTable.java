package com.github.jummes.suprememob.loot;

import com.github.jummes.supremeitem.libs.annotation.Enumerable;
import com.github.jummes.supremeitem.libs.annotation.Serializable;
import com.github.jummes.supremeitem.libs.model.Model;
import com.github.jummes.suprememob.utils.HeadUtils;
import lombok.Getter;
import org.bukkit.loot.LootTable;

import java.util.Map;

@Getter
@Enumerable.Parent(classArray = {SimpleDropTable.class, MinecraftDropTable.class})
public abstract class DropTable implements Model, LootTable {

    private static final String DEFAULT_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODllNzAxNjIxNDNjN2NhYTIwZTMwM2VlYTMxNGE5YWVkNWRiOWNjNjg0MzVlNzgzYjNjNTlhZjQzYmY0MzYzNSJ9fX0=";

    protected static final boolean DROPS_DEFAULT = true;

    @Serializable(headTexture = DEFAULT_HEAD, description = "gui.loot.default")
    private boolean defaultDropsEnabled;

    public DropTable(boolean defaultDropsEnabled) {
        this.defaultDropsEnabled = defaultDropsEnabled;
    }

    public DropTable(Map<String, Object> map) {
        this.defaultDropsEnabled = (boolean) map.getOrDefault("defaultDropsEnabled", DROPS_DEFAULT);
    }

}
