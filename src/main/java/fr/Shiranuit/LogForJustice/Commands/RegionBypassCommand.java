package fr.Shiranuit.LogForJustice.Commands;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.Nullable;

import fr.Shiranuit.LogForJustice.Manager.AreaManager;
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

public class RegionBypassCommand extends CommandBase{

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "bypass";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		// TODO Auto-generated method stub
		return "/bypass";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		AreaManager.setBypass(sender.getName(), !AreaManager.hasBypass(sender.getName()));
		ChatUtil.sendMessage(sender, "Bypass : "+String.valueOf(AreaManager.hasBypass(sender.getName())),ChatType.Region);
	}
	
	@Override
	public int getRequiredPermissionLevel() {
		return 4;
	}

}

