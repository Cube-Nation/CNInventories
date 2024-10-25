package de.cubenation.cninventories.command;

import com.google.gson.JsonSyntaxException;
import de.cubenation.api.bedrock.BasePlugin;
import de.cubenation.api.bedrock.annotation.Description;
import de.cubenation.api.bedrock.annotation.Permission;
import de.cubenation.api.bedrock.annotation.SubCommand;
import de.cubenation.api.bedrock.command.Command;
import de.cubenation.api.bedrock.command.CommandRole;
import de.cubenation.api.bedrock.service.command.CommandManager;
import de.cubenation.cninventories.util.InventoryUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Map;
import java.util.Objects;

@SuppressWarnings("CallToPrintStackTrace")
@Description("command.cninventories.upgrade.desc")
@SubCommand("upgrade")
@Permission(Name = "inventory.upgrade", Role = CommandRole.OWNER)
public class CNInventoriesUpgradeCommand extends Command {

    public CNInventoriesUpgradeCommand(BasePlugin plugin, CommandManager commandManager) {
        super(plugin, commandManager);
    }


    public void execute(CommandSender commandSender, String[] args) {
        if (commandSender instanceof Player) {
            commandSender.sendMessage("This command can only be executed as console.");
            return;
        }

        plugin.getLogger().info("Starting to upgrade files to new CNInventories version...");

        File fileBase = plugin.getDataFolder();
        File fileGroupsOld = new File(fileBase, "groups_old");
        if (!fileGroupsOld.exists() && !fileGroupsOld.mkdirs()) {
            throw new IllegalStateException("Could not create directory: "+fileGroupsOld);
        }

        File fileGroupsNew = new File(fileBase, "groups");
        if (!fileGroupsNew.exists() && !fileGroupsNew.mkdirs()) {
            throw new IllegalStateException("Could not create directory: "+fileGroupsOld);
        }

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                Files.walkFileTree(fileGroupsOld.toPath(),
                        new SimpleFileVisitor<>() {
                            @Override
                            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                                String relative = fileGroupsOld.toURI().relativize(dir.toUri()).getPath();
                                plugin.getLogger().info("Upgrading "+relative+"...");
                                //noinspection ResultOfMethodCallIgnored
                                new File(fileGroupsOld, relative).mkdirs();
                                return FileVisitResult.CONTINUE;
                            }

                            @Override
                            public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
                                String relative = fileGroupsOld.toURI().relativize(dir.toUri()).getPath();
                                plugin.getLogger().info("\nSuccessfully upgraded "+relative+"!");
                                return FileVisitResult.CONTINUE;
                            }

                            @Override
                            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                                String relative = fileGroupsOld.toURI().relativize(file.toUri()).getPath();
                                try {
                                    plugin.getLogger().info("* upgrading "+relative+"...");

                                    File fileOld = new File(fileGroupsOld, relative);
                                    File fileNew = new File(fileGroupsNew, relative);
                                    FileConfiguration configOld;
                                    try {
                                        configOld = YamlConfiguration.loadConfiguration(fileOld);
                                    } catch (IllegalArgumentException | JsonSyntaxException e) {
                                        plugin.getLogger().severe("* upgrading of " + relative + " failed: " + e.getMessage());
                                        return FileVisitResult.CONTINUE;
                                    }
                                    FileConfiguration configNew = YamlConfiguration.loadConfiguration(fileNew);

                                    if (configOld.getConfigurationSection("inventory") != null) {
                                        Map<String, Object> invContent = Objects.requireNonNull(configOld.getConfigurationSection("inventory")).getValues(false);
                                        ItemStack[] stacks = new ItemStack[41];
                                        invContent.forEach((k, v) -> stacks[Integer.parseInt(k)] = (ItemStack) v);
                                        configNew.set("inventory", InventoryUtil.serializePlayerInventory(stacks));
                                    }

                                    if (configOld.getConfigurationSection("ender-chest") != null) {
                                        Map<String, Object> invContent = Objects.requireNonNull(configOld.getConfigurationSection("ender-chest")).getValues(false);
                                        ItemStack[] stacks = new ItemStack[41];
                                        invContent.forEach((k, v) -> stacks[Integer.parseInt(k)] = (ItemStack) v);
                                        configNew.set("ender-chest", InventoryUtil.serializeEnderchestInventory(stacks));
                                    }

                                    if (configOld.get("health") != null) {
                                        configNew.set("health", configOld.getDouble("health"));
                                    }

                                    if (configOld.get("hunger") != null) {
                                        configNew.set("hunger", configOld.getInt("hunger"));
                                    }

                                    if (configOld.get("exp") != null) {
                                        configNew.set("exp", Float.parseFloat(Objects.requireNonNull(configOld.getString("exp"))));
                                    }

                                    if (configOld.get("exp-level") != null) {
                                        configNew.set("exp-level", configOld.getInt("exp-level"));
                                    }

                                    if (configOld.get("remaining-air") != null) {
                                        configNew.set("remaining-air", configOld.getInt("remaining-air"));
                                    }

                                    if (configOld.get("fire-ticks") != null) {
                                        configNew.set("fire-ticks", configOld.getInt("fire-ticks"));
                                    }

                                    if (configOld.get("saturation") != null) {
                                        configNew.set("saturation", configOld.getInt("saturation"));
                                    }

                                    if (configOld.get("exhaustion") != null) {
                                        configNew.set("exhaustion", configOld.getInt("exhaustion"));
                                    }

                                    if (configOld.get("potion-effects") != null) {
                                        configNew.set("potion-effects", configOld.get("potion-effects"));
                                    }

                                    configNew.save(fileNew);
                                } catch (Exception e) {
                                    plugin.getLogger().severe("Encountered exception while parsing inventory file ("+relative+"):");
                                    e.printStackTrace();
                                }
                                return FileVisitResult.CONTINUE;
                            }
                        });
            } catch (Exception e) {
                plugin.getLogger().severe("Encountered exception while parsing inventory file:");
                e.printStackTrace();
            }
        });
    }
}