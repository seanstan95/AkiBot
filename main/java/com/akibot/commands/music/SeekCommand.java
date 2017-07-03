package com.akibot.commands.music;

/*
    * AkiBot v3.1.0 by PhoenixAki: music + moderation bot for usage in Discord servers.
    *
    * Seek
    * Skips to the given time in the playing track. Restricted to MUSIC-level mods because it would be an easy way to get around skip being restricted as well.
    * Takes in format -ab seek mm:ss
 */

import com.akibot.commands.BaseCommand;

import com.akibot.core.bot.GuildObject;
import com.akibot.core.bot.Main;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.akibot.commands.Category.MUSIC;

public class SeekCommand extends BaseCommand {
    public SeekCommand(){
        super(MUSIC, "`seek` - Skips to the given time in the playing track.", "`seek mm:ss`: Skips to the specified position in the song.\n**Restricted to users with `MUSIC`-level mod privileges.**\nIf the given position is less than 00:00, the track just restarts at 00:00.\nIf the given position is after the end of the track, the track is skipped.", "Seek");
    }

    public void action(String[] args, MessageReceivedEvent event) {
        GuildObject guild = Main.guildMap.get(event.getGuild().getId());
        Main.updateLog(guild.getName(), guild.getId(), event.getAuthor().getName(), getName(), formatTime(null, event));
        final Pattern POSITION_PATTERN = Pattern.compile("([0-9]+:[0-9]+)");
        Long minutes, newPosition, seconds;

        //Ensures AkiBot is connected to voice before continuing
        if(!isVoiceOk(event.getGuild().getSelfMember().getVoiceState(), event.getMember().getVoiceState(), event.getChannel())){
            return;
        }

        //Ensures the user is of proper mod level to perform this command
        if(!isMod(guild, getCategory(), event)){
            return;
        }

        switch(args.length){
            case 1:
                //Ensures there is a song playing before trying to seek
                if(guild.getPlayer().getPlayingTrack() == null) {
                    event.getChannel().sendMessage("No song is playing!").queue();
                    return;
                }

                //Match the input with the valid pattern for positions (mm:ss)
                if(!POSITION_PATTERN.matcher(args[0]).find()){
                    event.getChannel().sendMessage("Invalid format! Type `-ab help seek` for more info.").queue();
                    return;
                }

                //Converts arguments into separate time values, and determines position to seek to in millis.
                try{
                    minutes = Long.parseLong(args[0].substring(0, 2)) * 60000;
                    seconds = Long.parseLong(args[0].substring(3)) * 1000;
                    newPosition = minutes + seconds;
                }catch(NumberFormatException e){
                    event.getChannel().sendMessage("Invalid format! Type `-ab help seek` for more info.").queue();
                    return;
                }

                guild.getPlayer().getPlayingTrack().setPosition(newPosition);
                return;
            default:
                event.getChannel().sendMessage("Invalid format! Type `-ab help seek` for more info.").queue();
        }
    }
}
