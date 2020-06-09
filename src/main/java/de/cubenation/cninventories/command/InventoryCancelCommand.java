package de.cubenation.cninventories.command;

import de.cubenation.api.bedrock.BasePlugin;
import de.cubenation.api.bedrock.annotation.Description;
import de.cubenation.api.bedrock.annotation.Permission;
import de.cubenation.api.bedrock.annotation.SubCommand;
import de.cubenation.api.bedrock.command.Command;
import de.cubenation.api.bedrock.command.CommandRole;
import de.cubenation.api.bedrock.service.command.CommandManager;
import de.cubenation.api.bedrock.service.confirm.ConfirmInterface;
import de.cubenation.api.bedrock.service.confirm.ConfirmRegistry;
import de.cubenation.cninventories.message.Messages;
import org.bukkit.command.CommandSender;

@Description("command.inventory.cancel.desc")
@SubCommand("cancel")
@Permission(Name = "inventory.cancel", Role = CommandRole.USER)
public class InventoryCancelCommand extends Command {

    public InventoryCancelCommand(BasePlugin plugin, CommandManager commandManager) {
        super(plugin, commandManager);
    }


    public void execute(CommandSender commandSender, String[] args) {
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