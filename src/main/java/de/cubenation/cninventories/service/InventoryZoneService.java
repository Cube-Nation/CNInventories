package de.cubenation.cninventories.service;

import de.cubenation.api.bedrock.BasePlugin;
import de.cubenation.api.bedrock.service.AbstractService;
import de.cubenation.cninventories.CNInventoriesPlugin;
import de.cubenation.cninventories.config.InventoryZoneConfig;
import de.cubenation.cninventories.helper.LocationHelper;
import de.cubenation.cninventories.model.InventoryZone;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InventoryZoneService extends AbstractService {

    private HashMap<World, ArrayList<InventoryZone>> zonesPerWorld = new HashMap<>();
    private HashMap<UUID, InventoryZone> playerWithinZone = new HashMap<>();

    public InventoryZoneService(BasePlugin plugin) {
        super(plugin);
    }

    @Override
    public void init() {
        this.reload();
    }

    @Override
    public void reload() {
        InventoryService invService = CNInventoriesPlugin.getInstance().getInventoryStoreService();
        // save all players in zones
        for(Map.Entry<UUID, InventoryZone> entry : playerWithinZone.entrySet()) {
            Player player = Bukkit.getPlayer(entry.getKey());
            invService.save(player, entry.getValue().getGroup());

            String worldGroup = CNInventoriesPlugin.getInstance().getGroupService().getWorldGroup(player.getWorld(), player.getGameMode());
            invService.apply(player, worldGroup);
        }
        playerWithinZone.clear();

        // reload zones
        InventoryZoneConfig config = (InventoryZoneConfig) plugin.getConfigService().getConfig(InventoryZoneConfig.class);

        /**
        for(Map.Entry<UUID, InventoryZone> entry : playerWithinZone.entrySet()) {
            String oldGroup = entry.getValue().getGroup();
            String newGroup = config.getZones().stream().filter(x -> x.getUuid().equals(entry.getValue().getUuid())).findAny().orElseThrow(IllegalArgumentException::new).getGroup();
            if(oldGroup.equals(newGroup))
                continue;
            invService.apply(Bukkit.getPlayer(entry.getKey()), entry.getValue().getGroup());
        }
         **/

        // finally apply
        setZonesPerWorld(config);
    }

    private void setZonesPerWorld(InventoryZoneConfig config) {
        HashMap<World, ArrayList<InventoryZone>> list = new HashMap<>();

        for (InventoryZone zone : config.getZones()) {
            ArrayList<InventoryZone> zones = null;
            if (list.containsKey(zone.getFromWorld())) {
                zones = list.get(zone.getFromWorld());
            } else {
                zones = new ArrayList<>();
            }
            zones.add(zone);
            list.put(zone.getFromWorld(), zones);
        }

        zonesPerWorld = list;
    }

    public void addPlayerToInvZone(Player player, InventoryZone zone) {
        this.playerWithinZone.put(player.getUniqueId(), zone);
    }

    public void removePlayerFromInvZone(Player player) {
        this.playerWithinZone.remove(player.getUniqueId());
    }

    public InventoryZone getZoneAtLocation(Location playerLocation) {

        if (!this.zonesPerWorld.containsKey(playerLocation.getWorld())) {
            return null;
        }

        ArrayList<InventoryZone> possibleLocations = this.zonesPerWorld.get(playerLocation.getWorld());

        for (InventoryZone possibleLocation : possibleLocations) {
            if (LocationHelper.isBetweenLocations(playerLocation, possibleLocation.getMinLocation(), possibleLocation.getMaxLocation())) {
                return possibleLocation;
            }
        }

        return null;
    }

    public InventoryZone getZoneForPlayer(Player player) {
        return playerWithinZone.get(player.getUniqueId());
    }
}
