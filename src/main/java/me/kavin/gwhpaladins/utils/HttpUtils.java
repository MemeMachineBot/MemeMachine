package me.kavin.gwhpaladins.utils;

import me.kavin.gwhpaladins.Main;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.json.JSONObject;

import com.mashape.unirest.http.Unirest;

public class HttpUtils {
	private static LocalDateTime lastUpdate = LocalDateTime.MIN;

    public static void updateCount(int i, String auth)
    {
        if (lastUpdate.until(LocalDateTime.now(), ChronoUnit.SECONDS) > 60)
        {
            JSONObject json = new JSONObject().put("server_count", i);

            Unirest.post("https://bots.discord.pw/api/bots/" + Main.api.getSelfUser().getId() + "/stats")
                    .header("Authorization", auth)
                    .header("Content-Type", "application/json")
                    .body(json);
        }
    }
}
