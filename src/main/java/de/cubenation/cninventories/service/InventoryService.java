package de.cubenation.cninventories.service;

import de.cubenation.cninventories.CNInventoriesPlugin;
import de.cubenation.cninventories.util.ItemStackUtil;
import dev.projectshard.core.FoundationPlugin;
import dev.projectshard.core.annotations.Inject;
import dev.projectshard.core.exceptions.InitializationException;
import dev.projectshard.core.lifecycle.Initializable;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class InventoryService implements Initializable {

    private final FoundationPlugin plugin;

    @Inject
    private GroupService groupService;

    private final File groupsDirectory;

    public InventoryService(FoundationPlugin plugin) {
        this.plugin = plugin;
        // TODO: Switch to NIO
        this.groupsDirectory = new File(plugin.getPluginFolder() + File.separator + "groups");
    }

    @Override
    public void init() throws InitializationException {
        try {
            createDataFolder(groupsDirectory);
        } catch (IOException e) {
            throw new InitializationException(e);
        }
    }

    private void createDataFolder(File dir) throws IOException {
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("Could not create folder " + dir.getName());
        }
    }

    public boolean storeInventoryContents(UUID uuid, String group, String type, ItemStack[] contents) {
        File groupDir = new File(this.groupsDirectory, group);
        try {
            createDataFolder(groupDir);
            File f = new File(groupDir, uuid.toString() + ".yml");
            FileConfiguration c = YamlConfiguration.loadConfiguration(f);
            c.set(type, ItemStackUtil.convertArrayToMap(contents));
            c.save(f);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean save(Player player, String group) {
        return save(player, group, true);
    }

    public boolean save(Player player, String group, boolean closeInv) {
        if (closeInv) {
            player.closeInventory();
        }

        File groupDir = new File(this.groupsDirectory, group);
        try {
            createDataFolder(groupDir);
        } catch (Exception e) {
            plugin.log(
                    Level.SEVERE,
                    "Inventory of player '"+player.getDisplayName()+"' could not be saved! " +
                            "The directory '"+groupDir.getAbsolutePath()+"' refused to be created..."
            );
            return false;
        }

        File f = new File(groupDir, player.getUniqueId() + ".yml");
        FileConfiguration c = YamlConfiguration.loadConfiguration(f);

        // inventory
        c.set("inventory", ItemStackUtil.convertArrayToMap(player.getInventory().getContents()));
        c.set("ender-chest", ItemStackUtil.convertArrayToMap(player.getEnderChest().getContents()));
        c.set("armor-contents.helmet", player.getInventory().getHelmet());
        c.set("armor-contents.chestplate", player.getInventory().getChestplate());
        c.set("armor-contents.leggins", player.getInventory().getLeggings());
        c.set("armor-contents.boots", player.getInventory().getBoots());

        // player stats
        c.set("health", player.getHealth());
        c.set("hunger", player.getFoodLevel());
        c.set("exp", player.getExp());
        c.set("exp-level", player.getLevel());
        c.set("remaining-air", player.getRemainingAir());
        c.set("fire-ticks", player.getFireTicks());
        c.set("saturation", player.getSaturation());
        c.set("exhaustion", player.getExhaustion());

        PotionEffect[] effects = player.getActivePotionEffects().toArray(new PotionEffect[player.getActivePotionEffects().size()]);
        // TODO: Is it even saving effects?
        c.set("potion-effects", effects);

        try {
            c.save(f);
        } catch (IOException e) {
            plugin.log(
                    Level.SEVERE,
                    "Inventory of player '"+player.getDisplayName()+"' could not be saved! " +
                            "Well that sucks..."
            );
            return false;
        }
        return true;
    }

    public void saveAll(boolean closeInv) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            String group = groupService.getWorldGroup(p.getWorld(), p.getGameMode());
            save(p, group, closeInv);
        }
    }

    public ItemStack[] getPlayerInventoryContents(UUID uuid, String group, int size) throws IOException {
        File groupDir = new File(this.groupsDirectory, group);

        File f = new File(groupDir, uuid.toString() + ".yml");
        FileConfiguration c = YamlConfiguration.loadConfiguration(f);

        if (c.getConfigurationSection("inventory") == null) {
            throw new IOException("Inventory is not existent.");
        }
        return ItemStackUtil.convertMapToArray(c.getConfigurationSection("inventory").getValues(false), size);
    }

    public ItemStack[] getEnderchestContents(UUID uuid, String group, int size) throws IOException {
        File groupDir = new File(this.groupsDirectory, group);

        File f = new File(groupDir, uuid.toString() + ".yml");
        FileConfiguration c = YamlConfiguration.loadConfiguration(f);

        if (c.getConfigurationSection("ender-chest") == null) {
            throw new IOException("Inventory is not existent.");
        }
        return ItemStackUtil.convertMapToArray(c.getConfigurationSection("ender-chest").getValues(false), size);
    }

    public boolean apply(Player player, String group) {
        File groupDir = new File(this.groupsDirectory, group);

        File f = new File(groupDir, player.getUniqueId().toString() + ".yml");
        FileConfiguration c = YamlConfiguration.loadConfiguration(f);

        // clear current inventories
        player.getInventory().clear();
        player.getEnderChest().clear();
        // apply saved inventories
        try {

            // populate player inventory
            if (c.getConfigurationSection("inventory") != null) {
                ItemStack[] invContent = ItemStackUtil.convertMapToArray(c.getConfigurationSection("inventory").getValues(false), player.getInventory().getSize());
                player.getInventory().setContents(invContent);
            }

            // populate ender chest
            if (c.getConfigurationSection("ender-chest") != null) {
                ItemStack[] enderChestContent = ItemStackUtil.convertMapToArray(c.getConfigurationSection("ender-chest").getValues(false), player.getEnderChest().getSize());
                player.getEnderChest().setContents(enderChestContent);
            }

        } catch (IOException e) {
            plugin.log(
                    Level.SEVERE,
                    "Inventory of player '"+player.getDisplayName()+"' could not be applied! " +
                            "Well that sucks..."
            );
            return false;
        }

        applyPlayerStats(player, c);

        return true;
    }

    // Same as apply but with extensive logging in case of mismatch
    public boolean safeApply(Player player, String group) {
        File groupDir = new File(this.groupsDirectory, group);

        File f = new File(groupDir, player.getUniqueId().toString() + ".yml");
        FileConfiguration c = YamlConfiguration.loadConfiguration(f);

        // clear current inventories
        player.getInventory().clear();
        player.getEnderChest().clear();
        // apply saved inventories
        try {

            // populate player inventory
            if(c.getConfigurationSection("inventory") != null) {
                ItemStack[] invContent = ItemStackUtil.convertMapToArray(c.getConfigurationSection("inventory").getValues(false), player.getInventory().getSize());

                if(!invContent.equals(player.getInventory().getContents())) {
                    plugin.log(
                            Level.WARNING,
                            "Mismatch between '"+player.getDisplayName()+"'s inventory and the saved version! " +
                                    "Reapplying saved version..."
                    );
                }

                player.getInventory().setContents(invContent);
            }

            // populate ender chest
            if(c.getConfigurationSection("ender-chest") != null) {
                ItemStack[] enderChestContent = ItemStackUtil.convertMapToArray(c.getConfigurationSection("ender-chest").getValues(false), player.getEnderChest().getSize());

                if(!enderChestContent.equals(player.getEnderChest().getContents())) {
                    plugin.log(
                            Level.WARNING,
                            "Mismatch between '"+player.getDisplayName()+"'s ender chest and the saved version! " +
                                    "Reapplying saved version..."
                    );
                }

                player.getEnderChest().setContents(enderChestContent);
            }

        } catch (IOException e) {
            plugin.log(
                    Level.SEVERE,
                    "Inventory of player '"+player.getDisplayName()+"' could not be applied! " +
                            "Well that sucks..."
            );
            return false;
        }

        applyPlayerStats(player, c);

        return true;
    }

    private void applyPlayerStats(Player player, FileConfiguration c) {
        player.setHealth(c.get("health") != null ? c.getDouble("health") : 20.0);
        player.setFoodLevel(c.get("hunger") != null ? c.getInt("hunger") : 20);
        player.setRemainingAir(c.get("remaining-air") != null ? c.getInt("remaining-air") : 300);
        player.setFireTicks(c.get("fire-ticks") != null ? c.getInt("fire-ticks") : -20);

        if (c.get("exp") != null) {
            player.setExp(Float.parseFloat(c.getString("exp")));
        }

        if(c.get("exp-level") != null) {
            player.setLevel(c.getInt("exp-level"));
        }

        if(c.get("saturation") != null) {
            player.setSaturation(c.getInt("saturation"));
        }

        if(c.get("exhaustion") != null) {
            player.setExhaustion(c.getInt("exhaustion"));
        }

        // clear current effects
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
        // apply saved effects
        if (c.get("potion-effects") != null) {
            player.addPotionEffects((Collection<PotionEffect>) c.get("potion-effects"));
        }
    }

    public void applyAll() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            String group = groupService.getWorldGroup(p.getWorld(), p.getGameMode());
            apply(p, group);
        }
    }

    private boolean isInventoryContentEmpty(ItemStack[] items) {
        for (ItemStack itemStack : items) {
            if (itemStack != null) {
                return false;
            }
        }
        return true;
    }

    private boolean isPlayerInventoryEmpty(Player player) {
        return isInventoryContentEmpty(player.getInventory().getContents());
    }

    private boolean isPlayerEnderChestEmpty(Player player) {
        return isInventoryContentEmpty(player.getEnderChest().getContents());
    }

    public boolean hasPlayerEmptyInventory(Player player) {
        return isPlayerInventoryEmpty(player) || isPlayerEnderChestEmpty(player);
    }
}
