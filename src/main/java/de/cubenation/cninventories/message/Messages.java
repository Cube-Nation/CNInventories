package de.cubenation.cninventories.message;

import de.cubenation.bedrock.core.FoundationPlugin;
import de.cubenation.bedrock.core.model.wrapper.BedrockChatSender;
import de.cubenation.bedrock.core.translation.JsonMessage;
import de.cubenation.cninventories.CNInventoriesPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Messages {

    public static CNInventoriesPlugin plugin = CNInventoriesPlugin.getInstance();

    public static void DebugModeSetSuccess(BedrockChatSender sender, boolean mode) {
        new JsonMessage(plugin, "cninventories.debugmode.set.success", "mode", String.valueOf(mode)).send(sender);
    }

    public static void InventoryContentUpdateSuccess(BedrockChatSender sender) {
        new JsonMessage(plugin, "inventory.content.update.success").send(sender);
    }

    public static void InventoryContentUpdateFail(BedrockChatSender sender) {
        new JsonMessage(plugin, "inventory.content.update.fail").send(sender);
    }

    public static class Confirm {

        public static void OverrideInventory(BedrockChatSender player) {
            String commandinfo = plugin.messages().getPlainText("plaintext.confirm.command.info", "command", "Oder gebe '/inventory confirm' ein");
            new JsonMessage(plugin, "confirm.overrideinventory", "command", "/inventory confirm", "commandinfo", commandinfo).send(player);
        }

        public static void Timeout(BedrockChatSender sender) {
            new JsonMessage(plugin, "confirm.timeout").send(sender);
        }

        public static void Cancel(BedrockChatSender sender) {
            new JsonMessage(plugin, "confirm.cancel").send(sender);
        }

        public static void Nothing(BedrockChatSender player) {
            new JsonMessage(plugin, "confirm.nothing").send(player);
        }
    }

    public static class Error {

        public static void ErrorNoSuchInvGroup(BedrockChatSender sender, String group, String target) {
            new JsonMessage(plugin, "error.nosuchinvgroup",
                    "group", group,
                    "target", target
            ).send(sender);
        }
    }
}
