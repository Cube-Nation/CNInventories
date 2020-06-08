package de.cubenation.cninventories.command;

import de.cubenation.api.bedrock.BasePlugin;
import de.cubenation.api.bedrock.annotation.Description;
import de.cubenation.api.bedrock.annotation.IngameCommand;
import de.cubenation.api.bedrock.annotation.Permission;
import de.cubenation.api.bedrock.annotation.SubCommand;
import de.cubenation.api.bedrock.command.Command;
import de.cubenation.api.bedrock.command.CommandRole;
import de.cubenation.api.bedrock.exception.CommandException;
import de.cubenation.api.bedrock.exception.IllegalCommandArgumentException;
import de.cubenation.api.bedrock.exception.InsufficientPermissionException;
import de.cubenation.api.bedrock.service.command.CommandManager;
import de.cubenation.cninventories.CNInventoriesPlugin;
import de.cubenation.cninventories.config.InventoryZoneConfig;
import de.cubenation.cninventories.message.Messages;
import de.cubenation.cninventories.model.InventoryZone;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Description("command.invzone.remove.desc")
@SubCommand({"remove", "delete"})
@Permission(Name = "invzone.remove", Role = CommandRole.ADMIN)
@IngameCommand
public class InvZoneRemoveCommand extends Command {

    public InvZoneRemoveCommand(BasePlugin plugin, CommandManager commandManager) {
        super(plugin, commandManager);
    }

    public void execute(CommandSender commandSender, String[] args) throws CommandException, IllegalCommandArgumentException, InsufficientPermissionException {
        Player player = (Player) commandSender;

        InventoryZone zone = CNInventoriesPlugin.getInstance().getInventoryZoneService().getZoneAtLocation(player.getLocation());
        if(zone == null) {
            Messages.Error.ErrorNoInvZoneAtLocation(player);
            return;
        }

        InventoryZoneConfig config = (InventoryZoneConfig) plugin.getConfigService().getConfig(InventoryZoneConfig.class);
        boolean success = config.removeZone(zone.getUuid());

        if(success)
            Messages.InvZoneRemoveSuccess(player);
        else
            Messages.InvZoneRemoveFail(player);
    }
}
