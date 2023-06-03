package de.cubenation.cninventories.util;

import de.cubenation.cninventories.message.Messages;
import de.cubenation.cninventories.service.InventoryService;
import dev.projectshard.core.FoundationPlugin;
import dev.projectshard.core.confirm.AbstractConfirmService;
import dev.projectshard.core.confirm.ConfirmRegistry;
import dev.projectshard.core.exceptions.TimeoutException;
import dev.projectshard.core.model.wrapper.ShardChatSender;
import jakarta.inject.Inject;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class ConfirmOverrideEnderchest extends AbstractConfirmService {

    @Inject
    private InventoryService inventoryService;

    public ConfirmOverrideEnderchest(FoundationPlugin plugin) {
        super(plugin);
    }

    @Override
    public void call() {

        // Cancel timeout info task
        task.cancel();

        // Check timeout
        try {
            this.checkExceeded();
        } catch (TimeoutException e) {
            return;
        }

        ShardChatSender sender = (ShardChatSender) this.get("sender").get();
        UUID target = (UUID) this.get("target").get();
        String group = (String) this.get("group").get();
        ItemStack[] contents = (ItemStack[]) this.get("contents").get();

        boolean success = inventoryService.storeInventoryContents(target, group, "ender-chest", contents);
        if(success)
            Messages.InventoryContentUpdateSuccess(sender);
        else
            Messages.InventoryContentUpdateFail(sender);
    }

    @Override
    public void abort() {
        ShardChatSender sender = (ShardChatSender) this.get("sender").get();
        ConfirmRegistry.getInstance().remove(sender);
        Messages.Confirm.Timeout(sender);
    }
}
