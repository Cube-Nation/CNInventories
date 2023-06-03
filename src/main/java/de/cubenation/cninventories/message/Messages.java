package de.cubenation.cninventories.message;

import de.cubenation.cninventories.CNInventoriesPlugin;
import dev.projectshard.core.chat.localization.JsonMessage;
import dev.projectshard.core.model.wrapper.ShardChatSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Messages {

    public static CNInventoriesPlugin plugin = CNInventoriesPlugin.getInstance();

    public static void DebugModeSetSuccess(ShardChatSender sender, boolean mode) {
        new JsonMessage(plugin, "cninventories.debugmode.set.success", "mode", String.valueOf(mode)).send(sender);
    }

    public static void InventoryContentUpdateSuccess(ShardChatSender sender) {
        new JsonMessage(plugin, "inventory.content.update.success").send(sender);
    }

    public static void InventoryContentUpdateFail(ShardChatSender sender) {
        new JsonMessage(plugin, "inventory.content.update.fail").send(sender);
    }

    public static class Confirm {

        public static void OverrideInventory(ShardChatSender player) {
            String commandinfo = plugin.messages().getPlainText("plaintext.confirm.command.info", "command", "Oder gebe '/inventory confirm' ein");
            new JsonMessage(plugin, "confirm.overrideinventory", "command", "/inventory confirm", "commandinfo", commandinfo).send(player);
        }

        public static void Timeout(ShardChatSender sender) {
            new JsonMessage(plugin, "confirm.timeout").send(sender);
        }

        public static void Cancel(ShardChatSender sender) {
            new JsonMessage(plugin, "confirm.cancel").send(sender);
        }

        public static void Nothing(ShardChatSender player) {
            new JsonMessage(plugin, "confirm.nothing").send(player);
        }
    }

    public static class Error {

        public static void ErrorNoSuchInvGroup(ShardChatSender sender, String group, String target) {
            new JsonMessage(plugin, "error.nosuchinvgroup",
                    "group", group,
                    "target", target
            ).send(sender);
        }
    }
}
