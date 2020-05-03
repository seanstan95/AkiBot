package com.akibot.commands.music;

import com.akibot.commands.BaseCommand;
import com.akibot.core.audio.TrackInfo;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import static com.akibot.commands.Category.MUSIC;

public class SongCommand extends BaseCommand {
    public SongCommand() {
        super(MUSIC, "`song` - Displays current song info.", "`song`: Displays info about the " +
                "currently playing song.", "Song");
    }

    public void action(String[] args, GuildMessageReceivedEvent event) {
        setup(event);

        //Ensures AkiBot is connected to voice before continuing
        if (!isVoiceOk(event.getGuild().getSelfMember(), event.getMember(), event.getChannel())) {
            return;
        }

        if (args.length == 0) {
            //Ensures that there is a currently playing track
            if (guildObj.getPlayer().getPlayingTrack() == null) {
                event.getChannel().sendMessage("No song is playing!").queue();
            } else {
                embedOutput(guildObj.getPlayer().getPlayingTrack(), event);
            }
        } else {
            event.getChannel().sendMessage("Invalid format! Type `-ab help song` for more info.").queue();
        }
    }

    private void embedOutput(AudioTrack track, GuildMessageReceivedEvent event) {
        EmbedBuilder embedBuilder = fillEmbed(new EmbedBuilder(), event);
        TrackInfo trackInfo = (TrackInfo) track.getUserData();

        long duration = track.getDuration(), currentTime = track.getPosition();
        String durationMin = Long.toString(duration / 1000 / 60), durationSec = Long.toString(duration / 1000 % 60);
        String currentMin = Long.toString(currentTime / 1000 / 60), currentSec = Long.toString(currentTime / 1000 % 60);

        //Padding for output, if necessary
        if (Long.parseLong(durationMin) < 10) {
            durationMin = "0" + durationMin;
        }
        if (Long.parseLong(durationSec) < 10) {
            durationSec = "0" + durationSec;
        }
        if (Long.parseLong(currentMin) < 10) {
            currentMin = "0" + currentMin;
        }
        if (Long.parseLong(currentSec) < 10) {
            currentSec = "0" + currentSec;
        }

        embedBuilder.addField("Song Title:", track.getInfo().title, false);
        embedBuilder.addField("Song Link:", track.getInfo().uri, false);
        embedBuilder.addField("Current Position:", currentMin + ":" + currentSec + "/" + durationMin + ":" + durationSec, false);
        embedBuilder.addField("Requested by:", trackInfo.getRequester(), false);
        event.getChannel().sendMessage(embedBuilder.build()).queue();
    }
}
