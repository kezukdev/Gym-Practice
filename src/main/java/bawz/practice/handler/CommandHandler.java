package bawz.practice.handler;

import bawz.practice.Main;
import bawz.practice.handler.command.ArenaCommand;
import bawz.practice.handler.command.CasualCommand;
import bawz.practice.handler.command.LadderCommand;
import bawz.practice.handler.command.LocationsCommand;
import bawz.practice.handler.command.QueueCommand;
import bawz.practice.handler.command.RankedCommand;
import bawz.practice.handler.command.SettingsCommand;

public class CommandHandler {
	
	public CommandHandler(final Main main) {
		main.getCommand("casual").setExecutor(new CasualCommand(main));
		main.getCommand("ranked").setExecutor(new RankedCommand(main));
		main.getCommand("settings").setExecutor(new SettingsCommand(main));
		main.getCommand("ladder").setExecutor(new LadderCommand(main));
		main.getCommand("arena").setExecutor(new ArenaCommand(main));
		main.getCommand("leavequeue").setExecutor(new QueueCommand(main));
		main.getCommand("location").setExecutor(new LocationsCommand(main));
	}

}
