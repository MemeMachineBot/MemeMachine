package me.kavin.mememachine.command.commands;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import me.kavin.mememachine.command.Command;
import me.kavin.mememachine.utils.ColorUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Define extends Command {

    private String q = null;

    public Define() {
        super(">define", "`Gets the definition of a term from urbandictionary.com`");
    }
    @Override
    public void onCommand(String message, MessageReceivedEvent event) {
        if (message.toLowerCase().startsWith(getPrefix())) {

            q = null;

            if (message.length() > 8) {
                q = "";
                for (int i = 7; i < message.length(); i++)
                    q += message.charAt(i);
            }

            event.getChannel().sendMessage(getSearch(q)).complete();
        }
    }
    private MessageEmbed getSearch(String q) {
        try {
            EmbedBuilder meb = new EmbedBuilder();
            String url = "http://api.urbandictionary.com/v0/autocomplete-extra?&term=" + q.replace(" ", "%20");
            JSONTokener tokener = new JSONTokener(Unirest.get(url).asString().getBody());
            JSONObject root = new JSONObject(tokener);
            meb.setTitle("Urban Dictionary Search: " + q);
            meb.setColor(ColorUtils.getRainbowColor(2000));
            JSONArray jArray = root.getJSONArray("results");
            if (jArray.length() == 0) {
                meb.addField("No Results", "Unfortunately I couldn't find any results for `" + q + "`", false);
                return meb.build();
            }
            JSONObject body = jArray.getJSONObject(0);
            String term = body.getString("term");
            meb.addField('`' + term + '`', StringUtils.abbreviate(getDescription(term, body.getString("preview")), 1024) + '\n', false);
            return meb.build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getDescription(String q, String find) {
        JSONObject jObject = null;
        try {
            jObject = new JSONObject(Unirest.get("http://api.urbandictionary.com/v0/define?term=" + q.replace(" ", "%20")).asString().getBody());
        } catch (JSONException | UnirestException e) {}
        JSONArray jArray = jObject.getJSONArray("list");
        for (int i = 0; i < jArray.length(); i++) {
            JSONObject obj = jArray.getJSONObject(i);
            String s = obj.getString("definition");
            if (s.startsWith(find))
                return s;
        }
        return find;
    }

}