package com.akibot.commands.administration;

import com.akibot.commands.BaseCommand;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import static com.akibot.commands.Category.ADMIN;

public class BanCommand extends BaseCommand {
    public BanCommand() {
        super(ADMIN, "`ban` - Bans a user.", "`ban <@user>`: " +
                "Bans the mentioned user from this server. Additionally, the last 3 days of messages " +
                "from the user are deleted.", "Ban");
    }

    public void action(String[] args, GuildMessageReceivedEvent event) {
        setup(event);

        //Ensures the user is of proper mod level to perform this command
        if (!isMod(guildObj, event)) {
            return;
        }

        if (args.length == 0) {
            event.getChannel().sendMessage("Invalid format! Type `-ab help ban` for more info.").queue();
        } else {
            if (event.getMessage().getMentionedUsers().size() == 1) {
                User banned = event.getMessage().getMentionedUsers().get(0);
                guildControl.ban(banned, 3).queue();
                event.getChannel().sendMessage(banned.getName() + " banned.").queue();
            } else {
                event.getChannel().sendMessage("Invalid format! Type `-ab help ban` for more info.").queue();
            }
        }
    }
}
