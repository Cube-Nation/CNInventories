package de.cubenation.cninventories.listener;

import de.cubenation.cninventories.model.GroupViewerInventoryHolder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public class InventoryListener implements Listener {

    @EventHandler(priority = EventPriority.NORMAL)
    public void onInventoryClose(InventoryCloseEvent event) {
        Inventory inv = event.getInventory();
        if(inv.getHolder() != null && inv.getHolder() instanceof GroupViewerInventoryHolder) {
            GroupViewerInventoryHolder holder = (GroupViewerInventoryHolder) inv.getHolder();
            holder.performClose();
        }
    }
}
