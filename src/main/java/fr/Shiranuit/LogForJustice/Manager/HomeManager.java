package fr.Shiranuit.LogForJustice.Manager;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

import com.mojang.authlib.GameProfile;

import fr.Shiranuit.LogForJustice.Main;
import fr.Shiranuit.LogForJustice.PlayerData.PlayerData;
import fr.Shiranuit.LogForJustice.PlayerData.Position;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.config.Configuration;

public class HomeManager {
	
	public static String modLogDir;
	public static HashMap<String, HashMap<String, Position>> homes = new HashMap<String, HashMap<String, Position>>();
	public static int homeNumber = 5;
	public static ArrayList<String> back = new ArrayList<String>();
	static {
		back.add("tp");
	}
	
	public static void setup() {
		File dir = new File(modLogDir);
		if (!(dir.exists() && dir.isDirectory())) {
			dir.mkdir();
		}
	}
	
	public static void setHome(String playername, String name, Position pos) {
		HashMap<String, Position> phomes = new HashMap<String, Position>();
		if (homes.containsKey(playername)) {
			phomes = homes.get(playername);
		}
		phomes.put(name, pos);
		File dir = new File(modLogDir+File.separatorChar+playername);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File f = new File(modLogDir+File.separatorChar+playername+File.separatorChar+name);
		if (f.exists()) {
			f.delete();
		}
		Configuration cfg = new Configuration(f);
		cfg.get("pos", "x", pos.getVec().x);
		cfg.get("pos", "y", pos.getVec().y);
		cfg.get("pos", "z", pos.getVec().z);
		cfg.get("pos", "dim", pos.getDim());
		cfg.save();
		
		homes.put(playername, phomes);
	}
	
	public static void setHome(EntityPlayer player, String name, Position pos) {
		HashMap<String, Position> phomes = new HashMap<String, Position>();
		if (homes.containsKey(player.getGameProfile().getId().toString())) {
			phomes = homes.get(player.getGameProfile().getId().toString());
		}
		phomes.put(name, pos);
		File dir = new File(modLogDir+File.separatorChar+player.getGameProfile().getId().toString());
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File f = new File(modLogDir+File.separatorChar+player.getGameProfile().getId().toString()+File.separatorChar+name);
		if (f.exists()) {
			f.delete();
		}
		Configuration cfg = new Configuration(f);
		cfg.get("pos", "x", pos.getVec().x);
		cfg.get("pos", "y", pos.getVec().y);
		cfg.get("pos", "z", pos.getVec().z);
		cfg.get("pos", "dim", pos.getDim());
		cfg.save();
		
		homes.put(player.getGameProfile().getId().toString(), phomes);
	}
	
	public static boolean hasHome(EntityPlayer player, String name) {
		if (homes.containsKey(player.getGameProfile().getId().toString())) {
			HashMap<String, Position> phomes = homes.get(player.getGameProfile().getId().toString());
			if (phomes.containsKey(name)) {
				return true;
			}
		}
		return false;
	}
	
	public static int countHome(EntityPlayer player) {
		if (homes.containsKey(player.getGameProfile().getId().toString())) {
			HashMap<String, Position> phomes = homes.get(player.getGameProfile().getId().toString());
			return phomes.size();
		}
		return 0;
	}
	
	public static void delHome(EntityPlayer player, String name) {
		if (homes.containsKey(player.getGameProfile().getId().toString())) {
			HashMap<String, Position> phomes = homes.get(player.getGameProfile().getId().toString());
			if (phomes.containsKey(name)) {
				phomes.remove(name);
				homes.put(player.getGameProfile().getId().toString(), phomes);
				File f = new File(modLogDir+File.separatorChar+player.getGameProfile().getId().toString()+File.separatorChar+name);
				if (f.exists()) {
					f.delete();
				}
			}
		}
	}
	
	public static Position getHome(String player, String name) {
		GameProfile profile = Main.mcserver.getPlayerProfileCache().getGameProfileForUsername(player);
		if (profile != null) {
			if (homes.containsKey(profile.getId().toString())) {
				HashMap<String, Position> phomes = homes.get(profile.getId().toString());
				if (phomes.containsKey(name)) {
					return phomes.get(name);
				}
			}
		}
		return null;
	}
	
	public static Position getHome(EntityPlayer player, String name) {
		if (homes.containsKey(player.getGameProfile().getId().toString())) {
			HashMap<String, Position> phomes = homes.get(player.getGameProfile().getId().toString());
			if (phomes.containsKey(name)) {
				return phomes.get(name);
			}
		}
		return null;
	}
	
	public static String[] listHomes(String name) {
			GameProfile profile = Main.mcserver.getPlayerProfileCache().getGameProfileForUsername(name);
			if (profile != null) {
				if (homes.containsKey(profile.getId().toString())) {
					HashMap<String, Position> phomes = homes.get(profile.getId().toString());
					String[] str = new String[phomes.keySet().size()];
					int i=0;
					for (String s : phomes.keySet()) {
						str[i]=s;
						i++;
					}
					return str;
				}
			}
			return new String[] {};
	}
	
	public static String[] listHomes(EntityPlayer player) {
		if (homes.containsKey(player.getGameProfile().getId().toString())) {
			HashMap<String, Position> phomes = homes.get(player.getGameProfile().getId().toString());
			String[] str = new String[phomes.keySet().size()];
			int i=0;
			for (String s : phomes.keySet()) {
				str[i]=s;
				i++;
			}
			return str;
		}
		return new String[] {};
	}
	
	public static void load() {
		File dir = new File(modLogDir);
		homes.clear();
		for (File f : dir.listFiles()) {
			if (f.isDirectory()) {
				HashMap<String, Position> lst = new HashMap<String, Position>();
				for (File home : f.listFiles()) {
					//System.out.println(f.getName()+" : "+home.getName());
					Configuration cfg = new Configuration(home);
					double x = cfg.get("pos","x",0D).getDouble();
					double y = cfg.get("pos","y",0D).getDouble();
					double z = cfg.get("pos","z",0D).getDouble();
					int dim = cfg.get("pos","dim",0).getInt();
					//System.out.println(x+" "+y+" "+z+" "+dim);
					cfg.save();
					lst.put(home.getName(), new Position(x,y,z,dim));
				}
				homes.put(f.getName(), lst);
			}
		}
	}

}
