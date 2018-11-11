package fr.Shiranuit.LogForJustice.Commands;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.Nullable;

import fr.Shiranuit.LogForJustice.Main;
import fr.Shiranuit.LogForJustice.Manager.PlayerManager;
import fr.Shiranuit.LogForJustice.PlayerData.PlayerData;
import fr.Shiranuit.LogForJustice.Utils.Time;
import fr.Shiranuit.LogForJustice.Utils.Util;
import fr.Shiranuit.LogForJustice.Utils.Chat.ChatType;
import fr.Shiranuit.LogForJustice.Utils.Chat.ChatUtil;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class SeenCommand extends CommandBase {

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "seen";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		// TODO Auto-generated method stub
		return "/seen <playername>";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length > 0) {
			EntityPlayer player = Util.playerByName(args[0]);
			if (player != null) {
				sender.sendMessage(new TextComponentString(""));
				sender.sendMessage(new TextComponentString(TextFormatting.GOLD+"==================[ Last Seen Infos ]=================="));
				sender.sendMessage(new TextComponentString(TextFormatting.GOLD+"Name: "+TextFormatting.RED+args[0]));
				sender.sendMessage(new TextComponentString(TextFormatting.GOLD+"Last Seen: "+TextFormatting.RED+"["+Util.getDate()+"]"+TextFormatting.GREEN+" (0 days, 0 hours, 0 minutes, 0 seconds"));
				sender.sendMessage(new TextComponentString(TextFormatting.GOLD+"Last Position: "+TextFormatting.LIGHT_PURPLE+Util.bposString(player.getPosition())));
				sender.sendMessage(new TextComponentString(TextFormatting.GOLD+"Last Bed Position: "+TextFormatting.LIGHT_PURPLE+Util.bposString(player.getBedLocation())));
				sender.sendMessage(new TextComponentString(TextFormatting.GOLD+"Tempban: "+TextFormatting.DARK_RED+"false"));
				sender.sendMessage(new TextComponentString(TextFormatting.GOLD+"Tempban Time: "+TextFormatting.RED+"0 days, 0 hours, 0 minutes, 0 seconds"));
			} else {
				PlayerData data = PlayerManager.getPlayerData(args[0], true);
				if (data != null) {
					sender.sendMessage(new TextComponentString(""));
					sender.sendMessage(new TextComponentString(TextFormatting.GOLD+"==================[ Last Seen Infos ]=================="));
					long now = new Date().getTime();
					Time time = new Time(now - data.lastSeen.getTime());
					sender.sendMessage(new TextComponentString(TextFormatting.GOLD+"Name: "+TextFormatting.RED+args[0]));
					sender.sendMessage(new TextComponentString(TextFormatting.GOLD+"Last Seen: "+TextFormatting.RED+"["+Util.getDate(data.lastSeen)+"] "+TextFormatting.GREEN+"("+time.getDays()+" days, "+time.getHours()+" hours, "+time.getMinutes()+" minutes, "+time.getSeconds()+" seconds"));
					sender.sendMessage(new TextComponentString(TextFormatting.GOLD+"Last Position: "+TextFormatting.LIGHT_PURPLE+Util.bposString(data.lastPos)));
					sender.sendMessage(new TextComponentString(TextFormatting.GOLD+"Last Bed Position: "+TextFormatting.LIGHT_PURPLE+Util.bposString(data.bedPos)));
					if (data.tempban != null) {
						now = new Date().getTime();
						long unban = data.tempban.time.getTime();
						long remaining = unban - now;
						Time unbantime = new Time(remaining);
						if (remaining > 0) {
							sender.sendMessage(new TextComponentString(TextFormatting.GOLD+"Tempban: "+TextFormatting.DARK_GREEN+"true"));
						} else {
							sender.sendMessage(new TextComponentString(TextFormatting.GOLD+"Tempban: "+TextFormatting.DARK_RED+"false"));
						}
						sender.sendMessage(new TextComponentString(TextFormatting.GOLD+"Tempban Time: "+TextFormatting.RED+unbantime.getDays()+" days, "+unbantime.getHours()+" hours, "+unbantime.getMinutes()+" minutes, "+unbantime.getSeconds()+" seconds"));
					} else {
						sender.sendMessage(new TextComponentString(TextFormatting.GOLD+"Tempban: "+TextFormatting.DARK_RED+"false"));
						sender.sendMessage(new TextComponentString(TextFormatting.GOLD+"Tempban Time: "+TextFormatting.RED+"0 days, 0 hours, 0 minutes, 0 seconds"));
					}
				} else {
					ChatUtil.sendMessage(sender, "Unknown player", ChatType.PlayerInfo);
				}
			}
		} else {
			ChatUtil.sendMessage(sender, "Usage : /seen <playername>", ChatType.LogForJustice);
		}
	}
	
	@Override
	public int getRequiredPermissionLevel() {
		return 2;
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
