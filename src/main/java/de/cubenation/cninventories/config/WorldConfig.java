package de.cubenation.cninventories.config;

import de.cubenation.api.bedrock.BasePlugin;
import de.cubenation.api.bedrock.service.config.CustomConfigurationFile;
import net.cubespace.Yamler.Config.Path;
import org.bukkit.GameMode;

import java.io.File;
import java.util.*;

public class WorldConfig extends CustomConfigurationFile {

    public static String getFilename() {
        return "worlds.yml";
    }

    public WorldConfig(BasePlugin plugin) {
        this(plugin, getFilename());
    }

    public WorldConfig(BasePlugin plugin, String filename) {
        CONFIG_FILE = new File(plugin.getDataFolder(), filename);
    }

    @Path("groups")
    private Map<String, Map<String, String>> groups = new HashMap<>();

    public Set<String> getWorldNames() {
        return groups.keySet();
    }

    public Map<String, Map<String, String>> getWorldGroups() {
        return this.groups;
    }

    public List<String> getGroups() {
        Set<String> res = new LinkedHashSet<>();
        for(Map.Entry<String, Map<String, String>> entry : groups.entrySet())
            res.addAll(entry.getValue().values());
        for(GameMode mode : GameMode.values())
            res.add("default/"+mode.name().toLowerCase());
        return new ArrayList<>(res);
    }

}
