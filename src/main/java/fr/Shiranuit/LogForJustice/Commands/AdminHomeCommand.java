package fr.Shiranuit.LogForJustice.Commands;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import fr.Shiranuit.LogForJustice.Dimension.TeleporterDim;
import fr.Shiranuit.LogForJustice.Manager.HomeManager;
import fr.Shiranuit.LogForJustice.Manager.PlayerManager;
import fr.Shiranuit.LogForJustice.PlayerData.Position;
import fr.Shiranuit.LogForJustice.Utils.Chat.ChatType;
import fr.Shiranuit.LogForJustice.Utils.Chat.ChatUtil;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class AdminHomeCommand extends CommandBase{

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "adminHome";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		// TODO Auto-generated method stub
		return "/adminHome help";
	}
	
	@Override
	public int getRequiredPermissionLevel() {
		return 4;
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
			@Nullable BlockPos targetPos) {
		if (sender.getCommandSenderEntity() instanceof EntityPlayer) {
			if (args.length == 1) {
				return getListOfStringsMatchingLastWord(args, "list","tp", "reload","help");
			} else if (args.length == 2) {
				if (args[0].equals("tp") || args[0].equals("list")) {
					return getListOfStringsMatchingLastWord(args, PlayerManager.offnames);
				}
			} else if (args.length == 3) {
				if (args[0].equals("tp")) {
					return getListOfStringsMatchingLastWord(args, HomeManager.listHomes(args[1]));
				}
			}
		}
		return Collections.<String>emptyList();
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length > 0) {
			if (args[0].equals("list")) {
				if (args.length >= 2) {
					sender.sendMessage(new TextComponentString(TextFormatting.RED+args[1]+"'s home list : ["+String.join(",", HomeManager.listHomes(args[1]))+"]"));
				} else {
					ChatUtil.sendMessage(sender, "Usage : /adminHome list <playername>", ChatType.LogForJustice);
				}
			} else if(args[0].equals("tp")) {
				if (args.length >= 3) {
					if (sender.getCommandSenderEntity() instanceof EntityPlayer) {
						EntityPlayer player = (EntityPlayer)sender.getCommandSenderEntity();
						Position pos = HomeManager.getHome(args[1], args[2]);
						if (pos != null) {
							Position previous = new Position(player);
							TeleporterDim tp = new TeleporterDim(pos);
							tp.teleport(player);
							PlayerManager.setPrevious(player, previous);
							sender.sendMessage(new TextComponentString(TextFormatting.RED+"Teleportation to "+args[1]+"'s home '"+args[2]+"'"));
						} else {
							sender.sendMessage(new TextComponentString(TextFormatting.RED+"Unknown home '"+args[2]+"' for "+args[1]));
						}
					}
				} else {
					ChatUtil.sendMessage(sender, "Usage : /adminHome tp <playername> <homeName>", ChatType.LogForJustice);
				}
			} else if(args[0].equals("reload")) {
				HomeManager.load();
				ChatUtil.sendMessage(sender, "Homes reloaded", ChatType.LogForJustice);
			} else if (args[0].equals("help")) {
				sender.sendMessage(new TextComponentString(TextFormatting.RED+"/adminHome list <playername>"));
				sender.sendMessage(new TextComponentString(TextFormatting.RED+"/adminHome tp <playername> <homeName>"));
				sender.sendMessage(new TextComponentString(TextFormatting.RED+"/adminHome reload"));
				sender.sendMessage(new TextComponentString(TextFormatting.RED+"/adminHome help"));
			} else {
				throw new WrongUsageException(this.getUsage(null),new Object[0]);
			}
		} else {
			throw new WrongUsageException(this.getUsage(null),new Object[0]);
		}
	}

}
