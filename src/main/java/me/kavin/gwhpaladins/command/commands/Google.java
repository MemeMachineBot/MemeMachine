package me.kavin.gwhpaladins.command.commands;

import org.json.JSONObject;
import org.json.JSONTokener;

import com.mashape.unirest.http.Unirest;

import me.kavin.gwhpaladins.command.Command;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Google extends Command{
	
	private String result = null;
	private String q = null;
	
	public Google(){
	super(".google `Allows you to search google`");
	}
	@Override
	public void onCommand(String message , MessageReceivedEvent event) {
	if (message.toUpperCase().startsWith(".google".toUpperCase())){
		
		q = null;
		
		if(message.length() > 7) {
			q = "";
			for (int i = 7;i < message.length();i++)
				q += message.charAt(i);
		}
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				event.getChannel().sendMessage(getSearch(q)).queue();
			}
		}).start();
		System.gc();
	}
	}
	private String getSearch(String q) {
		try {
			result = null;
			String url = "https://www.googleapis.com/customsearch/v1?" + "safe=medium&" + "q=" + q.replace(" ", "%20") + "&cx=008677437472124065250%3Ajljeb59kuse&imgSize=small&lr=lang_en&num=5&safe=off&key=AIzaSyBjdQtArYb2oOdPIgPy5NQVW7CgBTZsi5E";
			JSONTokener tokener = new JSONTokener(Unirest.get(url).asString().getBody());
			JSONObject root = new JSONObject(tokener);
			if (!root.has("items"))
				return "`No results found for " + q + "`";
			root.getJSONArray("items").forEach( item -> {
				JSONTokener itemTokener = new JSONTokener(item.toString());
				JSONObject body = new JSONObject(itemTokener);
				if(result != null)
					result += "\n`" + body.getString("title") + "`\n`" + body.getString("link") + "`";
				else
					result = "`" + body.getString("title") + "`\n" + body.getString("link");
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result == null ? "Error" : result;
	}
}
