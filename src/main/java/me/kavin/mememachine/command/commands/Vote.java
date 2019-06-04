package me.kavin.mememachine.command.commands;

import org.json.JSONObject;

import kong.unirest.Unirest;
import me.kavin.mememachine.command.Command;
import me.kavin.mememachine.consts.Constants;
import me.kavin.mememachine.utils.ColorUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Vote extends Command {
	public Vote() {
		super(">vote", "`Gives you the voting link`");
	}

	@Override
	public void onCommand(String message, MessageReceivedEvent event) {
		try {
			EmbedBuilder meb = new EmbedBuilder();

			meb.setTitle("Vote");
			meb.setColor(ColorUtils.getRainbowColor(2000));

			JSONObject jObject = new JSONObject(Unirest
					.get("https://discordbots.org/api/bots/" + event.getJDA().getSelfUser().getIdLong()
							+ "/check?userId=" + event.getMember().getUser().getIdLong())
					.header("Authorization", Constants.DBL_TOKEN).asString().getBody());

			boolean voted = jObject.getInt("voted") == 1;

			if (voted)
				meb.addField("You have already voted! ", "`Thank You`", true);
			else
				meb.addField("`You can vote at` ",
						"https://discordbots.org/bot/" + event.getJDA().getSelfUser().getId() + "/vote", true);

			event.getChannel().sendMessage(meb.build()).queue();
		} catch (Exception e) {
		}
	}
}