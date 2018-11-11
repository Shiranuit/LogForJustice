package fr.Shiranuit.LogForJustice.Commands;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.Nullable;

import fr.Shiranuit.LogForJustice.Manager.PlayerManager;
import fr.Shiranuit.LogForJustice.PlayerData.PlayerData;
import fr.Shiranuit.LogForJustice.PlayerData.TempbanData;
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

public class UntempbanCommand extends CommandBase{

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "untempban";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		// TODO Auto-generated method stub
		return "/untempban <playername>";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length >= 1) {
			if (PlayerManager.hasData(args[0],true)) {
				PlayerData data = PlayerManager.getPlayerData(args[0],true);
				data.tempban = null;
				data.save();
				ChatUtil.sendMessage(sender, "Player '"+args[0]+"' has been unban", ChatType.LogForJustice);
			} else {
				ChatUtil.sendMessage(sender, "Unkown player to unban", ChatType.LogForJustice);	
			}
		} else {
			ChatUtil.sendMessage(sender, "Usage : /untempban <playername>", ChatType.LogForJustice);
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
