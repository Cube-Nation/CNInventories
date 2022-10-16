package de.cubenation.cninventories.util;

import de.cubenation.bedrock.bukkit.api.BasePlugin;
import de.cubenation.bedrock.bukkit.api.service.confirm.AbstractConfirmService;
import de.cubenation.bedrock.core.FoundationPlugin;
import de.cubenation.bedrock.core.exception.TimeoutException;
import de.cubenation.bedrock.core.model.wrapper.BedrockChatSender;
import de.cubenation.bedrock.core.service.confirm.ConfirmRegistry;
import de.cubenation.cninventories.CNInventoriesPlugin;
import de.cubenation.cninventories.message.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class ConfirmOverrideEnderchest extends AbstractConfirmService {

    public ConfirmOverrideEnderchest(BasePlugin plugin) {
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

        BedrockChatSender sender = (BedrockChatSender) this.get("sender").get();
        UUID target = (UUID) this.get("target").get();
        String group = (String) this.get("group").get();
        ItemStack[] contents = (ItemStack[]) this.get("contents").get();

        boolean success = CNInventoriesPlugin.getInstance().getInventoryStoreService().storeEnderchestContents(target, group, contents);
        if(success)
            Messages.InventoryContentUpdateSuccess(sender);
        else
            Messages.InventoryContentUpdateFail(sender);
    }

    @Override
    public void abort() {
        BedrockChatSender sender = (BedrockChatSender) this.get("sender").get();
        ConfirmRegistry.getInstance().remove(sender);
        Messages.Confirm.Timeout(sender);
    }
}
