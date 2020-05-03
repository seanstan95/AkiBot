package com.akibot.commands.administration;

import com.akibot.commands.BaseCommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import static com.akibot.commands.Category.ADMIN;

public class DeleteCommand extends BaseCommand {
    public DeleteCommand() {
        super(ADMIN, "`delete` - Deletes a text/voice channel, or role.", "`delete <text/voice/role> " +
                "<name>`: Deletes the given text/voice channel, or role.\n\nIf there are multiple channels/roles " +
                "with the given name, AkiBot will only delete the first one.", "Delete");
    }

    public void action(String[] args, GuildMessageReceivedEvent event) {
        setup(event);

        //Ensures the user is of proper mod level to perform this command
        if (!isMod(guildObj, event)) {
            return;
        }

        if (args.length != 2) {
            event.getChannel().sendMessage("Invalid format! Type `-ab help delete` for more info.").queue();
        } else {
            switch (args[0].toLowerCase()) {
                case "text":
                    if (guildControl.getTextChannelsByName(args[1], true).size() > 0) {
                        guildControl.getTextChannelsByName(args[1], true).get(0).delete().queue();
                        event.getChannel().sendMessage("Text channel \"" + args[1] + "\" deleted.").queue();
                    } else {
                        event.getChannel().sendMessage("Text channel \"" + args[1] + "\" doesn't exist!").queue();
                    }
                    return;
                case "voice":
                    if (guildControl.getVoiceChannelsByName(args[1], true).size() > 0) {
                        guildControl.getVoiceChannelsByName(args[1], true).get(0).delete().queue();
                        event.getChannel().sendMessage("Voice channel \"" + args[1] + "\" deleted.").queue();
                    } else {
                        event.getChannel().sendMessage("Voice channel \"" + args[1] + "\" doesn't exist!").queue();
                    }
                    return;
                case "role":
                    if (guildControl.getRolesByName(args[1], true).size() > 0) {
                        guildControl.getRolesByName(args[1], true).get(0).delete().queue();
                        event.getChannel().sendMessage("Role \"" + args[1] + "\" deleted.").queue();
                    } else {
                        event.getChannel().sendMessage("Role \"" + args[1] + "\" doesn't exist!").queue();
                    }
                    return;
                default:
                    event.getChannel().sendMessage("Invalid format! Type `-ab help delete` for more info.").queue();
            }
        }
    }
}
