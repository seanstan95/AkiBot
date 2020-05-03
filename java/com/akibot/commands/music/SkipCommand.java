package com.akibot.commands.music;

import com.akibot.commands.BaseCommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import static com.akibot.commands.Category.MUSIC;

public class SkipCommand extends BaseCommand {
    public SkipCommand() {
        super(MUSIC, "`skip` - Skips the current song.", "`skip`: Skips the current song.", "Skip");
    }

    public void action(String[] args, GuildMessageReceivedEvent event) {
        setup(event);

        //Ensures AkiBot is connected to voice before continuing
        if (!isVoiceOk(event.getGuild().getSelfMember(), event.getMember(), event.getChannel())) {
            return;
        }

        if (args.length == 0) {
            if (guildObj.getPlayer().getPlayingTrack() == null) {
                event.getChannel().sendMessage("No song is playing!").queue();
            } else {
                event.getChannel().sendMessage("Song skipped.").queue();
                guildObj.getScheduler().nextTrack();
                if (guildObj.getPlayer().isPaused())
                    guildObj.getPlayer().setPaused(false);
            }
        } else {
            event.getChannel().sendMessage("Invalid format! Type `-ab help skip` for more info.").queue();
        }
    }
}
