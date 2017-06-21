package com.akibot.commands.info;

/*
    * AkiBot v3.0.1 by PhoenixAki: music + moderation bot for usage in Discord servers.
    *
    * User
    * Returns relevant information about the @mentioned user(s).
    * Takes in format !userinfo, !userinfo mention_users_here, and !userinfo help
 */

import com.akibot.commands.BaseCommand;
import com.akibot.core.bot.Main;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.Color;

import static com.akibot.commands.Category.INFO;

public class UserCommand extends BaseCommand {
    public UserCommand(){
        super(INFO, "`user` - Outputs info about the mentioned user(s).", "`user @userMentionHere`: Outputs info about the mentioned user(s).", "User");
    }

    public void action(String[] args, MessageReceivedEvent event) {
        Main.updateLog(event.getGuild().getName(), event.getGuild().getId(), event.getAuthor().getName(), getName(), formatTime(null, event));

        //Reads in all the mentioned users one at a time and embed outputs each one
        if(event.getMessage().getMentionedUsers().size() > 0){
            for(User user : event.getMessage().getMentionedUsers()){
                embedOutput(event, event.getGuild().getMember(user), user);
            }
        }else{
            event.getChannel().sendMessage("Invalid format! Type `-ab help user` for more info.").queue();
        }
    }

    private void embedOutput(MessageReceivedEvent event, Member member, User user){
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setAuthor("AkiBot " + Main.version, null, null);
        embedBuilder.setColor(Color.decode("#9900CC"));
        embedBuilder.addField("Nickname", member.getEffectiveName(), false);
        embedBuilder.addField("Account Creation", formatTime(user.getCreationTime(), event), false);
        embedBuilder.addField("Server Join Date", formatTime(member.getJoinDate(), event), false);
        embedBuilder.setFooter("Command received on: " + formatTime(null, event), event.getAuthor().getAvatarUrl());
        embedBuilder.setTitle("User: " + user.getName(), null);
        embedBuilder.setThumbnail(user.getAvatarUrl());
        event.getChannel().sendMessage(embedBuilder.build()).queue();
    }
}
