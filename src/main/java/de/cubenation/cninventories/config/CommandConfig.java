package de.cubenation.cninventories.config;

import de.cubenation.cninventories.command.*;
import dev.projectshard.core.annotations.CommandToken;
import dev.projectshard.core.annotations.HelpMenu;
import dev.projectshard.core.command.Command;

public class CommandConfig {


    @CommandToken("cninventories")
    public static class Base {

        @CommandToken("debug")
        public static Class<? extends Command> debug = CNInventoriesDebugCommand.class;
    }

    @HelpMenu
    @CommandToken("inventory")
    public static class Inventory {

        @CommandToken("open")
        public static Class<? extends Command> inv = InventoryOpenCommand.class;

        @CommandToken({"openec", "openenderchest"})
        public static Class<? extends Command> enderchest = InventoryOpenEnderChestCommand.class;

        @CommandToken("confirm")
        public static Class<? extends Command> confirm = InventoryConfirmCommand.class;

        @CommandToken("cancel")
        public static Class<? extends Command> cancel = InventoryCancelCommand.class;
    }
}
