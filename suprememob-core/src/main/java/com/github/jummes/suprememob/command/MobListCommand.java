package com.github.jummes.suprememob.command;

import com.github.jummes.supremeitem.libs.command.AbstractCommand;
import com.github.jummes.supremeitem.libs.gui.model.ModelCollectionInventoryHolder;
import com.github.jummes.suprememob.SupremeMob;
import com.github.jummes.suprememob.mob.Mob;
import lombok.SneakyThrows;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

public class MobListCommand extends AbstractCommand {
    public MobListCommand(CommandSender sender, String subCommand, String[] arguments, boolean isSenderPlayer) {
        super(sender, subCommand, arguments, isSenderPlayer);
    }

    @SneakyThrows
    @Override
    protected void execute() {
        Player player = (Player) sender;
        player.openInventory(new ModelCollectionInventoryHolder<>(SupremeMob.getInstance(), SupremeMob.getInstance().
                getMobManager(), "mobs").getInventory());
    }

    @Override
    protected boolean isOnlyPlayer() {
        return true;
    }

    @Override
    protected Permission getPermission() {
        return new Permission("suprememob.mob.list");
    }
}
