package de.cubenation.cninventories.listener;

import de.cubenation.cninventories.CNInventoriesPlugin;
import de.cubenation.cninventories.helper.PlayerStatHelper;
import de.cubenation.cninventories.manager.DebugManager;
import de.cubenation.cninventories.service.GroupService;
import de.cubenation.cninventories.service.InventoryService;
import dev.projectshard.core.FoundationPlugin;
import dev.projectshard.core.annotations.Inject;
import dev.projectshard.core.lifecycle.Component;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

public class PlayerListener extends Component implements Listener {

    @Inject
    private GroupService groupService;

    @Inject
    private InventoryService inventoryService;

    public PlayerListener(FoundationPlugin plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerChangeWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();

        DebugManager.getInstance().log("<Event> PlayerChangedWorldEvent for "+player.getDisplayName());

        World from = event.getFrom();
        World world = player.getWorld();
        GameMode mode = player.getGameMode();

        // always save the current inventory
        String prevGroup;
        // save current inventory to world group
        prevGroup = groupService.getWorldGroup(from, mode);
        DebugManager.getInstance().log("<Save> group '"+prevGroup+"' for "+player.getDisplayName());
        inventoryService.save(player, prevGroup);

        // load new world group
        String newGroup = groupService.getWorldGroup(world, mode);
        if (prevGroup.equals(newGroup))
            return; // same group -> no need to reapply inv

        // apply new world group
        DebugManager.getInstance().log("<Apply> group '"+newGroup+"' for "+player.getDisplayName());
        if (!inventoryService.apply(player, newGroup)) {
            player.getInventory().clear();
            PlayerStatHelper.resetPlayerStats(player);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerGamemodeChange(PlayerGameModeChangeEvent event) {
        Player player = event.getPlayer();

        DebugManager.getInstance().log("<Event> PlayerGameModeChangeEvent for "+player.getDisplayName());

        World world = player.getWorld();
        GameMode from = player.getGameMode();
        GameMode mode = event.getNewGameMode();

        String prevGroup = groupService.getCurrentGroupForPlayerManual(player, world, from);
        String newGroup = groupService.getWorldGroup(world, mode);
        if (prevGroup.equals(newGroup))
            return; // same group -> no need to reapply inv

        // save the current inventory
        DebugManager.getInstance().log("<Save> group '"+prevGroup+"' for "+player.getDisplayName());
        inventoryService.save(player, prevGroup);

        // apply new world group
        DebugManager.getInstance().log("<Apply> group '"+newGroup+"' for "+player.getDisplayName());
        if (!inventoryService.apply(player, newGroup)) {
            player.getInventory().clear();
            PlayerStatHelper.resetPlayerStats(player);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        DebugManager.getInstance().log("<Event> PlayerQuitEvent for "+player.getDisplayName());

        World world = player.getWorld();
        GameMode mode = player.getGameMode();

        String prevGroup = groupService.getWorldGroup(world, mode);
        DebugManager.getInstance().log("<Save> group '"+prevGroup+"' for "+player.getDisplayName());
        inventoryService.save(player, prevGroup);
        return;
    }
}
