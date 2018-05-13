# AkiBot - Multi-purpose Discord bot
## Purpose
AkiBot was created by me in Spring 2017 as a way to test my knowledge of using Java in a larger, more structured application 
than what I have made in classes before. I spent most of my free time from March-July of 2017 working on this bot on-and-off adding 
more features and commands as I went. I have placed a high priority on maintaining very clean and efficient code wherever possible, 
which always brought me back to try to improve the code even more over time.

You can invite AkiBot to a Discord server using this link: https://discordapp.com/oauth2/authorize?client_id=313955083584929792&permissions=8&scope=bot

**NOTE**: I do not regularly turn the bot online, as this was primarily for my own learning and not for actual public release. 
Please contact me on Discord at Phoenix#0353 if you would like me to turn the bot on for you to try it out.

## Commands
AkiBot's commands can be summarized into 4 categories: Administration, Fun, Information, and Music. All commands to the bot are prefaced 
by the phrase "-ab", or by saying @AkiBot in place of "-ab".

#### Administration (requires "Full" mod level)
ban <@user>: Bans the given user from the current server.
create <text/voice/role> <name>: Creates a text, voice, or role with the given name.
delete <text/voice/role> <name>: Deletes the text/voice channel, or role, with the given name.
kick <@user>: Kicks the given user from the current server.
leave: Makes AkiBot leave the current server.
mod <add/remove/list> <music/full> <@user>: Adds/removes a mod to the bot, or lists the current mods. Music mod level is required to 
do music commands; Full mod level is required to do administration commands.
mute <@user>: Mutes the given user in a voice channel.
nickname <newNickname> <@user>: Forces the given nickname on the given user.
prune <days>: Kicks all users in the server who have been offline for at least the given number of days.
unmute <@user>: Unmutes the given user in a voice channel.

#### Fun
8ball <message>: Returns a random result simulating an 8-ball to the given message.
roll <number>: Rolls a 6-sided dice a given number of times (defaults to once if no number is given).
rps <rock/paper/scissors>: Plays a game of Rock, Paper, Scissors with AkiBot.

#### Info
commands <admin/fun/info/music>: Prints a list of commands on a per-category basis.
help <commandName>: Prints the help text for a given command.
server: Prints information about the current server, such as creation date and member count.
status: Prints information about AkiBot's current status.
user <@user>: Prints information about the given user.

#### Music (some commands require "Music" or "Full" mod level)
pause: Pauses a currently playing track.
play <link/searchTerms>: Unpauses a track, plays a track via direct link, or searches youtube for a track given search terms.
queue <reset>: Prints (up to) 10 tracks from the current queue, or empties the queue.
remove <trackNumber>: Removes a track with the given track number from the queue.
seek <mm:ss>: Skips to the given time in the playing track. 
skip: Skips the currently playing track.
song: Prints information about the currently playing track.
stop: Stops the currently playing track.
voice <join/leave>: Summons or dismisses AkiBot to/from the current voice channel the calling user is in.
volume <newVolume>: Changes the volume of the currently playing track to the given new volume.
