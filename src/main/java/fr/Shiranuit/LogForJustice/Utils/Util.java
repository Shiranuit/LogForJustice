package fr.Shiranuit.LogForJustice.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nonnull;

import fr.Shiranuit.LogForJustice.Main;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.server.management.UserListOps;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.Constants.NBT;

public class Util {

	public static String translate(String txt) {
		return I18n.format("luapolisessentials."+txt);
	}
	
	public static String BlockPosToString(BlockPos p) {
		return p.getX()+"|"+p.getY()+"|"+p.getZ();
	}
	
	public static BlockPos BlockPosFromString(String str) {
		String[] data = str.split("[|]");
		if (data.length == 3) {
			try {
				return new BlockPos(Integer.valueOf(data[0]),Integer.valueOf(data[1]),Integer.valueOf(data[2]));
			} catch (Exception e) {
				return null;
			}
		}
		return null;
	}
	
	public static List<Entity> getEntities(Class<? extends Entity> type, World w, int x, int y, int z,int radius) {
		return w.getEntitiesWithinAABB(type, new AxisAlignedBB(x,y,z,x+1,y+1,z+1).grow(radius));
	}
	
	public static boolean findVanishedPlayer(List<Entity> lst) {
		for (Entity e : lst) {
			if (e instanceof EntityPlayer) {
				if (Main.VanishedPlayers.contains(e.getName())) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static String nameFromItemStack(ItemStack stack) {
		Item ihand = stack.getItem();
		String name = ihand.getRegistryName().getResourceDomain() + ":" + ihand.getRegistryName().getResourcePath();
		return name;
	}
	
	public static EntityPlayerMP createEntityPlayerMp(EntityPlayer p) {
		return new EntityPlayerMP(Main.mcserver, (WorldServer)Main.mcserver.getEntityWorld(), p.getGameProfile(), new PlayerInteractionManager(p.world));
	}
	
	public static boolean isOnline(String name) {
		return Main.mcserver.getPlayerList().getPlayerByUsername(name) != null;
	}
	
	public static EntityPlayer playerByName(String name) {
		return Main.mcserver.getPlayerList().getPlayerByUsername(name);
	}
	
	public static boolean isConnected(String name) {
		return Main.mcserver.getPlayerList().getPlayerByUsername(name) != null ? true : false;
	}
	
	  public static boolean isOp(String player) {
		  	if (Main.mcserver.isSinglePlayer()) {
		  		return true;
		  	}
			UserListOps op = Main.mcserver.getPlayerList().getOppedPlayers();
			if (op.getGameProfileFromName(player) != null) {
				return true;
			}
			return false;
	   }
	  
	  public static boolean isOp(EntityPlayer player) {
		  	if (Main.mcserver.isSinglePlayer()) {
		  		return true;
		  	}
			UserListOps op = Main.mcserver.getPlayerList().getOppedPlayers();
			if (op.getEntry(player.getGameProfile()) != null) {
				return true;
			}
			return false;
	   }
	 
	
	   public static String getDate()
	   {
		   return getDate(new Date());
	   }
	   
	   public static String getDate(Date date)
	   {
		  SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		  return formater.format(date);
	   }
	   
	   public static ArrayList<File> GetFiles(File dir) {
			  ArrayList<File> folder = new ArrayList<File>();
			  ArrayList<File> data = new ArrayList<File>();
			  folder.add(dir);
			  while (folder.size() > 0)
			  {
				  File[] f = folder.get(0).listFiles();
				  for (int i=0;i<f.length;i++) {
					  if (f[i].isDirectory()) {
						  folder.add(f[i]);
					  } else {
						data.add(f[i]);
					  }
				  }
				  folder.remove(0);
			  }
			  return data;
	   }
	   
	   public static ArrayList<File> GetFiles(File dir, String ext) {
			  ArrayList<File> folder = new ArrayList<File>();
			  ArrayList<File> data = new ArrayList<File>();
			  folder.add(dir);
			  while (folder.size() > 0)
			  {
				  File[] f = folder.get(0).listFiles();
				  for (File file : f) {
					  if (file.isDirectory()) {
						  folder.add(file);
					  } else {
						  if (file.getName().endsWith(ext)){
							  data.add(file);
						  }
					  }
				  }
				  folder.remove(0);
			  }
			  return data;
	   }
	   
	   public static ArrayList<String> readFileLines(File fin) throws IOException {
			  if (fin.exists() && !fin.isDirectory()) {
				  ArrayList<String> lines = new ArrayList<String>();
				FileInputStream fis = new FileInputStream(fin);
				BufferedReader br = new BufferedReader(new InputStreamReader(fis));
				String line = null;
				while ((line = br.readLine()) != null) {
					line = line.replace("\n", "");
					lines.add(line);
				}
				br.close();
				return lines;
			  }
			  return null;
		}
	   
	   public static Date parseDate(String date) throws Exception {
			String dateData[] = date.split("-");
			if (dateData.length >= 6) {
				try {
					int year = Integer.valueOf(dateData[0]);
					int month = Integer.valueOf(dateData[1]);
					int day = Integer.valueOf(dateData[2]);
					int hour = Integer.valueOf(dateData[3]);
					int minute = Integer.valueOf(dateData[4]);
					int second = Integer.valueOf(dateData[5]);
					Date d = new Date(year, month, day, hour, minute, second);
					return d;
				}catch (Exception e) {
					
				}
			} else {
				throw new Exception("Formatting Error : Date must be formatted like this (year-month-day-hour-minute-second) example : "+getDate());
			}
			return null;
	   }
	   
	   public static ArrayList<String> vanishedList() {
		   return Main.VanishedPlayers;
	   }
	   
	   public static String[] playersListWithVanished() {
		   return Main.mcserver.getOnlinePlayerNames();
	   }
	   
	   public static ArrayList<String> playersList() {
		   ArrayList<String> notVanished = new ArrayList<String>();
		   for (String pname : playersListWithVanished()) {
			   if (!Main.VanishedPlayers.contains(pname)) {
				   notVanished.add(pname);
			   }
		   }
		   return notVanished;
	   }
	   
	   public static long getFolderSize(File folder) {
		    long length = 0;
		    File[] files = folder.listFiles();
		 
		    int count = files.length;
		 
		    for (int i = 0; i < count; i++) {
		        if (files[i].isFile()) {
		            length += files[i].length();
		        }
		        else {
		            length += getFolderSize(files[i]);
		        }
		    }
		    return length;
		}
	   
	   public static int maxHeight(World w, int x, int z) {
			int a = w.getTopSolidOrLiquidBlock(new BlockPos(x,256,z)).getY();
			int b = w.getPrecipitationHeight(new BlockPos(x,256,z)).getY();
			int c = w.getHeight(new BlockPos(x,256,z)).getY();
		   return Math.min(Math.max(Math.max(a, b),c)+2,255);
	   }
	   
	   public static String bposString(BlockPos pos) {
		   if (pos != null) {
			   return "[X:"+pos.getX()+" Y:"+pos.getY()+" Z:"+pos.getZ()+"]";
		   } else {
			 return "[]";  
		   }
	   }
}
