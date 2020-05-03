package com.akibot.commands.music;

import com.akibot.commands.BaseCommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.regex.Pattern;

import static com.akibot.commands.Category.MUSIC;

public class SeekCommand extends BaseCommand {
    public SeekCommand() {
        super(MUSIC, "`seek` - Skips to the given time in the playing song.", "`seek <mm:ss>`: Skips to " +
                "the specified position in the song.\nIf the given position is less than " +
                "00:00, the song restarts at 00:00.\nIf the given position is after the " +
                "end of the song, the song is skipped.", "Seek");
    }

    public void action(String[] args, GuildMessageReceivedEvent event) {
        setup(event);
        final Pattern POSITION_PATTERN = Pattern.compile("([0-9]+:[0-9]+)");
        long minutes, newPosition, seconds;

        //Ensures AkiBot is connected to voice before continuing
        if (!isVoiceOk(event.getGuild().getSelfMember(), event.getMember(), event.getChannel())) {
            return;
        }

        if (args.length == 1) {
            //Ensures there is a song playing before trying to seek
            if (guildObj.getPlayer().getPlayingTrack() == null) {
                event.getChannel().sendMessage("No song is playing!").queue();
                return;
            }

            //Match the input with the pattern for positions
            if (!POSITION_PATTERN.matcher(args[0]).find()) {
                event.getChannel().sendMessage("Invalid format! Type `-ab help seek` for more info.").queue();
                return;
            }

            //Converts arguments into separate time values, and determines position to seek to in millis.
            try {
                minutes = Long.parseLong(args[0].substring(0, 2)) * 60000;
                seconds = Long.parseLong(args[0].substring(3)) * 1000;
                newPosition = minutes + seconds;
            } catch (NumberFormatException e) {
                event.getChannel().sendMessage("Invalid format! Type `-ab help seek` for more info.").queue();
                return;
            }

            guildObj.getPlayer().getPlayingTrack().setPosition(newPosition);
        } else {
            event.getChannel().sendMessage("Invalid format! Type `-ab help seek` for more info.").queue();
        }
    }
}
