package fr.Shiranuit.LogForJustice.Commands;

import fr.Shiranuit.LogForJustice.Main;
import fr.Shiranuit.LogForJustice.Manager.ConfigManager;
import fr.Shiranuit.LogForJustice.Utils.ArrayConverter;
import fr.Shiranuit.LogForJustice.Utils.Util;
import fr.Shiranuit.LogForJustice.Utils.Chat.ChatType;
import fr.Shiranuit.LogForJustice.Utils.Chat.ChatUtil;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextFormatting;

public class VanishCommand extends CommandBase {
	
	@Override
	public int getRequiredPermissionLevel() {
		return 4;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "vanish";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		// TODO Auto-generated method stub
		return "/vanish <playername>";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length > 0) {
			if (Util.playerByName(args[0].toString()) != null) {
				if (Main.VanishedPlayers.contains(args[0].toString())) {
					Main.VanishedPlayers.remove(args[0].toString());
					ConfigManager.EssentialsConfig.getCategory("op").get("vanished-players").set(ArrayConverter.convert(Main.VanishedPlayers));
					ChatUtil.sendMessage(sender, args[0].toString()+" : disabled",ChatType.Vanish);
					EntityPlayer player = Util.playerByName(args[0].toString());
					player.setInvisible(false);
					player.setEntityInvulnerable(false);
					ConfigManager.EssentialsConfig.save();
					Main.mcserver.getPlayerList().sendPacketToAllPlayers(new SPacketPlayerListItem(SPacketPlayerListItem.Action.ADD_PLAYER, (EntityPlayerMP)player));
				} else {
					Main.VanishedPlayers.add(args[0].toString());
					ConfigManager.EssentialsConfig.getCategory("op").get("vanished-players").set(ArrayConverter.convert(Main.VanishedPlayers));
					ChatUtil.sendMessage(sender, args[0].toString()+" : enabled",ChatType.Vanish);
					EntityPlayer player = Util.playerByName(args[0].toString());
					player.setInvisible(true);
					ConfigManager.EssentialsConfig.save();
					Main.mcserver.getPlayerList().sendPacketToAllPlayers(new SPacketPlayerListItem(SPacketPlayerListItem.Action.REMOVE_PLAYER, (EntityPlayerMP)player));
				}
			}
		} else {
			if (Main.VanishedPlayers.contains(sender.getName())) {
				Main.VanishedPlayers.remove(sender.getName());
				ConfigManager.EssentialsConfig.getCategory("op").get("vanished-players").set(ArrayConverter.convert(Main.VanishedPlayers));
				ChatUtil.sendMessage(sender, sender.getName()+" : disabled",ChatType.Vanish);
				EntityPlayer player = Util.playerByName(sender.getName());
				player.setInvisible(false);
				player.setEntityInvulnerable(false);
				ConfigManager.EssentialsConfig.save();
				Main.mcserver.getPlayerList().sendPacketToAllPlayers(new SPacketPlayerListItem(SPacketPlayerListItem.Action.ADD_PLAYER, (EntityPlayerMP)player));
			} else {
				Main.VanishedPlayers.add(sender.getName());
				ConfigManager.EssentialsConfig.getCategory("op").get("vanished-players").set(ArrayConverter.convert(Main.VanishedPlayers));
				ChatUtil.sendMessage(sender, sender.getName()+" : enabled",ChatType.Vanish);
				EntityPlayer player = Util.playerByName(sender.getName());
				player.setInvisible(true);
				ConfigManager.EssentialsConfig.save();
				Main.mcserver.getPlayerList().sendPacketToAllPlayers(new SPacketPlayerListItem(SPacketPlayerListItem.Action.REMOVE_PLAYER, (EntityPlayerMP)player));
			}
		}
		
	}


}
