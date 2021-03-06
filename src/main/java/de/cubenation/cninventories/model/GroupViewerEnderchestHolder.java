package de.cubenation.cninventories.model;

import de.cubenation.api.bedrock.ebean.BedrockPlayer;
import de.cubenation.api.bedrock.service.confirm.ConfirmRegistry;
import de.cubenation.api.bedrock.service.confirm.ConfirmStorable;
import de.cubenation.cninventories.CNInventoriesPlugin;
import de.cubenation.cninventories.message.Messages;
import de.cubenation.cninventories.util.ConfirmOverrideEnderchest;
import de.cubenation.cninventories.util.ItemStackUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;

public class GroupViewerEnderchestHolder implements InventoryHolder {

    private Player player;
    private BedrockPlayer target;
    private String group;
    private Inventory inv;
    private ItemStack[] prevContents;

    CNInventoriesPlugin plugin = CNInventoriesPlugin.getInstance();

    public GroupViewerEnderchestHolder(Player player, BedrockPlayer target, String group) {
        this.player = player;
        this.target = target;
        this.group = group;

        ItemStack[] contents;
        try {
            contents = loadCurrentContent();
        } catch (IOException e) {
            Messages.Error.ErrorNoSuchInvGroup(player, group, target.getUsername());
            return;
        }
        this.prevContents = contents;

        int rows = contents.length / 9 + ((contents.length % 9) > 0 ? 1 : 0);
        inv = Bukkit.createInventory(this, rows*9, target.getUsername()+" - "+group);
        inv.setContents(contents);
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }

    private ItemStack[] getContent() {
        return ItemStackUtil.shortenItemStack(inv.getContents(), prevContents.length);
    }

    public void performClose() {
        if(ItemStackUtil.areSameItemStacks(getContent(), prevContents))
            return; // no changes -> no need to save

        // compare to current content
        ItemStack[] currContent = new ItemStack[0];
        try {
            currContent = loadCurrentContent();
        } catch (IOException e) {
            currContent = null;
        }
        if(ItemStackUtil.areSameItemStacks(currContent, prevContents)) {
            //no changes other than own -> just save the content
            boolean success = plugin.getInventoryStoreService().storePlayerInventoryContents(target.getUUID(), group, inv.getContents());
            if(success)
                Messages.InventoryContentUpdateSuccess(player);
            else
                Messages.InventoryContentUpdateFail(player);
            return;
        }

        //changes other than own -> ask for overwrite confirmation
        createConfirm(player, target, group, getContent());

    }

    private ItemStack[] loadCurrentContent() throws IOException {
        return plugin.getInventoryStoreService().getEnderchestContents(target.getUUID(), group, player.getEnderChest().getSize());
    }

    private void createConfirm(Player player, BedrockPlayer target, String group, ItemStack[] contents) {
        ConfirmOverrideEnderchest confirm = new ConfirmOverrideEnderchest(CNInventoriesPlugin.getInstance());
        confirm.store("sender", new ConfirmStorable<Object>(player));
        confirm.store("target", new ConfirmStorable<Object>(target.getUUID()));
        confirm.store("group", new ConfirmStorable<Object>(group));
        confirm.store("contents", new ConfirmStorable<Object>(contents));

        ConfirmRegistry.getInstance().put(player, confirm);
        Messages.Confirm.OverrideInventory(player);
    }
}
