package de.cubenation.cninventories.command.autocomplete;

import de.cubenation.cninventories.config.WorldConfig;
import dev.projectshard.core.FoundationPlugin;
import dev.projectshard.core.annotations.Inject;
import dev.projectshard.core.command.AutoCompletionExecutor;
import dev.projectshard.core.lifecycle.Component;
import dev.projectshard.core.model.wrapper.ShardChatSender;

import java.util.ArrayList;

public class InventoryGroupAutoCompleter extends Component implements AutoCompletionExecutor {

    @Inject
    private WorldConfig worldConfig;

    public InventoryGroupAutoCompleter(FoundationPlugin plugin) {
        super(plugin);
    }

    @Override
    public Iterable<String> onAutoComplete(ShardChatSender shardChatSender, String[] strings) {
        return new ArrayList<>(worldConfig.getGroups());
    }
}
