package com.akibot.commands.music;

import com.akibot.commands.BaseCommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import static com.akibot.commands.Category.MUSIC;

public class QueueCommand extends BaseCommand {
    public QueueCommand() {
        super(MUSIC, "`queue` - Displays songs in the queue.", "`queue`: Displays info for up to " +
                "the first 10 songs in the queue.\n`queue <reset>`: Empties the queue.", "Queue");
    }

    public void action(String[] args, GuildMessageReceivedEvent event) {
        setup(event);

        //Ensures AkiBot is connected to voice before continuing
        if (!isVoiceOk(event.getGuild().getSelfMember(), event.getMember(), event.getChannel())) {
            return;
        }

        if (args.length == 0) {
            guildObj.getScheduler().displayQueue(event);
        } else if (args.length == 1 && args[0].equalsIgnoreCase("reset") && isMod(guildObj, event)) {
            guildObj.getScheduler().resetQueue(event);
        } else {
            event.getChannel().sendMessage("Invalid format! Type `-ab help queue` for more info.").queue();
        }
    }
}
