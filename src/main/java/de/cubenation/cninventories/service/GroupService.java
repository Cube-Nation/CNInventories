package de.cubenation.cninventories.service;

import de.cubenation.api.bedrock.BasePlugin;
import de.cubenation.api.bedrock.exception.ServiceInitException;
import de.cubenation.api.bedrock.service.AbstractService;
import de.cubenation.cninventories.CNInventoriesPlugin;
import de.cubenation.cninventories.config.WorldConfig;
import de.cubenation.cninventories.model.InventoryZone;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GroupService extends AbstractService {

    CNInventoriesPlugin plugin = CNInventoriesPlugin.getInstance();

    private Map<String, Map<String, String>> groups = new HashMap<>();

    public GroupService(BasePlugin plugin) {
        super(plugin);
    }

    @Override
    public void init() throws ServiceInitException {
        // load new groups
        groups.clear();
        WorldConfig config = (WorldConfig) plugin.getConfigService().getConfig(WorldConfig.class);
        groups = config.getWorldGroups();
    }

    @Override
    public void reload() {
        // save old Inventories
        CNInventoriesPlugin.getInstance().getInventoryStoreService().saveAll(true);

        // load new groups
        groups.clear();
        WorldConfig config = (WorldConfig) plugin.getConfigService().getConfig(WorldConfig.class);
        groups = config.getWorldGroups();

        // apply groups
        // deactivated -> may result in inventory loss...
        //CNInventoriesPlugin.getInstance().getInventoryStoreService().applyAll();
    }


    public Set<String> getWorldNames() {
        return groups.keySet();
    }

    public String getWorldGroup(World world, GameMode mode) {
        if(!isKnownWorld(world) || !isKnownMode(world, mode))
            return "default/"+mode.name().toLowerCase();

        return this.groups.get(world.getName()).get(mode.name().toLowerCase());
    }

    public boolean isKnownWorld(World world) {
        return this.getWorldNames().contains(world.getName());
    }

    public boolean isKnownMode(World world, GameMode mode) {
        return this.groups.get(world.getName()).keySet().contains(mode.name().toLowerCase());
    }

    public String getCurrentGroupForPlayerManual(Player player, World world, GameMode mode) {
        InventoryZone zone = plugin.getInventoryZoneService().getZoneForPlayer(player);
        return zone != null ? zone.getGroup() : getWorldGroup(world, mode);
    }

    public String getCurrentGroupForPlayer(Player player) {
        return getCurrentGroupForPlayerManual(player, player.getWorld(), player.getGameMode());
    }
}
