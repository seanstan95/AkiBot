package com.akibot.commands.music;

/*
    * AkiBot v3.0.1 by PhoenixAki: music + moderation bot for usage in Discord servers.
    *
    * Queue
    * Outputs info about the current queue, up to the first 10 songs (to avoid the embed being too long).
    * Takes in format -ab queue and -ab queue reset
 */

import com.akibot.commands.BaseCommand;

import com.akibot.core.bot.GuildObject;
import com.akibot.core.bot.Main;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import static com.akibot.commands.Category.MUSIC;

public class QueueCommand extends BaseCommand {
    public QueueCommand(){
        super(MUSIC, "`queue` - Displays info about songs in the queue.", "`queue`: Displays info for (up to) the first 10 songs in the queue.\n`queue reset`: **MUSIC-level mods only** Empties the queue.", "Queue");
    }

    public void action(String[] args, MessageReceivedEvent event) {
        GuildObject guild = Main.guildMap.get(event.getGuild().getId());
        Main.updateLog(guild.getName(), guild.getId(), event.getAuthor().getName(), getName(), formatTime(null, event));

        switch(args.length){
            case 0:
                //Calls outputQueue of the guild's scheduler
                guild.getScheduler().outputQueue(formatTime(null, event), event);
                return;
            case 1:
                if(args[0].equalsIgnoreCase("reset")){
                    if(isMod(guild, getCategory(), event)){
                        guild.getScheduler().resetQueue(event);
                        return;
                    }
                }
            default:
                event.getChannel().sendMessage("Invalid format! Type `-ab help queue` for more info.").queue();
        }
    }
}
