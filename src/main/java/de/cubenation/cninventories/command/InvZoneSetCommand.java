package de.cubenation.cninventories.command;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.BukkitPlayer;
import com.sk89q.worldedit.regions.Region;
import de.cubenation.api.bedrock.BasePlugin;
import de.cubenation.api.bedrock.annotation.*;
import de.cubenation.api.bedrock.command.Command;
import de.cubenation.api.bedrock.command.CommandRole;
import de.cubenation.api.bedrock.exception.CommandException;
import de.cubenation.api.bedrock.exception.IllegalCommandArgumentException;
import de.cubenation.api.bedrock.exception.InsufficientPermissionException;
import de.cubenation.api.bedrock.service.command.CommandManager;
import de.cubenation.cninventories.config.InventoryZoneConfig;
import de.cubenation.cninventories.config.WorldConfig;
import de.cubenation.cninventories.model.InventoryZone;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

@Description("command.invzone.set.desc")
@SubCommand("set")
@Argument(
        Description = "command.invzone.args.group.desc",
        Placeholder = "command.invzone.args.group.ph"
)
@Permission(Name = "invzone.set", Role = CommandRole.ADMIN)
@IngameCommand
public class InvZoneSetCommand extends Command {

    public InvZoneSetCommand(BasePlugin plugin, CommandManager commandManager) {
        super(plugin, commandManager);
    }

    public void execute(CommandSender commandSender, String[] args) throws CommandException, IllegalCommandArgumentException, InsufficientPermissionException {
        Player player = (Player) commandSender;
        String name = args[0];

        InventoryZoneConfig config = (InventoryZoneConfig) plugin.getConfigService().getConfig(InventoryZoneConfig.class);

        boolean success = createZoneWithWE(player, config, name);

        //TODO: Messages.InvZone.Create(player, success);
        player.sendMessage("Messages.InvZone.Create(player, success) : "+success);
    }

    private boolean createZoneWithWE(Player player, InventoryZoneConfig config, String name) {
        BukkitPlayer bPlayer = BukkitAdapter.adapt(player);
        Region selection = null;
        try {
            selection = WorldEdit.getInstance().getSessionManager().get(bPlayer).getSelection(bPlayer.getWorld());
        } catch (IncompleteRegionException e) {
            selection = null;
        }
        if (selection == null) {
            //TODO: Messages.InvZone.NoWESelection(player);
            player.sendMessage("Messages.InvZone.NoWESelection(player)");
            return false;
        }

        InventoryZone teleporter = new InventoryZone(UUID.randomUUID(), name, player.getWorld(), selection.getMinimumPoint(), selection.getMaximumPoint());

        return config.storeZone(teleporter);
    }

    @Override
    public ArrayList<String> getTabArgumentCompletion(CommandSender sender, int argumentIndex, String[] args) {
        if (argumentIndex == 0) {
            WorldConfig config = (WorldConfig) plugin.getConfigService().getConfig(WorldConfig.class);
            return new ArrayList<>(config.getGroups());
        }

        return null;
    }
}
