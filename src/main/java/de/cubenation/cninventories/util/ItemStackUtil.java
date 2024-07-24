package de.cubenation.cninventories.util;

import org.bukkit.inventory.ItemStack;

public class ItemStackUtil {

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
