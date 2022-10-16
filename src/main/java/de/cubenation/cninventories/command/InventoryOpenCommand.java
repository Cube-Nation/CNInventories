    package de.cubenation.cninventories.command;

    import de.cubenation.bedrock.bukkit.api.helper.BedrockEbeanHelper;
    import de.cubenation.bedrock.core.FoundationPlugin;
    import de.cubenation.bedrock.core.annotation.Argument;
    import de.cubenation.bedrock.core.annotation.Description;
    import de.cubenation.bedrock.core.annotation.IngameCommand;
    import de.cubenation.bedrock.core.annotation.Permission;
    import de.cubenation.bedrock.core.authorization.Role;
    import de.cubenation.bedrock.core.command.Command;
    import de.cubenation.bedrock.core.exception.IllegalCommandArgumentException;
    import de.cubenation.bedrock.core.helper.UUIDUtil;
    import de.cubenation.bedrock.core.model.wrapper.BedrockChatSender;
    import de.cubenation.bedrock.core.model.wrapper.BedrockPlayer;
    import de.cubenation.cninventories.config.WorldConfig;
    import de.cubenation.cninventories.model.GroupViewerInventoryHolder;
    import org.bukkit.command.CommandSender;
    import org.bukkit.entity.Player;

    import java.util.ArrayList;

    @Description("command.inventory.open.desc")
    @Permission(Name = "inventory.open", Role = Role.MODERATOR)
    @IngameCommand
    public class InventoryOpenCommand extends Command {

        public InventoryOpenCommand(FoundationPlugin plugin) {
            super(plugin);
        }

        public void execute(
                BedrockChatSender commandSender,
                @Argument(
                        Description = "command.inventory.args.player.desc",
                        Placeholder = "command.inventory.args.player.ph"
                ) BedrockPlayer target,
                @Argument(
                        Description = "command.inventory.args.group.desc",
                        Placeholder = "command.inventory.args.group.ph"
                )String group
        ) {
            BedrockPlayer player = (BedrockPlayer) commandSender;
            openGroupInventory(player, target, group);
        }

        private void openGroupInventory(BedrockPlayer player, BedrockPlayer target, String group) {
            // TODO: InvView
            // GroupViewerInventoryHolder holder = new GroupViewerInventoryHolder(player, target, group);
            // player.openInventory(holder.getInventory());
        }

        // TODO: tabcompletion
    //    @Override
    //    public ArrayList<String> getTabArgumentCompletion(CommandSender sender, int argumentIndex, String[] args) {
    //        if (argumentIndex == 1) {
    //            WorldConfig config = (WorldConfig) plugin.getConfigService().getConfig(WorldConfig.class);
    //            return new ArrayList<>(config.getGroups());
    //        }
    //
    //        return null;
    //    }
    }
