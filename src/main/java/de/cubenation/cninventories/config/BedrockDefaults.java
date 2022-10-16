package de.cubenation.cninventories.config;

import de.cubenation.bedrock.bukkit.api.BasePlugin;
import net.md_5.bungee.api.ChatColor;

import java.io.File;

@SuppressWarnings("unused")
public class BedrockDefaults extends de.cubenation.bedrock.core.config.BedrockDefaults {

    public BedrockDefaults(BasePlugin plugin) {
        CONFIG_FILE = new File(plugin.getDataFolder(), de.cubenation.bedrock.core.config.BedrockDefaults.getFilename());
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
