package de.cubenation.cninventories.command;

import de.cubenation.api.bedrock.BasePlugin;
import de.cubenation.api.bedrock.annotation.Description;
import de.cubenation.api.bedrock.annotation.Permission;
import de.cubenation.api.bedrock.annotation.SubCommand;
import de.cubenation.api.bedrock.command.Command;
import de.cubenation.api.bedrock.command.CommandRole;
import de.cubenation.api.bedrock.service.command.CommandManager;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Map;

@Description("command.cninventories.convert.desc")
@SubCommand("convert")
@Permission(Name = "inventory.convert", Role = CommandRole.OWNER)
public class CNInventoriesConvertCommand extends Command {

    public CNInventoriesConvertCommand(BasePlugin plugin, CommandManager commandManager) {
        super(plugin, commandManager);
    }


    public void execute(CommandSender commandSender, String[] args) {
        plugin.getLogger().info("Starting to convert xInventories files to CNInventories...");

        File fileBaseXi = new File(plugin.getDataFolder().getParentFile(), "xInventories");
        File fileGroupsXi = new File(fileBaseXi, "groups");
        if(!fileGroupsXi.exists()) {
            plugin.getLogger().severe("xInventories group folder not existent!");
            return;
        }

        File fileBaseCi = plugin.getDataFolder();
        File fileGroupsCi = new File(fileBaseCi, "groups");
        if(!fileGroupsCi.exists())
            fileGroupsCi.mkdirs();

        try {
            Files.walkFileTree(fileGroupsXi.toPath(),
                    new SimpleFileVisitor<Path>() {
                        @Override
                        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                            String relative = fileGroupsXi.toURI().relativize(dir.toUri()).getPath();
                            //if(!relative.equals("") && !relative.startsWith("default/") && !relative.startsWith("museum/"))
                            //    return FileVisitResult.SKIP_SUBTREE;
                            plugin.getLogger().info("Converting "+relative+"...");
                            new File(fileGroupsCi, relative).mkdirs();
                            return FileVisitResult.CONTINUE;
                        }

                        @Override
                        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                            String relative = fileGroupsXi.toURI().relativize(dir.toUri()).getPath();
                            plugin.getLogger().info("Successfully converted "+relative+"!");
                            return FileVisitResult.CONTINUE;
                        }

                        @Override
                        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                            String relative = fileGroupsXi.toURI().relativize(file.toUri()).getPath();
                            plugin.getLogger().info("* converting "+relative+"...");

                            File fileXi = new File(fileGroupsXi, relative);
                            File fileCi = new File(fileGroupsCi, relative);
                            FileConfiguration cXi = null;
                            try {
                                cXi = YamlConfiguration.loadConfiguration(fileXi);
                            } catch (IllegalArgumentException e) {
                                plugin.getLogger().info("* conversion of "+relative+" failed with IllegalArgument exception: "+e.getMessage());
                                return FileVisitResult.CONTINUE;
                            }
                            FileConfiguration cCi = YamlConfiguration.loadConfiguration(fileCi);

                            if(cXi.getConfigurationSection("inventory") != null) {
                                Map invContent = cXi.getConfigurationSection("inventory").getValues(false);
                                cCi.set("inventory", invContent);
                            }

                            if(cXi.getConfigurationSection("ender_chest") != null) {
                                Map invContent = cXi.getConfigurationSection("ender_chest").getValues(false);
                                cCi.set("ender-chest", invContent);
                            }

                            if(cXi.getConfigurationSection("armor-contents") != null) {
                                Map invContent = cXi.getConfigurationSection("armor-contents").getValues(false);
                                cCi.set("armor-contents", invContent);
                            }

                            if(cXi.get("health") != null)
                                cCi.set("health", cXi.getDouble("health"));
                            else
                                cCi.set("health", 20.0);

                            if(cXi.get("hunger") != null)
                                cCi.set("hunger", cXi.getInt("hunger"));
                            else
                                cCi.set("hunger", 20);

                            if(cXi.get("exp") != null)
                                cCi.set("exp", Float.parseFloat(cXi.getString("exp")));

                            if(cXi.get("exp-level") != null)
                                cCi.set("exp-level", cXi.getInt("exp-level"));

                            if(cXi.get("remainingAir") != null)
                                cCi.set("remaining-air", cXi.getInt("remainingAir"));
                            else
                                cCi.set("remaining-air", 300);

                            if(cXi.get("fireTicks") != null)
                                cCi.set("fire-ticks", cXi.getInt("fireTicks"));
                            else
                                cCi.set("fire-ticks", -20);

                            if(cXi.get("saturation") != null)
                                cCi.set("saturation", cXi.getInt("saturation"));

                            if(cXi.get("exhaustion") != null)
                                cCi.set("exhaustion", cXi.getInt("exhaustion"));

                            try {
                                cCi.save(fileCi);
                            } catch (IOException e) {
                                plugin.getLogger().severe(relative+" could not be saved!");
                            }
                            return FileVisitResult.CONTINUE;
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}