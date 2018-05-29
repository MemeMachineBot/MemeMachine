package me.kavin.mememachine.command;

import java.util.ArrayList;

import me.kavin.mememachine.command.commands.*;

public class CommandManager {
	
	public static ArrayList<Command> commands = new ArrayList<Command>();
	
	public CommandManager(){
		commands.add(new Help());
		commands.add(new UpTime());
		commands.add(new Meme());
		commands.add(new Dab());
		commands.add(new Yt());
		commands.add(new Google());
		commands.add(new Profile());
		commands.add(new Reddit());
		commands.add(new Invite());
		commands.add(new Define());
		commands.add(new CoinFlip());
		commands.add(new Roll());
		commands.add(new Cat());
		commands.add(new Ping());
	}
}