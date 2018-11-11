package fr.Shiranuit.LogForJustice.Commands;

import fr.Shiranuit.LogForJustice.Dimension.TeleporterDim;
import fr.Shiranuit.LogForJustice.Manager.PlayerManager;
import fr.Shiranuit.LogForJustice.Manager.RankManager;
import fr.Shiranuit.LogForJustice.PlayerData.Position;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class BackCommand extends CommandBase {

	@Override
	public String getName() {
		return "back";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "/back";
	}
	
	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		Entity ent = sender.getCommandSenderEntity();
		if (ent instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer)ent;
			Position pos = new Position(player.getPositionVector(), player.dimension);
			
			if (PlayerManager.hasPrevious(player)) {
				Position previous = PlayerManager.getPrevious(player);
				if (RankManager.hasPermission("back", "tp", "", -1, player)) {
					PlayerManager.setPrevious(player, pos);
				} else {
					PlayerManager.removePrevious(player);
				}
				TeleporterDim tp = new TeleporterDim(previous);
				tp.teleport(player);
				
			} else {
				sender.sendMessage(new TextComponentString(TextFormatting.RED+"You don't have any previous location to go"));
			}
		}
	}

}
