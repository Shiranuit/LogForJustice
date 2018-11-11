package fr.Shiranuit.LogForJustice.Commands;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.Nullable;

import fr.Shiranuit.LogForJustice.Dimension.TeleporterDim;
import fr.Shiranuit.LogForJustice.Manager.PlayerManager;
import fr.Shiranuit.LogForJustice.Manager.RankManager;
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

public class TPAcceptCommand extends CommandBase {

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "tpaccept";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		// TODO Auto-generated method stub
		return "/tpaccept <player>";
	}
	
	@Override
	public List<String> getAliases()
    {
        return Arrays.<String>asList("tpyes");
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
				Date date = PlayerManager.getTPARequest(sender.getName(), player.getName());
				if (date != null) {
					if (new Date().before(date)) {
						player.sendMessage(new TextComponentString(TextFormatting.RED+"Teleportation to "+sender.getName()));
						sender.sendMessage(new TextComponentString(TextFormatting.RED+"Teleportation request from "+player.getName()+" accepted"));
						Position pos = new Position(sender.getCommandSenderEntity());
						
						TeleporterDim tp = new TeleporterDim(pos);
						if (RankManager.hasPermission("back", "tp", "", -1, player)) {
							PlayerManager.setPrevious(player, pos);
						}
						
						tp.teleport(player);
						
						PlayerManager.TPARequestRemove(sender.getName(), player.getName());
					} else {
						sender.sendMessage(new TextComponentString(TextFormatting.RED+"The teleportation request from "+player.getName()+" as expired"));
						player.sendMessage(new TextComponentString(TextFormatting.RED+"The teleportation request to "+sender.getName()+" as expired"));
					}
				} else {
					sender.sendMessage(new TextComponentString(TextFormatting.RED+"There is no teleportation request from "+player.getName()));
				}
			} else {
				sender.sendMessage(new TextComponentString(TextFormatting.RED+"Unknown player '"+args[0]+"'"));
			}
		} else {
			throw new WrongUsageException("/tpa <player>");
		}
	}

}
