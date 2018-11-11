package fr.Shiranuit.LogForJustice.Manager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import fr.Shiranuit.LogForJustice.Utils.Util;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class CommandSpyManager {
	public static String modLogDir;
	public static HashMap<EntityPlayer, Boolean> spy = new HashMap<EntityPlayer, Boolean>();
	public static FileWriter file;
	
	public static void add(ICommand cmd, String[] param, ICommandSender sender, boolean success) {
		String maDate = Util.getDate();
	    BlockPos pos = new BlockPos(sender.getPosition().getX(), sender.getPosition().getY(), sender.getPosition().getZ());
	    String command = "";
	    if (success) {
	    	command = "["+maDate+"] [X="+pos.getX()+",Y="+pos.getY()+",Z="+pos.getZ()+"] [SUCCESS] <"+sender.getName()+"> /"+cmd.getName()+" "+String.join(" ", param);
	    } else {
	    	command = "["+maDate+"] [X="+pos.getX()+",Y="+pos.getY()+",Z="+pos.getZ()+"] [FAILURE] <"+sender.getName()+"> /"+cmd.getName()+" "+String.join(" ", param);
	    }
	    try {
	    	file.append(command+"\n");
			file.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void spyMode(EntityPlayer player, boolean state) {
		spy.put(player, state);
	}
	
	public static void Setup() {
		String log = modLogDir + File.separatorChar + Util.getDate() + ".log";
		File dir = new File(modLogDir);
		if (!(dir.exists() && dir.isDirectory())) {
			dir.mkdir();
		}
	
		try {
			File f = new File(log);
			if (!f.exists()) {
				f.createNewFile();
			}
			file = new FileWriter(log,true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void sendCommand(ICommand command, ICommandSender sender, String[] parameters, boolean success) {
		String cmd = "<"+sender.getName()+"> /"+command.getName() + " " + String.join(" ", parameters);
		for (EntityPlayer pl : CommandSpyManager.spy.keySet()) {
			if (!pl.getDisplayName().equals(sender.getName())) {
				if (CommandSpyManager.spy.get(pl).booleanValue()) {
					if (success) {
						TextComponentString txt = new TextComponentString(TextFormatting.GOLD+"[CMD] " + TextFormatting.GREEN + "SUCCESS"+TextFormatting.GOLD+" : "+cmd);
						pl.sendMessage(txt);
					} else {
						TextComponentString txt = new TextComponentString(TextFormatting.GOLD+"[CMD] " + TextFormatting.RED + "FAILURE"+TextFormatting.GOLD+" : "+cmd);
						pl.sendMessage(txt);
					}
				}
			}
		}
	}
}
