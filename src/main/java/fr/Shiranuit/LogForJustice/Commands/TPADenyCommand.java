package fr.Shiranuit.LogForJustice.Commands;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.Nullable;

import fr.Shiranuit.LogForJustice.Dimension.TeleporterDim;
import fr.Shiranuit.LogForJustice.Manager.PlayerManager;
import fr.Shiranuit.LogForJustice.PlayerData.Position;
import fr.Shiranuit.LogForJustice.Utils.Util;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class TPADenyCommand extends CommandBase {

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "tpdeny";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		// TODO Auto-generated method stub
		return "/tpdeny <player>";
	}
	
	@Override
	public List<String> getAliases()
    {
        return Arrays.<String>asList("tpno");
    }
	
	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}
	
	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
			@Nullable BlockPos targetPos) {
		if (args.length == 1) {
			return getListOfStringsMatchingLastWord(args, Util.playersList());
		} else {
			return Collections.<String>emptyList();
		}
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length >= 1) {
			EntityPlayer player = Util.playerByName(args[0]);
			if (player != null) {
				sender.sendMessage(new TextComponentString(TextFormatting.RED+"Teleportation request from "+player.getName()+" denied"));
				PlayerManager.TPARequestRemove(sender.getName(), player.getName());
			} else {
				sender.sendMessage(new TextComponentString(TextFormatting.RED+"Unknown player '"+args[0]+"'"));
			}
		} else {
			throw new WrongUsageException("/tpa <player>");
		}
	}

}
