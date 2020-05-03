package com.akibot.commands.music;

import com.akibot.commands.BaseCommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import static com.akibot.commands.Category.MUSIC;

public class RemoveCommand extends BaseCommand {
    public RemoveCommand() {
        super(MUSIC, "`remove` - Removes a song from the queue.", "`remove <songNumber>`: Removes a song " +
                "from the queue.\n\nOnly the user who requested a song is able " + "to remove it.", "Remove");
    }

    public void action(String[] args, GuildMessageReceivedEvent event) {
        setup(event);
        int trackNumber;

        //Ensures AkiBot is connected to voice before continuing
        if (!isVoiceOk(event.getGuild().getSelfMember(), event.getMember(), event.getChannel())) {
            return;
        }

        if (args.length == 1) {
            //Validate trackNumber as a valid number
            try {
                trackNumber = Integer.parseInt(args[0]) - 1;
            } catch (NumberFormatException exception) {
                event.getChannel().sendMessage("Invalid format! Type `-ab help remove` for more info.").queue();
                return;
            }

            //Ensures trackNumber is within range of the current queue
            if (trackNumber >= guildObj.getScheduler().getQueueSize() || trackNumber < 0) {
                event.getChannel().sendMessage("Invalid songNumber - there are " +
                        guildObj.getScheduler().getQueueSize() + " song(s) queued. Enter " +
                        "a number in range and try again.").queue();
                return;
            }

            guildObj.getScheduler().removeTrack(trackNumber, event);
        } else {
            event.getChannel().sendMessage("Invalid format! Type `-ab help remove` for more info.").queue();
        }
    }
}
