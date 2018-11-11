package fr.Shiranuit.LogForJustice.Commands;

import java.util.List;

import javax.annotation.Nullable;

import fr.Shiranuit.LogForJustice.Manager.ConfigManager;
import fr.Shiranuit.LogForJustice.Utils.Chat.ChatType;
import fr.Shiranuit.LogForJustice.Utils.Chat.ChatUtil;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class ConfigCommand extends CommandBase{

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "lfjconfig";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		// TODO Auto-generated method stub
		return "/lfjconfig reload";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length > 0) {
			if (args[0].equals("reload")) {
				ConfigManager.sync();
				ChatUtil.sendMessage(sender, "Configuration reloaded", ChatType.LogForJustice);
			} else {
				ChatUtil.sendMessage(sender, "Usage : /lfjconfig reload", ChatType.LogForJustice);
			}
		} else {
			ChatUtil.sendMessage(sender, "Usage : /lfjconfig reload", ChatType.LogForJustice);
		}
	}
	
	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
    	return getListOfStringsMatchingLastWord(args, "reload");
    }
}
