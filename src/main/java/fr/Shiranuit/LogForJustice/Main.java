package fr.Shiranuit.LogForJustice;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.feed_the_beast.ftbutilities.FTBUtilities;
import com.mojang.realmsclient.dto.PlayerInfo;

import fr.Shiranuit.LogForJustice.Commands.SpyCommand;
import fr.Shiranuit.LogForJustice.Commands.TPADenyCommand;
import fr.Shiranuit.LogForJustice.Commands.TPAcceptCommand;
import fr.Shiranuit.LogForJustice.Commands.TPDimCommand;
import fr.Shiranuit.LogForJustice.Commands.VanishCommand;
import fr.Shiranuit.LogForJustice.Commands.TellSpyCommand;
import fr.Shiranuit.LogForJustice.Commands.TempbanCommand;
import fr.Shiranuit.LogForJustice.Commands.TpaCommand;
import fr.Shiranuit.LogForJustice.Commands.UntempbanCommand;
import fr.Shiranuit.LogForJustice.Crypto.Keys;
import fr.Shiranuit.LogForJustice.Crypto.RSA;
import fr.Shiranuit.LogForJustice.Commands.AdminHomeCommand;
import fr.Shiranuit.LogForJustice.Commands.BackCommand;
import fr.Shiranuit.LogForJustice.Commands.ChunkClearCommand;
import fr.Shiranuit.LogForJustice.Commands.ConfigCommand;
import fr.Shiranuit.LogForJustice.Commands.DelHomeCommand;
import fr.Shiranuit.LogForJustice.Commands.FeedCommand;
import fr.Shiranuit.LogForJustice.Commands.HealCommand;
import fr.Shiranuit.LogForJustice.Commands.HomeCommand;
import fr.Shiranuit.LogForJustice.Commands.InvSeeCommand;
import fr.Shiranuit.LogForJustice.Commands.LFJLogsCommand;
import fr.Shiranuit.LogForJustice.Commands.LFJSearchCommand;
import fr.Shiranuit.LogForJustice.Commands.LogForJusticeCommand;
import fr.Shiranuit.LogForJustice.Commands.MoneyCommand;
import fr.Shiranuit.LogForJustice.Commands.MuteCommand;
import fr.Shiranuit.LogForJustice.Commands.PlayerInfoCommand;
import fr.Shiranuit.LogForJustice.Commands.PlotCommand;
import fr.Shiranuit.LogForJustice.Commands.RankCommand;
import fr.Shiranuit.LogForJustice.Commands.RegionBypassCommand;
import fr.Shiranuit.LogForJustice.Commands.RegionCommand;
import fr.Shiranuit.LogForJustice.Commands.SeenCommand;
import fr.Shiranuit.LogForJustice.Commands.SetHomeCommand;
import fr.Shiranuit.LogForJustice.Commands.SpawnCommand;
import fr.Shiranuit.LogForJustice.Events.Events;
import fr.Shiranuit.LogForJustice.Events.EventsOther;
import fr.Shiranuit.LogForJustice.Fake.AlterateCommand;
import fr.Shiranuit.LogForJustice.Integrations.FTBUtilities.FTBUHomes;
import fr.Shiranuit.LogForJustice.Integrations.Morph.Events.MorphEvents;
import fr.Shiranuit.LogForJustice.Integrations.Morph.Events.MorphEventsOther;
import fr.Shiranuit.LogForJustice.Manager.AreaManager;
import fr.Shiranuit.LogForJustice.Manager.CommandSpyManager;
import fr.Shiranuit.LogForJustice.Manager.ConfigManager;
import fr.Shiranuit.LogForJustice.Manager.HomeManager;
import fr.Shiranuit.LogForJustice.Manager.RankManager;
import fr.Shiranuit.LogForJustice.Manager.LogForJusticeManager;
import fr.Shiranuit.LogForJustice.Manager.MessageSpyManager;
import fr.Shiranuit.LogForJustice.Manager.PlayerManager;
import fr.Shiranuit.LogForJustice.Utils.ArrayConverter;
import me.ichun.mods.morph.common.Morph;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandHandler;
import net.minecraft.command.ICommand;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.ModClassLoader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import net.minecraftforge.fml.common.network.FMLEventChannel;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid="logforjustice", name="logforjustice", version="1.6.6", acceptableRemoteVersions="*")
public class Main {
      @Instance("logforjustice")
	  public static Main instance;
	  public static MinecraftServer mcserver;
	  public static String modDir = "";
	  public static ArrayList<String> VanishedPlayers;
	  public static boolean HideTabPacket;
	  public static double SSend = 1d;
	  public static Logger logChat = LogManager.getLogger("CHAT");
	  public static FMLEventChannel channel;
	  public static final Keys pub = new Keys("", "", "") // need to be setup
	  public static final SimpleNetworkWrapper network = NetworkRegistry.INSTANCE.newSimpleChannel("logforjustice");
	  public static long cifferOffset;
	  public static String cifferOffsetHex;
	  public static boolean enabled = true;
	  
