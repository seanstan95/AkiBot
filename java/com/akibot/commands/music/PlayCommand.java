package com.akibot.commands.music;

import com.akibot.commands.BaseCommand;
import com.akibot.core.audio.TrackInfo;
import com.akibot.core.bot.Main;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.akibot.commands.Category.MUSIC;

public class PlayCommand extends BaseCommand {
	public PlayCommand() {
		super(MUSIC, "`play` - Queues a song for playback. Unpauses the player if paused.", "`play`: " +
				"Resumes the player if previously paused/stopped.\n`play <link/searchTerms>`: Queues the song " +
				"if directly linked, or searches youtube and queues the first result.", "Play");
	}

	public void action(String[] args, GuildMessageReceivedEvent event) {
		setup(event);
		Pattern URL_PATTERN = Pattern.compile("^(https?|ftp)://([A-Za-z0-9-._~/?#\\[\\]:!$&'()*+,;=]+)$");

		//Ensures AkiBot is connected to voice before continuing
		if (!isVoiceOk(event.getGuild().getSelfMember(), event.getMember(), event.getChannel())) {
			return;
		}

		//Ensures the sending handler is properly set before continuing
		event.getGuild().getAudioManager().setSendingHandler(guildObj.getSendHandler());

		if (args.length == 0) {
			//Checks if the player is already paused, then resume playing.
			if (guildObj.getPlayer().isPaused()) {
				guildObj.getPlayer().setPaused(false);
				event.getChannel().sendMessage("Song resumed.").queue();
				return;
			}

			//Checks if the player was previously stopped but there are still songs in the queue.
			if (guildObj.getPlayer().getPlayingTrack() == null && guildObj.getScheduler().hasNextTrack()) {
				guildObj.getScheduler().nextTrack();
			} else {
				event.getChannel().sendMessage("No songs are queued.").queue();
			}
		} else {
			//If args[0] is a URL, process as-is. If not, search youtube and load the first result.
			Matcher m = URL_PATTERN.matcher(args[0]);
			String input = "";
			if (m.find()) {
				input = args[0];
			} else {
				for (String arg : args) {
					input = input.concat(arg + " ");
				}
				input = "ytsearch:" + input;
				event.getChannel().sendMessage("Searching youtube...").queue();
			}

			Main.playerManager.loadItemOrdered(Main.playerManager, input, new AudioLoadResultHandler() {
				public void trackLoaded(AudioTrack track) {
					track.setUserData(new TrackInfo(event.getAuthor().getName(), event.getChannel()));
					guildObj.getScheduler().addTrack(track);

					if (guildObj.getScheduler().getQueueSize() == 0) {
						event.getChannel().sendMessage(":musical_note: Now Playing: **" + track.getInfo().title
								+ "**, requested by **" + event.getMember().getEffectiveName() + "**").queue();
					} else {
						event.getChannel().sendMessage("Queued: **" + track.getInfo().title + "**").queue();
					}
				}

				public void playlistLoaded(AudioPlaylist playlist) {
					//ytsearch returns a playlist of results, but AkiBot loads just the first result
					if (playlist.isSearchResult()) {
						trackLoaded(playlist.getTracks().get(0));
					} else {
						guildObj.getScheduler().addPlaylist(playlist, event);
						event.getChannel().sendMessage("Playlist loaded successfully!").queue();
					}
				}

				public void noMatches() {
					event.getChannel().sendMessage("No results.").queue();
				}

				public void loadFailed(FriendlyException e) {
					e.printStackTrace();
				}
			});
		}
	}
}