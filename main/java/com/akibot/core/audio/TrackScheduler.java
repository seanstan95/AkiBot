package com.akibot.core.audio;

/*
    * AkiBot v3.0 by PhoenixAki: music + moderation bot for usage in Discord servers.
    *
    * TrackScheduler
    * Handles all music scheduling actions.
 */

import com.akibot.core.bot.Main;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.Color;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TrackScheduler extends AudioEventAdapter {
    private final AudioPlayer player;
    private final BlockingQueue<AudioTrack> requestQueue;

    public TrackScheduler(AudioPlayer player) {
        this.player = player;
        this.requestQueue = new LinkedBlockingQueue<>();
    }

    public void addTrack(AudioTrack track){
        if(!player.startTrack(track, true)){
            requestQueue.offer(track);
        }
    }

    public void addPlaylist(AudioPlaylist playlist, MessageReceivedEvent event){
        for(AudioTrack track : playlist.getTracks()){
            if(!player.startTrack(track, true)){
                requestQueue.offer(track);
            }
            track.setUserData(new TrackInfo(event.getAuthor().getName(), event.getChannel()));
        }
    }

    public void nextTrack(){
        //Check if there is a next track, and just return out if not (the player stops automatically).
        if(requestQueue.peek() == null){
            return;
        }

        TrackInfo currentTrackInfo = (TrackInfo)requestQueue.peek().getUserData();
        player.startTrack(requestQueue.poll(), false);
        currentTrackInfo.getChannel().sendMessage(":musical_note: Now Playing: **" + player.getPlayingTrack().getInfo().title + "**, requested by **" + currentTrackInfo.getRequester() + "**").queue();
    }

    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if(endReason.mayStartNext){
            nextTrack();
        }
    }

    public void outputQueue(String time, MessageReceivedEvent event){
        EmbedBuilder embedBuilder = new EmbedBuilder();
        int songCount = 0;

        //Checks if the queue is empty before continuing
        if(requestQueue.peek() == null){
            event.getChannel().sendMessage("Queue is empty!").queue();
            return;
        }

        //Loops through the queue until song #10 is processed, and stops
        for(AudioTrack track : requestQueue){
            ++songCount;
            TrackInfo currentTrackInfo = (TrackInfo)track.getUserData();
            String title = track.getInfo().title, requester = currentTrackInfo.getRequester();
            embedBuilder.addField("Song " + songCount, "**Title:** " + title + "\n**Requested by:** " + requester, false);
            if(songCount == 10){
                break;
            }
        }

        embedBuilder.setAuthor("AkiBot " + Main.version, null, null);
        embedBuilder.setColor(Color.decode("#9900CC"));
        embedBuilder.setFooter("Command received on: " + time, null);
        embedBuilder.setThumbnail(Main.THUMBNAIL);
        event.getChannel().sendMessage(embedBuilder.build()).queue();
    }

    public void removeTrack(int trackNumber, boolean mod, MessageReceivedEvent event){
        //Loops through the requestQueue until the correct track is found, and removes it.
        //Only the user who requested the track in question can remove it. MUSIC-level mods can override this and force remove any song.
        int i = 0;

        for(AudioTrack track : requestQueue){
            if(i == trackNumber){
                if(!((TrackInfo) track.getUserData()).getRequester().equalsIgnoreCase(event.getMember().getEffectiveName())){
                    if(mod){
                        event.getChannel().sendMessage("**" + track.getInfo().title + "** removed by mod override.").queue();
                    }else{
                        event.getChannel().sendMessage("Sorry, only **" + ((TrackInfo)track.getUserData()).getRequester() + "** can remove this track.").queue();
                    }
                    return;
                }else{
                    requestQueue.remove(track);
                    event.getChannel().sendMessage("**" + track.getInfo().title + "** removed from Queue.").queue();
                    break;
                }
            }else{
                ++i;
            }
        }
    }

    public void resetQueue(MessageReceivedEvent event){
        requestQueue.clear();
        event.getChannel().sendMessage("Queue emptied.").queue();
    }

    public boolean hasNextTrack(){
        return requestQueue.peek() != null;
    }

    public int getQueueSize(){
        return requestQueue.size();
    }
}
