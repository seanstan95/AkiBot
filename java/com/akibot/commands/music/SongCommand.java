package com.akibot.commands.music;

/*
    * AkiBot v3.1.5 by PhoenixAki: music + moderation bot for usage in Discord servers.
    *
    * Song
    * Outputs info about the currently playing song (title, url link, position/duration, and who requested it).
    * Takes in format -ab song
 */

import com.akibot.commands.BaseCommand;
import com.akibot.core.audio.TrackInfo;
import com.akibot.core.bot.GuildObject;
import com.akibot.core.bot.Main;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.Color;

import static com.akibot.commands.Category.MUSIC;

public class SongCommand extends BaseCommand {
    public SongCommand(){
        super(MUSIC, "`song` - Outputs current song info.", "`song`: Outputs info about the currently playing track.", "Song");
    }

    public void action(String[] args, MessageReceivedEvent event) {
        GuildObject guild = Main.guildMap.get(event.getGuild().getId());
        Main.updateLog(guild.getName(), guild.getId(), event.getAuthor().getName(), getName(), formatTime(null, event));

        switch(args.length){
            case 0:
                //Ensures that there is a currentTrack. If so, continue to embedOutput to retrieve track info
                if(guild.getPlayer().getPlayingTrack() == null) {
                    event.getChannel().sendMessage("No song is playing!").queue();
                }else{
                    embedOutput(guild.getPlayer().getPlayingTrack(), event);
                }
                return;
            default:
                event.getChannel().sendMessage("Invalid format! Type `-ab help song` for more info.").queue();
        }
    }

    private void embedOutput(AudioTrack track, MessageReceivedEvent event){
        EmbedBuilder embedBuilder = new EmbedBuilder();

        TrackInfo trackInfo = (TrackInfo)track.getUserData();
        Long duration = track.getDuration(), currentTime = track.getPosition();
        String durationMinutes = Long.toString((duration/60000)%60), durationSeconds = Long.toString((duration/1000)%60);
        String currentMinutes = Long.toString((currentTime/60000)%60), currentSeconds = Long.toString((currentTime/1000)%60);

        //Padding for output, if necessary
        if(Integer.parseInt(durationMinutes) < 10){
            durationMinutes = "0" + durationMinutes;
        }
        if(Integer.parseInt(durationSeconds) < 10){
            durationSeconds = "0" + durationSeconds;
        }
        if(Integer.parseInt(currentMinutes) < 10){
            currentMinutes = "0" + currentMinutes;
        }
        if(Integer.parseInt(currentSeconds) < 10){
            currentSeconds = "0" + currentSeconds;
        }

        embedBuilder.setAuthor("AkiBot " + Main.version, null, null);
        embedBuilder.setColor(Color.decode("#9900CC"));
        embedBuilder.addField("Song Title:", track.getInfo().title, false);
        embedBuilder.addField("Song Link:", track.getInfo().uri, false);
        embedBuilder.addField("Current Position:", currentMinutes + ":" + currentSeconds + "/" + durationMinutes + ":" + durationSeconds, false);
        embedBuilder.addField("Requested by:", trackInfo.getRequester(), false);
        embedBuilder.setFooter("Command received on: " + formatTime(null, event), event.getAuthor().getAvatarUrl());
        embedBuilder.setThumbnail(Main.THUMBNAIL);
        event.getChannel().sendMessage(embedBuilder.build()).queue();
    }
}
