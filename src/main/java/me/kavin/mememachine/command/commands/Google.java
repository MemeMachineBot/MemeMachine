package me.kavin.mememachine.command.commands;

import org.json.JSONObject;
import org.json.JSONTokener;

import com.mashape.unirest.http.Unirest;

import me.kavin.mememachine.command.Command;
import me.kavin.mememachine.utils.ColorUtils;
import me.kavin.mememachine.utils.Multithreading;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Google extends Command{
	
	private String q = null;
	
	public Google(){
	super(">google", "`Allows you to search google`");
	}
	@Override
	public void onCommand(String message , MessageReceivedEvent event) {
		Multithreading.runAsync(new Runnable() {
			@Override
			public void run() {
				if (message.toLowerCase().startsWith(getPrefix())){
					
					q = null;
					
					if(message.length() > 8) {
						q = "";
						for (int i = 7;i < message.length();i++)
							q += message.charAt(i);
					}
					
					event.getChannel().sendMessage(getSearch(q)).queue();
				}
			}
		});
	}
	private MessageEmbed getSearch(String q) {
		try {
			EmbedBuilder meb = new EmbedBuilder();
			String url = "https://www.googleapis.com/customsearch/v1?" + "safe=medium&" + "q=" + q.replace(" ", "%20") + "&cx=008677437472124065250%3Ajljeb59kuse&imgSize=small&lr=lang_en&num=5&key=AIzaSyBjdQtArYb2oOdPIgPy5NQVW7CgBTZsi5E";
			JSONTokener tokener = new JSONTokener(Unirest.get(url).asString().getBody());
			JSONObject root = new JSONObject(tokener);
			meb.setTitle("Google Search: " + q);
			meb.setColor(ColorUtils.getRainbowColor(2000));
			if (!root.has("items")) {
				meb.addField("No Results", "Unfortunately I couldn't find any results for `" + q + "`", true);
				return meb.build();
			}
			root.getJSONArray("items").forEach( item -> {
				JSONTokener itemTokener = new JSONTokener(item.toString());
				JSONObject body = new JSONObject(itemTokener);
				meb.addField('`' + body.getString("title") + '`', body.getString("link"), true);
			});
			return meb.build();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
