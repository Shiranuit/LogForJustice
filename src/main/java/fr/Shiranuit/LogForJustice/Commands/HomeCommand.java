package fr.Shiranuit.LogForJustice.Commands;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import fr.Shiranuit.LogForJustice.Dimension.TeleporterDim;
import fr.Shiranuit.LogForJustice.Manager.HomeManager;
import fr.Shiranuit.LogForJustice.Manager.PlayerManager;
import fr.Shiranuit.LogForJustice.Manager.RankManager;
import fr.Shiranuit.LogForJustice.PlayerData.Position;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class HomeCommand extends CommandBase{

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "home";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		// TODO Auto-generated method stub
		return "/home <name>";
	}
	
	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
			@Nullable BlockPos targetPos) {
		if (args.length == 1) {
			if (sender.getCommandSenderEntity() instanceof EntityPlayer) {
				return getListOfStringsMatchingLastWord(args, HomeManager.listHomes((EntityPlayer)sender.getCommandSenderEntity()));
			} else {
				return Collections.<String>emptyList();
			}
		} else {
			return Collections.<String>emptyList();
		}
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length >= 1) {
			if (sender.getCommandSenderEntity() instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer)sender.getCommandSenderEntity();
				if (args[0].equals("list")) {
					sender.sendMessage(new TextComponentString(TextFormatting.RED+"Home list : ["+String.join(",", HomeManager.listHomes(player))+"]"));
				} else {
					Position pos = HomeManager.getHome(player, args[0]);
					if (pos != null) {
						Position previous = new Position(player);
						TeleporterDim tp = new TeleporterDim(pos);
						tp.teleport(player);
						if (RankManager.hasPermission("back", "tp", "", -1, player)) {
							PlayerManager.setPrevious(player, previous);
						}
						sender.sendMessage(new TextComponentString(TextFormatting.RED+"Teleportation to home '"+args[0]+"'"));
					} else {
						sender.sendMessage(new TextComponentString(TextFormatting.RED+"Unknown home '"+args[0]+"'"));
					}
				}
			}
		} else {
			if (sender.getCommandSenderEntity() instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer)sender.getCommandSenderEntity();
				Position pos = HomeManager.getHome(player, "home");
				if (pos != null) {
					Position previous = new Position(player);
					TeleporterDim tp = new TeleporterDim(pos);
					tp.teleport(player);
					if (RankManager.hasPermission("back", "tp", "", -1, player)) {
						PlayerManager.setPrevious(player, previous);
					}
					sender.sendMessage(new TextComponentString(TextFormatting.RED+"Teleportation to home 'home'"));
				} else {
					sender.sendMessage(new TextComponentString(TextFormatting.RED+"Unknown home 'home'"));
				}
			}
		}
	}

}
