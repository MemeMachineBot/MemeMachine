package me.kavin.mememachine.command.commands;

import org.json.JSONObject;

import com.mashape.unirest.http.Unirest;

import me.kavin.mememachine.command.Command;
import me.kavin.mememachine.consts.Constants;
import me.kavin.mememachine.utils.ColorUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Profile extends Command {

	public Profile() {
		super(">profile", "`Displays your level and xp`");
	}

	@Override
	public void onCommand(String message, MessageReceivedEvent event) {
		try {
			if (message.toLowerCase().equals(getPrefix())) {

				EmbedBuilder meb = new EmbedBuilder();

				meb.setTitle("Your Profile");
				meb.setColor(ColorUtils.getRainbowColor(2000));

				String resp = Unirest
						.get("https://" + Constants.FB_URL + ".firebaseio.com/users/xp/"
								+ event.getMember().getUser().getIdLong() + ".json" + "?auth=" + Constants.FB_SECRET)
						.asString().getBody();

				if (resp.equals("null")) {
					meb.addField("", "No Data Available" + "\n", false);
				} else {
					meb.addField("XP required:", 500 - (new JSONObject(resp).getInt("xp") % 500) + "\n", false);
					meb.addField("Your Level:", new JSONObject(resp).getInt("xp") / 500 + "\n", false);
				}

				event.getChannel().sendMessage(meb.build()).complete();
			}
		} catch (Exception e) {
		}
	}
}