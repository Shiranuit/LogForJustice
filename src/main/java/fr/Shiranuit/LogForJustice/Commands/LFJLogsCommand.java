package fr.Shiranuit.LogForJustice.Commands;

import fr.Shiranuit.LogForJustice.Manager.ConfigManager;
import fr.Shiranuit.LogForJustice.Manager.LogForJusticeManager;
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

public class LFJLogsCommand extends CommandBase {

	
	@Override
	public int getRequiredPermissionLevel() {
		return 4;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "lfjlogs";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		// TODO Auto-generated method stub
		return "/lfjlogs <true/false>";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length < 1) {
			throw new WrongUsageException("/lfjlogs <true/false>", new Object[0]);
		} else {
			Boolean state = Boolean.valueOf(args[0]);
				if (state == true) {
					ChatUtil.sendMessage(sender," [LFJ Status] : Enable",ChatType.Status);
				} else {
					ChatUtil.sendMessage(sender," [LFJ Status] : Disable",ChatType.Status);
				}
				ConfigManager.EssentialsConfig.getCategory("logforjustice").get("block-logs").set(state);
				ConfigManager.EssentialsConfig.save();
				LogForJusticeManager.enabled = state;
		}
	}
}
