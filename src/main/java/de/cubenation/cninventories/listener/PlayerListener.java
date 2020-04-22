package de.cubenation.cninventories.listener;

import de.cubenation.cninventories.CNInventoriesPlugin;
import de.cubenation.cninventories.helper.PlayerStatHelper;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerChangeWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();

        World from = event.getFrom();
        World world = player.getWorld();
        GameMode mode = player.getGameMode();

        CNInventoriesPlugin.getInstance().getInventoryStoreService().save(player, from, mode);
        if(!CNInventoriesPlugin.getInstance().getInventoryStoreService().apply(player, world, mode)) {
            player.getInventory().clear();
            PlayerStatHelper.resetPlayerStats(player);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerGamemodeChange(PlayerGameModeChangeEvent event) {
        Player player = event.getPlayer();

        World world = player.getWorld();
        GameMode from = player.getGameMode();
        GameMode mode = event.getNewGameMode();

        CNInventoriesPlugin.getInstance().getInventoryStoreService().save(player, world, from);
        player.getInventory().clear();
        if(!CNInventoriesPlugin.getInstance().getInventoryStoreService().apply(player, world, mode)) {
            player.getInventory().clear();
            PlayerStatHelper.resetPlayerStats(player);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        World world = player.getWorld();
        GameMode mode = player.getGameMode();

        CNInventoriesPlugin.getInstance().getInventoryStoreService().save(player, world, mode);
    }

}
