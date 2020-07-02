package me.kavin.mememachine.command.commands;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import kong.unirest.Unirest;
import me.kavin.mememachine.command.Command;
import me.kavin.mememachine.event.EventHandler;
import me.kavin.mememachine.event.events.EventGuildReactionAdd;
import me.kavin.mememachine.event.events.EventGuildReactionRemove;
import me.kavin.mememachine.utils.ColorUtils;
import me.kavin.mememachine.utils.Multithreading;
import me.kavin.mememachine.utils.WYRGame;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;

public class WYR extends Command {

	Long2ObjectOpenHashMap<WYRGame> sent = new Long2ObjectOpenHashMap<>();
	private String emoji1 = "1⃣";
	private String emoji2 = "2⃣";

	public WYR() {
		super(">wyr", "`The would you rather game!`");
	}

	@Override
	public void onCommand(String message, MessageReceivedEvent event) {
		try {

			EmbedBuilder meb = new EmbedBuilder();

			meb.setTitle("Would You Rather");
			meb.setColor(ColorUtils.getRainbowColor(2000));

			Elements options = Jsoup.parse(Unirest.get("http://either.io/").asString().getBody())
					.getElementsByClass("option-text");

			String choice1 = options.get(0).text();
			String choice2 = options.get(1).text();

			meb.addField(emoji1, choice1 + "\n", false);
			meb.addField(emoji2, choice2 + "\n", false);

			Message msg = event.getChannel().sendMessage(meb.build()).submit().get();

			msg.addReaction(emoji1).queue();
			msg.addReaction(emoji2).queue();

			sent.put(msg.getIdLong(), new WYRGame(choice1, choice2));

			Multithreading.runAsync(new Runnable() {
				@Override
				public void run() {

					try {
						Thread.sleep(45000);
					} catch (Exception e) {
					}

					long msgId = msg.getIdLong();
					WYRGame game = sent.get(msgId);

					int votes1 = game.getVotes1();
					int votes2 = game.getVotes2();

					EmbedBuilder meb = new EmbedBuilder();

					meb.setTitle("Would You Rather Results");
					meb.setColor(ColorUtils.getRainbowColor(2000));

					if (votes1 > votes2) {
						meb.addField("", "The winner was `" + game.getChoice1() + "`", false);
					} else if (votes2 > votes1) {
						meb.addField("", "The winner was `" + game.getChoice2() + "`", false);
					} else {
						meb.addField("", "It was a `draw`", false);
					}

					event.getChannel().sendMessage(meb.build()).queue();

					sent.remove(msgId);
				}
			});
		} catch (Exception e) {
		}
	}

	@EventHandler
	private void onReactionAdd(EventGuildReactionAdd event) throws Exception {
		GuildMessageReactionAddEvent reactionEvent = event.getEvent();

		User user = reactionEvent.getJDA().retrieveUserById(reactionEvent.getUserIdLong()).submit().get();

		if (user.isBot())
			return;

		long msgId = reactionEvent.getMessageIdLong();

		if (sent.containsKey(msgId)) {
			if (reactionEvent.getReactionEmote().getName().equals(emoji1))
				sent.get(msgId).addVotes1();
			if (reactionEvent.getReactionEmote().getName().equals(emoji2))
				sent.get(msgId).addVotes2();
		}
	}

	@EventHandler
	private void onReactionRemove(EventGuildReactionRemove event) throws Exception {
		GuildMessageReactionRemoveEvent reactionEvent = event.getEvent();

		User user = reactionEvent.getJDA().retrieveUserById(reactionEvent.getUserIdLong()).submit().get();

		if (user.isBot())
			return;

		long msgId = reactionEvent.getMessageIdLong();

		if (sent.containsKey(msgId)) {
			if (reactionEvent.getReactionEmote().getName().equals(emoji1))
				sent.get(msgId).removeVotes1();
			if (reactionEvent.getReactionEmote().getName().equals(emoji2))
				sent.get(msgId).removeVotes2();
		}
	}
}