package com.akibot.commands.music;

/*
    * AkiBot v3.1.2 by PhoenixAki: music + moderation bot for usage in Discord servers.
    *
    * Volume
    * Displays the current bot volume (default is set to 35), or changes to the given value.
    * Takes in format -ab volume and -ab volume newVolume
 */

import com.akibot.commands.BaseCommand;
import com.akibot.core.bot.GuildObject;
import com.akibot.core.bot.Main;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import static com.akibot.commands.Category.MUSIC;

public class VolumeCommand extends BaseCommand {
    public VolumeCommand(){
        super(MUSIC, "`volume` - Changes the current volume.", "`volume`: Displays the current volume.\n`volume newVolume`: Sets the volume to this value (must be 1-100).", "Volume");
    }

    public void action(String[] args, MessageReceivedEvent event) {
        GuildObject guild = Main.guildMap.get(event.getGuild().getId());
        Main.updateLog(guild.getName(), guild.getId(), event.getAuthor().getName(), getName(), formatTime(null, event));
        int newVolume;

        //Ensures AkiBot is connected to voice before continuing
        if(!isVoiceOk(event.getGuild().getSelfMember().getVoiceState(), event.getMember().getVoiceState(), event.getChannel())){
            return;
        }

        switch(args.length){
            case 0:
                event.getChannel().sendMessage("Current volume: " + Integer.toString(guild.getPlayer().getVolume())).queue();
                return;
            case 1:
                try{
                    newVolume = Integer.parseInt(args[0]);
                }catch(NumberFormatException e){
                    event.getChannel().sendMessage("Invalid argument (newVolume must be a number). Type `-ab help volume` for more info.").queue();
                    return;
                }

                if(newVolume < 1 || newVolume > 100){
                    event.getChannel().sendMessage("Invalid argument (newVolume must be 1-100). Type `-ab help volume` for more info.").queue();
                }else{
                    guild.getPlayer().setVolume(newVolume);
                    event.getChannel().sendMessage("Volume changed to " + Integer.toString(newVolume)).queue();
                    Main.updateGuilds(false, guild.getName(), guild.getId());
                }
                return;
            default:
                event.getChannel().sendMessage("Invalid format! Type `-ab help volume` for more info.").queue();
        }
    }
}
