package me.kavin.gwhpaladins;

import java.util.ArrayList;

import javax.security.auth.login.LoginException;

import me.kavin.gwhpaladins.command.CommandManager;
import me.kavin.gwhpaladins.listener.DiscordListener;
import me.kavin.gwhpaladins.utils.Timer;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class Main extends ListenerAdapter{
	public static JDA api;
	public static Timer uptime = new Timer();
	public static ArrayList<TextChannel> channels;

	public static void main(String[] args) {
		try {
			JDABuilder builder = new JDABuilder(AccountType.BOT).setToken("Mzg1ODE0MjgzMzM1NzYxOTIy.DQG1LQ.fhNTyZA6O6aIIPSxYJzlbqV1Syc");
			api = builder.buildAsync();
			api.setAutoReconnect(true);
			DiscordListener.init();
			uptime.reset();
			new CommandManager();
		} catch (LoginException | IllegalArgumentException | RateLimitedException e) {
			e.printStackTrace();
		}
	}
}