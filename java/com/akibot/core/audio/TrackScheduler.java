package com.akibot.core.audio;

import com.akibot.commands.BaseCommand;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.LinkedList;

public class TrackScheduler extends AudioEventAdapter {
    private final AudioPlayer player;
    private final LinkedList<AudioTrack> queue;

    public TrackScheduler(AudioPlayer player) {
        this.player = player;
        this.queue = new LinkedList<>();
    }

    public void addTrack(AudioTrack track) {
        if (!player.startTrack(track, true)) {
            queue.offer(track);
        }
    }

    public void addPlaylist(AudioPlaylist playlist, GuildMessageReceivedEvent event) {
        for (AudioTrack track : playlist.getTracks()) {
            if (!player.startTrack(track, true)) {
                queue.offer(track);
            }
            track.setUserData(new TrackInfo(event.getAuthor().getName(), event.getChannel()));
        }
    }

    public void nextTrack() {
        //Check if there is a next track, and just return out if not (the player stops automatically).
        if (queue.peek() == null) {
            return;
        }

        TrackInfo currentTrackInfo = (TrackInfo) queue.peek().getUserData();
        player.startTrack(queue.poll(), false);
        currentTrackInfo.getChannel().sendMessage(":musical_note: " +
                "Now Playing: **" + player.getPlayingTrack().getInfo().title + "**, " +
                "requested by **" + currentTrackInfo.getRequester() + "**").queue();
    }

    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason.mayStartNext) {
            nextTrack();
        }
    }

    public void displayQueue(GuildMessageReceivedEvent event) {
        EmbedBuilder embedBuilder = BaseCommand.fillEmbed(new EmbedBuilder(), event);
        int songCount = 0;

        //Checks if the queue is empty before continuing
        if (queue.peek() == null) {
            event.getChannel().sendMessage("Queue is empty!").queue();
            return;
        }

        //Loops through the queue until song #10 is processed, and stops
        for (AudioTrack track : queue) {
            ++songCount;
            TrackInfo currentTrackInfo = (TrackInfo) track.getUserData();
            embedBuilder.addField("Song " + songCount, "**Title:** " + track.getInfo().title
                    + "\n**Requested " + "by:** " + currentTrackInfo.getRequester(), false);
            if (songCount == 10) {
                break;
            }
        }

        event.getChannel().sendMessage(embedBuilder.build()).queue();
    }

    public void removeTrack(int trackNumber, GuildMessageReceivedEvent event) {
        if (((TrackInfo) queue.get(trackNumber).getUserData()).getRequester()
                .equalsIgnoreCase(event.getAuthor().getName())) {
            queue.remove(trackNumber);
            event.getChannel().sendMessage("Song removed!").queue();
        } else {
            event.getChannel().sendMessage("Song not removed - only the person who requested " +
                    "the song can remove it.").queue();
        }
    }

    public void resetQueue(GuildMessageReceivedEvent event) {
        queue.clear();
        event.getChannel().sendMessage("Queue emptied.").queue();
    }

    public boolean hasNextTrack() {
        return queue.peek() != null;
    }

    public int getQueueSize() {
        return queue.size();
    }
}
