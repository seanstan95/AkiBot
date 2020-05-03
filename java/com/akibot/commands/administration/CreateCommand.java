package com.akibot.commands.administration;

import com.akibot.commands.BaseCommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.regex.Pattern;

import static com.akibot.commands.Category.ADMIN;

public class CreateCommand extends BaseCommand {
    public CreateCommand() {
        super(ADMIN, "`create` - Creates a new text/voice channel or role.", "`create <text/voice/role> " +
                "<name>`: Creates a new text/voice channel, or role, with the given name.\n\n**Note**: Names must " +
                "be alphanumeric (A-Z, 0-9) and - or _ only (no spaces).", "Create");
    }

    public void action(String[] args, GuildMessageReceivedEvent event) {
        setup(event);
        Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z0-9-_]+$");

        //Ensures the user is of proper mod level to perform this command
        if (!isMod(guildObj, event)) {
            return;
        }

        if (args.length != 2) {
            event.getChannel().sendMessage("Invalid format! Type `-ab help create` for more info.").queue();
        } else {
            if (NAME_PATTERN.matcher(args[1]).find()) {
                switch (args[0].toLowerCase()) {
                    case "text":
                        guildControl.createTextChannel(args[1]).queue();
                        event.getChannel().sendMessage("Text channel \"" + args[1] + "\" created.").queue();
                        return;
                    case "voice":
                        guildControl.createVoiceChannel(args[1]).queue();
                        event.getChannel().sendMessage("Voice channel \"" + args[1] + "\" created.").queue();
                        return;
                    case "role":
                        guildControl.createRole().setName(args[1]).queue();
                        event.getChannel().sendMessage("Role \"" + args[1] + "\" created.").queue();
                        return;
                    default:
                        event.getChannel().sendMessage("Invalid format! Type `-ab help create` for more info.").queue();
                }
            } else {
                event.getChannel().sendMessage("Invalid name! Type `-ab help create` for more info.").queue();
            }
        }
    }
}
