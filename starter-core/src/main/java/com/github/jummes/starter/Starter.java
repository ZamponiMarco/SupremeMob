package com.github.jummes.starter;

import com.github.jummes.libs.core.Libs;
import com.github.jummes.libs.localization.PluginLocale;
import com.google.common.collect.Lists;
import org.bukkit.plugin.java.JavaPlugin;

public class Starter extends JavaPlugin {

    public static Starter getInstance() {
        return getPlugin(Starter.class);
    }

    @Override
    public void onEnable() {
        PluginLocale locale = new PluginLocale(this, Lists.newArrayList("en-US"), "en-US");
        Libs.initializeLibrary(this, locale);
    }
}
