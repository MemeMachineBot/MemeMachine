package me.kavin.gwhpaladins;

import javax.security.auth.login.LoginException;

import me.kavin.gwhpaladins.command.CommandManager;
import me.kavin.gwhpaladins.listener.DiscordListener;
import me.kavin.gwhpaladins.utils.Timer;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class Main extends ListenerAdapter{
	public static JDA api;
	public static Timer uptime = new Timer();

	public static void main(String[] args) {
		try {
			// iblizzilo MzY5ODE4NTk2MjQ0NzE3NTY5.DUIiCA.XmnJyu6oHtyK82xRiDuzLbDslMA
			// Meme Machine NDQ1ODAwNTA1MzE1NDI2MzE1.DdvvmQ.7zoTIo-5hKDGOurO-luWR-hC258
			JDABuilder builder = new JDABuilder(AccountType.BOT).setToken("NDQ1ODAwNTA1MzE1NDI2MzE1.DdvvmQ.7zoTIo-5hKDGOurO-luWR-hC258");
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