package de.cubenation.cninventories.config.locale;

import de.cubenation.api.bedrock.BasePlugin;
import de.cubenation.api.bedrock.service.config.CustomConfigurationFile;
import net.cubespace.Yamler.Config.Comment;
import net.cubespace.Yamler.Config.Path;

import java.io.File;
import java.util.HashMap;

public class de_DE extends CustomConfigurationFile {

    @SuppressWarnings("WeakerAccess")
    public static String getFilename() {
        return "locale" + System.getProperty("file.separator") + "de_DE.yml";
    }

    public de_DE(BasePlugin plugin) {
        CONFIG_FILE = new File(plugin.getDataFolder(), getFilename());
    }

    /**
     * Commands
     */

    @Path("command.inventory.zone.set.desc")
    private String commandInvzoneSetDesc = "Setzt eine Inventar-Zone.";

    @Path("command.inventory.zone.remove.desc")
    private String commandInvzoneRemoveDesc = "Löscht eine Inventar-Zone in der du dich befindest.";

    @Path("command.inventory.zone.modify.group.desc")
    private String commandInvzoneModifyGroupDesc = "Ändert die Gruppe der Inventar-Zone in der du dich befindest.";

    @Path("command.inventory.open.desc")
    private String commandOpeninvgroupDesc = "Öffnet die Inventar-Gruppe eines Spielers.";

    @Path("command.inventory.args.group")
    private HashMap<String, String> commandInventoryArgsGroup = new HashMap<String, String>() {{
        put("desc", "einzigartige Gruppen-ID");
        put("ph", "Gruppe");
    }};

    @Path("command.inventory.args.player")
    private HashMap<String, String> commandInventoryArgsPlayer = new HashMap<String, String>() {{
        put("desc", "Letzter Spielername oder UUID");
        put("ph", "Spieler");
    }};

    /**
     * Command Messages
     */

    @Path("inventory.zone.set.success")
    private String inventoryZoneSetSuccess = "{\"text\":\"%plugin_prefix%\",\"extra\":[{\"text\":\" Die Inventar-Zone wurde erfolgreich erstellt!\",\"color\":\"&SECONDARY&\"}]}";

    @Path("inventory.zone.set.fail")
    private String inventoryZoneSetFail = "{\"text\":\"%plugin_prefix%\",\"extra\":[{\"text\":\" Die Inventar-Zone konnte nicht erstellt werden!\",\"color\":\"red\"}]}";

    @Path("inventory.zone.remove.success")
    private String inventoryZoneRemoveSuccess = "{\"text\":\"%plugin_prefix%\",\"extra\":[{\"text\":\" Die Inventar-Zone wurde erfolgreich erstellt!\",\"color\":\"&SECONDARY&\"}]}";

    @Path("inventory.zone.remove.fail")
    private String inventoryZoneRemoveFail = "{\"text\":\"%plugin_prefix%\",\"extra\":[{\"text\":\" Die Inventar-Zone konnte nicht gelöscht werden!\",\"color\":\"red\"}]}";

    @Path("inventory.zone.modify.group.success")
    private String inventoryZoneModifyGroupSuccess = "{\"text\":\"%plugin_prefix%\",\"extra\":[{\"text\":\" Die Inventar-Gruppe wurde erfolgreich bearbeitet!\",\"color\":\"&SECONDARY&\"}]}";

    @Path("inventory.zone.modify.group.fail")
    private String inventoryZoneModifyGroupFail = "{\"text\":\"%plugin_prefix%\",\"extra\":[{\"text\":\" Die Inventar-Gruppe konnte nicht bearbeitet werden!\",\"color\":\"red\"}]}";

    @Path("inventory.content.update.success")
    private String inventoryContentUpdateSuccess = "{\"text\":\"%plugin_prefix%\",\"extra\":[{\"text\":\" Der Inventarinhalt wurde erfolgreich überschrieben!\",\"color\":\"&SECONDARY&\"}]}";

    @Path("inventory.content.update.fail")
    private String inventoryContentUpdateFail = "{\"text\":\"%plugin_prefix%\",\"extra\":[{\"text\":\" Der Inventarinhalt konnte nicht überschrieben werden!\",\"color\":\"red\"}]}";

    //  CONFIRM

    @Comment("Args: %commandinfo%")
    @Path("confirm.overrideinventory")
    private String confirmOverrideinventory = "{\"text\":\"\",\"extra\":[{\"text\":\"%plugin_prefix%\"},{\"text\":\" \",\"color\":\"white\"},{\"text\":\"<?>\",\"color\":\"dark_gray\"},{\"text\":\" \",\"color\":\"white\"},{\"text\":\"Das Inventar wurde in der Zwischenzeit abgeändert. Klicke hier wenn du die Änderungen mit deinen überschreiben möchtest, oder gebe '/inventory cancel' ein\",\"color\":\"&TEXT&\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"%command%\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"%commandinfo%\"}}]}";

    @Path("confirm.timeout")
    private String confirmTimeout = "{\"text\":\"\",\"extra\":[{\"text\":\"%plugin_prefix%\"},{\"text\":\" \",\"color\":\"white\"},{\"text\":\"Du warst leider nicht schnell genug. Bitte versuche es erneut.\",\"color\":\"&TEXT&\"}]}";

    @Path("confirm.nothing")
    private String confirmNothing = "{\"text\":\"\",\"extra\":[{\"text\":\"%plugin_prefix%\"},{\"text\":\" \",\"color\":\"white\"},{\"text\":\"Du hast nichts zum Bestätigen!\",\"color\":\"&TEXT&\"}]}";

    @Path("confirm.cancel")
    private String confirmCancel = "{\"text\":\"\",\"extra\":[{\"text\":\"%plugin_prefix%\"},{\"text\":\" \",\"color\":\"white\"},{\"text\":\"Bestätigung abgebrochen!\",\"color\":\"&TEXT&\"}]}";

    /**
     * Error
     */

    @Path("error.noweselection")
    private String errorNoweselection = "{\"text\":\"%plugin_prefix%\",\"extra\":[{\"text\":\" Du musst zunächst eine viereckige WorldEdit Selektion machen.\",\"color\":\"red\"}]}";

    @Path("error.noinvzone.location")
    private String errorNoinvzoneLocation = "{\"text\":\"%plugin_prefix%\",\"extra\":[{\"text\":\" Du musst dafür in einer Inventarzone stehen.\",\"color\":\"red\"}]}";

    @Path("error.nosuchinvgroup")
    private String errorNoSuchInvGroup = "{\"text\":\"%plugin_prefix%\",\"extra\":[{\"text\":\" '%group%' ist keine bekannte Inventar-Gruppe für den Spieler '%target%'.\",\"color\":\"red\"}]}";

}
