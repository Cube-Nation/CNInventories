package de.cubenation.cninventories.message;

import de.cubenation.api.bedrock.helper.MessageHelper;
import de.cubenation.api.bedrock.translation.JsonMessage;
import de.cubenation.cninventories.CNInventoriesPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Messages extends MessageHelper {

    public static CNInventoriesPlugin plugin = CNInventoriesPlugin.getInstance();

    public static void DebugModeSetSuccess(CommandSender sender, boolean mode) {
        new JsonMessage(plugin, "cninventories.debugmode.set.success", "mode", String.valueOf(mode)).send(sender);
    }

    public static void InventoryContentUpdateSuccess(CommandSender sender) {
        new JsonMessage(plugin, "inventory.content.update.success").send(sender);
    }

    public static void InventoryContentUpdateFail(CommandSender sender) {
        new JsonMessage(plugin, "inventory.content.update.fail").send(sender);
    }

    public static class Confirm {

        public static void OverrideInventory(Player player) {
            String commandinfo = getPlainText(plugin, "plaintext.confirm.command.info", "command", "Oder gebe '/inventory confirm' ein");
            new JsonMessage(plugin, "confirm.overrideinventory", "command", "/inventory confirm", "commandinfo", commandinfo).send(player);
        }

        public static void Timeout(CommandSender sender) {
            new JsonMessage(plugin, "confirm.timeout").send(sender);
        }

        public static void Cancel(CommandSender sender) {
            new JsonMessage(plugin, "confirm.cancel").send(sender);
        }

        public static void Nothing(CommandSender player) {
            new JsonMessage(plugin, "confirm.nothing").send(player);
        }
    }

    public static class Error {
        public static void ErrorNoSuchInvGroup(CommandSender sender, String group, String target) {
            new JsonMessage(plugin, "error.nosuchinvgroup",
                    "group", group,
                    "target", target
            ).send(sender);
        }
    }
}
