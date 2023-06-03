package de.cubenation.cninventories.command;

import de.cubenation.cninventories.message.Messages;
import dev.projectshard.core.FoundationPlugin;
import dev.projectshard.core.annotations.Description;
import dev.projectshard.core.annotations.Permission;
import dev.projectshard.core.authorization.Role;
import dev.projectshard.core.command.Command;
import dev.projectshard.core.confirm.ConfirmInterface;
import dev.projectshard.core.confirm.ConfirmRegistry;
import dev.projectshard.core.model.wrapper.ShardChatSender;

@Description("command.inventory.cancel.desc")
@Permission(Name = "inventory.cancel", Role = Role.USER)
public class InventoryCancelCommand extends Command {

    public InventoryCancelCommand(FoundationPlugin plugin) {
        super(plugin);
    }

    public void execute(ShardChatSender sender) {
        ConfirmInterface ci = ConfirmRegistry.getInstance().get(sender);

        if (ci == null) {
            Messages.Confirm.Nothing(sender);
            return;
        }

        // Remove sender from ConfirmService Registry
        ConfirmRegistry.getInstance().remove(sender);

        ci.invalidate();
        Messages.Confirm.Cancel(sender);
    }
}