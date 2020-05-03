package com.akibot.commands.music;

import com.akibot.commands.BaseCommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import static com.akibot.commands.Category.MUSIC;

public class PauseCommand extends BaseCommand {
    public PauseCommand() {
        super(MUSIC, "`pause` - Pauses the current song.", "`pause`: Pauses the current song. " +
                "To resume playback, use `-ab play`.", "Pause");
    }

    public void action(String[] args, GuildMessageReceivedEvent event) {
        setup(event);

        //Ensures AkiBot is connected to voice before continuing
        if (!isVoiceOk(event.getGuild().getSelfMember(), event.getMember(), event.getChannel())) {
            return;
        }

        if (args.length == 0) {
            //Ensures there is a playing track before continuing
            if (guildObj.getPlayer().getPlayingTrack() == null) {
                event.getChannel().sendMessage("No song playing!").queue();
                return;
            }

            //If already paused, don't do anything. Otherwise, pause the track.
            if (guildObj.getPlayer().isPaused()) {
                event.getChannel().sendMessage("Song is already paused! Type `-ab play` to resume.").queue();
            } else {
                guildObj.getPlayer().setPaused(true);
                event.getChannel().sendMessage("Song paused.").queue();
            }
        } else {
            event.getChannel().sendMessage("Invalid format! Type `-ab help pause` for more info.").queue();
        }
    }
}
