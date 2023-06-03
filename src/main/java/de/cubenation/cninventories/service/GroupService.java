package de.cubenation.cninventories.service;

import de.cubenation.cninventories.CNInventoriesPlugin;
import de.cubenation.cninventories.config.WorldConfig;
import dev.projectshard.core.FoundationPlugin;
import dev.projectshard.core.annotations.Inject;
import dev.projectshard.core.exceptions.InitializationException;
import dev.projectshard.core.lifecycle.Reloadable;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

public class GroupService implements Reloadable {

    private final FoundationPlugin plugin;

    @Inject
    private WorldConfig worldConfig;

    @Inject
    private InventoryService inventoryService;

    private Map<String, Map<String, String>> groups = new HashMap<>();

    public GroupService(FoundationPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void init() throws InitializationException {
        // load new groups
        groups.clear();
        groups = worldConfig.getWorldGroups();
    }

    @Override
    public void reload() {
        plugin.log(Level.FINE, "Reloading groups...");

        // save old Inventories
        inventoryService.saveAll(true);

        // load new groups
        groups.clear();
        groups = worldConfig.getWorldGroups();

        // apply groups
        // deactivated -> may result in inventory loss...
        //CNInventoriesPlugin.getInstance().getInventoryStoreService().applyAll();

        plugin.log(Level.FINE, "Reloading groups done!");
    }


    public Set<String> getWorldNames() {
        return groups.keySet();
    }

    public String getWorldGroup(World world, GameMode mode) {
        if (!isKnownWorld(world) || !isKnownMode(world, mode))
            return "default/"+mode.name().toLowerCase();

        return groups.get(world.getName()).get(mode.name().toLowerCase());
    }

    public boolean isKnownWorld(World world) {
        return this.getWorldNames().contains(world.getName());
    }

    public boolean isKnownMode(World world, GameMode mode) {
        return groups.get(world.getName()).keySet().contains(mode.name().toLowerCase());
    }

    public String getCurrentGroupForPlayerManual(Player player, World world, GameMode mode) {
        return getWorldGroup(world, mode);
    }

    public String getCurrentGroupForPlayer(Player player) {
        return getCurrentGroupForPlayerManual(player, player.getWorld(), player.getGameMode());
    }
}
