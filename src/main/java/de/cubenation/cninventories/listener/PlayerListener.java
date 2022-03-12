package de.cubenation.cninventories.listener;

import de.cubenation.cninventories.CNInventoriesPlugin;
import de.cubenation.cninventories.helper.PlayerStatHelper;
import de.cubenation.cninventories.manager.DebugManager;
import de.cubenation.cninventories.model.InventoryZone;
import de.cubenation.cninventories.service.GroupService;
import de.cubenation.cninventories.service.InventoryService;
import de.cubenation.cninventories.service.InventoryZoneService;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

public class PlayerListener implements Listener {

    private final CNInventoriesPlugin plugin = CNInventoriesPlugin.getInstance();
    private final GroupService groupService = plugin.getGroupService();
    private final InventoryZoneService zoneService = plugin.getInventoryZoneService();
    private final InventoryService invService = plugin.getInventoryStoreService();

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerChangeWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();

        DebugManager.getInstance().log("<Event> PlayerChangedWorldEvent for "+player.getDisplayName());

        World from = event.getFrom();
        World world = player.getWorld();
        GameMode mode = player.getGameMode();

        // always save the current inventory
        String prevGroup;
        InventoryZone prevZone = zoneService.getZoneForPlayer(player);
        if(prevZone == null) {
            // save current inventory to world group
            prevGroup = plugin.getGroupService().getWorldGroup(from, mode);
            DebugManager.getInstance().log("<Save> group '"+prevGroup+"' for "+player.getDisplayName());
            invService.save(player, prevGroup);
        } else {
            // save current inventory to zone group
            prevGroup = prevZone.getGroup();
            DebugManager.getInstance().log("<Save> group '"+prevGroup+"' for "+player.getDisplayName());
            invService.save(player, prevGroup);
            zoneService.removePlayerFromInvZone(player);
        }

        // load new world group
        String newGroup = CNInventoriesPlugin.getInstance().getGroupService().getWorldGroup(world, mode);
        if(prevGroup.equals(newGroup))
            return; // same group -> no need to reapply inv

        // apply new world group
        DebugManager.getInstance().log("<Apply> group '"+newGroup+"' for "+player.getDisplayName());
        if(!invService.apply(player, newGroup)) {
            player.getInventory().clear();
            PlayerStatHelper.resetPlayerStats(player);
        }

