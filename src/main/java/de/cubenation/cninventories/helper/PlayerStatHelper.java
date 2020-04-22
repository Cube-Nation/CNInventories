package de.cubenation.cninventories.helper;

import org.bukkit.entity.Player;

public class PlayerStatHelper {

    public static void resetPlayerStats(Player player) {

        player.setHealth(20);
        player.setFoodLevel(20);
        player.setExp(0);
        player.setLevel(0);
        player.setRemainingAir(300);
        player.setFireTicks(-20);
        player.setSaturation(0);
        player.setExhaustion(0);
    }
}
