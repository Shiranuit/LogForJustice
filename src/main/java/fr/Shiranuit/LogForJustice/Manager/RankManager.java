package fr.Shiranuit.LogForJustice.Manager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.common.io.Files;

import fr.Shiranuit.LogForJustice.Main;
import fr.Shiranuit.LogForJustice.Grade.EnumCommandUse;
import fr.Shiranuit.LogForJustice.Grade.RankInfo;
import fr.Shiranuit.LogForJustice.Utils.ArrayConverter;
import fr.Shiranuit.LogForJustice.Utils.Permission;
import fr.Shiranuit.LogForJustice.Utils.Util;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.CommandBlockBaseLogic;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

public class RankManager {
	public static String modLogDir;
	public static HashMap<String, RankInfo> grades = new HashMap<String, RankInfo>();
	public static Permission commandsLevel = new Permission(0);
	public static Permission permissionsLevel = new Permission(Integer.MAX_VALUE);
	public static HashMap<String, String> playerRank = new HashMap<String, String>();
	public static String connectionMessage = "";
	public static boolean invulnerable = false;
	
	public static void setup() {
		File dir = new File(modLogDir);
		if (!(dir.exists() && dir.isDirectory())) {
			dir.mkdir();
		}
	}
	
	public static boolean createRank(String name, int power, String displayname) {
		if (!grades.containsKey(name)) {
			File f = new File(modLogDir + File.separatorChar + name + ".cfg");
			Configuration cfg = new Configuration(f);
			cfg.get("rankinfo", "members", new String[] {}, "List of players that are members of this rank");
			cfg.get("rankinfo", "displayname", displayname, "name display before playernames");
			cfg.get("rankinfo", "level", power, "power rank of the members");
			cfg.get("rankinfo", "maxhomes", HomeManager.homeNumber, "Max homes players member of this rank can have");
			cfg.get("rankinfo", "commands", new String[] {}, "Add or Remove commands to ranks");
			cfg.save();
			grades.put(name, new RankInfo(displayname, power, new ArrayList<String>(), cfg, HomeManager.homeNumber, new ArrayList<String>()));
			return true;
		}
		return false;
	}
	
	public static void removeRank(String name) {
		if (grades.containsKey(name)) {
			RankInfo info = grades.get(name);
			info.cfg.getConfigFile().delete();
			RankInfo definfo = grades.get("default");
			for (String pname : info.members) {
				if (!definfo.members.contains(pname)) {
					playerRank.put(pname, "default");
					definfo.members.add(pname);
				}
			}
			grades.put("default", definfo);
			definfo.save();
			grades.remove(name);
		}
	}
	
	public static boolean saveRank(String name) {
		if (existRank(name)) {
			RankInfo info = grades.get(name);
			info.save();
			return true;
		}
		return false;
	}
	
	public static boolean existRank(String name) {
		return grades.containsKey(name);
	}
	
	public static EnumCommandUse hasCommand(String rankname, String cmd) {
		if (existRank(rankname)) {
			RankInfo info = grades.get(rankname);
			if (info.cmd.contains(cmd)) {
				return EnumCommandUse.ALLOW;
			} else if (info.cmd.contains("-"+cmd)) {
				return EnumCommandUse.DENY;
			} else {
				return EnumCommandUse.NULL;
			}
		}
		return EnumCommandUse.NULL;
	}
	
	public static int getCommandLevel(String cmd) {
		return commandsLevel.permissionPower(cmd);
	}
	
	public static int getCommandLevel(ICommand cmd) {
		return getCommandLevel(cmd.getName());
	}
	
	public static boolean setRank(String name, String gradename) {
		if (grades.containsKey(gradename)) {
			String rank = rankName(name); 
			if (rank != null) {
				removeGrade(name);
			}
			RankInfo info = grades.get(gradename);
			info.members.add(name);
			grades.put(gradename, info);
			playerRank.put(name, gradename);
			return true;
		}
		return false;
	}
	
	public static boolean setRank(EntityPlayer pl, String gradename) {
		return setRank(pl.getName(), gradename);
	}
	
	public static boolean removeGrade(String name) {
		String grade = rankName(name); 
		if (grade != null) {
			RankInfo info = grades.get(grade);
			info.members.remove(name);
			grades.put(grade, info);
			info.save();
			return true;
		}
		return false;
	}
	
	public static boolean removeGrade(EntityPlayer pl) {
		return removeGrade(pl.getName());
	}
	
	public static String rankName(String name) {
		if (playerRank.containsKey(name)) {
			return playerRank.get(name);
		} else {
			for (String grade : grades.keySet()) {
				RankInfo info = grades.get(grade);
				if (info.members.contains(name)) {
					playerRank.put(name, grade);
					return grade;
				}
			}
			return null;
		}
	}
	
	public static String rankName(EntityPlayer pl) {
		return rankName(pl.getName());
	}
	
	public static int getPlayerPower(String name) {
		if (playerRank.containsKey(name)) {
			RankInfo info = grades.get(playerRank.get(name));
			return info.power;
		} else {
			for (String grade : grades.keySet()) {
				RankInfo info = grades.get(grade);
				if (info.members.contains(name)) {
					playerRank.put(name, grade);
					return info.power;
				}
			}
		}
		return 0;
	}
	
	public static int getPlayerPower(EntityPlayer pl) {
		return getPlayerPower(pl.getName());
	}
	
