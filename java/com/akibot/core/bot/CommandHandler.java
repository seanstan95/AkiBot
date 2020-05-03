package com.akibot.core.bot;

import com.akibot.commands.BaseCommand;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Arrays;
import java.util.HashMap;

public class CommandHandler extends ListenerAdapter {

    public void onGuildJoin(GuildJoinEvent event) {
        //Creates a guild object for every new guild that is joined (this is checked again in onGuildMessageReceieved)
        if (!Main.guildMap.containsKey(event.getGuild().getId())) {
            GuildObject newGuildObject = new GuildObject(Main.playerManager.createPlayer(),
                    event.getGuild().getId(), event.getGuild().getName(), null, 35);
            newGuildObject.addMod(event.getGuild().getOwner().getUser().getId());
            Main.guildMap.put(event.getGuild().getId(), newGuildObject);
            System.out.println("Joining: " + event.getGuild().getName() + " (" + event.getGuild().getId() + ")");
            Main.updateGuilds(false, null, null);
        }
    }

    public void onGuildLeave(GuildLeaveEvent event) {
        Main.updateGuilds(true, event.getGuild().getName(), event.getGuild().getId());
    }

    public void onGuildMemberRemove(GuildMemberRemoveEvent event) {
        if (Main.guildMap.containsKey(event.getGuild().getId())) {
            GuildObject guildObj = Main.guildMap.get(event.getGuild().getId());
            if (guildObj.getModList().contains(event.getUser().getId())) {
                guildObj.removeMod(event.getGuild().getId());
                Main.updateGuilds(false, event.getGuild().getName(), event.getGuild().getId());
            }
        }
    }

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        //Checks that if the message received is a bot, ignore it and don't increment message count.
        if (event.getAuthor().isBot()) {
            return;
        }

        if (!Main.guildMap.containsKey(event.getGuild().getId())) {
            GuildObject newGuildObject = new GuildObject(Main.playerManager.createPlayer(),
                    event.getGuild().getId(), event.getGuild().getName(), null, 35);
            newGuildObject.addMod(event.getGuild().getOwner().getUser().getId());
            Main.guildMap.put(event.getGuild().getId(), newGuildObject);
            System.out.println("Joining: " + event.getGuild().getName() + " (" + event.getGuild().getId() + ")");
            Main.updateGuilds(false, null, null);
        }

        ++Main.messageCount;

        //AkiBot only processes commands if they start with -ab or an @mention to its name
        if (event.getMessage().getContentDisplay().startsWith("-ab")) {
            String message = event.getMessage().getContentDisplay().replaceFirst("-ab ", "");
            handleCommand(message.split("\\s+"), event);
        } else if (event.getMessage().getContentRaw().startsWith("<@!313955083584929792>")
                && event.getMessage().getContentDisplay().indexOf(' ') > 0) {
            String message = event.getMessage().getContentDisplay().substring(event.getMessage().getContentDisplay().indexOf(' ')).trim();
            handleCommand(message.split("\\s+"), event);
        }
    }

    private static void handleCommand(String[] args, GuildMessageReceivedEvent event) {
        //Checks that the command list has a command associated with the parsed message.
        //If successful, calls action() of the corresponding command.
        boolean safe = false;
        String saveKey = "", saveCat = "";

        for (String category : Main.categories.keySet()) {
            HashMap<String, BaseCommand> commands = Main.categories.get(category);
            if (commands.containsKey(args[0])) {
                saveKey = args[0];
                saveCat = category;
                safe = true;
                break;
            }
        }

        if (safe) {
            ++Main.commandCount;
            Main.categories.get(saveCat).get(saveKey).action(Arrays.copyOfRange(args, 1, args.length), event);
        } else {
            event.getChannel().sendMessage("Invalid command name! Type `-ab help commands` for a list of commands.").queue();
        }
    }
}
