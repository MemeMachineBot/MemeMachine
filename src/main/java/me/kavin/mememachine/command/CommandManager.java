package me.kavin.mememachine.command;

import java.util.ArrayList;

import me.kavin.mememachine.command.commands.Avatar;
import me.kavin.mememachine.command.commands.Aww;
import me.kavin.mememachine.command.commands.Bitcoin;
import me.kavin.mememachine.command.commands.Cat;
import me.kavin.mememachine.command.commands.CoinFlip;
import me.kavin.mememachine.command.commands.CopyPasta;
import me.kavin.mememachine.command.commands.Dab;
import me.kavin.mememachine.command.commands.Define;
import me.kavin.mememachine.command.commands.Dog;
import me.kavin.mememachine.command.commands.Google;
import me.kavin.mememachine.command.commands.Hacked;
import me.kavin.mememachine.command.commands.Hastebin;
import me.kavin.mememachine.command.commands.Help;
import me.kavin.mememachine.command.commands.Image;
import me.kavin.mememachine.command.commands.Invite;
import me.kavin.mememachine.command.commands.Meme;
import me.kavin.mememachine.command.commands.PasswordGen;
import me.kavin.mememachine.command.commands.Ping;
import me.kavin.mememachine.command.commands.RPS;
import me.kavin.mememachine.command.commands.Reddit;
import me.kavin.mememachine.command.commands.Roll;
import me.kavin.mememachine.command.commands.Shorten;
import me.kavin.mememachine.command.commands.Subs;
import me.kavin.mememachine.command.commands.UpTime;
import me.kavin.mememachine.command.commands.Vote;
import me.kavin.mememachine.command.commands.Yt;

public class CommandManager {

	public static ArrayList<Command> commands = new ArrayList<Command>();

	public CommandManager() {
		commands.add(new Help());
		commands.add(new UpTime());
		commands.add(new Meme());
		commands.add(new Dab());
		commands.add(new Yt());
		commands.add(new Google());
		commands.add(new Avatar());
		commands.add(new Reddit());
		commands.add(new Invite());
		commands.add(new Define());
		commands.add(new CoinFlip());
		commands.add(new Roll());
		commands.add(new Cat());
		commands.add(new Ping());
		commands.add(new Aww());
		commands.add(new Dog());
		commands.add(new Vote());
		commands.add(new Shorten());
		commands.add(new CopyPasta());
		commands.add(new RPS());
		commands.add(new PasswordGen());
		commands.add(new Bitcoin());
		commands.add(new Hacked());
		commands.add(new Image());
		commands.add(new Hastebin());
		commands.add(new Subs());
	}
}