package me.kavin.mememachine.command.commands;

import java.util.concurrent.ThreadLocalRandom;

import me.kavin.mememachine.command.Command;
import me.kavin.mememachine.utils.ColorUtils;
import me.kavin.mememachine.utils.RPSChoice;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class RPS extends Command {
	public RPS() {
		super(">rps", "`Rock Paper Scissors!`");
	}

	@Override
	public void onCommand(String message, MessageReceivedEvent event) {

		String[] split = message.split(" ");

		if (split.length < 2) {
			event.getChannel().sendMessage("`Please choose a choice your argument like` \n>rps <rock/paper/scissors>")
					.complete();
			return;
		}

		EmbedBuilder meb = new EmbedBuilder();

		meb.setTitle("Rock Paper Scissors");
		meb.setColor(ColorUtils.getRainbowColor(2000));

		RPSChoice computer = RPSChoice.values()[ThreadLocalRandom.current().nextInt(3)];
		RPSChoice user = null;

		for (RPSChoice ch : RPSChoice.values())
			if (ch.name().equalsIgnoreCase(split[1])) {
				user = ch;
				break;
			}

		String RESULT = null;

		if (user != null) {
			switch (computer) {
			case ROCK:
				switch (user) {
				case ROCK:
					RESULT = "Tie";
					break;
				case PAPER:
					RESULT = "Win";
					break;
				case SCISSORS:
					RESULT = "Lose";
					break;
				}
				break;
			case PAPER:
				switch (user) {
				case ROCK:
					RESULT = "Lose";
					break;
				case PAPER:
					RESULT = "Tie";
					break;
				case SCISSORS:
					RESULT = "Win";
					break;
				}
				break;
			case SCISSORS:
				switch (user) {
				case ROCK:
					RESULT = "Win";
					break;
				case PAPER:
					RESULT = "Lose";
					break;
				case SCISSORS:
					RESULT = "Tie";
					break;
				}
				break;
			}
		} else {
			event.getChannel()
					.sendMessage("`Please choose a valid choice your argument like` \n>rps <rock/paper/scissors>")
					.complete();
			return;
		}

		meb.addField("I choose `" + computer.name().toLowerCase() + "`!", "It was a `" + RESULT + "` for you!", true);

		event.getChannel().sendMessage(meb.build()).complete();

	}
}
