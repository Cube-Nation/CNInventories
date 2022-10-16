package de.cubenation.cninventories.config;

import de.cubenation.bedrock.core.FoundationPlugin;
import de.cubenation.bedrock.core.config.CustomConfigurationFile;
import net.cubespace.Yamler.Config.Comment;
import net.cubespace.Yamler.Config.Path;

import java.io.File;

public class CNInventoriesConfig extends CustomConfigurationFile {

    public static String getFilename() {
        return "config.yml";
    }

    public CNInventoriesConfig(FoundationPlugin plugin) {
        this(plugin, getFilename());
    }

    public CNInventoriesConfig(FoundationPlugin plugin, String filename) {
        CONFIG_FILE = new File(plugin.getDataFolder(), filename);
    }

    @Comment("Time between inventory autosaves in minutes (-1 = no autosave)")
    @Path("autosave")
    private int autosaveTime = 30;

    public int getAutosaveTime() {
        return autosaveTime;
    }
}
