package gym.practice.handler;

import gym.practice.Main;
import gym.practice.handler.command.ArenaCommand;
import gym.practice.handler.command.BuildCommand;
import gym.practice.handler.command.CasualCommand;
import gym.practice.handler.command.LadderCommand;
import gym.practice.handler.command.LocationsCommand;
import gym.practice.handler.command.MergeCommand;
import gym.practice.handler.command.QueueCommand;
import gym.practice.handler.command.RankedCommand;
import gym.practice.handler.command.ScoreboardCommand;
import gym.practice.handler.command.SettingsCommand;

public class CommandHandler {
	
	public CommandHandler(final Main main) {
		main.getCommand("casual").setExecutor(new CasualCommand(main));
		main.getCommand("ranked").setExecutor(new RankedCommand(main));
		main.getCommand("settings").setExecutor(new SettingsCommand(main));
		main.getCommand("ladder").setExecutor(new LadderCommand(main));
		main.getCommand("arena").setExecutor(new ArenaCommand(main));
		main.getCommand("leavequeue").setExecutor(new QueueCommand(main));
		main.getCommand("location").setExecutor(new LocationsCommand(main));
		main.getCommand("sb").setExecutor(new ScoreboardCommand(main));
		main.getCommand("merge").setExecutor(new MergeCommand(main));
		main.getCommand("build").setExecutor(new BuildCommand(main));
	}

}
