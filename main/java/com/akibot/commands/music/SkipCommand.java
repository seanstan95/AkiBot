package com.akibot.commands.music;

/*
    * AkiBot v3.0.1 by PhoenixAki: music + moderation bot for usage in Discord servers.
    *
    * Skip
    * Skips the current track and loads the next song in the queue.
    * Takes in format -ab skip
 */

import com.akibot.commands.BaseCommand;

import com.akibot.core.bot.GuildObject;
import com.akibot.core.bot.Main;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import static com.akibot.commands.Category.MUSIC;

public class SkipCommand extends BaseCommand {
    public SkipCommand(){
        super(MUSIC, "`skip` - Skips the current track.", "`skip`: Skips the current track.\n**Restricted to users with MUSIC-level mod privileges.**", "Skip");
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
                    guild.getScheduler().nextTrack();
                    event.getChannel().sendMessage("Track skipped.").queue();
                }
                return;
            default:
                event.getChannel().sendMessage("Invalid format! Type `-ab help skip` for more info.").queue();
        }
    }
}
