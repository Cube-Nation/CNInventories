package de.cubenation.cninventories.config.locale;

import de.cubenation.api.bedrock.BasePlugin;
import de.cubenation.api.bedrock.service.config.CustomConfigurationFile;
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

    @Path("command.invzone.set.desc")
    private String commandInvzoneSetDesc = "Setzt eine Inventar-Zone.";

    @Path("command.invzone.remove.desc")
    private String commandInvzoneRemoveDesc = "Löscht eine Inventar-Zone in der du dich befindest.";

    @Path("command.invzone.modify.group.desc")
    private String commandInvzoneModifyGroupDesc = "Ändert die Gruppe der Inventar-Zone in der du dich befindest.";

    @Path("command.invzone.args.group")
    private HashMap<String, String> command_city_arg_delete_flags = new HashMap<String, String>() {{
        put("desc", "Der relative Pfad unter welchem das Inventar gespeichert werden soll.");
        put("ph", "Gruppe");
    }};

    /**
     * Command Messages
     */

    @Path("invzone.set.success")
    private String invzoneSetSuccess = "{\"text\":\"%plugin_prefix%\",\"extra\":[{\"text\":\" Die Inventar-Zone wurde erfolgreich erstellt!\",\"color\":\"&PRIMARY&\"}]}";

    @Path("invzone.set.fail")
    private String invzoneSetFail = "{\"text\":\"%plugin_prefix%\",\"extra\":[{\"text\":\" Die Inventar-Zone konnte nicht erstellt werden!\",\"color\":\"red\"}]}";

    @Path("invzone.remove.success")
    private String invzoneRemoveSuccess = "{\"text\":\"%plugin_prefix%\",\"extra\":[{\"text\":\" Die Inventar-Zone wurde erfolgreich erstellt!\",\"color\":\"&PRIMARY&\"}]}";

    @Path("invzone.remove.fail")
    private String invzoneRemoveFail = "{\"text\":\"%plugin_prefix%\",\"extra\":[{\"text\":\" Die Inventar-Zone konnte nicht gelöscht werden!\",\"color\":\"red\"}]}";

    @Path("invzone.modify.group.success")
    private String invzoneModifyGroupSuccess = "{\"text\":\"%plugin_prefix%\",\"extra\":[{\"text\":\" Die Inventar-Gruppe wurde erfolgreich bearbeitet!\",\"color\":\"&PRIMARY&\"}]}";

    @Path("invzone.modify.group.fail")
    private String invzoneModifyGroupFail = "{\"text\":\"%plugin_prefix%\",\"extra\":[{\"text\":\" Die Inventar-Gruppe konnte nicht bearbeitet werden!\",\"color\":\"red\"}]}";

    /**
     * Error
     */

    @Path("error.noweselection")
    private String errorNoweselection = "{\"text\":\"%plugin_prefix%\",\"extra\":[{\"text\":\" Du musst zunächst eine viereckige WorldEdit Selektion machen.\",\"color\":\"red\"}]}";

    @Path("error.noinvzone.location")
    private String errorNoinvzoneLocation = "{\"text\":\"%plugin_prefix%\",\"extra\":[{\"text\":\" Du musst dafür in einer Inventarzone stehen.\",\"color\":\"red\"}]}";

}
