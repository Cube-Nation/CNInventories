package de.cubenation.cninventories;

import de.cubenation.bedrock.bukkit.api.BasePlugin;
import de.cubenation.bedrock.core.annotation.ConfigurationFile;
import de.cubenation.bedrock.core.annotation.Service;
import de.cubenation.cninventories.config.CNInventoriesConfig;
import de.cubenation.cninventories.config.WorldConfig;
import de.cubenation.cninventories.config.locale.de_DE;
import de.cubenation.cninventories.listener.InventoryListener;
import de.cubenation.cninventories.listener.PlayerListener;
import de.cubenation.cninventories.service.GroupService;
import de.cubenation.cninventories.service.InventoryService;
import org.bukkit.scheduler.BukkitRunnable;

@ConfigurationFile(CNInventoriesConfig.class)
@ConfigurationFile(WorldConfig.class)
@ConfigurationFile(de_DE.class)
@Service(GroupService.class)
@Service(InventoryService.class)
public class CNInventoriesPlugin extends BasePlugin {

    private static CNInventoriesPlugin instance;

    public static CNInventoriesPlugin getInstance() {
        return instance;
    }

    @Override
    public void onPreEnable() {
        instance = this;
    }

    @Override
    public void onPostEnable() {
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);

        scheduleTasks();
    }

    private void scheduleTasks() {
        // Inventory Saver every 30min
        CNInventoriesConfig config = (CNInventoriesConfig) this.getConfigService().getConfig(CNInventoriesConfig.class);
        if(config.getAutosaveTime() > 0) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    getInventoryStoreService().saveAll(false);
                }
            }.runTaskTimer(this, 20 * 60 * config.getAutosaveTime(), 20 * 60 * config.getAutosaveTime());
        }
    }

    public GroupService getGroupService() {
        return (GroupService) getServiceManager().getService(GroupService.class);
    }

    public InventoryService getInventoryStoreService() {
        return (InventoryService) getServiceManager().getService(InventoryService.class);
    }
}
