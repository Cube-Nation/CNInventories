//package de.cubenation.cninventories.listener;
//
//import dev.projectshard.core.FoundationPlugin;
//import org.bukkit.event.EventHandler;
//import org.bukkit.event.EventPriority;
//import org.bukkit.event.Listener;
//import org.bukkit.event.inventory.InventoryCloseEvent;
//import org.bukkit.inventory.Inventory;
//import org.bukkit.inventory.InventoryHolder;
//
//public class InventoryListener implements Listener {
//
//    private final FoundationPlugin plugin;
//
//    public InventoryListener(FoundationPlugin plugin) {
//        this.plugin = plugin;
//    }
//
//    @EventHandler(priority = EventPriority.LOW)
//    public void onInventoryClose(InventoryCloseEvent event) {
//        InventoryHolder holder = event.getInventory().getHolder();
//        assert holder instanceof FakeInventoryHolder;
//        BedrockInventory inventory = ((FakeInventoryHolder) holder).getBedrockInventory();
//        assert inventory != null;
//
//        int viewers = plugin.getInventorySessionManager().getViewers(inventory).size();
//        if (viewers == 0) {
//
//        }
//    }
//}
