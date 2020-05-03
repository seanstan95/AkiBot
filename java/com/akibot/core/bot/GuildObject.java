package com.akibot.core.bot;

import com.akibot.core.audio.AudioPlayerSendHandler;
import com.akibot.core.audio.TrackScheduler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import net.dv8tion.jda.api.entities.Guild;

public class GuildObject {
    private final AudioPlayer player;
    private final TrackScheduler scheduler;
    private ArrayList<String> modList = new ArrayList<>();
    private String guildId, guildName;

    GuildObject(AudioPlayer player, String guildId, String guildName, ArrayList<String> modList, int volume) {
        this.player = player;
        scheduler = new TrackScheduler(player);
        player.addListener(scheduler);
        this.guildId = guildId;
        this.guildName = guildName;
        if (modList != null) {
            this.modList = modList;
        }
        player.setVolume(volume);
    }

    public AudioPlayer getPlayer() {
        return player;
    }

    public TrackScheduler getScheduler() {
        return scheduler;
    }

    public ArrayList<String> getModList() {
        return modList;
    }

    public String getId() {
        return guildId;
    }

    public String getName() {
        return guildName;
    }

    public AudioPlayerSendHandler getSendHandler() {
        return new AudioPlayerSendHandler(player);
    }

    public void addMod(String memberId) {
        modList.add(memberId);
    }

    public void removeMod(String memberId) {
        modList.remove(memberId);
    }

    public void update(Guild guild) {
        modList.removeIf(modId -> guild.getMemberById(modId) == null);
        guildName = guild.getName();
    }

    JSONObject toJSON() {
        JSONArray mods = new JSONArray();
        JSONObject rootObj = new JSONObject();

        for (String modId : modList) {
            mods.put(new JSONObject().put("Id", modId));
        }

        return rootObj.put("GuildId", guildId)
                .put("GuildName", guildName)
                .put("Volume", player.getVolume())
                .put("Mods", mods);
    }
}