package me.kavin.gwhpaladins.command;

import java.util.ArrayList;

import me.kavin.gwhpaladins.command.commands.*;

public class CommandManager {
	
	public static ArrayList<Command> commands = new ArrayList<Command>();
	
	public CommandManager(){
	commands.add(new Help());
	commands.add(new UpTime());
	commands.add(new Meme());
	}
	
}