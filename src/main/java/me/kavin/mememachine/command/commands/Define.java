package me.kavin.mememachine.command.commands;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.mashape.unirest.http.Unirest;

import me.kavin.mememachine.command.Command;
import me.kavin.mememachine.utils.ColorUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Define extends Command{
	
	private String q = null;
	
	public Define(){
	super(">define", "`Gets the definition of a term from urbandictionary.com`");
	}
	@Override
	public void onCommand(String message , MessageReceivedEvent event) {
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
	private MessageEmbed getSearch(String q) {
		try {
			EmbedBuilder meb = new EmbedBuilder();
			String url = "https://api.urbandictionary.com/v0/autocomplete-extra?&term=" + q.replace(" ", "%20");
			JSONTokener tokener = new JSONTokener(Unirest.get(url).asString().getBody());
			JSONObject root = new JSONObject(tokener);
			meb.setTitle("Urban Dictionary Search: " + q);
			meb.setColor(ColorUtils.getRainbowColor(2000));
			JSONArray jArray = root.getJSONArray("results");
			if (jArray.length() == 0) {
				meb.addField("No Results", "Unfortunately I couldn't find any results for `" + q + "`", false);
				return meb.build();
			}
			jArray.forEach( item -> {
				JSONObject body = new JSONObject(item.toString());
				meb.addField('`' + body.getString("term") + '`', body.getString("preview") + '\n', false);
			});
			return meb.build();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
