package fr.Shiranuit.LogForJustice.Commands;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import fr.Shiranuit.LogForJustice.Manager.HomeManager;
import fr.Shiranuit.LogForJustice.PlayerData.Position;
import fr.Shiranuit.LogForJustice.Utils.Util;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class DelHomeCommand extends CommandBase {

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "delhome";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		// TODO Auto-generated method stub
		return "/delhome <name>";
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
				Position pos = new Position(player);
				HomeManager.delHome(player, args[0]);
				sender.sendMessage(new TextComponentString(TextFormatting.RED+"Home '"+args[0]+"' deleted"));
			}
		} else {
			if (sender.getCommandSenderEntity() instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer)sender.getCommandSenderEntity();
				Position pos = new Position(player);
				HomeManager.delHome(player, "home");
				sender.sendMessage(new TextComponentString(TextFormatting.RED+"Home 'home' deleted"));
			}
		}
	}

}
