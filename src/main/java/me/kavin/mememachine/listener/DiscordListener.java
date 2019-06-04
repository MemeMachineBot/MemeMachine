package me.kavin.mememachine.listener;

import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
import me.kavin.mememachine.Main;
import me.kavin.mememachine.command.Command;
import me.kavin.mememachine.command.CommandManager;
import me.kavin.mememachine.event.EventManager;
import me.kavin.mememachine.event.events.EventGuildReaction;
import me.kavin.mememachine.event.events.EventGuildReactionAdd;
import me.kavin.mememachine.event.events.EventGuildReactionRemove;
import me.kavin.mememachine.lists.API;
import me.kavin.mememachine.utils.Multithreading;
import me.kavin.mememachine.utils.XpHelper;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Activity.ActivityType;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateOwnerEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GenericGuildMessageReactionEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class DiscordListener extends ListenerAdapter {

	Long2LongOpenHashMap lastMsg = new Long2LongOpenHashMap();
	Long2IntOpenHashMap cachedXp = new Long2IntOpenHashMap();

	private void setPresence() {
		for (JDA jda : Main.api.getShards())
			jda.getPresence().setActivity(
					Activity.of(ActivityType.DEFAULT, "Meminq | >help | " + Main.api.getGuilds().size() + " Servers!"));
	}

	@Override
	public void onReady(ReadyEvent event) {
		API.loop();
		event.getJDA().getPresence().setStatus(OnlineStatus.IDLE);
		setPresence();
		System.gc();
	}

	@Override
	public void onGuildJoin(GuildJoinEvent event) {
		setPresence();
	}

	@Override
	public void onGuildUpdateOwner(GuildUpdateOwnerEvent event) {
		try {
			event.getGuild().getOwner().getUser().openPrivateChannel().submit().get()
					.sendMessage("Congrats on becoming the new owner of `" + event.getGuild().getName() + "`!").queue();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onGuildLeave(GuildLeaveEvent event) {
		setPresence();
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (event.isFromType(ChannelType.PRIVATE) || event.getAuthor() == Main.api.getShards().get(0).getSelfUser()
				|| event.getAuthor().isBot())
			return;

		{
			if (event.getMessage().getContentRaw().length() > 0 && event.getMessage().getContentRaw().charAt(0) == '>')
				for (Command cmd : CommandManager.commands)
					if (event.getMessage().getContentRaw().split(" ")[0].equalsIgnoreCase(cmd.getPrefix()))
						Multithreading.runAsync(new Runnable() {
							@Override
							public void run() {
								cmd.onCommand(event.getMessage().getContentRaw(), event);
							}
						});
		}

		addXp(event.getAuthor().getIdLong());
	}

	private XpHelper xpHelper = new XpHelper();

	private void addXp(long id) {
		try {
			if (!lastMsg.containsKey(id))
				lastMsg.put(id, 0L);

			if (lastMsg.containsKey(id) && System.currentTimeMillis() - lastMsg.get(id) > 60000) {

				if (cachedXp.containsKey(id))
					cachedXp.put(id, cachedXp.get(id) + 25);
				else
					cachedXp.put(id, xpHelper.getXp(id) + 25);

				xpHelper.setXp(id, cachedXp.get(id));

				lastMsg.put(id, System.currentTimeMillis());
			}
		} catch (Exception e) {
		}
	}

	@Override
	public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
		if (event.getAuthor() != Main.api.getShards().get(0).getSelfUser()
				&& event.getMessage().getContentRaw().startsWith(">"))
			event.getMessage().getChannel().sendMessage("Error: I don't reply to PM's!").queue();
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