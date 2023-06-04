    package de.cubenation.cninventories.command;

    import de.cubenation.cninventories.command.autocomplete.InventoryGroupAutoCompleter;
    import dev.projectshard.core.FoundationPlugin;
    import dev.projectshard.core.annotations.*;
    import dev.projectshard.core.authorization.Role;
    import dev.projectshard.core.command.Command;
    import dev.projectshard.core.model.wrapper.ShardPlayer;

    @Description("command.inventory.open.desc")
    @Permission(Name = "inventory.open", Role = Role.MODERATOR)
    @IngameCommand
    public class InventoryOpenCommand extends Command {

        public InventoryOpenCommand(FoundationPlugin plugin) {
            super(plugin);
        }

        public void execute(
                ShardPlayer player,
                @Argument(
                        Description = "command.inventory.args.player.desc",
                        Placeholder = "command.inventory.args.player.ph"
                )
                ShardPlayer target,
                @Argument(
                        Description = "command.inventory.args.group.desc",
                        Placeholder = "command.inventory.args.group.ph",
                        AutoComplete = InventoryGroupAutoCompleter.class
                )
                String group
        ) {
            // TODO: openGroupInventory(player, target, group);
        }

//        private void openGroupInventory(BedrockPlayer player, BedrockPlayer target, String group) {
//             GroupViewerInventoryHolder holder = new GroupViewerInventoryHolder(player, target, group);
//             player.openInventory(holder.getInventory());
//        }
    }
