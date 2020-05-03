package com.akibot.commands.music;

import com.akibot.commands.BaseCommand;
import com.akibot.core.bot.Main;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import static com.akibot.commands.Category.MUSIC;

public class VolumeCommand extends BaseCommand {
    public VolumeCommand() {
        super(MUSIC, "`volume` - Changes the current volume.", "`volume`: Displays the " +
                "current volume.\n`volume <newVolume>` Updates the current volume (must be 1-100).", "Volume");
    }

    public void action(String[] args, GuildMessageReceivedEvent event) {
        setup(event);
        int newVolume;

        //Ensures AkiBot is connected to voice before continuing
        if (!isVoiceOk(event.getGuild().getSelfMember(), event.getMember(), event.getChannel())) {
            return;
        }

        if (args.length == 0) {
            event.getChannel().sendMessage("Current volume: " + guildObj.getPlayer().getVolume()).queue();
        } else if (args.length == 1) {
            try {
                newVolume = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                event.getChannel().sendMessage("Invalid format! Type `-ab help volume` for more info.").queue();
                return;
            }

            if (newVolume < 1 || newVolume > 100) {
                event.getChannel().sendMessage("Invalid format! Type `-ab help volume` for more info.").queue();
            } else {
                guildObj.getPlayer().setVolume(newVolume);
                event.getChannel().sendMessage("Volume changed to " + newVolume).queue();
                Main.updateGuilds(false, event.getGuild().getName(), guildObj.getId());
            }
        } else {
            event.getChannel().sendMessage("Invalid format! Type `-ab help volume` for more info.").queue();
        }
    }
}
