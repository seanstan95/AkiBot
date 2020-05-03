package com.akibot.commands.administration;

import com.akibot.commands.BaseCommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import static com.akibot.commands.Category.ADMIN;

public class NicknameCommand extends BaseCommand {
    public NicknameCommand() {
        super(ADMIN, "`nickname` - Nicknames a user.", "`nickname <nickname> <@user>`: Applies a " +
                "new nickname onto the mentioned user. The nickname must be one word.", "Nickname");
    }

    public void action(String[] args, GuildMessageReceivedEvent event) {
        setup(event);

        //Ensures the user is of proper mod level to perform this command
        if (!isMod(guildObj, event)) {
            return;
        }

        if (args.length != 2) {
            event.getChannel().sendMessage("Invalid format! Type `-ab help nickname` for more info.").queue();
        } else {
            if (event.getMessage().getMentionedUsers().size() > 0) {
                Member member = guildControl.getMember(event.getMessage().getMentionedUsers().get(0));
                String nameBefore = member.getEffectiveName();
                member.modifyNickname(args[0]).queue();
                event.getChannel().sendMessage(nameBefore + " nicknamed as \"" + args[0] + "\".").queue();
            } else {
                event.getChannel().sendMessage("Invalid format! Type `-ab help nickname` for more info.").queue();
            }
        }
    }
}
