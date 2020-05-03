package com.akibot.commands.info;

import com.akibot.commands.BaseCommand;
import com.akibot.core.bot.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import static com.akibot.commands.Category.INFO;

public class UserCommand extends BaseCommand {
    public UserCommand() {
        super(INFO, "`user` - Displays info about the mentioned user.", "`user <@user>`: Displays " +
                "info about the mentioned user.", "User");
    }

    public void action(String[] args, GuildMessageReceivedEvent event) {
        setup(event);
        Main.updateLog(Main.guildMap.get(event.getGuild().getId()),
                event.getAuthor().getName(), getName(), formatTime(null, event));
        User user;

        if (event.getMessage().getMentionedUsers().size() > 0) {
            user = event.getMessage().getMentionedUsers().get(0);
        } else if (args.length == 0) {
            user = event.getAuthor();
        } else {
            event.getChannel().sendMessage("Invalid format! Type `-ab help user` for more info.").queue();
            return;
        }
        embedOutput(event, guildControl.getMember(user), user);
    }

    private void embedOutput(GuildMessageReceivedEvent event, Member member, User user) {
        EmbedBuilder embed = fillEmbed(new EmbedBuilder(), event);
        embed.addField("Nickname", member.getEffectiveName(), false);
        embed.addField("Created account on:", formatTime(user.getTimeCreated(), event), false);
        embed.addField("Joined server on:", formatTime(member.getTimeJoined(), event), false);
        embed.setTitle("User: " + user.getName(), null);
        embed.setThumbnail(user.getAvatarUrl());
        event.getChannel().sendMessage(embed.build()).queue();
    }
}
