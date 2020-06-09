package de.cubenation.cninventories.util;

import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ItemStackUtil {

    public static Map<String, ItemStack> convertArrayToMap(ItemStack[] array) {
        HashMap<String,ItemStack> items = new HashMap<>();
        for(int i = 0; i < array.length; i++) {
            if(array[i] == null)
                continue;
            items.put(i + "", array[i]);
        }
        return items;
    }

    public static ItemStack[] convertMapToArray(Map<String, Object> map, int size) throws IOException {
        ItemStack[] items = new ItemStack[size];
        for(Map.Entry<String, Object> entry : map.entrySet())
            if(!(entry.getValue() instanceof ItemStack))
                throw new IOException("ItemStack in FileConfiguration does not meet correct structuring.");
            else
                items[Integer.parseInt(entry.getKey())] = (ItemStack) entry.getValue();
        return items;
    }

    public static boolean areSameItemStacks(ItemStack[] first, ItemStack[] second) {
        if(first == null && second == null) return true;
        if((first == null && second != null) || (first != null && second == null)) return false;
        if(first.length != second.length) return false;
        for(int i = 0; i < first.length; i++) {
            if(first[i] == null && second[i] == null) continue;
            else if(first[i] == null && second[i] != null) return false;
            else if(second[i] == null && first[i] != null) return false;
            else if(!first[i].isSimilar(second[i]) || first[i].getAmount() != second[i].getAmount()) return false;
        }
        return true;
    }

    public static ItemStack[] shortenItemStack(ItemStack[] contents, int length) {
        ItemStack[] out = new ItemStack[length];
        System.arraycopy(contents, 0, out, 0, length);
        return out;
    }
}
