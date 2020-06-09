package de.cubenation.cninventories.config;

import de.cubenation.api.bedrock.BasePlugin;
import net.md_5.bungee.api.ChatColor;

import java.io.File;

@SuppressWarnings("unused")
public class BedrockDefaults extends de.cubenation.api.bedrock.config.BedrockDefaults {

    public BedrockDefaults(BasePlugin plugin) {
        CONFIG_FILE = new File(plugin.getDataFolder(), de.cubenation.api.bedrock.config.BedrockDefaults.getFilename());
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
