package saturne.practice.handler;

import saturne.practice.Main;
import saturne.practice.handler.command.ArenaCommand;
import saturne.practice.handler.command.CasualCommand;
import saturne.practice.handler.command.LadderCommand;
import saturne.practice.handler.command.RankedCommand;

public class CommandHandler {
	
	public CommandHandler(final Main main) {
		main.getCommand("casual").setExecutor(new CasualCommand());
		main.getCommand("ranked").setExecutor(new RankedCommand());
		main.getCommand("ladder").setExecutor(new LadderCommand());
		main.getCommand("arena").setExecutor(new ArenaCommand());
	}

}
