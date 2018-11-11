package fr.Shiranuit.LogForJustice.Commands;

import fr.Shiranuit.LogForJustice.Manager.CommandSpyManager;
import fr.Shiranuit.LogForJustice.Manager.MessageSpyManager;
import fr.Shiranuit.LogForJustice.Utils.Util;
import fr.Shiranuit.LogForJustice.Utils.Chat.ChatType;
import fr.Shiranuit.LogForJustice.Utils.Chat.ChatUtil;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextFormatting;

public class TellSpyCommand extends CommandBase{

	
	@Override
	public int getRequiredPermissionLevel() {
		return 4;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "messagespy";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		// TODO Auto-generated method stub
		return "/messagespy <true/false>";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length < 1) {
			throw new WrongUsageException("/messagespy <true/false>", new Object[0]);
		} else {
			Boolean state = Boolean.valueOf(args[0]);
			EntityPlayer player = Util.playerByName(sender.getName());
			if (player != null && state != null) {
				MessageSpyManager.spyMode(player, state);
				ChatUtil.sendMessage(sender,player.getDisplayNameString() + " MessageSpy : " + state.toString(),ChatType.MessageSpy);	
			}
		}
		
	}
}
