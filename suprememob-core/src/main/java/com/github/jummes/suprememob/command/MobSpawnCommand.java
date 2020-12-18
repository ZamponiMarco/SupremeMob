package com.github.jummes.suprememob.command;

import com.github.jummes.supremeitem.libs.command.AbstractCommand;
import com.github.jummes.supremeitem.libs.core.Libs;
import com.github.jummes.suprememob.SupremeMob;
import com.github.jummes.suprememob.mob.Mob;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

public class MobSpawnCommand extends AbstractCommand {
    public MobSpawnCommand(CommandSender sender, String subCommand, String[] arguments, boolean isSenderPlayer) {
        super(sender, subCommand, arguments, isSenderPlayer);
    }

    @Override
    protected void execute() {
        Player player = (Player) sender;
        if (arguments.length < 1) {
            player.sendMessage(Libs.getLocale().get("messages.command.mob-incorrect-usage"));
            return;
        }

        Mob mob = SupremeMob.getInstance().getMobManager().getByName(arguments[0]);
        if (mob == null) {
            player.sendMessage(Libs.getLocale().get("messages.command.mob-not-found"));
            return;
        }

        int level = 1;

        if (arguments.length > 1 && StringUtils.isNumeric(arguments[1])) {
            level = Integer.parseInt(arguments[1]);
        }

        mob.spawn(player.getLocation(), level);
    }

    @Override
    protected boolean isOnlyPlayer() {
        return true;
    }

    @Override
    protected Permission getPermission() {
        return new Permission("suprememob.mob.spawn");
    }
}
