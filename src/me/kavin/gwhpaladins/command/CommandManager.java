package me.kavin.gwhpaladins.command;

import java.util.ArrayList;

import me.kavin.gwhpaladins.command.commands.*;

public class CommandManager {
	public static ArrayList<Command> commands = new ArrayList<Command>();
	public CommandManager(){
	this.commands.add(new Help());
	this.commands.add(new UpTime());
	this.commands.add(new Meme());
	}
}