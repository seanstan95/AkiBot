package com.akibot.commands.music;

/*
    * AkiBot v3.1.1 by PhoenixAki: music + moderation bot for usage in Discord servers.
    *
    * Remove
    * Removes a track from the queue, based on its position in the queue. Only the user who requested a song can remove it (or a mod override).
    * Takes in format -ab remove trackNumber
 */

import com.akibot.commands.BaseCommand;

import com.akibot.core.bot.GuildObject;
import com.akibot.core.bot.Main;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import static com.akibot.commands.Category.MUSIC;

public class RemoveCommand extends BaseCommand {
    public RemoveCommand(){
        super(MUSIC, "`remove` - Removes a track from the queue.", "`remove track_number`: Removes a track from the queue.\n\nOnly the user who requested a song is able to remove it.\nUsers with `MUSIC`-level mod privelages can force remove any song.", "Remove");
    }

    public void action(String[] args, MessageReceivedEvent event) {
        GuildObject guild = Main.guildMap.get(event.getGuild().getId());
        Main.updateLog(guild.getName(), guild.getId(), event.getAuthor().getName(), getName(), formatTime(null, event));
        int trackNumber, queueSize = guild.getScheduler().getQueueSize();

        //Ensures AkiBot is connected to voice before continuing
        if(!isVoiceOk(event.getGuild().getSelfMember().getVoiceState(), event.getMember().getVoiceState(), event.getChannel())){
            return;
        }

        switch(args.length){
            case 1:
                //Validate trackNumber as a valid number
                try{
                    trackNumber = Integer.parseInt(args[0])-1;
                }catch(NumberFormatException exception){
                    event.getChannel().sendMessage("Invalid argument (trackNumber must be a number). Type `-ab help remove` for more info.").queue();
                    return;
                }

                //Ensures trackNumber is within range of the current queue
                if(trackNumber >= queueSize || trackNumber < 0){
                    event.getChannel().sendMessage("Invalid trackNumber - there are " + queueSize + " song(s) queued. Enter a number in range and try again.").queue();
                    return;
                }

                //Mods can force-remove any song, so send true/false accordingly
                if(isMod(guild, getCategory(), event)){
                    guild.getScheduler().removeTrack(trackNumber, true, event);
                }else{
                    guild.getScheduler().removeTrack(trackNumber, false, event);
                }
                return;
            default:
                event.getChannel().sendMessage("Invalid format! Type `-ab help remove` for more info.").queue();
        }
    }
}
