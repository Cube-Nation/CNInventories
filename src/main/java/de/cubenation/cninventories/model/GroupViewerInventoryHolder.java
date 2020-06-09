package de.cubenation.cninventories.model;

import de.cubenation.cninventories.CNInventoriesPlugin;
import de.cubenation.cninventories.message.Messages;
import de.cubenation.cninventories.util.ItemStackUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;

public class GroupViewerInventoryHolder implements InventoryHolder {

    private Player player;
    private Player target;
    private String group;
    private Inventory inv;
    private ItemStack[] prevContents;

    CNInventoriesPlugin plugin = CNInventoriesPlugin.getInstance();

    public GroupViewerInventoryHolder(Player player, Player target, String group) {
        this.player = player;
        this.target = target;
        this.group = group;

        ItemStack[] contents = loadCurrentContent();
        this.prevContents = contents;

        int rows = contents.length / 9 + ((contents.length % 9) > 0 ? 1 : 0);
        inv = Bukkit.createInventory(this, rows*9, target.getDisplayName()+" - "+group);
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
        ItemStack[] currContent = loadCurrentContent();
        if(ItemStackUtil.areSameItemStacks(currContent, prevContents)) {
            //no changes other than own -> just save the content
            boolean success = plugin.getInventoryStoreService().storePlayerInventoryContents(target, group, inv.getContents());
            if(success)
                Messages.InventoryContentUpdateSuccess(player);
            else
                Messages.InventoryContentUpdateFail(player);
            return;
        }

        //TODO: changes other than own -> ask for overwrite confirmation
        player.sendMessage("please confirm");

    }

    private ItemStack[] loadCurrentContent() {
        ItemStack[] contents = new ItemStack[target.getInventory().getSize()];

        try {
            contents = plugin.getInventoryStoreService().getPlayerInventoryContents(target, group);
        } catch (IOException e) {
            // e.printStackTrace();
        }

        return contents;
    }
}
