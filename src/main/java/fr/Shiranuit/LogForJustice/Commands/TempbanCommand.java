package fr.Shiranuit.LogForJustice.Commands;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.Nullable;

import fr.Shiranuit.LogForJustice.Manager.PlayerManager;
import fr.Shiranuit.LogForJustice.PlayerData.PlayerData;
import fr.Shiranuit.LogForJustice.PlayerData.TempbanData;
import fr.Shiranuit.LogForJustice.Utils.ObjectUtil;
import fr.Shiranuit.LogForJustice.Utils.Time;
import fr.Shiranuit.LogForJustice.Utils.Util;
import fr.Shiranuit.LogForJustice.Utils.Chat.ChatType;
import fr.Shiranuit.LogForJustice.Utils.Chat.ChatUtil;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class TempbanCommand extends CommandBase {
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "tempban";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		// TODO Auto-generated method stub
		return "/tempban <playername> <time...>";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length > 1) {
			EntityPlayer player = Util.playerByName(args[0]);
			if (PlayerManager.hasData(args[0], true)) {
				PlayerData data = PlayerManager.getPlayerData(args[0], true);
				long _time = 0;
				for (int i=1; i<args.length; i++) {
					if (args[i].endsWith("d")) {
						_time += ObjectUtil.Long(args[i].replace("d", ""))*86400000;
					} else if (args[i].endsWith("h")) {
						_time += ObjectUtil.Long(args[i].replace("h", ""))*3600000;
					} else if (args[i].endsWith("m")) {
						_time += ObjectUtil.Long(args[i].replace("m", ""))*60000;
					} else if (args[i].endsWith("s")) {
						_time += ObjectUtil.Long(args[i].replace("s", ""))*1000;
					}
				}
				
				long now = new Date().getTime();
				
				long unbantime = now+_time;
				
				data.tempban = new TempbanData(new Date(unbantime));
				data.save();
				Time time = new Time(_time);
				ChatUtil.sendMessage(sender, "Player '"+args[0]+"' has been tempban for "+TextFormatting.GREEN+time.getDays()+" days, "+time.getHours()+" hours, "+time.getMinutes()+" minutes, "+time.getSeconds()+" seconds", ChatType.LogForJustice);
				if (player != null) {
					EntityPlayerMP playermp = ((EntityPlayerMP)player);
					playermp.connection.disconnect(new TextComponentString("You have been banned for "+time.getDays()+" days, "+time.getHours()+" hours, "+time.getMinutes()+" minutes, "+time.getSeconds()+" seconds"));
				}
				
			} else {
				ChatUtil.sendMessage(sender, "Unkown player to tempban", ChatType.LogForJustice);	
			}
		} else {
			ChatUtil.sendMessage(sender, "Usage : /tempban <playername> <time...>", ChatType.LogForJustice);
			ChatUtil.sendMessage(sender, "Example : /tempban test 3d 1h 15m 30s", ChatType.LogForJustice);
			ChatUtil.sendMessage(sender, "This will ban the player named 'test' for 3days, 1hour, 15minutes and 30seconds", ChatType.LogForJustice);
		}
	}
	
	@Override
	public int getRequiredPermissionLevel() {
		return 4;
	}
	
	@Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
		if (args.length ==  1) {
			return getListOfStringsMatchingLastWord(args, PlayerManager.offnames);
		} else {
			return Collections.<String>emptyList();
		}
    }


}