	  @Mod.EventHandler
	  public void onPreInit(FMLPreInitializationEvent event)
	    throws IOException
	  {
		  SecureRandom rnd = new SecureRandom();
		  cifferOffset = Math.abs(rnd.nextLong());
		  cifferOffsetHex = RSA.Ciffer(cifferOffset, pub);
		  
		  channel = NetworkRegistry.INSTANCE.newEventDrivenChannel("LogForJustice");
		  FMLCommonHandler.instance().bus().register(new EventsOther());
		  MinecraftForge.EVENT_BUS.register(new Events());
		  modDir = event.getModConfigurationDirectory().getPath() + File.separatorChar + "LogForJustice";
		  
			ConfigManager.modLogDir = modDir;
			ConfigManager.sync();
	  }
	  
	  @Mod.EventHandler
	  public void init(FMLInitializationEvent event)
	  {
		  
	  }
	  
	  @Mod.EventHandler
	  public void ready(FMLServerStartedEvent e ){
	    	// Overrides Commands
	    	HashMap<String, ICommand> nCmd = new HashMap<String, ICommand>();
	    	CommandHandler cmdHandle = (CommandHandler)mcserver.getCommandManager();
	    	Map commands = cmdHandle.getCommands();
	    	for (Object o : commands.keySet()) {
	    		String name = (String)o;
	    		ICommand cmd = (ICommand) commands.get(o);
	    		nCmd.put(name, new AlterateCommand(cmd));
	    	}
	    	
	    	ArrayList<String> cmds = new ArrayList<String>();
	    	
	    	for (String name : nCmd.keySet()) {
	    		ICommand cmd = nCmd.get(name);
	    		commands.put(name, cmd);
	    		if (!RankManager.commandsLevel.permission.containsKey(cmd.getName())) {
	    			RankManager.commandsLevel.permission.put(cmd.getName(), 0);
	    		}
	    		int lvl = RankManager.commandsLevel.permission.get(cmd.getName());
	    		if (!cmds.contains(cmd.getName()+":"+lvl)) {
	    			cmds.add(cmd.getName()+":"+lvl);
	    		}
	    	}
	    	
	    	ConfigManager.EssentialsConfig.getCategory("grade").get("commandslevel").set(ArrayConverter.convert(cmds));
	    	ConfigManager.EssentialsConfig.save();
	    	
	    	File f = new File(modDir + File.separatorChar + "EntityNameList.txt");
	    	try {
				FileWriter writer = new FileWriter(f,false);
				for (Iterator<ResourceLocation> iterator = EntityList.getEntityNameList().iterator(); iterator.hasNext(); ) {
					ResourceLocation loc = iterator.next();
					String name = loc.getResourceDomain() + "." + loc.getResourcePath();
					writer.write(name+"\n");
				}
				writer.close();
			} catch (IOException e1) {
				
			}
	    	
	    	
	    	// Integrations
	    	
	    	if (Loader.isModLoaded("morph")) {
	    		MinecraftForge.EVENT_BUS.register(new MorphEvents());
	    		FMLCommonHandler.instance().bus().register(new MorphEventsOther());
	    	}
	    	
	    	if (Loader.isModLoaded("ftbutilities")) {
	    		FTBUHomes.retrieveHomes();
	    	}
	  }
	  
