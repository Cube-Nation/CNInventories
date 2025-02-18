package de.cubenation.cninventories.service;

import de.cubenation.api.bedrock.BasePlugin;
import de.cubenation.api.bedrock.exception.ServiceInitException;
import de.cubenation.api.bedrock.exception.ServiceReloadException;
import de.cubenation.api.bedrock.service.AbstractService;
import de.cubenation.cninventories.CNInventoriesPlugin;
import de.cubenation.cninventories.util.InventoryUtil;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
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

public class InventoryService extends AbstractService {

    private File groupsDirectory;

    public InventoryService(BasePlugin plugin) {
        super(plugin);
        this.groupsDirectory = new File(plugin.getDataFolder() + File.separator + "groups");
    }

    @Override
    public void init() throws ServiceInitException {
        try {
            this.createDataFolder(this.groupsDirectory);
        } catch (Exception e) {
            throw new ServiceInitException(e.getMessage());
        }
    }

    @Override
    public void reload() throws ServiceReloadException {

    }

    private void createDataFolder(File dir) throws Exception {
        if (!dir.exists() && !dir.mkdirs())
            throw new Exception("Could not create folder " + dir.getName());
    }

    public boolean storePlayerInventoryContents(UUID uuid, String group, ItemStack[] contents) {
        File groupDir = new File(this.groupsDirectory, group);
        try {
            createDataFolder(groupDir);
        } catch (Exception e) {
            return false;
        }

        File f = new File(groupDir, uuid.toString() + ".yml");
        FileConfiguration c = YamlConfiguration.loadConfiguration(f);

        c.set("inventory", InventoryUtil.serializePlayerInventory(contents));

        try {
            c.save(f);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public boolean storeEnderchestContents(UUID uuid, String group, ItemStack[] contents) {
        File groupDir = new File(this.groupsDirectory, group);
        try {
            createDataFolder(groupDir);
        } catch (Exception e) {
            return false;
        }

        File f = new File(groupDir, uuid.toString() + ".yml");
        FileConfiguration c = YamlConfiguration.loadConfiguration(f);

        c.set("ender-chest", InventoryUtil.serializeEnderchestInventory(contents));

        try {
            c.save(f);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public boolean save(Player player, String group) {
        return save(player, group, true);
    }

    public boolean save(Player player, String group, boolean closeInv) {
        if(closeInv)
            player.closeInventory();

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

        File f = new File(groupDir, player.getUniqueId().toString() + ".yml");
        FileConfiguration c = YamlConfiguration.loadConfiguration(f);

        // inventory
        c.set("inventory", InventoryUtil.serializePlayerInventory(player.getInventory().getContents()));
        c.set("ender-chest", InventoryUtil.serializeEnderchestInventory(player.getEnderChest().getContents()));

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
        List<Player> onlinePlayers = (List<Player>) Bukkit.getOnlinePlayers();
        for(Player p : onlinePlayers) {
            String group = CNInventoriesPlugin.getInstance().getGroupService().getWorldGroup(p.getWorld(), p.getGameMode());
            save(p, group, closeInv);
        }
    }

    public ItemStack[] getPlayerInventoryContents(UUID uuid, String group, int size) throws IOException {
        File groupDir = new File(this.groupsDirectory, group);

        File f = new File(groupDir, uuid.toString() + ".yml");
        FileConfiguration c = YamlConfiguration.loadConfiguration(f);

        if(c.getConfigurationSection("inventory") == null)
            throw new IOException("Inventory is not existent.");
        return InventoryUtil.deserializePlayerInventory(c.getConfigurationSection("inventory").getValues(false));
    }

    public ItemStack[] getEnderchestContents(UUID uuid, String group, int size) throws IOException {
        File groupDir = new File(this.groupsDirectory, group);

        File f = new File(groupDir, uuid.toString() + ".yml");
        FileConfiguration c = YamlConfiguration.loadConfiguration(f);

        if(c.getConfigurationSection("ender-chest") == null)
            throw new IOException("Inventory is not existent.");
        return InventoryUtil.deserializeEnderchestInventory(c.getConfigurationSection("ender-chest").getValues(false));
    }

    public boolean apply(Player player, String group) {
        File groupDir = new File(this.groupsDirectory, group);

        File f = new File(groupDir, player.getUniqueId() + ".yml");
        FileConfiguration c = YamlConfiguration.loadConfiguration(f);

        // clear current inventories
        player.getInventory().clear();
        player.getEnderChest().clear();

        // populate player inventory
        if(c.getConfigurationSection("inventory") != null) {
            ItemStack[] invContent = InventoryUtil.deserializePlayerInventory(c.getConfigurationSection("inventory").getValues(false));
            player.getInventory().setContents(invContent);
        }

        // populate ender chest
        if(c.getConfigurationSection("ender-chest") != null) {
            ItemStack[] enderChestContent = InventoryUtil.deserializeEnderchestInventory(c.getConfigurationSection("ender-chest").getValues(false));
            player.getEnderChest().setContents(enderChestContent);
        }

        // clear current effects
        for(PotionEffect effect : player.getActivePotionEffects())
            player.removePotionEffect(effect.getType());
        // apply saved effects
        if(c.get("potion-effects") != null)
            player.addPotionEffects((Collection<PotionEffect>) c.get("potion-effects"));

        double healthCap = player.getAttribute(Attribute.MAX_HEALTH).getValue();
        if (c.get("health") != null) {
            double health = Math.min(healthCap, c.getDouble("health"));
            player.setHealth(health);
        } else {
            player.setHealth(healthCap);
        }

        if(c.get("hunger") != null)
            player.setFoodLevel(c.getInt("hunger"));
        else
            player.setFoodLevel(20);

        if(c.get("exp") != null)
            player.setExp(Float.parseFloat(c.getString("exp")));

        if(c.get("exp-level") != null)
            player.setLevel(c.getInt("exp-level"));

        if(c.get("remaining-air") != null)
            player.setRemainingAir(c.getInt("remaining-air"));
        else
            player.setRemainingAir(300);

        if(c.get("fire-ticks") != null)
            player.setFireTicks(c.getInt("fire-ticks"));
        else
            player.setFireTicks(-20);

        if(c.get("saturation") != null)
            player.setSaturation(c.getInt("saturation"));

        if(c.get("exhaustion") != null)
            player.setExhaustion(c.getInt("exhaustion"));

        return true;
    }
}
