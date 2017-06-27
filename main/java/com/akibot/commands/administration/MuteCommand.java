package com.akibot.commands.administration;

/*
    * AkiBot v3.0.2 by PhoenixAki: music + moderation bot for usage in Discord servers.
    *
    * Mute
    * Mutes a user that is connected to a voice channel.
    * Takes in format -ab mute @userMentionsHere
 */

import com.akibot.commands.BaseCommand;
import com.akibot.core.bot.GuildObject;
import com.akibot.core.bot.Main;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.managers.GuildController;

import static com.akibot.commands.Category.ADMIN;

public class MuteCommand extends BaseCommand {
    public MuteCommand(){
        super(ADMIN, "`mute` - Mutes a user in voice.", "`mute @userMentionsHere`: Mutes the @mentioned users, if they are connected to voice.\nTo unmute a user, use `-ab unmute`.", "Mute");
    }

    public void action(String[] args, MessageReceivedEvent event) {
        GuildController controller = event.getGuild().getController();
        GuildObject guild = Main.guildMap.get(event.getGuild().getId());
        Main.updateLog(event.getGuild().getName(), event.getGuild().getId(), event.getAuthor().getName(), getName(), formatTime(null, event));
        String output = "";

        //Ensures the user is of proper mod level to perform this command
        if(!isMod(guild, getCategory(), event)){
            return;
        }

        switch(args.length){
            case 0:
                event.getChannel().sendMessage("Invalid format! Type `-ab help mute` for more info.").queue();
                return;
            default:
                if(event.getMessage().getMentionedUsers().size() > 0){
                    for(User user : event.getMessage().getMentionedUsers()){
                        Member member = event.getGuild().getMember(user);
                        if(member.getVoiceState().inVoiceChannel() && !member.getVoiceState().isMuted()){
                            controller.setMute(member, true).queue();
                            output = output.concat(", " + member.getEffectiveName());
                        }
                    }
                    event.getChannel().sendMessage("User(s) " + output.substring(2) + " muted.").queue();
                }else{
                    event.getChannel().sendMessage("Invalid format! Type `-ab help mute` for more info.").queue();
                }
        }
    }
}
