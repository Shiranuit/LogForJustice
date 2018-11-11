package fr.Shiranuit.LogForJustice.Manager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import fr.Shiranuit.LogForJustice.Area.Area;
import fr.Shiranuit.LogForJustice.Area.Flag;
import fr.Shiranuit.LogForJustice.Area.FlagPerm;
import fr.Shiranuit.LogForJustice.Area.FlagType;
import fr.Shiranuit.LogForJustice.Area.Selection;
import fr.Shiranuit.LogForJustice.Utils.Permission;
import fr.Shiranuit.LogForJustice.Utils.Util;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class AreaManager {
	public static String modLogDir;
	public static HashMap<String, Area> Areas = new HashMap<String,Area>();
	public static HashMap<String, Flag> flagDefault = new HashMap<String, Flag>();
	public static HashMap<String, Selection> selections = new HashMap<String, Selection>();
	public static HashMap<String, Boolean> bypass = new HashMap<String, Boolean>();
	public static HashMap<String, Double> currency = new HashMap<String, Double>();
	
	static {
		flagDefault.put("explosions", new Flag(FlagType.BOOLEAN, FlagPerm.OP, true, "Prevent block from being destroyed by explosions"));
		flagDefault.put("pvp", new Flag(FlagType.BOOLEAN, FlagPerm.OP, true,"Prevent players from doing damage to other players"));
		
		flagDefault.put("break", new Flag(FlagType.BOOLEAN, FlagPerm.OWNER, false,"Prevent players from breaking blocks"));
		flagDefault.put("place", new Flag(FlagType.BOOLEAN, FlagPerm.OWNER, false,"Prevent players from placing blocks"));
		flagDefault.put("interact", new Flag(FlagType.BOOLEAN, FlagPerm.OWNER, false,"Prevent players from interacting with blocks"));
		flagDefault.put("interact-entity", new Flag(FlagType.BOOLEAN, FlagPerm.OP, true, "Prevent players from interacting with entity"));
		flagDefault.put("use", new Flag(FlagType.BOOLEAN, FlagPerm.OWNER, false,"Prevent players from using items"));
		flagDefault.put("itempickup", new Flag(FlagType.BOOLEAN, FlagPerm.OWNER, true,"Prevent players from picking items up"));
		
		flagDefault.put("is_buyable", new Flag(FlagType.BOOLEAN, FlagPerm.OP, false,"Define if the area can be buy"));
		flagDefault.put("cost", new Flag(FlagType.DOUBLE, FlagPerm.OP, 0D,"Define the amount of money needed to buy this area"));
		
		flagDefault.put("can_chat", new Flag(FlagType.BOOLEAN, FlagPerm.OP, true, "Prevent players from sending messages"));
		flagDefault.put("can_use_command", new Flag(FlagType.BOOLEAN, FlagPerm.OP, true, "Prevent players from using commands"));
		
		flagDefault.put("owner", new Flag(FlagType.STRING, FlagPerm.OWNER, "","Define who own the area"));
		flagDefault.put("members", new Flag(FlagType.LIST_STRING, FlagPerm.OWNER, new String[] {},"List of area members"));
		
		flagDefault.put("spawn_entity", new Flag(FlagType.PERMISSION, FlagPerm.OP, new Permission(1,new String[] {"creature.*:1","monster.*:1","object.*:1"}),"0 = Prevent mob from spawning, 1 = Allow mob to spawn"));
		
		flagDefault.put("can_morph", new Flag(FlagType.BOOLEAN, FlagPerm.OP, true, "Prevent players from morphing"));
		flagDefault.put("can_aquire_morph", new Flag(FlagType.BOOLEAN, FlagPerm.OP, true, "Prevent player from aquiring morphs"));
		flagDefault.put("force_unmorph", new Flag(FlagType.BOOLEAN, FlagPerm.OP, false, "Force players to be unmorphed"));
	}
	
	public static Selection getSelection(String name) {
		if (selections.containsKey(name)) {
			return selections.get(name);
		} else {
			Selection selection = new Selection();
			selections.put(name, selection);
			return selection;
		}
	}
	
	public static Selection getSelection(EntityPlayer player) {
		return getSelection(player.getName());
	}
	
	public static Selection getSelection(ICommandSender player) {
		return getSelection(player.getName());
	}
	
	public static boolean hasBypass(String name) {
		if (bypass.containsKey(name)) {
			return bypass.get(name);
		} else {
			return false;
		}
	}
	
	public static boolean hasBypass(EntityPlayer player) {
		return hasBypass(player.getDisplayNameString());
	}
	
	public static void setBypass(String name, boolean state) {
		bypass.put(name, state);
	}
	
	public static void setBypass(EntityPlayer player, boolean state){
		bypass.put(player.getDisplayNameString(), state);
	}
	
	public static void setup() {
			File dir = new File(modLogDir);
			if (!(dir.exists() && dir.isDirectory())) {
				dir.mkdir();
			}
	}
	
	public static void reloadProtection() {
		File dir = new File(modLogDir);
		Areas = new HashMap<String,Area>();
		 if (!dir.exists()) {
			 dir.mkdirs();
		 }
		 File[] lst = dir.listFiles();
		 for (File f : lst) {
			 Configuration config = new Configuration(f);
			int x = config.get("pos","x",0).getInt();
			int y = config.get("pos","y",0).getInt();
			int z = config.get("pos","z",0).getInt();
			
			int dx = config.get("pos","dx",0).getInt();
			int dy = config.get("pos","dy",0).getInt();
			int dz = config.get("pos","dz",0).getInt();
			int dimensionID = config.get("pos","dimensionID",0).getInt();
			
			
			String[] whitelist = config.get("protect","whitelist",new String[] {}).getStringList();
			String[] blacklist = config.get("protect","blacklist",new String[] {}).getStringList();
			
			HashMap<String, Flag> flags = new HashMap<String, Flag>();
		
			for (String key : flagDefault.keySet()) {
				Flag flag = flagDefault.get(key);
				flags.put(key, new Flag(config, flag.getType(), key, flag.getDefaultValue(), flag.getPermLevel(), flag.getDescription()));
			}
			config.save();
			Areas.put(f.getName(), new Area(x,y,z,dx,dy,dz,dimensionID,flags));
		 }
	}
	
	public static void writeProtection(String name, int x, int y, int z, int dx, int dy, int dz, int dimensionID, HashMap<String, Flag> flags) {
		writeProtection(new Area(x, y, z, dx, dy, dz, dimensionID, flags), name);
	}
	
	public static void writeProtection(String name, int x, int y, int z, int dx, int dy, int dz, int dimensionID) {
		writeProtection(new Area(x, y, z, dx, dy, dz, dimensionID, null), name);
	}
	
	public static void writeProtection(Area p, String name) {
		File f = new File(modLogDir + File.separatorChar + name);
		if (f.exists()) {
			f.delete();
		}
		Configuration config = new Configuration(f);
		 config.get("pos","x",p.x);
		 config.get("pos","y",p.y);
		 config.get("pos","z",p.z);
		
		 config.get("pos","dx",p.dx);
		 config.get("pos","dy",p.dy);
		 config.get("pos","dz",p.dz);
		
		 config.get("pos","dimensionID",p.dimensionID);
		 
		 if (p.flags != null) {
			for (String key : flagDefault.keySet()) {	
				if (p.flags.containsKey(key)) {
					Flag flag = p.flags.get(key);
					Flag newFlag = new Flag(config,flag);
					newFlag.setValue(flag.getValue());
					p.flags.put(key, newFlag);
				} else {
					Flag flag = flagDefault.get(key);
					p.flags.put(key, new Flag(config, flag.getType(), key, flag.getDefaultValue(), flag.getDescription()));
				}
			}
		} else {
			HashMap<String, Flag> flags = new HashMap<String, Flag>();
			for (String key : flagDefault.keySet()) {
				Flag flag = flagDefault.get(key);
				flags.put(key, new Flag(config, flag.getType(), key, flag.getDefaultValue(),flag.getDescription()));
			}
			p.flags=flags;
		}
		
		 
		 Areas.put(f.getName(), p);
		 config.save();
	}
	
	public static void deleteProtection(String name) {
		if (Areas.containsKey(name)) {
			Areas.remove(name);
		}
		File f = new File(modLogDir + File.separatorChar + name);
		if (f.exists()) {
			f.delete();
		}
	}
	
	public static Area findShortestAreaIn(World w, BlockPos pos, Area ref) {
		int x = Integer.MIN_VALUE;
		int y = Integer.MIN_VALUE;
		int z = Integer.MIN_VALUE;
		int dx = Integer.MAX_VALUE;
		int dy = Integer.MAX_VALUE;
		int dz = Integer.MAX_VALUE;
		ref = ref != null ? ref : new Area(pos.getX(), pos.getY(), pos.getZ(), pos.getX(), pos.getY(), pos.getZ(), w.provider.getDimension(), null);
		Area zone = null;
		for (String name : AreaManager.Areas.keySet()) {
			Area area = AreaManager.Areas.get(name);
			if (area.isIn(w, pos)) {
				if (area.x>=x && area.y>y && area.z>=z && area.dx<=dx && area.dy<=dy && area.dz<=dz) {
					if (area.x <= ref.x && area.y <= ref.y && area.z <= ref.z && area.dx >= ref.dx && area.dy >= ref.dy && area.dz >= ref.dz) {
						x = area.x;
						y = area.y;
						z = area.z;
						dx = area.dx;
						dy = area.dy;
						dz = area.dz;
						zone=area;
					}
				}
			}
		}
		if (zone != null) {
			return zone;
		}
		return null;
	}
	
	public static Area findShortestAreaIn(World w, Vec3d pos, Area ref) {
		int x = Integer.MIN_VALUE;
		int y = Integer.MIN_VALUE;
		int z = Integer.MIN_VALUE;
		int dx = Integer.MAX_VALUE;
		int dy = Integer.MAX_VALUE;
		int dz = Integer.MAX_VALUE;
		ref = ref != null ? ref : new Area((int)pos.x, (int)pos.y, (int)pos.z, (int)pos.x, (int)pos.y, (int)pos.z, w.provider.getDimension(), null);
		Area zone = null;
		for (String name : AreaManager.Areas.keySet()) {
			Area area = AreaManager.Areas.get(name);
			if (area.isIn(w, pos)) {
				if (area.x>=x && area.y>=y && area.z>=z && area.dx<=dx && area.dy<=dy && area.dz<=dz) {
					if (area.x <= ref.x && area.y <= ref.y && area.z <= ref.z && area.dx >= ref.dx && area.dy >= ref.dy && area.dz >= ref.dz) {
						x = area.x;
						y = area.y;
						z = area.z;
						dx = area.dx;
						dy = area.dy;
						dz = area.dz;
						zone=area;
					}
				}
			}
		}
		if (zone != null) {
			return zone;
		}
		return null;
	}
	
	public static Area findShortestAreaIn(Entity e, Area ref) {
		int x = Integer.MIN_VALUE;
		int y = Integer.MIN_VALUE;
		int z = Integer.MIN_VALUE;
		int dx = Integer.MAX_VALUE;
		int dy = Integer.MAX_VALUE;
		int dz = Integer.MAX_VALUE;
		ref = ref != null ? ref : new Area((int)e.posX, (int)e.posY, (int)e.posZ, (int)e.posX, (int)e.posY, (int)e.posZ, e.dimension, null);
		Area zone = null;
		for (String name : AreaManager.Areas.keySet()) {
			Area area = AreaManager.Areas.get(name);
			if (area.isIn(e)) {
				if (area.x>=x && area.y>=y && area.z>=z && area.dx<=dx && area.dy<=dy && area.dz<=dz) {
					if (area.x <= ref.x && area.y <= ref.y && area.z <= ref.z && area.dx >= ref.dx && area.dy >= ref.dy && area.dz >= ref.dz) {
						x = area.x;
						y = area.y;
						z = area.z;
						dx = area.dx;
						dy = area.dy;
						dz = area.dz;
						zone=area;
					}
				}
			}
		}
		if (zone != null) {
			return zone;
		}
		return null;
	}
	
	public static Area findBiggestAreaIn(World w, BlockPos pos, Area ref) {
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();
		int dx = pos.getX();
		int dy = pos.getY();
		int dz = pos.getZ();
		Area zone = ref != null ? ref : new Area(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, w.provider.getDimension(), null);
		Area res = null;
		for (String name : AreaManager.Areas.keySet()) {
			Area area = AreaManager.Areas.get(name);
			if (area.isIn(w, pos)) {
				if (area.x<=x && area.y<=y && area.z<=z && area.dx>=dx && area.dy>=dy && area.dz>=dz) {
					if (area.x >= zone.x && area.y >= zone.y && area.z >= zone.z && area.dx <= zone.dx && area.dy <= zone.dy && area.dz <= zone.dz) {
						x = area.x;
						y = area.y;
						z = area.z;
						dx = area.dx;
						dy = area.dy;
						dz = area.dz;
						res=area;
					}
				}
			}
		}
		return res;
	}
	
	public static Area findBiggestAreaIn(Entity e, Area ref) {
		int x = (int)e.posX;
		int y = (int)e.posY;
		int z = (int)e.posZ;
		int dx = (int)e.posX;
		int dy = (int)e.posY;
		int dz = (int)e.posZ;
		Area zone = ref != null ? ref : new Area(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, e.dimension, null);
		Area res = null;
		for (String name : AreaManager.Areas.keySet()) {
			Area area = AreaManager.Areas.get(name);
			if (area.isIn(e)) {
				if (area.x<=x && area.y<=y && area.z<=z && area.dx>=dx && area.dy>=dy && area.dz>=dz) {
					if (area.x >= zone.x && area.y >= zone.y && area.z >= zone.z && area.dx <= zone.dx && area.dy <= zone.dy && area.dz <= zone.dz) {
						x = area.x;
						y = area.y;
						z = area.z;
						dx = area.dx;
						dy = area.dy;
						dz = area.dz;
						res=area;
					}
				}
			}
		}
		return res;
	}
	
	public static Area findBiggestAreaIn(World w, Vec3d pos, Area ref) {
		int x = (int)pos.x;
		int y = (int)pos.y;
		int z = (int)pos.z;
		int dx = (int)pos.x;
		int dy = (int)pos.y;
		int dz = (int)pos.z;
		Area zone = ref != null ? ref : new Area(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, w.provider.getDimension(), null);
		Area res = null;
		for (String name : AreaManager.Areas.keySet()) {
			Area area = AreaManager.Areas.get(name);
			if (area.isIn(w, pos)) {
				if (area.x<=x && area.y<=y && area.z<=z && area.dx>=dx && area.dy>=dy && area.dz>=dz) {
					if (area.x >= zone.x && area.y >= zone.y && area.z >= zone.z && area.dx <= zone.dx && area.dy <= zone.dy && area.dz <= zone.dz) {
						x = area.x;
						y = area.y;
						z = area.z;
						dx = area.dx;
						dy = area.dy;
						dz = area.dz;
						res=area;
					}
				}
			}
		}
		return res;
	}
	
	public static String getAreaName(Entity e) {
		int x = Integer.MIN_VALUE;
		int y = Integer.MIN_VALUE;
		int z = Integer.MIN_VALUE;
		int dx = Integer.MAX_VALUE;
		int dy = Integer.MAX_VALUE;
		int dz = Integer.MAX_VALUE;
		String zone = "";
		for (String name : AreaManager.Areas.keySet()) {
			Area area = AreaManager.Areas.get(name);
			if (area.isIn(e)) {
				if (area.x>=x && area.y>=y && area.z>=z && area.dx<=dx && area.dy<=dy && area.dz<=dz) {
					x = area.x;
					y = area.y;
					z = area.z;
					dx = area.dx;
					dy = area.dy;
					dz = area.dz;
					zone=name;
				}
			}
		}
		if (zone != null) {
			return zone;
		}
		return "";
	}
	
	public static String getAreaName(World w, BlockPos pos) {
		int x = Integer.MIN_VALUE;
		int y = Integer.MIN_VALUE;
		int z = Integer.MIN_VALUE;
		int dx = Integer.MAX_VALUE;
		int dy = Integer.MAX_VALUE;
		int dz = Integer.MAX_VALUE;
		String zone = "";
		for (String name : AreaManager.Areas.keySet()) {
			Area area = AreaManager.Areas.get(name);
			if (area.isIn(w, pos)) {
				if (area.x>=x && area.y>=y && area.z>=z && area.dx<=dx && area.dy<=dy && area.dz<=dz) {
					x = area.x;
					y = area.y;
					z = area.z;
					dx = area.dx;
					dy = area.dy;
					dz = area.dz;
					zone=name;
				}
			}
		}
		if (zone != null) {
			return zone;
		}
		return "";
	}
	
	public static String getAreaName(World w, Vec3d pos) {
		int x = Integer.MIN_VALUE;
		int y = Integer.MIN_VALUE;
		int z = Integer.MIN_VALUE;
		int dx = Integer.MAX_VALUE;
		int dy = Integer.MAX_VALUE;
		int dz = Integer.MAX_VALUE;
		String zone = "";
		for (String name : AreaManager.Areas.keySet()) {
			Area area = AreaManager.Areas.get(name);
			if (area.isIn(w, pos)) {
				if (area.x>=x && area.y>=y && area.z>=z && area.dx<=dx && area.dy<=dy && area.dz<=dz) {
					x = area.x;
					y = area.y;
					z = area.z;
					dx = area.dx;
					dy = area.dy;
					dz = area.dz;
					zone=name;
				}
			}
		}
		if (zone != null) {
			return zone;
		}
		return "";
	}
	
	public static Area getArea(World w, BlockPos pos) {
		int x = Integer.MIN_VALUE;
		int y = Integer.MIN_VALUE;
		int z = Integer.MIN_VALUE;
		int dx = Integer.MAX_VALUE;
		int dy = Integer.MAX_VALUE;
		int dz = Integer.MAX_VALUE;
		Area zone = null;
		for (String name : AreaManager.Areas.keySet()) {
			Area area = AreaManager.Areas.get(name);
			if (area.isIn(w, pos)) {
				if (area.x>=x && area.y>=y && area.z>=z && area.dx<=dx && area.dy<=dy && area.dz<=dz) {
					x = area.x;
					y = area.y;
					z = area.z;
					dx = area.dx;
					dy = area.dy;
					dz = area.dz;
					zone=area;
				}
			}
		}
		if (zone != null) {
			return zone;
		}
		return null;
	}
	
	public static Area getArea(World w, Vec3d pos) {
		int x = Integer.MIN_VALUE;
		int y = Integer.MIN_VALUE;
		int z = Integer.MIN_VALUE;
		int dx = Integer.MAX_VALUE;
		int dy = Integer.MAX_VALUE;
		int dz = Integer.MAX_VALUE;
		Area zone = null;
		for (String name : AreaManager.Areas.keySet()) {
			Area area = AreaManager.Areas.get(name);
			if (area.isIn(w, pos)) {
				if (area.x>=x && area.y>=y && area.z>=z && area.dx<=dx && area.dy<=dy && area.dz<=dz) {
					x = area.x;
					y = area.y;
					z = area.z;
					dx = area.dx;
					dy = area.dy;
					dz = area.dz;
					zone=area;
				}
			}
		}
		if (zone != null) {
			return zone;
		}
		return null;
	}
	
	
	
	public static Area getArea(Entity e) {
		if (e != null) {
			int x = Integer.MIN_VALUE;
			int y = Integer.MIN_VALUE;
			int z = Integer.MIN_VALUE;
			int dx = Integer.MAX_VALUE;
			int dy = Integer.MAX_VALUE;
			int dz = Integer.MAX_VALUE;
			Area zone = null;
			for (String name : AreaManager.Areas.keySet()) {
				Area area = AreaManager.Areas.get(name);
				if (area.isIn(e)) {
					if (area.x>=x && area.y>=y && area.z>=z && area.dx<=dx && area.dy<=dy && area.dz<=dz) {
						x = area.x;
						y = area.y;
						z = area.z;
						dx = area.dx;
						dy = area.dy;
						dz = area.dz;
						zone=area;
					}
				}
			}
			return zone;
		} else {
			return null;
		}
	}
	
	public static Flag getFlag(Entity e, String flagName) {
		Area zone = getArea(e);
		if (zone != null) {
			if (zone.flags.containsKey(flagName)) {
				return zone.flags.get(flagName);
			}
		}
		return Flag.NULL;
	}
	
	public static Flag getFlag(Area zone, String flagName) {
		if (zone != null) {
			if (zone.flags.containsKey(flagName)) {
				return zone.flags.get(flagName);
			}
		}
		return Flag.NULL;
	}
	
	public static boolean isOwner(Area zone, Entity e) {
		if (zone != null) {
			if (zone.flags.containsKey("owner")) {
				return zone.flags.get("owner").isEqual(e.getName());
			}
		}
		return false;
	}
	
	public static boolean isOwner(Entity e) {
		return isOwner(getArea(e), e);
	}
	
	public static boolean isMember(Area zone, Entity e) {
		if (zone != null) {
			if (zone.flags.containsKey("members")) {
				return zone.flags.get("members").contains(e.getName());
			}
		}
		return false;
	}
	
	public static boolean isMember(Entity e) {
		return isMember(getArea(e), e);
	}
	
	public static void processCurrency(String str) {
		String[] data = str.split("=");
		String name;
		double value = 0D;
		int meta = 0;
		if (data.length >= 2){
			try {
				value = Double.valueOf(data[1]).doubleValue();
				name = data[0];
			} catch (Exception e) {
				name=str;
			}
		} else {
			name=str;
		}
		
		String[] ndata = name.split("[/]");
		String id;
		if (data.length == 2) {
			try {
				id = ndata[0];
				meta = Integer.valueOf(ndata[1]);
			} catch (Exception e) {
				id = data[0];
				meta = 0;
			}
		} else {
			id = name;
		}
		currency.put(id+"/"+meta, value);
	}
	
	public static void processCurrencies(ArrayList<String> lst) {
		currency.clear();
		for (String cmd :  lst) {
			processCurrency(cmd);
		}
	}
}
