package de.cubenation.cninventories.command;

import de.cubenation.api.bedrock.BasePlugin;
import de.cubenation.api.bedrock.annotation.*;
import de.cubenation.api.bedrock.command.Command;
import de.cubenation.api.bedrock.command.CommandRole;
import de.cubenation.api.bedrock.exception.CommandException;
import de.cubenation.api.bedrock.exception.IllegalCommandArgumentException;
import de.cubenation.api.bedrock.exception.InsufficientPermissionException;
import de.cubenation.api.bedrock.helper.BedrockEbeanHelper;
import de.cubenation.api.bedrock.helper.MessageHelper;
import de.cubenation.api.bedrock.helper.UUIDUtil;
import de.cubenation.api.bedrock.service.command.CommandManager;
import de.cubenation.cninventories.config.WorldConfig;
import de.cubenation.cninventories.model.GroupViewerInventoryHolder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

@Description("command.inventory.open.desc")
@SubCommand({"open"})
@Argument(
        Description = "command.inventory.args.player.desc",
        Placeholder = "command.inventory.args.player.ph"
)
@Argument(
        Description = "command.inventory.args.group.desc",
        Placeholder = "command.inventory.args.group.ph"
)
@Permission(Name = "inventory.open", Role = CommandRole.MODERATOR)
@IngameCommand
public class InventoryOpenCommand extends Command {

    public InventoryOpenCommand(BasePlugin plugin, CommandManager commandManager) {
        super(plugin, commandManager);
    }

    public void execute(CommandSender commandSender, String[] args) throws CommandException, IllegalCommandArgumentException, InsufficientPermissionException {
        Player player = (Player) commandSender;
        if (args.length < 1) {
            throw new IllegalCommandArgumentException();
        }
        performPlayerSearch(player, args, args[0]);
    }

    private void performPlayerSearch(final Player player, final String[] args, final String group) {
        if (UUIDUtil.isUUID(args[0])) {
            BedrockEbeanHelper.requestBedrockPlayer(args[0], bedrockPlayers -> {
                if (bedrockPlayers == null) {
                    MessageHelper.noSuchPlayer(plugin, player, args[0]);
                } else {
                    openGroupInventory(player, bedrockPlayers.getPlayer(), args[1]);
                }
            }, e -> MessageHelper.noSuchPlayer(plugin, player, args[0]));
        } else {
            BedrockEbeanHelper.requestBedrockPlayerForLastKnownName(args[0], false, bedrockPlayers -> {
                if (bedrockPlayers == null || bedrockPlayers.isEmpty()) {
                    MessageHelper.noSuchPlayer(plugin, player, args[0]);
                } else {
                    openGroupInventory(player, bedrockPlayers.get(0).getPlayer(), args[1]);
                }
            }, e -> MessageHelper.noSuchPlayer(plugin, player, args[0]));
        }
    }

    private void openGroupInventory(Player player, Player target, String group) {
        GroupViewerInventoryHolder holder = new GroupViewerInventoryHolder(player, target, group);
        player.openInventory(holder.getInventory());
    }

    @Override
    public ArrayList<String> getTabArgumentCompletion(CommandSender sender, int argumentIndex, String[] args) {
        if (argumentIndex == 1) {
            WorldConfig config = (WorldConfig) plugin.getConfigService().getConfig(WorldConfig.class);
            return new ArrayList<>(config.getGroups());
        }

        return null;
    }
}
