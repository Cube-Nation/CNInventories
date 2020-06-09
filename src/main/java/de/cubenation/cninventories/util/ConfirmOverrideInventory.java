package de.cubenation.cninventories.util;

import de.cubenation.api.bedrock.BasePlugin;
import de.cubenation.api.bedrock.exception.TimeoutException;
import de.cubenation.api.bedrock.service.confirm.AbstractConfirmService;
import de.cubenation.api.bedrock.service.confirm.ConfirmRegistry;
import de.cubenation.cninventories.CNInventoriesPlugin;
import de.cubenation.cninventories.message.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class ConfirmOverrideInventory extends AbstractConfirmService {

    public ConfirmOverrideInventory(BasePlugin plugin) {
        super(plugin);
    }

    @Override
    public void call() {

        // cancel timeout info task
        this.task.cancel();

        // check timeout
        try {
            this.checkExceeded();
        } catch (TimeoutException e) {
            return;
        }

        CommandSender sender = (CommandSender) this.get("sender").get();
        UUID target = (UUID) this.get("target").get();
        String group = (String) this.get("group").get();
        ItemStack[] contents = (ItemStack[]) this.get("contents").get();

        boolean success = CNInventoriesPlugin.getInstance().getInventoryStoreService().storePlayerInventoryContents(target, group, contents);
        if(success)
            Messages.InventoryContentUpdateSuccess(sender);
        else
            Messages.InventoryContentUpdateFail(sender);
    }

    @Override
    public void abort() {
        CommandSender sender = (CommandSender) this.get("sender").get();
        ConfirmRegistry.getInstance().remove(sender);
        Messages.Confirm.Timeout(sender);
    }
}
