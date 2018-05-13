package com.akibot.core.audio;

/*
    * AkiBot v3.1.5 by PhoenixAki: music + moderation bot for usage in Discord servers.
    *
    * AudioPlayerSendHandler
    * Handles the process of sending audio to the guild's audio player.
 */

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;
import net.dv8tion.jda.core.audio.AudioSendHandler;

public class AudioPlayerSendHandler implements AudioSendHandler {
    private final AudioPlayer audioPlayer;
    private AudioFrame lastFrame;

    public AudioPlayerSendHandler(AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
    }

    public boolean canProvide() {
        lastFrame = audioPlayer.provide();
        return lastFrame != null;
    }

    public byte[] provide20MsAudio() {
        return lastFrame.data;
    }

    public boolean isOpus() {
        return true;
    }
}
