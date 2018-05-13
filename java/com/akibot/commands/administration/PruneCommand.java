package com.akibot.commands.administration;

/*
    * AkiBot v3.1.4 by PhoenixAki: music + moderation bot for usage in Discord servers.
    *
    * Prune
    * Kicks all users who have been offline for a given number of days.
 */

import com.akibot.commands.BaseCommand;
import com.akibot.core.bot.GuildObject;
import com.akibot.core.bot.Main;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.managers.GuildController;

import static com.akibot.commands.Category.ADMIN;

public class PruneCommand extends BaseCommand {
    public PruneCommand(){
        super(ADMIN, "`prune` - Kicks inactive users from the server.", "`prune days_here`: Kicks users in the server who have been offline for `days_here` days or more.", "Prune");
    }

    public void action(String[] args, MessageReceivedEvent event) {
        GuildController controller = event.getGuild().getController();
        GuildObject guild = Main.guildMap.get(event.getGuild().getId());
        Main.updateLog(guild.getName(), guild.getId(), event.getAuthor().getName(), getName(), formatTime(null, event));
        int days;

        //Ensures the user is of proper mod level to perform this command
        if(!isMod(guild, getCategory(), event)){
            return;
        }

        switch(args.length){
            case 1:
                try{
                    days = Integer.parseInt(args[0]);
                }catch(NumberFormatException e){
                    event.getTextChannel().sendMessage("Invalid argument (days must be a number). Type `-ab help prune` for more info.").queue();
                    return;
                }

                controller.prune(days).queue(i -> event.getChannel().sendMessage("Number of users kicked: " + i).queue());
                return;
            default:
                event.getChannel().sendMessage("Invalid format! Type `-ab help prune` for more info.").queue();
        }
    }
}
