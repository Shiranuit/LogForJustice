package fr.Shiranuit.LogForJustice.Commands;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import fr.Shiranuit.LogForJustice.Manager.PlayerManager;
import fr.Shiranuit.LogForJustice.Utils.Util;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentUtils;
import net.minecraft.util.text.TextFormatting;

public class TpaCommand extends CommandBase {

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "tpa";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		// TODO Auto-generated method stub
		return "/tpa <player>";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
			@Nullable BlockPos targetPos) {
		if (args.length == 1) {
			return getListOfStringsMatchingLastWord(args, Util.playersList());
		} else {
			return Collections.<String>emptyList();
		}
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length >= 1) {
			EntityPlayer player = Util.playerByName(args[0]);
			if (player != null) {
				PlayerManager.TPARequestAdd(player.getName(), sender.getName());
				sender.sendMessage(new TextComponentString(TextFormatting.RED+"A teleportation request has been sent to "+player.getName()));
				
                ITextComponent itextcomponent = ITextComponent.Serializer.jsonToComponent("[\"\",{\"text\":\""+sender.getName()+"\",\"color\":\"blue\"},{\"text\":\" request TPA! \"},{\"text\":\"[Accept]\",\"color\":\"green\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tpaccept "+sender.getName()+"\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"/tpaccept "+sender.getName()+"\"}},{\"text\":\" or \"},{\"text\":\"[Deny]\",\"color\":\"red\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tpdeny "+sender.getName()+"\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"/tpdeny "+sender.getName()+"\"}}]");
                player.sendMessage(TextComponentUtils.processComponent(sender, itextcomponent, player));
				
			} else {
				sender.sendMessage(new TextComponentString(TextFormatting.RED+"Unknown player '"+args[0]+"'"));
			}
		} else {
			throw new WrongUsageException("/tpa <player>");
		}
	}

}
