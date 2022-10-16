package de.cubenation.cninventories.command;

import de.cubenation.bedrock.bukkit.api.BasePlugin;
import de.cubenation.bedrock.core.FoundationPlugin;
import de.cubenation.bedrock.core.annotation.Description;
import de.cubenation.bedrock.core.annotation.Permission;
import de.cubenation.bedrock.core.authorization.Role;
import de.cubenation.bedrock.core.command.Command;
import de.cubenation.bedrock.core.model.wrapper.BedrockChatSender;
import de.cubenation.cninventories.manager.DebugManager;
import de.cubenation.cninventories.message.Messages;
import org.bukkit.command.CommandSender;

@Description("command.cninventories.debug.desc")
@Permission(Name = "inventory.debug", Role = Role.OWNER)
public class CNInventoriesDebugCommand extends Command {
    public CNInventoriesDebugCommand(FoundationPlugin plugin) {
        super(plugin);
    }

    public void execute(BedrockChatSender commandSender) {
        boolean debugModeEnabled = DebugManager.getInstance().toggleDebugMode();
        Messages.DebugModeSetSuccess(commandSender, debugModeEnabled);
    }
}