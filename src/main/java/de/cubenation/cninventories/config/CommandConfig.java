package de.cubenation.cninventories.config;

import de.cubenation.bedrock.core.annotation.CommandToken;
import de.cubenation.bedrock.core.command.Command;
import de.cubenation.cninventories.command.*;

public class CommandConfig {

    @CommandToken("cninventories")
    public static class Base {

        @CommandToken("debug")
        public static Class<? extends Command> debug = CNInventoriesDebugCommand.class;
    }

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
