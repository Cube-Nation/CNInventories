package de.cubenation.cninventories.model;

import com.sk89q.worldedit.math.BlockVector3;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.UUID;

public class InventoryZone {

    private UUID uuid;
    private String group;

    private World fromWorld;
    private Double fromXMin;
    private Double fromYMin;
    private Double fromZMin;
    private Double fromXMax;
    private Double fromYMax;
    private Double fromZMax;

    public InventoryZone(UUID uuid, String group, Location from, Location to) {
        this(uuid, from.getWorld(), group == null ? uuid.toString() : group,
                from.getX(), from.getY(), from.getZ(),
                to.getX(), to.getY(), to.getZ());
    }

    public InventoryZone(UUID uuid, World world, String group,
                      Double fromX, Double fromY, Double fromZ,
                      Double toX, Double toY, Double toZ) {
        this.uuid = uuid;
        this.group = group;

        this.fromWorld = world;
        this.fromXMin = fromX <= toX ? fromX : toX;
        this.fromYMin = fromY <= toY ? fromY : toY;
        this.fromZMin = fromZ <= toZ ? fromZ : toZ;
        this.fromXMax = toX >= fromX ? toX : fromX;
        this.fromYMax = toY >= fromY ? toY : fromY;
        this.fromZMax = toZ >= fromZ ? toZ : fromZ;
    }

    public InventoryZone(UUID uuid, String group, World world, BlockVector3 minimumPoint, BlockVector3 maximumPoint) {
        this(
                uuid,
                group,
                new Location(world, minimumPoint.getBlockX(), minimumPoint.getBlockY(), minimumPoint.getBlockZ()),
                new Location(world, maximumPoint.getBlockX(), maximumPoint.getBlockY(), maximumPoint.getBlockZ())
        );
    }

    // Implementation

    public Location getMinLocation() {
        return new Location(fromWorld, fromXMin, fromYMin, fromZMin);
    }

    public Location getMaxLocation() {
        return new Location(fromWorld, fromXMax + 1, fromYMax + 1, fromZMax + 1);
    }

    public Location getCenterLocation() {
        return getMinLocation().add(getMaxLocation().subtract(getMinLocation()).multiply(0.5));
    }

    public Location getTeleportLocation() {
        Location centerLocation = getCenterLocation();
        centerLocation.setY(getMaxLocation().getBlockY());
        centerLocation.add(0, 5, 0);
        return centerLocation;
    }


    // Getter & Setter

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public World getFromWorld() {
        return fromWorld;
    }

    public void setFromWorld(World fromWorld) {
        this.fromWorld = fromWorld;
    }

    public Double getFromXMin() {
        return fromXMin;
    }

    public void setFromXMin(Double fromXMin) {
        this.fromXMin = fromXMin;
    }

    public Double getFromYMin() {
        return fromYMin;
    }

    public void setFromYMin(Double fromYMin) {
        this.fromYMin = fromYMin;
    }

    public Double getFromZMin() {
        return fromZMin;
    }

    public void setFromZMin(Double fromZMin) {
        this.fromZMin = fromZMin;
    }

    public Double getFromXMax() {
        return fromXMax;
    }

    public void setFromXMax(Double fromXMax) {
        this.fromXMax = fromXMax;
    }

    public Double getFromYMax() {
        return fromYMax;
    }

    public void setFromYMax(Double fromYMax) {
        this.fromYMax = fromYMax;
    }

    public Double getFromZMax() {
        return fromZMax;
    }

    public void setFromZMax(Double fromZMax) {
        this.fromZMax = fromZMax;
    }

    @Override
    public String toString() {
        return "InventoryZone{" +
                "uuid=" + uuid +
                ", name='" + group + '\'' +
                ", fromWorld=" + fromWorld +
                ", fromXMin=" + fromXMin +
                ", fromYMin=" + fromYMin +
                ", fromZMin=" + fromZMin +
                ", fromXMax=" + fromXMax +
                ", fromYMax=" + fromYMax +
                ", fromZMax=" + fromZMax +
                '}';
    }
}
