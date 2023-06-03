package de.cubenation.cninventories;

import de.cubenation.cninventories.config.CNInventoriesConfig;
import de.cubenation.cninventories.config.WorldConfig;
import de.cubenation.cninventories.config.locale.de_DE;
import de.cubenation.cninventories.listener.PlayerListener;
import de.cubenation.cninventories.service.GroupService;
import de.cubenation.cninventories.service.InventoryService;
import dev.projectshard.core.RuntimeContext;
import dev.projectshard.core.annotations.ConfigurationFile;
import dev.projectshard.core.annotations.Inject;
import dev.projectshard.core.annotations.ManagedInstance;
import dev.projectshard.mc.bukkit.core.BasePlugin;
import org.bukkit.scheduler.BukkitRunnable;

@ConfigurationFile(CNInventoriesConfig.class)
@ConfigurationFile(WorldConfig.class)
@ConfigurationFile(de_DE.class)
@ManagedInstance(GroupService.class)
@ManagedInstance(InventoryService.class)
public class CNInventoriesPlugin extends BasePlugin {

    private static CNInventoriesPlugin instance;

    public static CNInventoriesPlugin getInstance() {
        return instance;
    }

    @Inject
    private CNInventoriesConfig config;

    @Inject
    private InventoryService inventoryService;

    @Override
    public void onPreEnable() {
        instance = this;
    }

    @Override
    public void onPostEnable() {
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
//        getServer().getPluginManager().registerEvents(new InventoryListener(this), this);

        scheduleTasks();
    }

    @Override
    public RuntimeContext getRuntimeContext() {
        return null;
    }

    private void scheduleTasks() {
        // Inventory Saver every 30min
        if (config.getAutosaveTime() > 0) {
            // TODO: Replace with Shard Executor
            new BukkitRunnable() {
                @Override
                public void run() {
                    inventoryService.saveAll(false);
                }
            }.runTaskTimer(this, 20 * 60 * config.getAutosaveTime(), 20 * 60 * config.getAutosaveTime());
        }
    }
}
