package de.cubenation.cninventories.manager;

import de.cubenation.cninventories.CNInventoriesPlugin;

import java.util.logging.Level;

public class DebugManager {

    private static DebugManager instance;

    public static DebugManager getInstance() {
        if(instance == null)
            instance = new DebugManager();

        return instance;
    }

    private CNInventoriesPlugin plugin = CNInventoriesPlugin.getInstance();
    private boolean debugModeEnabled = false;

    public boolean isDebugModeEnabled() {
        return debugModeEnabled;
    }

    public boolean toggleDebugMode() {
        debugModeEnabled = !debugModeEnabled;
        return debugModeEnabled;
    }

    public void log(String text) {
        if(debugModeEnabled)
            plugin.log(Level.INFO, "DEBUG MODE: "+text);
    }
}
