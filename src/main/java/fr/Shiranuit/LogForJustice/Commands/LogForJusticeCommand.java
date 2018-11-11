package fr.Shiranuit.LogForJustice.Commands;

import java.util.Arrays;
import java.util.List;

import fr.Shiranuit.LogForJustice.Main;
import fr.Shiranuit.LogForJustice.Manager.ConfigManager;
import fr.Shiranuit.LogForJustice.Utils.ObjectUtil;
import fr.Shiranuit.LogForJustice.Utils.Chat.ChatType;
import fr.Shiranuit.LogForJustice.Utils.Chat.ChatUtil;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class LogForJusticeCommand extends CommandBase {

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "logforjustice";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		// TODO Auto-generated method stub
		return "/logforjustice <true/false>";
	}
	
	@Override
	public List<String> getAliases()
    {
        return Arrays.<String>asList("lfj");
    }
	
	@Override
	public int getRequiredPermissionLevel() {
		return 4;
	}


	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length > 0) {
			boolean state = false;
			if (args[0].toLowerCase().equals("false")) {
				state = false;
			} else if (args[0].toLowerCase().equals("true")) {
				state = true;
			} else {
				ChatUtil.sendMessage(sender, "/!\\This command Disable or Enable LogForJustice/!\\\n/logforjustice <true/false>", ChatType.LogForJustice);
				return;
			}
			Main.enabled = state;
			ConfigManager.EssentialsConfig.getCategory("logforjustice").get("enabled").set(state);
			ConfigManager.EssentialsConfig.save();
		}
	}

}
