package de.cubenation.cninventories.command;

import de.cubenation.api.bedrock.BasePlugin;
import de.cubenation.api.bedrock.annotation.Description;
import de.cubenation.api.bedrock.annotation.Permission;
import de.cubenation.api.bedrock.annotation.SubCommand;
import de.cubenation.api.bedrock.command.Command;
import de.cubenation.api.bedrock.command.CommandRole;
import de.cubenation.api.bedrock.service.command.CommandManager;
import de.cubenation.cninventories.manager.DebugManager;
import de.cubenation.cninventories.message.Messages;
import org.bukkit.command.CommandSender;

@Description("command.cninventories.debug.desc")
@SubCommand("debug")
@Permission(Name = "inventory.debug", Role = CommandRole.OWNER)
public class CNInventoriesDebugCommand extends Command {

    public CNInventoriesDebugCommand(BasePlugin plugin, CommandManager commandManager) {
        super(plugin, commandManager);
    }

    public void execute(CommandSender commandSender, String[] args) {
        boolean debugModeEnabled = DebugManager.getInstance().toggleDebugMode();
        Messages.DebugModeSetSuccess(commandSender, debugModeEnabled);
    }
}