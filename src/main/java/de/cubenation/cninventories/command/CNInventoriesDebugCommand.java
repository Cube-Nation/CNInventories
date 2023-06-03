package de.cubenation.cninventories.command;

import de.cubenation.cninventories.manager.DebugManager;
import de.cubenation.cninventories.message.Messages;
import dev.projectshard.core.FoundationPlugin;
import dev.projectshard.core.annotations.Description;
import dev.projectshard.core.annotations.Permission;
import dev.projectshard.core.authorization.Role;
import dev.projectshard.core.command.Command;
import dev.projectshard.core.model.wrapper.ShardChatSender;

@Description("command.cninventories.debug.desc")
@Permission(Name = "inventory.debug", Role = Role.OWNER)
public class CNInventoriesDebugCommand extends Command {
    public CNInventoriesDebugCommand(FoundationPlugin plugin) {
        super(plugin);
    }

    public void execute(ShardChatSender sender) {
        boolean debugModeEnabled = DebugManager.getInstance().toggleDebugMode();
        Messages.DebugModeSetSuccess(sender, debugModeEnabled);
    }
}