        // check for inventory zones
        handlePlayerChangeLocation(player);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerGamemodeChange(PlayerGameModeChangeEvent event) {
        Player player = event.getPlayer();

        DebugManager.getInstance().log("<Event> PlayerGameModeChangeEvent for "+player.getDisplayName());

        World world = player.getWorld();
        GameMode from = player.getGameMode();
        GameMode mode = event.getNewGameMode();

        if(zoneService.getZoneForPlayer(player) != null)
            return; // player is registered in zone ignoring gamemode changes

        String prevGroup = groupService.getCurrentGroupForPlayerManual(player, world, from);
        String newGroup = groupService.getWorldGroup(world, mode);
        if(prevGroup.equals(newGroup))
            return; // same group -> no need to reapply inv

        // save the current inventory
        DebugManager.getInstance().log("<Save> group '"+prevGroup+"' for "+player.getDisplayName());
        invService.save(player, prevGroup);

        // apply new world group
        DebugManager.getInstance().log("<Apply> group '"+newGroup+"' for "+player.getDisplayName());
        if(!invService.apply(player, newGroup)) {
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

        InventoryZone prevZone = zoneService.getZoneForPlayer(player);
        if(prevZone == null) {
            // save current inventory to world group
            String prevGroup = plugin.getGroupService().getWorldGroup(world, mode);
            DebugManager.getInstance().log("<Save> group '"+prevGroup+"' for "+player.getDisplayName());
            invService.save(player, prevGroup);
            return;
        }

        String prevGroup = prevZone.getGroup();
        DebugManager.getInstance().log("<Save> group '"+prevGroup+"' for "+player.getDisplayName());
        invService.save(player, prevGroup);
        zoneService.removePlayerFromInvZone(player);

        // load world group (in case zone gets deleted while offline)
        String worldGroup = groupService.getWorldGroup(world, mode);
        if(prevGroup.equals(worldGroup))
            return; // same group -> no need to reapply inv

        // apply world group
        DebugManager.getInstance().log("<Apply> group '"+worldGroup+"' for "+player.getDisplayName());
        if(!invService.apply(player, worldGroup)) {
            player.getInventory().clear();
            PlayerStatHelper.resetPlayerStats(player);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        DebugManager.getInstance().log("<Event> PlayerJoinEvent for "+player.getDisplayName());

//        World world = player.getWorld();
//        GameMode mode = player.getGameMode();
//
//        String worldGroup = groupService.getWorldGroup(world, mode);
//
//        // safe apply inventory in case of an empty inventory after joining
//        if(invService.hasPlayerEmptyInventory(player)) {
//            DebugManager.getInstance().log("<Apply> group '"+worldGroup+"' for "+player.getDisplayName());
//            invService.safeApply(player, worldGroup);
//        }

        // check for inventory zones
        handlePlayerChangeLocation(player);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerMoveEvent(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        //DebugManager.getInstance().log("<Event> PlayerMoveEvent for "+player.getDisplayName());

        // check for inventory zones
        handlePlayerChangeLocation(player);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();

        DebugManager.getInstance().log("<Event> PlayerTeleportEvent for "+player.getDisplayName());

        // check for inventory zones
        handlePlayerChangeLocation(player);
    }

    public void handlePlayerChangeLocation(Player player) {
        InventoryZone prevZone = zoneService.getZoneForPlayer(player);
        InventoryZone newZone = zoneService.getZoneAtLocation(player.getLocation());

        if(prevZone != null) {
            // > player was in inv-zone >
            if(newZone != null) {
                // > player changed zone >
                if(!prevZone.getGroup().equals(newZone.getGroup())) {
                    // inv-group changed -> load new inv-group
                    // save prev zone inv
                    DebugManager.getInstance().log("<Save> group '"+prevZone.getGroup()+"' for "+player.getDisplayName());
                    invService.save(player, prevZone.getGroup());
                    // load new zone inv
                    DebugManager.getInstance().log("<Apply> group '"+newZone.getGroup()+"' for "+player.getDisplayName());
                    invService.apply(player, newZone.getGroup());
                }

                zoneService.removePlayerFromInvZone(player);
                zoneService.addPlayerToInvZone(player, newZone);
            } else {
                // > player left zone >
                zoneService.removePlayerFromInvZone(player);
                // save prev zone inv
                DebugManager.getInstance().log("<Save> group '"+prevZone.getGroup()+"' for "+player.getDisplayName());
                invService.save(player, prevZone.getGroup());
                // load world inv
                String prevGroup = CNInventoriesPlugin.getInstance().getGroupService().getWorldGroup(player.getWorld(), player.getGameMode());
                DebugManager.getInstance().log("<Apply> group '"+prevGroup+"' for "+player.getDisplayName());
                invService.apply(player, prevGroup);
            }
        } else if(newZone != null) {
            // > player wasn't in inv-zone >
            zoneService.addPlayerToInvZone(player, newZone);

            // save world inv
            String prevGroup = CNInventoriesPlugin.getInstance().getGroupService().getWorldGroup(player.getWorld(), player.getGameMode());
            DebugManager.getInstance().log("<Save> group '"+prevGroup+"' for "+player.getDisplayName());
            invService.save(player, prevGroup);
            // load new zone inv
            DebugManager.getInstance().log("<Apply> group '"+newZone.getGroup()+"' for "+player.getDisplayName());
            invService.apply(player, newZone.getGroup());
        }
    }
}
