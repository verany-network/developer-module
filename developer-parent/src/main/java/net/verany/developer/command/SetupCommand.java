package net.verany.developer.command;

import de.dytanic.cloudnet.command.sub.SubCommandBuilder;
import de.dytanic.cloudnet.command.sub.SubCommandHandler;
import net.verany.developer.setup.DeveloperSetup;

import static de.dytanic.cloudnet.command.sub.SubCommandArgumentTypes.*;

public class SetupCommand extends SubCommandHandler {

    public SetupCommand() {
        super(
                SubCommandBuilder.create()
                        .generateCommand((subCommand, sender, command, args, commandLine, properties, internalProperties) -> {
                                    new DeveloperSetup();
                                },
                                anyStringIgnoreCase("setup")).getSubCommands(), "dev"
        );

        this.permission = "verany.command.dev";
        this.prefix = "verany-devs";
    }
}
