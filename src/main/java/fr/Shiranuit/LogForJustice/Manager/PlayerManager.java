package fr.Shiranuit.LogForJustice.Manager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import com.mojang.authlib.GameProfile;

import fr.Shiranuit.LogForJustice.Main;
import fr.Shiranuit.LogForJustice.PlayerData.NBTPlayerData;
import fr.Shiranuit.LogForJustice.PlayerData.PlayerData;
import fr.Shiranuit.LogForJustice.PlayerData.Position;
import fr.Shiranuit.LogForJustice.Utils.Util;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.config.Configuration;

public class PlayerManager {
	public static String modLogDir;
	
	public static HashMap<String, PlayerData> playerdata = new HashMap<String, PlayerData>();
	public static HashMap<String, NBTPlayerData> nbtplayerdata = new HashMap<String, NBTPlayerData>();
	public static ArrayList<String> offnames = new ArrayList<String>();
	public static HashMap<String, Position> previousPositions = new HashMap<String, Position>();
	public static HashMap<String, HashMap<String, Date>> tprequest = new HashMap<String, HashMap<String, Date>>();
	
	public static int requestExpireTime = 30;
	
	public static void setup() {
		File dir = new File(modLogDir);
		if (!(dir.exists() && dir.isDirectory())) {
			dir.mkdir();
		}
	}
	
	public static boolean hasData(String name, boolean isName) {
		if (isName) {
			GameProfile profile = Main.mcserver.getPlayerProfileCache().getGameProfileForUsername(name);
			if (profile != null) {
				return playerdata.containsKey(profile.getId().toString());
			}
			return false;
		} else {
			return playerdata.containsKey(name);
		}
	}
	
	public static void add(EntityPlayer player) {
		if (!hasData(player.getName(), true)) {
			playerdata.put(player.getGameProfile().getId().toString(), new PlayerData(player));
			if (!offnames.contains(player.getName())) {
				offnames.add(player.getName());
			}
		}
	}
	
	public static PlayerData getPlayerData(String name, boolean isName) {
		if (isName) {
			GameProfile profile = Main.mcserver.getPlayerProfileCache().getGameProfileForUsername(name);
			if (profile != null) {
				return playerdata.get(profile.getId().toString());
			}
			return null;
		} else {
			return playerdata.get(name);
		}
	}
	
	public static PlayerData getPlayerData(EntityPlayer player) {
		return getPlayerData(player.getGameProfile().getId().toString(), false);
	}
	
	public static void load() {
		File dir = new File(modLogDir);
		for (File f : dir.listFiles()) {
			playerdata.put(f.getName(), new PlayerData(new Configuration(f)));
			GameProfile profile = Main.mcserver.getPlayerProfileCache().getProfileByUUID(UUID.fromString(f.getName()));
			if (profile != null) {
				offnames.add(profile.getName());
			}
		}
	}
	
	/*
	 * Previous Positions
	 */
	
	public static void setPrevious(EntityPlayer player, Position pos) {
		previousPositions.put(player.getGameProfile().getId().toString(), pos);
	}
	
	public static boolean hasPrevious(EntityPlayer player) {
		return previousPositions.containsKey(player.getGameProfile().getId().toString());
	}
	
	public static Position getPrevious(EntityPlayer player) {
		return previousPositions.get(player.getGameProfile().getId().toString());
	}
	
	public static Position removePrevious(EntityPlayer player) {
		return previousPositions.remove(player.getGameProfile().getId().toString());
	}
	
	/*
	 * 
	 */
	
	public static void TPARequestAdd(String target, String source) {
		HashMap<String, Date> dt = new HashMap<String, Date>();
		if (tprequest.containsKey(target)) {
			dt = tprequest.get(target);
		}
		
		dt.put(source, new Date(new Date().getTime() + 1000 * requestExpireTime));
		
		tprequest.put(target, dt);
	}
	
	public static boolean hasTPARequest(String target, String source) {
		if (tprequest.containsKey(target)) {
			HashMap<String, Date> dt = tprequest.get(target);
			if (dt.containsKey(source)) {
				return true;
			}
		}
		return false;
	}
	
	public static void TPARequestRemove(String target, String source) {
		HashMap<String, Date> dt = new HashMap<String, Date>();
		if (tprequest.containsKey(target)) {
			dt = tprequest.get(target);
		}
		
		if (dt.containsKey(source)) {
			dt.remove(source);
		}
		
		tprequest.put(target, dt);
	}
	
	public static Date getTPARequest(String target, String source) {
		if (tprequest.containsKey(target)) {
			HashMap<String, Date> dt = tprequest.get(target);
			if (dt.containsKey(source)) {
				return dt.get(source);
			}
		}
		return null;
	}
	
}
