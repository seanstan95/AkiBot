package com.akibot.core.bot;

/*
	* AkiBot v3.1.5 by PhoenixAki: music + moderation bot for usage in Discord servers.
	* 
	* Main
	* Mostly startup tasks and variable storage for use from commands.
*/

import com.akibot.commands.*;
import com.akibot.commands.administration.*;
import com.akibot.commands.fun.*;
import com.akibot.commands.info.*;
import com.akibot.commands.music.*;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

import static com.akibot.commands.ModLevel.FULL;
import static com.akibot.commands.ModLevel.MUSIC;

public class Main {

    //Bot Info Variables
    public static final String THUMBNAIL = "http://i.imgur.com/k3zVzOc.png", version = "v3.1.5";
    public static long commandCount = 0, messageCount = 0, startupTime;
    private static String botToken;

    //Main Variables (used in startup + updating files)
    public static ArrayList<String> eightBallResponses = new ArrayList<>();
    public static AudioPlayerManager playerManager;
    private static File guilds;
    private static FileReader guildReader;
    private static PrintWriter guildWriter = null, logWriter = null;
    private static Scanner eightBallStream = null;

    //Reference Variables (used in other classes)
	public static File log;
	public static HashMap<String, BaseCommand> commands = new HashMap<>();
	public static HashMap<String, GuildObject> guildMap = new HashMap<>();
    static final ArrayList<String> PM_COMMANDS = new ArrayList<>(Arrays.asList("shutdown", "8ball", "roll", "rps", "commands", "help", "log", "server", "status", "user"));
	
	public static void main(String[] args){
		//Opens file input streams and startup api object.
	    openFiles();

		try{
			JDA jda = new JDABuilder(AccountType.BOT).addEventListener(new com.akibot.core.bot.CommandHandler()).setToken(botToken).buildBlocking();
			jda.setAutoReconnect(true);
			jda.getPresence().setGame(Game.of(Game.GameType.DEFAULT, "AkiBot 3.1.5 | -ab help"));
			startupTime = System.currentTimeMillis() + 14400000;
		}catch(Exception e){
			e.printStackTrace();
		}

		//Startup tasks: initialize music variables, populate lists, add commands, and log startup time.
		startup(startupTime);
	}

	private static void openFiles(){
		//Open files for reading input.
		try{
			guilds = new File("resources\\Guilds.txt");
			log = new File("Log.txt");
			logWriter = new PrintWriter(log);
	        eightBallStream = new Scanner(new File("resources\\8Ball.txt")).useDelimiter("\\n");
	        guildReader = new FileReader(guilds);
	        botToken = new Scanner(new File("resources\\Token.txt")).useDelimiter("\\n").next().trim();
	    }catch(FileNotFoundException e){
	        System.out.println("Can't find file(s)!");
	    }
	}
	
	private static void startup(long UtcTimeInMillis) {
		//Initializes player manager.
        playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);

        //Populates 8ball list, parses GuildObject JSON information, and fills command list.
		while(eightBallStream.hasNext()){
			eightBallResponses.add(eightBallStream.next().trim());
		}

		parseJSON();
		addCommands();

		//Logs the bot startup time.
		SimpleDateFormat sdf = new SimpleDateFormat("M/dd/yyyy, h:mm:ss a 'UTC'");
		String time = sdf.format(UtcTimeInMillis);
		Main.updateLog("Guild Name", "Guild ID", "Username", "Command Name", "Date/Time");
		Main.updateLog("AkiBot", "N/A", "AkiBot", "Startup", time);
	}
	
	private static void addCommands() {
        //Administration Commands
        commands.put("ban", new BanCommand());
        commands.put("create", new CreateCommand());
        commands.put("delete", new DeleteCommand());
        commands.put("leave", new LeaveCommand());
        commands.put("kick", new KickCommand());
        commands.put("mod", new ModCommand());
        commands.put("mute", new MuteCommand());
        commands.put("nickname", new NicknameCommand());
        commands.put("prune", new PruneCommand());
        commands.put("shutdown", new ShutdownCommand());
        commands.put("unmute", new UnmuteCommand());

		//Fun Commands
		commands.put("8ball", new EightBallCommand());
		commands.put("roll", new RollCommand());
		commands.put("rps", new RpsCommand());

        //Info Commands
	    commands.put("commands", new CommandsCommand());
	    commands.put("help", new HelpCommand());
		commands.put("log", new LogCommand());
		commands.put("server", new ServerCommand());
		commands.put("status", new StatusCommand());
		commands.put("user", new UserCommand());

		//Music Commands
        commands.put("pause", new PauseCommand());
        commands.put("play", new PlayCommand());
        commands.put("queue", new QueueCommand());
        commands.put("remove", new RemoveCommand());
        commands.put("seek", new SeekCommand());
        commands.put("skip", new SkipCommand());
        commands.put("song", new SongCommand());
        commands.put("stop", new StopCommand());
        commands.put("voice", new VoiceCommand());
        commands.put("volume", new VolumeCommand());
	}

	private static void parseJSON() {
	    //Parsing JSON from Guilds.JSON - first confirms Guilds.JSON has content
	    //Then loops through each entry in the root array of guilds, putting the resulting GuildObject into guildMap
        if(guilds.length() == 0){
            System.out.println("Empty JSON File!");
            return;
        }

        JSONArray array = new JSONArray(new JSONTokener(guildReader));
        HashMap<String, ModLevel> modMap = new HashMap<>();

        for (int i = 0; i < array.length(); ++i) {
            //Get guild entry and save the id
            JSONObject guildEntry = (JSONObject) array.get(i);
            JSONArray mods = (JSONArray) guildEntry.get("Mods");
            String guildId = (String) guildEntry.get("GuildId");

            //Iterate through the list of mods
            for (int j = 0; j < mods.length(); ++j) {
                JSONObject modEntry = (JSONObject) mods.get(j);
                String modId = (String) modEntry.get("Id"), modLevel = (String) modEntry.get("ModLevel");
                if (modLevel.equalsIgnoreCase("MUSIC")) {
                    modMap.put(modId, MUSIC);
                } else if (modLevel.equalsIgnoreCase("FULL")) {
                    modMap.put(modId, FULL);
                }
            }
            //Once all done iterating through list of mods, ready to put new guildObject into guildMap
            guildMap.put(guildId, new GuildObject(playerManager, guildId, (String)guildEntry.get("GuildName"), modMap, (int)guildEntry.get("Volume")));
        }
	}

	public static void updateLog(String guildName, String guildId, String username, String command, String time){
		logWriter.println(guildName + "\t" + guildId + "\t" + username + "\t" + command + "\t" + time);
		logWriter.flush();
	}

	public static void updateGuilds(boolean leave, String name, String id){
		//If the bot has left a server, update the list of GuildObjects before updating Guilds.txt
		if(leave){
			System.out.printf("Leaving: %s (%s)", name, id);
			if(guildMap.containsKey(id)){
				guildMap.remove(id);
			}
		}

		try{
			guildWriter = new PrintWriter(guilds);
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}

		//Pre-output setting up structure - root array to which every GuildObject will be added to the root as a JSONObject
		//All objects that are found from the for-each loop will be added to the root array for file output
		JSONArray root = new JSONArray();

		for(String key : guildMap.keySet()){
			root.put(guildMap.get(key).toJSONObject());
		}

		guildWriter.println(root.toString(2));
		guildWriter.flush();
	}
}