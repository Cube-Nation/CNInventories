package de.cubenation.cninventories.config;

import dev.projectshard.core.FoundationPlugin;
import dev.projectshard.core.config.CustomConfigFile;
import net.cubespace.Yamler.Config.Comment;
import net.cubespace.Yamler.Config.Path;

import java.io.File;

public class CNInventoriesConfig extends CustomConfigFile {

    public static String getFilename() {
        return "config.yml";
    }

    public CNInventoriesConfig(FoundationPlugin plugin) {
        this(plugin, getFilename());
    }

    public CNInventoriesConfig(FoundationPlugin plugin, String filename) {
        CONFIG_FILE = new File(plugin.getPluginFolder(), filename);
    }

    @Comment("Time between inventory autosaves in minutes (-1 = no autosave)")
    @Path("autosave")
    private int autosaveTime = 30;

    public int getAutosaveTime() {
        return autosaveTime;
    }
}