	public static int getPlayerPower(ICommandSender sender) {
		if (sender instanceof CommandBlockBaseLogic) {
			return Integer.MAX_VALUE;
		} else if (sender instanceof MinecraftServer) {
			return Integer.MAX_VALUE;
		} else if (sender.getCommandSenderEntity() instanceof EntityPlayer) {
			return RankManager.getPlayerPower(sender.getName());
		} else {
			return 0;
		}
	}
	
	public static boolean isPlayerRanked(String name) {
		if (playerRank.containsKey(name)) {
			return true;
		} else {
			boolean find = false;
			for (String grade : grades.keySet()) {
				RankInfo info = grades.get(grade);
				if (info.members.contains(name)) {
					playerRank.put(name, grade);
					find = true;
					break;
				}
			}
			return find;
		}
	}
	
	public static boolean isPlayerRanked(EntityPlayer pl) {
		return isPlayerRanked(pl.getName());
	}
	
	public static boolean canUseCommand(ICommand cmd,  ICommandSender sender) {
		return canUseCommand(cmd.getName(), sender.getName());
	}
	
	public static boolean canUseCommand(String cmd,  String sender) {
		if ((getPlayerPower(sender) >= getCommandLevel(cmd) && hasCommand(rankName(sender), cmd) == EnumCommandUse.NULL) || hasCommand(rankName(sender), cmd) == EnumCommandUse.ALLOW) {
			return true;
		} else {
			return false;
		}
	}
	
	public static void loadRankFromConfig(Configuration cfg) {
		String name = Files.getNameWithoutExtension(cfg.getConfigFile().getName());
		ArrayList<String> playernames = ArrayConverter.convert(cfg.get("rankinfo", "members", new String[] {}, "List of players that are members of this rank").getStringList());
		String displayname = cfg.get("rankinfo", "displayname", "", "name display before playernames").getString();
		int level = cfg.get("rankinfo", "level", 0, "power rank of the members").getInt();
		int maxhomes = cfg.get("rankinfo", "maxhomes", HomeManager.homeNumber, "Max homes players member of this rank can have").getInt();
		ArrayList<String> cmd = ArrayConverter.convert(cfg.get("rankinfo", "commands", new String[] {}, "Add or Remove commands to ranks").getStringList());
		cfg.save();
		grades.put(name, new RankInfo(displayname, level, playernames, cfg, maxhomes, cmd));
	}
	
	public static int rankPower(String name) {
		if (existRank(name)) {
			RankInfo info = grades.get(name);
			return info.power;
		}
		return 0;
	}
	// Permissions
	
	public static boolean existPermission(String name) {
		return permissionsLevel.existPermission(name);
	}
	
	public static int permissionPower(String name) {
		return permissionsLevel.permissionPower(name);
	}
	
	public static boolean hasPermission(String global, String modname, String name, int meta, String playername) {
		int playerLevel = getPlayerPower(playername);
		if (RankManager.existPermission(global+"."+modname+"."+name+"."+meta)) {
			if (RankManager.permissionPower(global+"."+modname+"."+name+"."+meta) <= playerLevel) {
				return true;
			} else {
				return false;
			}
		} else if (RankManager.existPermission(global+"."+modname+"."+name+".*") || RankManager.existPermission(global+"."+modname+"."+name)) {
			if (RankManager.permissionPower(global+"."+modname+"."+name+".*") <= playerLevel || RankManager.permissionPower(global+"."+modname+"."+name) <= playerLevel) {
				return true;
			} else {
				return false;
			}
		} else if (RankManager.existPermission(global+"."+modname+".*") || RankManager.existPermission(global+"."+modname)) {
			if (RankManager.permissionPower(global+"."+modname+".*") <= playerLevel || RankManager.permissionPower(global+"."+modname) <= playerLevel) {
				return true;
			} else {
				return false;
			}
		} else {
			if (RankManager.permissionPower(global+".*") <= playerLevel) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	public static boolean hasPermission(String global, String modname, String name, int meta, EntityPlayer player) {
		return hasPermission(global, modname, name, meta, player.getName());
	}
	
	public static void saveRanks() {
		for (String grade : grades.keySet()) {
			RankInfo info = grades.get(grade);
			info.save();
		}
	}
	
	public static void loadRanks() {
		grades.clear();
		File dir = new  File(modLogDir);
		File def = new File(modLogDir + File.separatorChar + "default.cfg");
		Configuration defcfg = new Configuration(def);
		defcfg.get("rankinfo", "members", new String[] {}, "List of players that are members of this rank");
		defcfg.get("rankinfo", "displayname", "", "name display before playernames");
		defcfg.get("rankinfo", "level", 0, "power rank of the members");
		defcfg.get("rankinfo", "maxhomes", HomeManager.homeNumber, "Max homes players member of this rank can have");
		defcfg.get("rankinfo", "commands", new String[] {}, "Add or Remove commands to ranks").getStringList();
		invulnerable=defcfg.get("rankinfo", "invulnerable", false, "is the player invulenrable to damages").getBoolean();
		connectionMessage=defcfg.get("rankinfo", "first-connection-message", "", "message sent to the player at his first connection on the server, '{PLAYERNAME}' is replaced by the name of the player").getString();
		defcfg.save();
		
		for (File f : dir.listFiles()) {
			if (f.getName().endsWith(".cfg")) {
				try {
					Configuration cfg = new Configuration(f);
					loadRankFromConfig(cfg);
				} catch (Exception e) {
					
				}
			}
		}
	}

	
}
