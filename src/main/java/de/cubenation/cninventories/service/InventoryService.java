package de.cubenation.cninventories.service;

import de.cubenation.api.bedrock.BasePlugin;
import de.cubenation.api.bedrock.exception.ServiceInitException;
import de.cubenation.api.bedrock.exception.ServiceReloadException;
import de.cubenation.api.bedrock.service.AbstractService;
import de.cubenation.cninventories.CNInventoriesPlugin;
import de.cubenation.cninventories.util.ItemStackUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.List;

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

    public boolean save(Player player, String group) {
        player.sendMessage("saving "+group+" ...");

        // first close currently open inventories...
        player.getOpenInventory().close();

        File groupDir = new File(this.groupsDirectory, group);
        try {
            createDataFolder(groupDir);
        } catch (Exception e) {
            return false;
        }

        File f = new File(groupDir, player.getUniqueId().toString() + ".yml");
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

        try {
            c.save(f);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public void saveAll() {
        List<Player> onlinePlayers = (List<Player>) Bukkit.getOnlinePlayers();
        for(Player p : onlinePlayers) {
            String group = CNInventoriesPlugin.getInstance().getGroupService().getWorldGroup(p.getWorld(), p.getGameMode());
            save(p, group);
        }
    }

    public boolean apply(Player player, String group) {
        player.sendMessage("applying "+group+" ...");

        File groupDir = new File(this.groupsDirectory, group);

        File f = new File(groupDir, player.getUniqueId().toString() + ".yml");
        FileConfiguration c = YamlConfiguration.loadConfiguration(f);

        try {

            // populate player inventory
            if(c.getConfigurationSection("inventory") != null) {
                ItemStack[] invContent = ItemStackUtil.convertMapToArray(c.getConfigurationSection("inventory").getValues(false), player.getInventory().getSize());
                player.getInventory().setContents(invContent);
            }

            // populate ender chest
            if(c.getConfigurationSection("ender-chest") != null) {
                ItemStack[] enderChestContent = ItemStackUtil.convertMapToArray(c.getConfigurationSection("ender-chest").getValues(false), player.getEnderChest().getSize());
                player.getEnderChest().setContents(enderChestContent);
            }

        } catch (IOException e) {
            return false;
        }

        if(c.get("health") != null)
            player.setHealth(c.getDouble("health"));
        if(c.get("hunger") != null)
            player.setFoodLevel(c.getInt("hunger"));
        if(c.get("exp") != null)
            player.setExp(Float.parseFloat(c.getString("exp")));
        if(c.get("exp-level") != null)
            player.setLevel(c.getInt("exp-level"));
        if(c.get("remaining-air") != null)
            player.setRemainingAir(c.getInt("remaining-air"));
        if(c.get("fire-ticks") != null)
            player.setFireTicks(c.getInt("fire-ticks"));
        if(c.get("saturation") != null)
            player.setSaturation(c.getInt("saturation"));
        if(c.get("exhaustion") != null)
            player.setExhaustion(c.getInt("exhaustion"));

        return true;
    }

    public void applyAll() {
        List<Player> onlinePlayers = (List<Player>) Bukkit.getOnlinePlayers();
        for(Player p : onlinePlayers) {
            String group = CNInventoriesPlugin.getInstance().getGroupService().getWorldGroup(p.getWorld(), p.getGameMode());
            apply(p, group);
        }
    }
}
