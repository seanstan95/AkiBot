package com.akibot.commands.administration;

import com.akibot.commands.BaseCommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import static com.akibot.commands.Category.ADMIN;

public class UnmuteCommand extends BaseCommand {
    public UnmuteCommand() {
        super(ADMIN, "`unmute` - Unmutes a user in voice.", "`unmute <@user>`: Unmutes a mentioned user, " +
                "if they are connected to voice.\nTo mute a user, use `-ab mute`.", "Unmute");
    }

    public void action(String[] args, GuildMessageReceivedEvent event) {
        setup(event);

        //Ensures the user is of proper mod level to perform this command
        if (!isMod(guildObj, event)) {
            return;
        }

        if (args.length != 1) {
            event.getChannel().sendMessage("Invalid format! Type `-ab help unmute` for more info.").queue();
        } else if (event.getMessage().getMentionedUsers().size() > 0) {
            Member unmute = guildControl.getMember(event.getMessage().getMentionedUsers().get(0));
            if (unmute.getVoiceState().inVoiceChannel() && unmute.getVoiceState().isGuildMuted()) {
                unmute.mute(false).queue();
                event.getChannel().sendMessage("User " + unmute.getEffectiveName() + " unmuted.").queue();
            } else {
                event.getChannel().sendMessage("User " + unmute.getEffectiveName() + " not in voice, or " +
                        "is not muted.").queue();
            }
        } else {
            event.getChannel().sendMessage("Invalid format! Type `-ab help unmute` for more info.").queue();
        }
    }
}
