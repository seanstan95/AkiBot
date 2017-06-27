package com.akibot.core.bot;

/*
    * AkiBot v3.0.2 by PhoenixAki: music + moderation bot for usage in Discord servers.
    *
    * CommandHandler
    * Handles the process of picking up a message, parsing it, and verifying for command existence.
 */

import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.Arrays;

import static com.akibot.commands.ModLevel.FULL;

public class CommandHandler extends ListenerAdapter {

    public void onGuildJoin(GuildJoinEvent event){
        //Creates a guild object for every new guild that is joined (this is checked a second time in onMessageReceieved)
        if(!Main.guildMap.containsKey(event.getGuild().getId())){
            GuildObject newGuildObject = new GuildObject(Main.playerManager, event.getGuild().getId(), event.getGuild().getName(), null, 35);
            newGuildObject.changeMod(event.getGuild().getOwner().getUser().getId(), FULL, false, false);
            Main.guildMap.put(event.getGuild().getId(), newGuildObject);
            System.out.println("Joining: " + event.getGuild().getName() + " (" + event.getGuild().getId() + ")");
            Main.updateGuilds(false, null, null);
        }
    }

    public void onGuildLeave(GuildLeaveEvent event){
        Main.updateGuilds(true, event.getGuild().getName(), event.getGuild().getId());
    }

    public void onMessageReceived(MessageReceivedEvent event) {
        //Checks that if the message received is a bot, ignore it and don't increment message count.
        if(event.getAuthor().isBot()){
            return;
        }

        if(event.getGuild() != null) {
            if(!Main.guildMap.containsKey(event.getGuild().getId())) {
                GuildObject newGuildObject = new GuildObject(Main.playerManager, event.getGuild().getId(), event.getGuild().getName(), null, 35);
                newGuildObject.changeMod(event.getGuild().getOwner().getUser().getId(), FULL, false, false);
                Main.guildMap.put(event.getGuild().getId(), newGuildObject);
                System.out.println("Joining: " + event.getGuild().getName() + " (" + event.getGuild().getId() + ")");
                Main.updateGuilds(false, null, null);
            }
        }

        ++Main.messageCount;

        //AkiBot only processes commands if they start with -ab or an @mention to its name
        if(event.getMessage().getContent().startsWith("-ab")) {
            String message = event.getMessage().getContent().replaceFirst("-ab ", "");
            handleCommand(message.split("\\s+"), event);
        }else if(event.getMessage().getRawContent().startsWith("<@313955083584929792>") && event.getMessage().getContent().indexOf(' ') > 0){
            String message = event.getMessage().getContent().substring(event.getMessage().getContent().indexOf(' ')).trim();
            handleCommand(message.split("\\s+"), event);
        }
    }

    private static void handleCommand(String[] args, MessageReceivedEvent event){
        //In the event that this message came via DM, check if it is one of the allowed ones
        //More efficient to confirm here if the command in question is allowed in PMs vs. separately in each of the invalid commands
        if(!event.getChannelType().isGuild() && !Main.PM_COMMANDS.contains(args[0])){
            event.getChannel().sendMessage("Sorry, this command can't be done in PMs.").queue();
            return;
        }

        //Checks that the command list has a command associated with the parsed message.
        //If successful, calls action() of the corresponding command.
        boolean safe = false;
        String saveKey = "";

        for(String key : Main.commands.keySet()){
            if(key.equalsIgnoreCase(args[0].toLowerCase())){
                saveKey = key;
                safe = true;
                break;
            }
        }

        if(safe){
            ++Main.commandCount;
            Main.commands.get(saveKey).action(Arrays.copyOfRange(args, 1, args.length), event);
        }
    }
}
