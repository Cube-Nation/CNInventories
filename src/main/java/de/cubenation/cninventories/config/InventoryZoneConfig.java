package de.cubenation.cninventories.config;

import de.cubenation.api.bedrock.BasePlugin;
import de.cubenation.api.bedrock.service.config.CustomConfigurationFile;
import de.cubenation.cninventories.CNInventoriesPlugin;
import de.cubenation.cninventories.model.InventoryZone;
import de.cubenation.cninventories.model.converter.InventoryZoneConverter;
import net.cubespace.Yamler.Config.Comment;
import net.cubespace.Yamler.Config.InvalidConfigurationException;
import net.cubespace.Yamler.Config.InvalidConverterException;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

public class InventoryZoneConfig extends CustomConfigurationFile {

    public static String getFilename() {
        return "zones.yaml";
    }

    public InventoryZoneConfig(BasePlugin plugin) {
        this(plugin, getFilename());
    }

    public InventoryZoneConfig(BasePlugin plugin, String filename) {
        CONFIG_FILE = new File(plugin.getDataFolder(), filename);
        try {
            addConverter(InventoryZoneConverter.class);
        } catch (InvalidConverterException e) {
            e.printStackTrace();
        }
    }

    @Comment("List of stored inventory zones")
    private ArrayList<InventoryZone> zones = new ArrayList<>();

    public ArrayList<InventoryZone> getZones() {
        return zones;
    }

    public boolean storeZone(InventoryZone teleporter) {
        zones.add(teleporter);
        return saveAndReload();
    }

    public boolean removeZone(UUID uuid) {
        boolean success = this.zones.removeIf(x -> x.getUuid().equals(uuid));

        if(!success)
            return false;

        return saveAndReload();
    }

    public boolean updateGroup(UUID uuid, String newGroup) {
        try {
            this.zones.stream()
                    .filter(x -> x.getUuid().equals(uuid))
                    .findAny()
                    .orElseThrow(IllegalArgumentException::new)
                    .setGroup(newGroup);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return saveAndReload();
    }

    private boolean saveAndReload() {
        try {
            this.save();
            this.reload();

            CNInventoriesPlugin.getInstance().getInventoryZoneService().reload();

            return true;
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
            return false;
        }
    }
}
