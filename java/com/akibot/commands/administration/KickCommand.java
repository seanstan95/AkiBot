package com.akibot.commands.administration;

import com.akibot.commands.BaseCommand;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import static com.akibot.commands.Category.ADMIN;

public class KickCommand extends BaseCommand{
    public KickCommand() {
        super(ADMIN, "`kick` - Kicks a user.", "`kick <@user>`: Kicks the mentioned user " +
                "from this server.", "Kick");
    }

    public void action(String[] args, GuildMessageReceivedEvent event) {
        setup(event);

        //Ensures the user is of proper mod level to perform this command
        if (!isMod(guildObj, event)) {
            return;
        }

        if (args.length == 0) {
            event.getChannel().sendMessage("Invalid format! Type `-ab help kick` for more info.").queue();
        } else {
            if (event.getMessage().getMentionedUsers().size() == 1) {
                User kicked = event.getMessage().getMentionedUsers().get(0);
                guildControl.getMember(kicked).kick().queue();
                event.getChannel().sendMessage(kicked.getName() + " kicked.").queue();
            } else {
                event.getChannel().sendMessage("Invalid format! Type `-ab help kick` for more info.").queue();
            }
        }
    }
}
