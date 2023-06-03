package de.cubenation.cninventories.config;

import dev.projectshard.core.FoundationPlugin;
import dev.projectshard.core.configs.ShardDefaultsConfig;
import net.md_5.bungee.api.ChatColor;

import java.io.File;

@SuppressWarnings("unused")
public class ShardDefaults extends ShardDefaultsConfig {

    public ShardDefaults(FoundationPlugin plugin) {
        CONFIG_FILE = new File(plugin.getPluginFolder(), ShardDefaultsConfig.getFilename());
        CONFIG_HEADER = getHeader();

        this.setLocalizationLocale("de_DE");

        this.setPermissionPrefix("cninventories");


        this.setColorSchemeName("CUSTOM");

        this.setColorSchemePrimary(ChatColor.RED);
        this.setColorSchemeSecondary(ChatColor.GOLD);
        this.setColorSchemeFlag(ChatColor.DARK_RED);
        this.setColorSchemeText(ChatColor.WHITE);
    }

}
