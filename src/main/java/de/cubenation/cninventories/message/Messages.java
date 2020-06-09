package de.cubenation.cninventories.message;

import de.cubenation.api.bedrock.helper.MessageHelper;
import de.cubenation.api.bedrock.translation.JsonMessage;
import de.cubenation.cninventories.CNInventoriesPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Messages extends MessageHelper {

    public static CNInventoriesPlugin plugin = CNInventoriesPlugin.getInstance();

    public static void InvZoneSetSuccess(CommandSender sender) {
        new JsonMessage(plugin, "invzone.set.success").send(sender);
    }

    public static void InvZoneSetFail(CommandSender sender) {
        new JsonMessage(plugin, "invzone.set.fail").send(sender);
    }

    public static void InvZoneRemoveSuccess(CommandSender sender) {
        new JsonMessage(plugin, "invzone.remove.success").send(sender);
    }

    public static void InvZoneRemoveFail(CommandSender sender) {
        new JsonMessage(plugin, "invzone.remove.fail").send(sender);
    }

    public static void InvZoneModifyGroupSuccess(CommandSender sender) {
        new JsonMessage(plugin, "invzone.modify.group.success").send(sender);
    }

    public static void InvZoneModifyGroupFail(CommandSender sender) {
        new JsonMessage(plugin, "invzone.modify.group.fail").send(sender);
    }

    public static void InventoryContentUpdateSuccess(CommandSender sender) {
        new JsonMessage(plugin, "inventory.content.update.success").send(sender);
    }

    public static void InventoryContentUpdateFail(CommandSender sender) {
        new JsonMessage(plugin, "inventory.content.update.fail").send(sender);
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
