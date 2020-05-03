package com.akibot.commands.administration;

import com.akibot.commands.BaseCommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import static com.akibot.commands.Category.ADMIN;

public class MuteCommand extends BaseCommand {
    public MuteCommand() {
        super(ADMIN, "`mute` - Mutes a user in voice.", "`mute <@user>`: Mutes a mentioned user, " +
                "if they are connected to voice.\nTo unmute a user, use `-ab unmute`.", "Mute");
    }

    public void action(String[] args, GuildMessageReceivedEvent event) {
        setup(event);

        //Ensures the user is of proper mod level to perform this command
        if (!isMod(guildObj, event)) {
            return;
        }

        if (args.length != 1) {
            event.getChannel().sendMessage("Invalid format! Type `-ab help mute` for more info.").queue();
        } else if (event.getMessage().getMentionedUsers().size() > 0) {
            Member mute = guildControl.getMember(event.getMessage().getMentionedUsers().get(0));
            if (mute.getVoiceState().inVoiceChannel() && !mute.getVoiceState().isGuildMuted()) {
                mute.mute(true).queue();
                event.getChannel().sendMessage("User " + mute.getEffectiveName() + " muted.").queue();
            } else {
                event.getChannel().sendMessage("User " + mute.getEffectiveName() + " is not in voice, or is " +
                        "already muted.").queue();
            }
        } else {
            event.getChannel().sendMessage("Invalid format! Type `-ab help mute` for more info.").queue();
        }
    }
}