	  @Mod.EventHandler
	  public void onServerStarting(FMLServerStartingEvent event)
	  {
		  mcserver = event.getServer();
		  File f = new File(modDir);
	    	if (!(f.exists() && f.isDirectory())) {
	    		f.mkdir();
	    	}
	    	
	    	LogForJusticeManager.modLogDir = modDir + File.separatorChar + "Logs";
	    	LogForJusticeManager.oldDir = modDir + File.separatorChar + "oldLogs";
	    	File b = new File(modDir + File.separatorChar + "BlocksLogs");
	    	if (b.exists() && b.isDirectory()) {
	    		for(File fD : b.listFiles()){
	    		    fD.delete();
	    		}
	    		b.delete();
	    	}
	    	
	    	LogForJusticeManager.setup();
	    	LogForJusticeManager.loadLFJ();
	    	
	    	CommandSpyManager.modLogDir = modDir + File.separatorChar + "CommandsLogs";
	    	CommandSpyManager.Setup();
	    	
	    	MessageSpyManager.modLogDir = modDir + File.separatorChar + "PrivateMessageLogs";
	    	MessageSpyManager.Setup();
	    	
	    	RankManager.modLogDir = modDir + File.separatorChar + "Grades";
	    	RankManager.setup();
	    	RankManager.loadRanks();
	    	
	    	AreaManager.modLogDir = modDir + File.separatorChar + "Area";
	    	AreaManager.setup();
	    	AreaManager.reloadProtection();
	    	
	    	PlayerManager.modLogDir = modDir + File.separatorChar + "PlayerData";
	    	PlayerManager.setup();
	    	PlayerManager.load();
	    	
	    	HomeManager.modLogDir = modDir + File.separatorChar + "Homes";
	    	HomeManager.setup();
	    	HomeManager.load();
	    	
	    	// REGISTER COMMANDS
	    	event.registerServerCommand(new LFJSearchCommand());
	    	event.registerServerCommand(new LFJLogsCommand());
	    	event.registerServerCommand(new SpyCommand());
	    	event.registerServerCommand(new TellSpyCommand());
	    	event.registerServerCommand(new VanishCommand());
	    	event.registerServerCommand(new RankCommand());
	    	event.registerServerCommand(new ChunkClearCommand());
	    	event.registerServerCommand(new RegionCommand());
	    	event.registerServerCommand(new PlotCommand());
	    	event.registerServerCommand(new ConfigCommand());
	    	event.registerServerCommand(new PlayerInfoCommand());
	    	event.registerServerCommand(new SeenCommand());
	    	event.registerServerCommand(new TempbanCommand());
	    	event.registerServerCommand(new UntempbanCommand());
	    	event.registerServerCommand(new RegionBypassCommand());
	    	event.registerServerCommand(new MoneyCommand());
	    	event.registerServerCommand(new HealCommand());
	    	event.registerServerCommand(new FeedCommand());
	    	event.registerServerCommand(new LogForJusticeCommand());
	    	event.registerServerCommand(new TPDimCommand());
	    	
	    	event.registerServerCommand(new BackCommand());
	    	event.registerServerCommand(new TpaCommand());
	    	event.registerServerCommand(new TPAcceptCommand());
	    	event.registerServerCommand(new TPADenyCommand());
	    	event.registerServerCommand(new SetHomeCommand());
	    	event.registerServerCommand(new HomeCommand());
	    	event.registerServerCommand(new DelHomeCommand());
	    	event.registerServerCommand(new AdminHomeCommand());
	    	event.registerServerCommand(new SpawnCommand());
	    	event.registerServerCommand(new MuteCommand());
	    	event.registerServerCommand(new InvSeeCommand());
	    	
	  }
	  
	  @Mod.EventHandler
	  public void onServerStopped(FMLServerStoppedEvent event)
	    throws IOException
	  {
		  RankManager.saveRanks();
	  }
}
