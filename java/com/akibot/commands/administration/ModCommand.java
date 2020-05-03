package com.akibot.commands.administration;

import com.akibot.commands.BaseCommand;
import com.akibot.core.bot.Main;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;

import static com.akibot.commands.Category.ADMIN;

public class ModCommand extends BaseCommand {
    public ModCommand() {
        super(ADMIN, "`mod` - Manages AkiBot mods for this server.", "`mod <add/remove/list> <@user>`: " +
                "Adds/removes the mentioned user as a mod, or lists the current mods.", "Mod");
    }

    public void action(String[] args, GuildMessageReceivedEvent event) {
        setup(event);
        String output = "";
        guildObj.update(guildControl); //update to ensure only active members are listed
        ArrayList<String> modList = guildObj.getModList();

        if (!isMod(guildObj, event)) {
            return;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
            for (String modId : guildObj.getModList()) {
                output = output.concat(", " + guildControl.getMemberById(modId).getUser().getName());
            }
            event.getChannel().sendMessage("List of mods:\n" + output.substring(2)).queue();
        } else if (args.length == 2) {
            switch (args[0].toLowerCase()) {
                case "add":
                    System.out.println(event.getMessage().getMentionedUsers().size());
                    for (User user : event.getMessage().getMentionedUsers()) {
                        System.out.println(user.getName());
                    }
                    if (event.getMessage().getMentionedUsers().size() > 0) {
                        User newMod = event.getMessage().getMentionedUsers().get(0);
                        if (!modList.contains(newMod.getId()) && newMod != event.getJDA().getSelfUser()) {
                            guildObj.addMod(newMod.getId());
                            event.getChannel().sendMessage("User " + newMod.getName() + " added as a mod.").queue();
                            Main.updateGuilds(false, null, null);
                        } else {
                            event.getChannel().sendMessage(newMod.getName() + " is already a mod.").queue();
                        }
                    } else {
                        event.getChannel().sendMessage("Invalid format! Type `-ab help mod` for more info.").queue();
                    }
                    return;
                case "remove":
                    if (event.getMessage().getMentionedUsers().size() > 0) {
                        User removeMod = event.getMessage().getMentionedUsers().get(0);
                        if (modList.contains(removeMod.getId())) {
                            guildObj.removeMod(removeMod.getId());
                            event.getChannel().sendMessage("User(s) " + removeMod.getName() + " removed as mod(s).").queue();
                            Main.updateGuilds(false, null, null);
                        } else {
                            event.getChannel().sendMessage(removeMod.getName() + " is not a mod.").queue();
                        }
                    } else {
                        event.getChannel().sendMessage("Invalid format! Type `-ab help mod` for more info.").queue();
                    }
                    return;
                default:
                    event.getChannel().sendMessage("Invalid format! Type `-ab help mod` for more info.").queue();
            }
        } else {
            event.getChannel().sendMessage("Invalid format! Type `-ab help mod` for more info.").queue();
        }
    }
}
