package com.github.jummes.suprememob;

import com.github.jummes.supremeitem.libs.command.PluginCommandExecutor;
import com.github.jummes.supremeitem.libs.core.Libs;
import com.github.jummes.suprememob.actuator.Actuator;
import com.github.jummes.suprememob.api.SupremeMobAPI;
import com.github.jummes.suprememob.command.HelpCommand;
import com.github.jummes.suprememob.command.MobListCommand;
import com.github.jummes.suprememob.command.MobSpawnCommand;
import com.github.jummes.suprememob.command.SpawnerListCommand;
import com.github.jummes.suprememob.goal.GoalSelector;
import com.github.jummes.suprememob.listener.MobListener;
import com.github.jummes.suprememob.loot.DropTable;
import com.github.jummes.suprememob.loot.drop.Drop;
import com.github.jummes.suprememob.manager.CooldownManager;
import com.github.jummes.suprememob.manager.MobManager;
import com.github.jummes.suprememob.manager.SpawnerManager;
import com.github.jummes.suprememob.mob.Mob;
import com.github.jummes.suprememob.mob.options.AttributeOptions;
import com.github.jummes.suprememob.mob.options.BehaviorOptions;
import com.github.jummes.suprememob.mob.options.EquipmentOptions;
import com.github.jummes.suprememob.mob.options.GeneralOptions;
import com.github.jummes.suprememob.spawner.Spawner;
import com.github.jummes.suprememob.target.TargetSelector;
import com.github.jummes.suprememob.wrapper.VersionWrapper;
import com.google.common.collect.Lists;
import lombok.Getter;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class SupremeMob extends JavaPlugin {

    static {
        ConfigurationSerialization.registerClass(Mob.class);

        ConfigurationSerialization.registerClass(GeneralOptions.class);
        ConfigurationSerialization.registerClass(AttributeOptions.class);
        ConfigurationSerialization.registerClass(BehaviorOptions.class);
        ConfigurationSerialization.registerClass(EquipmentOptions.class);
        ConfigurationSerialization.registerClass(EquipmentOptions.Equipment.class);

        ConfigurationSerialization.registerClass(Actuator.class);

        ConfigurationSerialization.registerClass(TargetSelector.class);

        ConfigurationSerialization.registerClass(GoalSelector.class);

        ConfigurationSerialization.registerClass(DropTable.class);
        ConfigurationSerialization.registerClass(Drop.class);
    }

    private SupremeMobAPI api;

    private MobManager mobManager;
    private SpawnerManager spawnerManager;
    private CooldownManager cooldownManager;
    private VersionWrapper wrapper;

    public static SupremeMob getInstance() {
        return getPlugin(SupremeMob.class);
    }

    @Override
    public void onEnable() {
        setUpLocale();
        setUpData();
        setUpListener();
        setUpCommands();
        setUpWrapper();
    }

    private void setUpLocale() {
        Libs.getLocale().registerLocaleFiles(this, Lists.newArrayList("en-US"), "en-US");
    }

    private void setUpData() {
        this.mobManager = new MobManager(Mob.class, "comp", this);
        this.spawnerManager = new SpawnerManager(Spawner.class, "comp", this);
        this.cooldownManager = new CooldownManager();
        this.api = new SupremeMobAPI();
    }

    private void setUpListener() {
        getServer().getPluginManager().registerEvents(new MobListener(), this);
    }

    private void setUpCommands() {
        PluginCommandExecutor ex = new PluginCommandExecutor(HelpCommand.class, "help");
        ex.registerCommand("mobs", MobListCommand.class);
        ex.registerCommand("spawners", SpawnerListCommand.class);
        ex.registerCommand("spawn", MobSpawnCommand.class);
        getCommand("sm").setExecutor(ex);
    }

    private void setUpWrapper() {
        this.wrapper = new VersionWrapper();
    }
}