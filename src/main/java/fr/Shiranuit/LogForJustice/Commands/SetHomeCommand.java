package fr.Shiranuit.LogForJustice.Commands;

import fr.Shiranuit.LogForJustice.Grade.RankInfo;
import fr.Shiranuit.LogForJustice.Manager.HomeManager;
import fr.Shiranuit.LogForJustice.Manager.RankManager;
import fr.Shiranuit.LogForJustice.PlayerData.Position;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class SetHomeCommand extends CommandBase {

	@Override
	public String getName() {
		return "sethome";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "/sethome <name>";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}
	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length >= 1) {
			if (sender.getCommandSenderEntity() instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer)sender.getCommandSenderEntity();
				int maxhomes = HomeManager.homeNumber;
				String rank = RankManager.rankName(player);
				if (rank != null) {
					RankInfo info = RankManager.grades.get(rank);
					if (info != null) {
						maxhomes = info.homenumber;
					}
				}
				if (HomeManager.countHome(player) < maxhomes || HomeManager.hasHome(player, args[0])) {
					Position pos = new Position(player);
					HomeManager.setHome(player, args[0], pos);
					sender.sendMessage(new TextComponentString(TextFormatting.RED+"Home '"+args[0]+"' set"));
				} else {
					sender.sendMessage(new TextComponentString(TextFormatting.RED+"You can't have more than "+maxhomes+" Homes"));
				}
			}
		} else {
			EntityPlayer player = (EntityPlayer)sender.getCommandSenderEntity();
			int maxhomes = HomeManager.homeNumber;
			String  rank = RankManager.rankName(player);
			if (rank != null) {
				RankInfo info = RankManager.grades.get(rank);
				if (info != null) {
					maxhomes = info.homenumber;
				}
			}
			if (HomeManager.countHome(player) < maxhomes || HomeManager.hasHome(player, "home")) {
				Position pos = new Position(player);
				HomeManager.setHome(player, "home", pos);
				sender.sendMessage(new TextComponentString(TextFormatting.RED+"Home 'home' set"));
			} else {
				sender.sendMessage(new TextComponentString(TextFormatting.RED+"You can't have more than "+maxhomes+" Homes"));
			}
		}
	}

}
