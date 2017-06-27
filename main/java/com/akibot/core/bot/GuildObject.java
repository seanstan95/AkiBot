package com.akibot.core.bot;

/*
    * AkiBot v3.0.2 by PhoenixAki: music + moderation bot for usage in Discord servers.
    *
    * GuildObject
    * Every guild AkiBot joins has a GuildObject associated to it.
    * An instance of a GuildObject handles the audio player, list of mods, etc. that are specific to that guild.
*/

import com.akibot.commands.ModLevel;
import com.akibot.core.audio.AudioPlayerSendHandler;
import com.akibot.core.audio.TrackScheduler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class GuildObject {
    private final AudioPlayer player;
    private final TrackScheduler scheduler;
    private HashMap<String, ModLevel> modList = new HashMap<>();
    private String id, name;

    GuildObject(AudioPlayerManager manager, String guildId, String guildName, HashMap<String, ModLevel> modList, int volume){
        player = manager.createPlayer();
        scheduler = new TrackScheduler(player);
        player.addListener(scheduler);
        this.id = guildId;
        this.name = guildName;
        if(modList != null){
            this.modList = modList;
        }
        player.setVolume(volume);
    }

    public AudioPlayer getPlayer(){
        return player;
    }

    public TrackScheduler getScheduler(){
        return scheduler;
    }

    public HashMap<String, ModLevel> getModList(){
        return modList;
    }

    public String getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public AudioPlayerSendHandler getSendHandler(){
        return new AudioPlayerSendHandler(player);
    }

    public void changeMod(String id, ModLevel modLevel, boolean change, boolean remove){
        //On creation of a Guild Object, the owner is added with FULL access (this is to ensure the owner is always able to add other mods).
        //change is used to determine if a pre-existing mod's level is being changed.
        //remove is used to determine if removing a pre-existing mod from the list

        if(change){
            modList.remove(id);
            modList.put(id, modLevel);
        }else if(remove) {
            modList.remove(id);
        }else{
            modList.put(id, modLevel);
        }
    }

    JSONObject toJSONObject(){
        JSONArray mods = new JSONArray();
        JSONObject rootObj = new JSONObject();

        rootObj.put("GuildId", id).put("GuildName", name).put("Volume", player.getVolume());

        for(String key : modList.keySet()){
            mods.put(new JSONObject().put("Id", key).put("ModLevel", modList.get(key)));
        }

        return rootObj.put("Mods", mods);
    }
}
