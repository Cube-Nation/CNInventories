package de.cubenation.cninventories.helper;

import org.bukkit.Location;

public class LocationHelper {

    @SuppressWarnings("RedundantIfStatement")
    public static boolean isBetweenLocations(Location playerLocation, Location minLocation, Location maxLoaction) {
        if (!playerLocation.getWorld().equals(minLocation.getWorld()) || !playerLocation.getWorld().equals(maxLoaction.getWorld())) {
            // Is not this world -> false
            return false;
        }

        if (minLocation.getX() >= playerLocation.getX() && maxLoaction.getX() <= playerLocation.getX() &&
                minLocation.getY() >= playerLocation.getY() && maxLoaction.getY() <= playerLocation.getY() &&
                minLocation.getZ() >= playerLocation.getZ() && maxLoaction.getZ() <= playerLocation.getZ()) {
            return true;
        }

        if (maxLoaction.getX() >= playerLocation.getX() && minLocation.getX() <= playerLocation.getX() &&
                maxLoaction.getY() >= playerLocation.getY() && minLocation.getY() <= playerLocation.getY() &&
                maxLoaction.getZ() >= playerLocation.getZ() && minLocation.getZ() <= playerLocation.getZ()) {
            return true;
        }

        return false;
    }
}
