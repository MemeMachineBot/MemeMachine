package me.kavin.mememachine.listener;

import org.json.JSONObject;

import com.mashape.unirest.http.Unirest;

import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
import me.kavin.mememachine.Main;
import me.kavin.mememachine.command.Command;
import me.kavin.mememachine.command.CommandManager;
import me.kavin.mememachine.consts.Constants;
import me.kavin.mememachine.event.EventManager;
import me.kavin.mememachine.event.events.EventGuildReaction;
import me.kavin.mememachine.event.events.EventGuildReactionAdd;
import me.kavin.mememachine.event.events.EventGuildReactionRemove;
import me.kavin.mememachine.lists.API;
import me.kavin.mememachine.utils.Multithreading;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Game.GameType;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.core.events.guild.update.GuildUpdateOwnerEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.react.GenericGuildMessageReactionEvent;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class DiscordListener extends ListenerAdapter {

	Long2LongOpenHashMap lastMsg = new Long2LongOpenHashMap();
	Long2IntOpenHashMap cachedXp = new Long2IntOpenHashMap();

	public static void init() {
		Main.api.addEventListener(new DiscordListener());
	}

	private void setPresence() {
		Main.api.getPresence().setGame(
				Game.of(GameType.DEFAULT, "Meminq | >help | " + Main.api.getGuilds().size() + " Servers!", "Hax.kill"));
	}

	@Override
	public void onReady(ReadyEvent event) {
		API.loop();
		Main.api.getPresence().setStatus(OnlineStatus.IDLE);
		setPresence();
		System.gc();
	}

	@Override
	public void onGuildJoin(GuildJoinEvent event) {
		setPresence();
	}

	@Override
	public void onGuildUpdateOwner(GuildUpdateOwnerEvent event) {
		event.getGuild().getOwner().getUser().openPrivateChannel().complete()
				.sendMessage("Congrats on becoming the new owner of `" + event.getGuild().getName() + "`!").complete();
	}

	@Override
	public void onGuildLeave(GuildLeaveEvent event) {
		setPresence();
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (event.isFromType(ChannelType.PRIVATE) || event.getAuthor() == Main.api.getSelfUser()
				|| event.getAuthor().isBot())
			return;

		{
			if (event.getMessage().getContentRaw().charAt(0) == '>')
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

	private void addXp(long id) {
		try {
			if (!lastMsg.containsKey(id))
				lastMsg.put(id, 0L);

			if (lastMsg.containsKey(id) && System.currentTimeMillis() - lastMsg.get(id) > 60000) {
				if (cachedXp.containsKey(id)) {
					Unirest.put("https://" + Constants.FB_URL + ".firebaseio.com/users/xp/" + id + ".json" + "?auth="
							+ Constants.FB_SECRET).header("content-type", "application/json")
							.body(new JSONObject().put("xp", cachedXp.get(id) + 25)).asString();
				} else {
					String resp = Unirest.get("https://" + Constants.FB_URL + ".firebaseio.com/users/xp/" + id + ".json"
							+ "?auth=" + Constants.FB_SECRET).asString().getBody();
					if (resp.equals("null")) {
						cachedXp.put(id, 25);
						Unirest.put("https://" + Constants.FB_URL + ".firebaseio.com/users/xp/" + id + ".json"
								+ "?auth=" + Constants.FB_SECRET).header("content-type", "application/json")
								.body(new JSONObject().put("xp", 25)).asString();
					} else {
						JSONObject xp = new JSONObject(resp);
						cachedXp.put(id, xp.getInt("xp") + 25);
						Unirest.put("https://" + Constants.FB_URL + ".firebaseio.com/users/xp/" + id + ".json"
								+ "?auth=" + Constants.FB_SECRET).header("content-type", "application/json")
								.body(new JSONObject(resp).put("xp", cachedXp.get(id))).asString();
					}
				}

				lastMsg.put(id, System.currentTimeMillis());
			}
		} catch (Exception e) {
		}
	}

	@Override
	public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
		if (event.getAuthor() != Main.api.getSelfUser() && event.getMessage().getContentRaw().startsWith(">"))
			event.getMessage().getChannel().sendMessage("Error: I don't reply to PM's!").complete();
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