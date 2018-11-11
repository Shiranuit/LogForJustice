package fr.Shiranuit.LogForJustice.Commands;

import fr.Shiranuit.LogForJustice.Dimension.TeleporterDim;
import fr.Shiranuit.LogForJustice.Manager.PlayerManager;
import fr.Shiranuit.LogForJustice.PlayerData.NBTPlayerData;
import fr.Shiranuit.LogForJustice.PlayerData.Position;
import fr.Shiranuit.LogForJustice.Utils.Util;
import fr.Shiranuit.LogForJustice.Utils.Chat.ChatType;
import fr.Shiranuit.LogForJustice.Utils.Chat.ChatUtil;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class MuteCommand extends CommandBase {

	@Override
	public String getName() {
		return "mute";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "/mute <playername>";
	}
	
	@Override
	public int getRequiredPermissionLevel() {
		return 4;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length >= 1) {
			EntityPlayer player = Util.playerByName(args[0]);
			if (player != null) {
				NBTPlayerData data = PlayerManager.nbtplayerdata.get(player.getName());
				if (data.mute) {
					ChatUtil.sendMessage(player, args[0]+" is now unmuted", ChatType.LogForJustice);
				} else {
					ChatUtil.sendMessage(player, args[0]+" is now muted", ChatType.LogForJustice);
				}
				data.mute = !data.mute;
				data.save();
			}
		} else {
			throw new WrongUsageException("Usage: /mute <playername>");
		}
	}

}
