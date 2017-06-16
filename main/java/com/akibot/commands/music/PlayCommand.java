package com.akibot.commands.music;

/*
	* AkiBot v3.0 by PhoenixAki: music + moderation bot for usage in Discord servers.
	*
	* Play
	* Queues a song or playlist to be played by the guild's AudioPlayer. Also resumes the player if paused/stopped.
	* Allows links from YouTube, SoundCloud, BandCamp, and live Twitch streams.
	* Takes in format -ab play, -ab play linkHere, and -ab play searchTermsHere
 */

import com.akibot.core.audio.TrackInfo;
import com.akibot.commands.BaseCommand;

import com.akibot.core.bot.GuildObject;
import com.akibot.core.bot.Main;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.akibot.commands.Category.MUSIC;

public class PlayCommand extends BaseCommand {
	public PlayCommand() {
		super(MUSIC, "`play` - Queues a track for playback. Unpauses the player if paused.", "`play`: Resumes the player if previously paused/stopped.\n`play linkHere`: Queues the song. If this is not a direct video link, AkiBot will search youtube and queue the first result.", "Play");
	}

	public void action(String[] args, MessageReceivedEvent event) {
		GuildObject guild = Main.guildMap.get(event.getGuild().getId());
		Main.updateLog(guild.getName(), guild.getId(), event.getAuthor().getName(), getName(), formatTime(null, event));
		final Pattern URL_PATTERN = Pattern.compile("^(https?|ftp)://([A-Za-z0-9-._~/?#\\\\[\\\\]:!$&'()*+,;=]+)$");
		String input = "";

		//Ensures AkiBot is connected to voice before continuing
		if(!isVoiceOk(event.getGuild().getSelfMember().getVoiceState(), event.getMember().getVoiceState(), event.getChannel())){
			return;
		}

		//Ensures the sending handler is properly set before continuing
		event.getGuild().getAudioManager().setSendingHandler(guild.getSendHandler());

		switch (args.length) {
			case 0:
				//Checks if the player is already paused, then resume playing.
				if(guild.getPlayer().isPaused()){
					guild.getPlayer().setPaused(false);
					event.getChannel().sendMessage("Track resumed.").queue();
					return;
				}

				//Checks if the player was previously stopped but there are still songs in the queue.
				if(guild.getPlayer().getPlayingTrack() == null && guild.getScheduler().hasNextTrack()){
					guild.getScheduler().nextTrack();
				}else{
					event.getChannel().sendMessage("Player isn't paused/stopped, or no tracks are queued.").queue();
				}
				return;
			default:
				//If args[0] is a URL, process as-is. If not, search youtube and load the first result.
				Matcher m = URL_PATTERN.matcher(args[0]);
				if(m.find()){
					input = args[0];
				}else {
					for(String arg : args){
						input = input.concat(arg);
					}
					input = "ytsearch:" + input;
                    event.getChannel().sendMessage("Searching youtube...").queue();
				}

				Main.playerManager.loadItemOrdered(Main.playerManager, input, new AudioLoadResultHandler() {
					public void trackLoaded(AudioTrack track){
						track.setUserData(new TrackInfo(event.getAuthor().getName(), event.getChannel()));
						guild.getScheduler().addTrack(track);

						if(guild.getScheduler().getQueueSize() == 0){
							event.getChannel().sendMessage(":musical_note: Now Playing: **" + track.getInfo().title + "**, requested by **" + event.getMember().getEffectiveName() + "**").queue();
						}else{
							event.getChannel().sendMessage("Queued: **" + track.getInfo().title + "**").queue();
						}
					}

					public void playlistLoaded(AudioPlaylist playlist) {
						//ytsearch: returns a playlist of results, but AkiBot loads just the first result
						if(playlist.isSearchResult()){
							AudioTrack track = playlist.getTracks().get(0);
							track.setUserData(new TrackInfo(event.getAuthor().getName(), event.getChannel()));
							guild.getScheduler().addTrack(track);

							if(guild.getScheduler().getQueueSize() == 0){
								event.getChannel().sendMessage(":musical_note: Now Playing: **" + track.getInfo().title + "**, requested by **" + event.getMember().getEffectiveName() + "**").queue();
							}else{
								event.getChannel().sendMessage("Queued: **" + track.getInfo().title + "**").queue();
							}
						}else{
							guild.getScheduler().addPlaylist(playlist, event);
							event.getChannel().sendMessage("Playlist loaded successfully!").queue();
						}
					}

					public void noMatches() {
							event.getChannel().sendMessage("No match found :c").queue();
                    }

					public void loadFailed(FriendlyException e) {
							e.printStackTrace();
                    }
                });
		}
	}
}