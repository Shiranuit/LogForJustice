package fr.Shiranuit.LogForJustice.Commands;

import fr.Shiranuit.LogForJustice.Dimension.TeleporterDim;
import fr.Shiranuit.LogForJustice.Manager.HomeManager;
import fr.Shiranuit.LogForJustice.Manager.PlayerManager;
import fr.Shiranuit.LogForJustice.PlayerData.Position;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class SpawnCommand extends CommandBase {

	@Override
	public String getName() {
		return "spawn";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "spawn";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (sender.getCommandSenderEntity() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer)sender.getCommandSenderEntity();
			Position pos = new Position(server.getEntityWorld().getSpawnPoint(),0);
			Position previous = new Position(player);
			TeleporterDim tp = new TeleporterDim(pos);
			tp.teleport(player);
			PlayerManager.setPrevious(player, previous);
		}
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}
	
}
