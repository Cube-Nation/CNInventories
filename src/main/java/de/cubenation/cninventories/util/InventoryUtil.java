package de.cubenation.cninventories.util;

import com.google.common.collect.Streams;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InventoryUtil {

    public static Map<String, byte[]> serializePlayerInventory(final ItemStack[] inventoryContents) {
        ItemStack[] hotbarContents = Arrays.copyOfRange(inventoryContents, 0, 9);
        ItemStack[] containerContents = Arrays.copyOfRange(inventoryContents, 9, 36);
        ItemStack[] armorContents = Arrays.copyOfRange(inventoryContents, 36, 40);
        ItemStack offHandContent = inventoryContents[40];

        LinkedHashMap<String, byte[]> items = new LinkedHashMap<>();
        items.putAll(serializeContents(hotbarContents, "hotbar"));
        items.putAll(serializeContents(containerContents, "container"));
        items.putAll(serializeContents(armorContents, "armor"));
        if (offHandContent != null && offHandContent.getType() != Material.AIR) {
            items.put("offhand", offHandContent.serializeAsBytes());
        }
        return items;
    }

    public static Map<String, byte[]> serializeEnderchestInventory(final ItemStack[] inventoryContents) {
        return serializeContents(inventoryContents, "container");
    }

    public static Map<String, byte[]> serializeContents(final ItemStack[] inventoryContents, final @NotNull String prefix) {
        HashMap<String, byte[]> items = new HashMap<>();
        for (int i = 0; i < inventoryContents.length; i++) {
            if (inventoryContents[i] == null || inventoryContents[i].getType() == Material.AIR) {
                continue;
            }
            items.put(String.format("%s-%d", prefix, i), inventoryContents[i].serializeAsBytes());
        }
        return items;
    }

    public static ItemStack[] deserializePlayerInventory(final Map<String, Object> map) {
        Map<String, Object> hotbarContents = map.entrySet().stream().filter(e -> e.getKey().startsWith("hotbar")).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        Map<String, Object> containerContents = map.entrySet().stream().filter(e -> e.getKey().startsWith("container")).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        Map<String, Object> armorContents = map.entrySet().stream().filter(e -> e.getKey().startsWith("armor")).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        ItemStack offHandContent = map.get("offhand") instanceof byte[] bytes ? ItemStack.deserializeBytes(bytes) : null;

        return Streams.concat(
                Arrays.stream(deserializeContents(hotbarContents, 9)),
                Arrays.stream(deserializeContents(containerContents, 27)),
                Arrays.stream(deserializeContents(armorContents, 4)),
                Stream.of(offHandContent)
        ).toArray(ItemStack[]::new);
    }

    public static ItemStack[] deserializeEnderchestInventory(final Map<String, Object> map) {
        Map<String, Object> containerContents = map.entrySet().stream().filter(e -> e.getKey().startsWith("container")).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return deserializeContents(containerContents, 27);
    }

    public static ItemStack[] deserializeContents(final Map<String, Object> map, int size) {
        ItemStack[] items = new ItemStack[size];
        map.forEach((key, value) -> {
            String[] keyParts = key.split("-");
            if (keyParts.length < 1) {
                throw new IllegalStateException("Malformed inventory slot key");
            }
            int i = Integer.parseInt(keyParts[keyParts.length - 1]);
            if (value instanceof byte[] bytes) {
                items[i] = ItemStack.deserializeBytes(bytes);
            } else {
                throw new IllegalStateException("ItemStack in FileConfiguration does not meet correct structuring.");
            }
        });
        return items;
    }
}
