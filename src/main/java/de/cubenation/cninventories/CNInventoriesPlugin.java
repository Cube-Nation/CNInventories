package de.cubenation.cninventories;

import de.cubenation.api.bedrock.BasePlugin;
import de.cubenation.api.bedrock.annotation.CommandHandler;
import de.cubenation.api.bedrock.annotation.ConfigurationFile;
import de.cubenation.api.bedrock.annotation.Service;
import de.cubenation.cninventories.command.InvZoneModifyGroupCommand;
import de.cubenation.cninventories.command.InvZoneRemoveCommand;
import de.cubenation.cninventories.command.InvZoneSetCommand;
import de.cubenation.cninventories.config.CNInventoriesConfig;
import de.cubenation.cninventories.config.InventoryZoneConfig;
import de.cubenation.cninventories.config.WorldConfig;
import de.cubenation.cninventories.listener.PlayerListener;
import de.cubenation.cninventories.service.GroupService;
import de.cubenation.cninventories.service.InventoryService;
import de.cubenation.cninventories.service.InventoryZoneService;
import org.bukkit.scheduler.BukkitRunnable;

@CommandHandler(Command = "invzone", Handlers = {
        InvZoneSetCommand.class,
        InvZoneRemoveCommand.class,
        InvZoneModifyGroupCommand.class,
})
@ConfigurationFile(CNInventoriesConfig.class)
@ConfigurationFile(WorldConfig.class)
@ConfigurationFile(InventoryZoneConfig.class)
@Service(GroupService.class)
@Service(InventoryService.class)
@Service(InventoryZoneService.class)
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
    protected void onPostEnable() throws Exception {
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);

        scheduleTasks();
    }

    private void scheduleTasks() {
        // Inventory Saver every 30min
        CNInventoriesConfig config = (CNInventoriesConfig) this.getConfigService().getConfig(CNInventoriesConfig.class);
        if(config.getAutosaveTime() > 0) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    getInventoryStoreService().saveAll();
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

    public InventoryZoneService getInventoryZoneService() {
        return (InventoryZoneService) getServiceManager().getService(InventoryZoneService.class);
    }
}
