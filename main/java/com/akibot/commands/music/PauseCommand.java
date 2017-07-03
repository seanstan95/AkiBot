package com.akibot.commands.music;

/*
	* AkiBot v3.1.0 by PhoenixAki: music + moderation bot for usage in Discord servers.
	*
	* Pause
	* Pauses the current track.
	* Takes in format -ab pause
 */

import com.akibot.commands.BaseCommand;

import com.akibot.core.bot.GuildObject;
import com.akibot.core.bot.Main;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import static com.akibot.commands.Category.MUSIC;

public class PauseCommand extends BaseCommand {
    public PauseCommand(){
        super(MUSIC, "`pause` - Pauses the current track.", "`pause`: Pauses the current track. To resume a paused track, type `-ab play`.", "Pause");
    }

    public void action(String[] args, MessageReceivedEvent event) {
        GuildObject guild = Main.guildMap.get(event.getGuild().getId());
        Main.updateLog(guild.getName(), guild.getId(), event.getAuthor().getName(), getName(), formatTime(null, event));

        //Ensures AkiBot is connected to voice before continuing
        if(!isVoiceOk(event.getGuild().getSelfMember().getVoiceState(), event.getMember().getVoiceState(), event.getChannel())){
            return;
        }

        switch(args.length){
            case 0:
                //Ensures there is a playing song before continuing
                if(guild.getPlayer().getPlayingTrack() == null){
                    event.getChannel().sendMessage("No song playing!").queue();
                    return;
                }

                //If already paused, don't do anything. Otherwise, pause the track.
                if(guild.getPlayer().isPaused()){
                    event.getChannel().sendMessage("Track is already paused! Type `-ab play` to resume.").queue();
                }else{
                    guild.getPlayer().setPaused(true);
                    event.getChannel().sendMessage("Track paused.").queue();
                }
                return;
            default:
                event.getChannel().sendMessage("Invalid format! Type `-ab help pause` for more info.").queue();
        }
    }
}
