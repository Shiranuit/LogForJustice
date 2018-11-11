package fr.Shiranuit.LogForJustice.Manager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import fr.Shiranuit.LogForJustice.Main;
import fr.Shiranuit.LogForJustice.Crypto.XCiffer;
import fr.Shiranuit.LogForJustice.Utils.ClearString;
import fr.Shiranuit.LogForJustice.Utils.Util;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class MessageSpyManager {
	public static String modLogDir;
	public static HashMap<EntityPlayer, Boolean> spy = new HashMap<EntityPlayer, Boolean>();
	public static FileWriter file;
	public static XCiffer xciffer;
	
	public static void add(ICommand cmd, String[] param, ICommandSender sender) {
		String maDate = Util.getDate();
	    BlockPos pos = new BlockPos(sender.getPosition().getX(), sender.getPosition().getX(), sender.getPosition().getZ());
	    String command = "["+maDate+"] [X="+pos.getX()+",Y="+pos.getY()+",Z="+pos.getZ()+"] <"+sender.getName()+"> /"+cmd.getName()+" "+String.join(" ", param);
	    
	    try {
	    	String c = ClearString.format(xciffer.ciffer(command));
	    	file.append(c+"\n");
			file.flush();
		} catch (IOException e) {
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
		
		xciffer = new XCiffer(Main.cifferOffset);
		
		try {
			File f = new File(log);
			if (!f.exists()) {
				f.createNewFile();
			}
			file = new FileWriter(log,true);
			file.append(Main.cifferOffsetHex+"\n");
			file.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void sendWhisper(ICommand command, ICommandSender sender, String[] parameters) {
		String cmd = "<"+sender.getName()+"> /"+command.getName() + " " + String.join(" ", parameters);
		for (EntityPlayer pl : MessageSpyManager.spy.keySet()) {
			if (!pl.getDisplayName().equals(sender.getName())) {
				if (MessageSpyManager.spy.get(pl).booleanValue()) {
					TextComponentString txt = new TextComponentString(TextFormatting.GOLD+"[WHISPER] "+cmd);
					pl.sendMessage(txt);
				}
			}
		}
	}
}
