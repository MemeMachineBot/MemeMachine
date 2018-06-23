package me.kavin.mememachine.command.commands;

import java.net.URLEncoder;

import me.kavin.mememachine.Main;
import me.kavin.mememachine.command.Command;
import me.kavin.mememachine.consts.Constants;
import me.kavin.mememachine.utils.ColorUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Robot extends Command {

	public Robot() {
		super(">robot", "`Shows a robot avatar of you or the person you tag!`");
	}

	@Override
	public void onCommand(String message, MessageReceivedEvent event) {
		try {
			if (message.toLowerCase().startsWith(getPrefix()) || message.equalsIgnoreCase(getPrefix())) {

				EmbedBuilder meb = new EmbedBuilder();

				meb.setTitle("Your Robot Avatar");
				meb.setColor(ColorUtils.getRainbowColor(2000));

				String[] split = message.split(" ");

				if (split.length == 1)
					meb.setImage(Constants.GOOGLE_PROXY_IMAGE + URLEncoder.encode(
							"https://robohash.org/" + URLEncoder.encode(event.getAuthor().getName(), "UTF-8"),
							"UTF-8"));
				else {
					long memberId;
					try {
						memberId = getLong(split[1]);
					} catch (NumberFormatException e) {
						event.getChannel().sendMessage("`Please tag a person as your argument like` \n>robot "
								+ Main.api.getSelfUser().getAsMention()).complete();
						return;
					}

					if (event.getGuild().getMemberById(memberId) != null)
						meb.setImage(Constants.GOOGLE_PROXY_IMAGE + URLEncoder.encode(
								"https://robohash.org/" + URLEncoder
										.encode(event.getGuild().getMemberById(memberId).getUser().getName(), "UTF-8"),
								"UTF-8"));

				}
				event.getChannel().sendMessage(meb.build()).complete();
			}
		} catch (Exception e) {
		}
	}

	private long getLong(String s) throws NumberFormatException {
		StringBuilder sb = new StringBuilder();
		char c;
		for (int i = 0; i < s.length(); i++)
			if ((c = s.charAt(i)) >= '0' && c <= '9')
				sb.append(c);
		return Long.parseLong(sb.toString());
	}
}