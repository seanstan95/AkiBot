package com.akibot.commands.administration;

/*
    * AkiBot v3.1.1 by PhoenixAki: music + moderation bot for usage in Discord servers.
    *
    * Ban
    * Bans a user from this server.
    * Takes in format -ab ban @userMentionsHere
 */

import com.akibot.commands.BaseCommand;
import com.akibot.core.bot.GuildObject;
import com.akibot.core.bot.Main;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.managers.GuildController;

import static com.akibot.commands.Category.ADMIN;

public class BanCommand extends BaseCommand {
    public BanCommand(){
        super(ADMIN, "`ban` - Bans a user.", "`ban @userMentionsHere`: Bans the @mentioned user(s) from this server.", "Ban");
    }

    public void action(String[] args, MessageReceivedEvent event) {
        GuildController controller = event.getGuild().getController();
        GuildObject guild = Main.guildMap.get(event.getGuild().getId());
        Main.updateLog(guild.getName(), guild.getId(), event.getAuthor().getName(), getName(), formatTime(null, event));
        String output = "";

        //Ensures the user is of proper mod level to perform this command
        if(!isMod(guild, getCategory(), event)){
            return;
        }

        switch(args.length){
            case 0:
                event.getChannel().sendMessage("Invalid format! Type `-ab help ban` for more info.").queue();
                return;
            default:
                if(event.getMessage().getMentionedUsers().size() > 0){
                    for(User user : event.getMessage().getMentionedUsers()){
                        controller.ban(user, 0).queue();
                        output = output.concat(", " + user.getName());
                    }
                    event.getChannel().sendMessage("User(s) " + output.substring(2) + " banned from this server.").queue();
                }else{
                    event.getChannel().sendMessage("Invalid format! Types `-ab help ban` for more info.").queue();
                }
        }
    }
}
