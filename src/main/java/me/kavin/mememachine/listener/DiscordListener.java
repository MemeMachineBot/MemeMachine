package me.kavin.mememachine.listener;

import java.util.concurrent.TimeUnit;

import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
import me.kavin.mememachine.Main;
import me.kavin.mememachine.command.Command;
import me.kavin.mememachine.command.CommandManager;
import me.kavin.mememachine.event.EventManager;
import me.kavin.mememachine.event.events.EventGuildReaction;
import me.kavin.mememachine.event.events.EventGuildReactionAdd;
import me.kavin.mememachine.event.events.EventGuildReactionRemove;
import me.kavin.mememachine.utils.ColorUtils;
import me.kavin.mememachine.utils.Multithreading;
import me.kavin.mememachine.utils.XpHelper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Activity.ActivityType;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GenericGuildMessageReactionEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class DiscordListener extends ListenerAdapter {

	private static final Long2LongOpenHashMap lastMsg = new Long2LongOpenHashMap();
	private static final Long2IntOpenHashMap cachedXp = new Long2IntOpenHashMap();

	private void setPresence() {
		for (JDA jda : Main.api.getShards())
			jda.getPresence().setActivity(
					Activity.of(ActivityType.DEFAULT, "Meminq | >help | " + Main.api.getGuilds().size() + " Servers!"));
	}

	@Override
	public void onReady(ReadyEvent event) {
		event.getJDA().getPresence().setStatus(OnlineStatus.IDLE);
		setPresence();
	}

	@Override
	public void onGuildJoin(GuildJoinEvent event) {
		setPresence();
	}

	@Override
	public void onGuildLeave(GuildLeaveEvent event) {
		setPresence();
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {

		if (event.isFromType(ChannelType.PRIVATE) || event.getAuthor().isBot())
			return;

		{
			if (event.getMessage().getContentRaw().length() > 0 && event.getMessage().getContentRaw().charAt(0) == '>')
				for (Command cmd : CommandManager.commands)
					for (String prefix : cmd.getPossiblePrefixes())
						if (event.getMessage().getContentRaw().split(" ")[0].equalsIgnoreCase(prefix))
							Multithreading.runAsync(new Runnable() {
								@Override
								public void run() {
									try {
										cmd.onCommand(event.getMessage().getContentRaw(), event);
									} catch (Exception e) {
										EmbedBuilder meb = new EmbedBuilder();
										meb.setColor(ColorUtils.getRainbowColor(2000));
										meb.setTitle("Error when running command!");
										meb.setDescription(
												"An exception occoured when processing your command, please open a GitHub issue if this continues.");
										event.getChannel().sendMessage(meb.build()).queue();
										e.printStackTrace();
									}
								}
							});
		}

		addXp(event.getAuthor().getIdLong());
	}

	private XpHelper xpHelper = new XpHelper();

	private void addXp(long id) {
		if (!lastMsg.containsKey(id))
			lastMsg.put(id, 0L);

		if (lastMsg.containsKey(id) && System.currentTimeMillis() - lastMsg.get(id) > TimeUnit.SECONDS.toMillis(25)) {

			Multithreading.runAsync(new Runnable() {
				@Override
				public void run() {
					cachedXp.put(id, (cachedXp.containsKey(id) ? cachedXp.get(id) : xpHelper.getXp(id)) + 25);
					xpHelper.setXp(id, cachedXp.get(id));
				}
			});

			lastMsg.put(id, System.currentTimeMillis());
		}
	}

	@Override
	public void onGenericGuildMessageReaction(GenericGuildMessageReactionEvent event) {
		Multithreading.runAsync(new Runnable() {
			@Override
			public void run() {
				EventManager.getDefault().call(new EventGuildReaction(event));
			}
		});
	}

	@Override
	public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
		Multithreading.runAsync(new Runnable() {
			@Override
			public void run() {
				EventManager.getDefault().call(new EventGuildReactionAdd(event));
			}
		});
	}

	@Override
	public void onGuildMessageReactionRemove(GuildMessageReactionRemoveEvent event) {
		Multithreading.runAsync(new Runnable() {
			@Override
			public void run() {
				EventManager.getDefault().call(new EventGuildReactionRemove(event));
			}
		});
	}
}