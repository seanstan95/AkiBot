package com.akibot.commands.administration;

/*
    * AkiBot v3.0.1 by PhoenixAki: music + moderation bot for usage in Discord servers.
    *
    * Unmute
    * Unmutes a user in voice.
    * Takes in format -ab unmute @userMentionsHere
 */

import com.akibot.commands.BaseCommand;
import com.akibot.core.bot.GuildObject;
import com.akibot.core.bot.Main;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.managers.GuildController;

import static com.akibot.commands.Category.ADMIN;

public class UnmuteCommand extends BaseCommand {
    public UnmuteCommand(){
        super(ADMIN, "`unmute` - Unmutes a user in voice.", "`unmute @userMentionsHere`: Unmutes the mentioned users, if they are in voice. To mute a user, use `-ab mute`.", "Unmute");
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
                event.getChannel().sendMessage("Invalid format! Type `-ab help unmute` for more info.").queue();
                return;
            default:
                if(event.getMessage().getMentionedUsers().size() > 0){
                    for(User user : event.getMessage().getMentionedUsers()){
                        Member member = event.getGuild().getMember(user);
                        if(member.getVoiceState().inVoiceChannel() && member.getVoiceState().isMuted()){
                            controller.setMute(member, false).queue();
                            output = output.concat(", " + member.getEffectiveName());
                        }
                    }
                    event.getChannel().sendMessage("User(s) " + output.substring(2) + " unmuted.").queue();
                }else{
                    event.getChannel().sendMessage("Invalid format! Type `-ab help unmute` for more info.").queue();
                }
        }
    }
}
