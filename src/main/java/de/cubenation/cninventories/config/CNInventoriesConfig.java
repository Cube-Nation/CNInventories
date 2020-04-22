package de.cubenation.cninventories.config;

import de.cubenation.api.bedrock.BasePlugin;
import de.cubenation.api.bedrock.service.config.CustomConfigurationFile;
import net.cubespace.Yamler.Config.Comment;
import net.cubespace.Yamler.Config.Path;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CNInventoriesConfig extends CustomConfigurationFile {

    public static String getFilename() {
        return "config.yml";
    }

    public CNInventoriesConfig(BasePlugin plugin) {
        this(plugin, getFilename());
    }

    public CNInventoriesConfig(BasePlugin plugin, String filename) {
        CONFIG_FILE = new File(plugin.getDataFolder(), filename);
    }

    @Comment("Time between inventory autosaves in minutes (-1 = no autosave)")
    @Path("autosave")
    private int autosaveTime = 30;

    @Path("groups")
    private Map<String, Map<String, String>> groups = new HashMap<>();

    public Set<String> getWorldNames() {
        return groups.keySet();
    }

    public Map<String, Map<String, String>> getGroups() {
        return this.groups;
    }

    public int getAutosaveTime() {
        return autosaveTime;
    }
}
