package com.akibot.commands.music;

/*
    * AkiBot v3.1.1 by PhoenixAki: music + moderation bot for usage in Discord servers.
    *
    * Stop
    * Stops the audio player.
    * Takes in format -ab stop
 */

import com.akibot.commands.BaseCommand;

import com.akibot.core.bot.GuildObject;
import com.akibot.core.bot.Main;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import static com.akibot.commands.Category.MUSIC;

public class StopCommand extends BaseCommand {
    public StopCommand(){
        super(MUSIC, "`stop` - Stops the current track.", "`stop`: Stops the audio player. Use `-ab play` to resume audio playback.\nStopping and then resuming the player is effectively the same as calling `-ab skip`.", "Stop");
    }

    public void action(String[] args, MessageReceivedEvent event) {
        GuildObject guild = Main.guildMap.get(event.getGuild().getId());
        Main.updateLog(guild.getName(), guild.getId(), event.getAuthor().getName(), getName(), formatTime(null, event));

        //Ensures AkiBot is connected to voice before continuing
        if(!isVoiceOk(event.getGuild().getSelfMember().getVoiceState(), event.getMember().getVoiceState(), event.getChannel())){
            return;
        }

        //Ensures the user is of proper mod level to perform this command
        if(!isMod(guild, getCategory(), event)){
            return;
        }

        switch(args.length){
            case 0:
                if(guild.getPlayer().getPlayingTrack() == null){
                    event.getChannel().sendMessage("No track is playing!").queue();
                }else{
                    guild.getPlayer().stopTrack();
                    event.getChannel().sendMessage("Track stopped.").queue();
                }
                return;
            default:
                event.getChannel().sendMessage("Invalid format! Type `-ab help stop` for more info.").queue();
        }
    }
}
