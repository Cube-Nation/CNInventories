package de.cubenation.cninventories.model.converter;

import de.cubenation.cninventories.model.InventoryZone;
import net.cubespace.Yamler.Config.ConfigSection;
import net.cubespace.Yamler.Config.Converter.Converter;
import net.cubespace.Yamler.Config.InternalConverter;
import org.bukkit.Bukkit;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

public class InventoryZoneConverter implements Converter {

    private final String UUID = "uuid";
    private final String NAME = "name";

    private final String WORLDUUID = "worlduuid";
    private final String WORLDNAME = "worldname";
    private final String XMIN = "xMin";
    private final String YMIN = "yMin";
    private final String ZMIN = "zMin";
    private final String XMAX = "xMax";
    private final String YMAX = "yMax";
    private final String ZMAX = "zMax";

    public InventoryZoneConverter(InternalConverter converter) {
    }

    @Override
    public Object toConfig(Class<?> type, Object obj, ParameterizedType genericType) throws Exception {
        InventoryZone teleporter = (InventoryZone) obj;
        Map<String, Object> saveMap = new HashMap<>();
        saveMap.put(UUID, teleporter.getUuid().toString());
        saveMap.put(NAME, teleporter.getGroup());

        if (teleporter.getFromWorld() == null) {
            saveMap.put(WORLDUUID, null);
            saveMap.put(WORLDNAME, null);
        } else {
            saveMap.put(WORLDUUID, teleporter.getFromWorld().getUID().toString());
            saveMap.put(WORLDNAME, teleporter.getFromWorld().getName());
        }

        saveMap.put(XMIN, teleporter.getFromXMin());
        saveMap.put(YMIN, teleporter.getFromYMin());
        saveMap.put(ZMIN, teleporter.getFromZMin());
        saveMap.put(XMAX, teleporter.getFromXMax());
        saveMap.put(YMAX, teleporter.getFromYMax());
        saveMap.put(ZMAX, teleporter.getFromZMax());

        return saveMap;
    }

    @Override
    public Object fromConfig(Class type, Object section, ParameterizedType genericType) throws Exception {
        Map<String, Object> locationMap;
        if (section instanceof Map) {
            locationMap = (Map<String, Object>) section;
        } else {
            locationMap = (Map<String, Object>) ((ConfigSection) section).getRawMap();
        }

        return new InventoryZone(
                java.util.UUID.fromString((String) locationMap.get(UUID)),
                Bukkit.getWorld(java.util.UUID.fromString((String) locationMap.get(WORLDUUID))),
                (String) locationMap.get(NAME),
                (Double) locationMap.get(XMIN),
                (Double) locationMap.get(YMIN),
                (Double) locationMap.get(ZMIN),
                (Double) locationMap.get(XMAX),
                (Double) locationMap.get(YMAX),
                (Double) locationMap.get(ZMAX));
    }

    @Override
    public boolean supports(Class<?> type) {
        return InventoryZone.class.isAssignableFrom(type);
    }
}
