package de.cubenation.cninventories.command;

import de.cubenation.bedrock.core.FoundationPlugin;
import de.cubenation.bedrock.core.annotation.Description;
import de.cubenation.bedrock.core.annotation.Permission;
import de.cubenation.bedrock.core.authorization.Role;
import de.cubenation.bedrock.core.command.Command;
import de.cubenation.bedrock.core.model.wrapper.BedrockChatSender;
import de.cubenation.bedrock.core.service.confirm.ConfirmInterface;
import de.cubenation.bedrock.core.service.confirm.ConfirmRegistry;
import de.cubenation.cninventories.message.Messages;
import org.bukkit.command.CommandSender;

@Description("command.inventory.cancel.desc")
@Permission(Name = "inventory.cancel", Role = Role.USER)
public class InventoryCancelCommand extends Command {

    public InventoryCancelCommand(FoundationPlugin plugin) {
        super(plugin);
    }

    public void execute(BedrockChatSender commandSender) {
        ConfirmInterface ci = ConfirmRegistry.getInstance().get(commandSender);

        if (ci == null) {
            Messages.Confirm.Nothing(commandSender);
            return;
        }

        // remove sender from ConfirmService Registry
        ConfirmRegistry.getInstance().remove(commandSender);

        ci.invalidate();
        Messages.Confirm.Cancel(commandSender);
    }
}