package de.cubenation.cninventories.command;

import de.cubenation.api.bedrock.BasePlugin;
import de.cubenation.api.bedrock.annotation.*;
import de.cubenation.api.bedrock.command.Command;
import de.cubenation.api.bedrock.command.CommandRole;
import de.cubenation.api.bedrock.exception.CommandException;
import de.cubenation.api.bedrock.exception.IllegalCommandArgumentException;
import de.cubenation.api.bedrock.exception.InsufficientPermissionException;
import de.cubenation.api.bedrock.service.command.CommandManager;
import de.cubenation.cninventories.CNInventoriesPlugin;
import de.cubenation.cninventories.config.InventoryZoneConfig;
import de.cubenation.cninventories.model.InventoryZone;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Description("command.invzone.modify.group.desc")
@SubCommand({"modify"})
@SubCommand({"group"})
@Argument(
        Description = "command.invzone.args.group.desc",
        Placeholder = "command.invzone.args.group.ph"
)
@Permission(Name = "invzone.modify.group", Role = CommandRole.ADMIN)
@IngameCommand
public class InvZoneModifyGroupCommand extends Command {

    public InvZoneModifyGroupCommand(BasePlugin plugin, CommandManager commandManager) {
        super(plugin, commandManager);
    }

    public void execute(CommandSender commandSender, String[] args) throws CommandException, IllegalCommandArgumentException, InsufficientPermissionException {
        Player player = (Player) commandSender;

        InventoryZone zone = CNInventoriesPlugin.getInstance().getInventoryZoneService().getZoneAtLocation(player.getLocation());
        if(zone == null) {
            //TODO: Messages.InvZone.Modify(player, success);
            player.sendMessage("Messages.InvZone.Modify(player)");
            return;
        }

        InventoryZoneConfig config = (InventoryZoneConfig) plugin.getConfigService().getConfig(InventoryZoneConfig.class);
        boolean success = config.updateGroup(zone.getUuid(), args[0]);

        //TODO: Messages.InvZone.Modify(player, success);
        player.sendMessage("Messages.InvZone.Modify(player) : "+success);
    }
}
