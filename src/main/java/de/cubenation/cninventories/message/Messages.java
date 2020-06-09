package de.cubenation.cninventories.message;

import de.cubenation.api.bedrock.helper.MessageHelper;
import de.cubenation.api.bedrock.translation.JsonMessage;
import de.cubenation.cninventories.CNInventoriesPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Messages extends MessageHelper {

    public static CNInventoriesPlugin plugin = CNInventoriesPlugin.getInstance();

    public static void InvZoneSetSuccess(CommandSender sender) {
        new JsonMessage(plugin, "inventory.zone.set.success").send(sender);
    }

    public static void InvZoneSetFail(CommandSender sender) {
        new JsonMessage(plugin, "inventory.zone.set.fail").send(sender);
    }

    public static void InvZoneRemoveSuccess(CommandSender sender) {
        new JsonMessage(plugin, "inventory.zone.remove.success").send(sender);
    }

    public static void InvZoneRemoveFail(CommandSender sender) {
        new JsonMessage(plugin, "inventory.zone.remove.fail").send(sender);
    }

    public static void InvZoneModifyGroupSuccess(CommandSender sender) {
        new JsonMessage(plugin, "inventory.zone.modify.group.success").send(sender);
    }

    public static void InvZoneModifyGroupFail(CommandSender sender) {
        new JsonMessage(plugin, "inventory.zone.modify.group.fail").send(sender);
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
        public static void ErrorNoWESelection(CommandSender sender) {
            new JsonMessage(plugin, "error.noweselection").send(sender);
        }

        public static void ErrorNoInvZoneAtLocation(CommandSender sender) {
            new JsonMessage(plugin, "error.noinvzone.location").send(sender);
        }

        public static void ErrorNoSuchInvGroup(CommandSender sender, String group, Player target) {
            new JsonMessage(plugin, "error.nosuchinvgroup",
                    "group", group,
                    "target", target.getDisplayName()
            ).send(sender);
        }
    }
}
