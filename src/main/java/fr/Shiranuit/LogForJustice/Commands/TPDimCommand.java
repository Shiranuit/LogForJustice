package fr.Shiranuit.LogForJustice.Commands;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import fr.Shiranuit.LogForJustice.Main;
import fr.Shiranuit.LogForJustice.Dimension.TeleporterDim;
import fr.Shiranuit.LogForJustice.Manager.PlayerManager;
import fr.Shiranuit.LogForJustice.Manager.RankManager;
import fr.Shiranuit.LogForJustice.PlayerData.Position;
import fr.Shiranuit.LogForJustice.Utils.ObjectUtil;
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

public class TPDimCommand extends CommandBase {

	@Override
	public String getName() {
		return "tpDim";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "/tpDim <x> <y> <z> <dimensionID>\n/tpDim <player>";
	}
	
	@Override
	public int getRequiredPermissionLevel() {
		return 4;
	}
	
	@Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
		if (args.length ==  1) {
			return getListOfStringsMatchingLastWord(args, Util.playersListWithVanished());
		} else {
			return Collections.<String>emptyList();
		}
    }

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length >= 4) {
			double x = ObjectUtil.Double(args[0]);
			double y = ObjectUtil.Double(args[1]);
			double z = ObjectUtil.Double(args[2]);
			int d = ObjectUtil.Int(args[3]);
			
			if ( net.minecraftforge.common.DimensionManager.getWorld(d) != null || Main.mcserver.getWorld(d) != null) {
				Position pos = new Position(x,y,z,d);
				if (sender.getCommandSenderEntity() instanceof EntityPlayer) {
					EntityPlayer me = (EntityPlayer)sender.getCommandSenderEntity();
					if (RankManager.hasPermission("back", "tp", "", -1, me)) {
						PlayerManager.setPrevious(me, pos);
					}
				}
				TeleporterDim tp = new TeleporterDim(pos);
				
				tp.teleport(sender.getCommandSenderEntity());
			}
		} else if (args.length >= 1) {
			EntityPlayer player = Util.playerByName(args[0]);
			if (player != null) {
				Position pos = new Position(player);
				if (sender.getCommandSenderEntity() instanceof EntityPlayer) {
					EntityPlayer me = (EntityPlayer)sender.getCommandSenderEntity();
					if (RankManager.hasPermission("back", "tp", "", -1, me)) {
						PlayerManager.setPrevious(me, pos);
					}
				}
				TeleporterDim tp = new TeleporterDim(player);
				
				tp.teleport(sender.getCommandSenderEntity());
			} else {
				sender.sendMessage(new TextComponentString(TextFormatting.RED+"Unknown playername '"+args[0]+"'"));
			}
		} else {
			throw new WrongUsageException("/tpDim <x> <y> <z> <dimensionID>\n/tpDim <player>");
		}
	}

}
