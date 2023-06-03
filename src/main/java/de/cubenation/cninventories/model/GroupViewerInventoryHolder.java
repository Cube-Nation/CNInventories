//package de.cubenation.cninventories.model;
//
//import de.cubenation.bedrock.core.model.wrapper.BedrockPlayer;
//import de.cubenation.bedrock.core.service.confirm.ConfirmRegistry;
//import de.cubenation.bedrock.core.service.confirm.ConfirmStorable;
//import de.cubenation.cninventories.CNInventoriesPlugin;
//import de.cubenation.cninventories.message.Messages;
//import de.cubenation.cninventories.util.ConfirmOverrideInventory;
//import de.cubenation.cninventories.util.ItemStackUtil;
//import org.bukkit.Bukkit;
//import org.bukkit.entity.Player;
//import org.bukkit.inventory.Inventory;
//import org.bukkit.inventory.InventoryHolder;
//import org.bukkit.inventory.ItemStack;
//
//import java.io.IOException;
//
//public class GroupViewerInventoryHolder implements InventoryHolder {
//
//    private BedrockPlayer player;
//    private BedrockPlayer target;
//    private String group;
//    private Inventory inv;
//    private ItemStack[] prevContents;
//
//    CNInventoriesPlugin plugin = CNInventoriesPlugin.getInstance();
//
//    public GroupViewerInventoryHolder(BedrockPlayer player, BedrockPlayer target, String group) {
//        this.player = player;
//        this.target = target;
//        this.group = group;
//
//        ItemStack[] contents;
//        try {
//            contents = loadCurrentContent();
//        } catch (IOException e) {
//            Messages.Error.ErrorNoSuchInvGroup(player, group, target.getName());
//            return;
//        }
//        this.prevContents = contents;
//
//        int rows = contents.length / 9 + ((contents.length % 9) > 0 ? 1 : 0);
//        inv = Bukkit.createInventory(this, rows*9, target.getName()+" - "+group);
//        inv.setContents(contents);
//    }
//
//    @Override
//    public Inventory getInventory() {
//        return inv;
//    }
//
//    private ItemStack[] getContent() {
//        return ItemStackUtil.shortenItemStack(inv.getContents(), prevContents.length);
//    }
//
//    public void performClose() {
//        if(ItemStackUtil.areSameItemStacks(getContent(), prevContents))
//            return; // no changes -> no need to save
//
//        // compare to current content
//        ItemStack[] currContent = new ItemStack[0];
//        try {
//            currContent = loadCurrentContent();
//        } catch (IOException e) {
//            currContent = null;
//        }
//        if(ItemStackUtil.areSameItemStacks(currContent, prevContents)) {
//            //no changes other than own -> just save the content
//            boolean success = plugin.getInventoryStoreService().storePlayerInventoryContents(target.getUniqueId(), group, inv.getContents());
//            if(success)
//                Messages.InventoryContentUpdateSuccess(player);
//            else
//                Messages.InventoryContentUpdateFail(player);
//            return;
//        }
//
//        //changes other than own -> ask for overwrite confirmation
//        createConfirm(player, target, group, getContent());
//
//    }
//
//    private ItemStack[] loadCurrentContent() throws IOException {
//        // TODO: return plugin.getInventoryStoreService().getPlayerInventoryContents(target.getUniqueId(), group, player.getInventory().getSize());
//        return new ItemStack[]{};
//    }
//
//    private void createConfirm(BedrockPlayer player, BedrockPlayer target, String group, ItemStack[] contents) {
//        ConfirmOverrideInventory confirm = new ConfirmOverrideInventory(CNInventoriesPlugin.getInstance());
//        confirm.store("sender", new ConfirmStorable<Object>(player));
//        confirm.store("target", new ConfirmStorable<Object>(target.getUniqueId()));
//        confirm.store("group", new ConfirmStorable<Object>(group));
//        confirm.store("contents", new ConfirmStorable<Object>(contents));
//
//        ConfirmRegistry.getInstance().put(player, confirm);
//        Messages.Confirm.OverrideInventory(player);
//    }
//}
