package com.github.jummes.suprememob.command;

import com.github.jummes.supremeitem.libs.command.AbstractCommand;
import com.github.jummes.supremeitem.libs.gui.model.ModelCollectionInventoryHolder;
import com.github.jummes.suprememob.SupremeMob;
import lombok.SneakyThrows;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

public class SpawnerListCommand extends AbstractCommand {
    public SpawnerListCommand(CommandSender sender, String subCommand, String[] arguments, boolean isSenderPlayer) {
        super(sender, subCommand, arguments, isSenderPlayer);
    }

    @SneakyThrows
    @Override
    protected void execute() {
        Player player = (Player) sender;
        player.openInventory(new ModelCollectionInventoryHolder<>(SupremeMob.getInstance(), SupremeMob.getInstance().
                getSpawnerManager(), "spawners").getInventory());
    }

    @Override
    protected boolean isOnlyPlayer() {
        return true;
    }

    @Override
    protected Permission getPermission() {
        return new Permission("suprememob.spawner.list");
    }
}
