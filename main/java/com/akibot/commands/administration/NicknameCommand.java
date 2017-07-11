package com.akibot.commands.administration;

/*
    * AkiBot v3.1.2 by PhoenixAki: music + moderation bot for usage in Discord servers.
    *
    * Nickname
    * Forces a nickname on user(s) in this server.
    * Takes in format -ab nickname newName @userMentionsHere
 */

import com.akibot.commands.BaseCommand;
import com.akibot.core.bot.GuildObject;
import com.akibot.core.bot.Main;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.managers.GuildController;

import static com.akibot.commands.Category.ADMIN;

public class NicknameCommand extends BaseCommand {
    public NicknameCommand(){
        super(ADMIN, "`nickname` - Nicknames a user.", "`nickname newName @userMentionsHere`: Forces a new nickname onto the @mentioned user(s).", "Nickname");
    }

    public void action(String[] args, MessageReceivedEvent event) {
        GuildController controller = event.getGuild().getController();
        GuildObject guild = Main.guildMap.get(event.getGuild().getId());
        Main.updateLog(guild.getName(), guild.getId(), event.getAuthor().getName(), getName(), formatTime(null, event));

        //Ensures the user is of proper mod level to perform this command
        if(!isMod(guild, getCategory(), event)){
            return;
        }

        switch(args.length){
            case 0: case 1:
                event.getChannel().sendMessage("Invalid format! Type `-ab help nickname` for more info.").queue();
                return;
            default:
                if(event.getMessage().getMentionedUsers().size() > 0){
                    for(User user : event.getMessage().getMentionedUsers()){
                        Member member = event.getGuild().getMember(user);
                        String nameBefore = member.getEffectiveName();
                        controller.setNickname(member, args[0]).queue();
                        event.getChannel().sendMessage(nameBefore + " nicknamed to \"" + args[0] + "\".").queue();
                    }
                }else{
                    event.getChannel().sendMessage("Invalid format! Type `-ab help nickname` for more info.").queue();
                }
        }
    }
}
