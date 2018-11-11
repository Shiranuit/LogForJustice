package fr.Shiranuit.LogForJustice.Commands;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import fr.Shiranuit.LogForJustice.Inventory.InvSeeInventory;
import fr.Shiranuit.LogForJustice.Manager.PlayerManager;
import fr.Shiranuit.LogForJustice.Utils.Util;
import fr.Shiranuit.LogForJustice.Utils.Chat.ChatType;
import fr.Shiranuit.LogForJustice.Utils.Chat.ChatUtil;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class InvSeeCommand extends CommandBase {

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "invsee";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		// TODO Auto-generated method stub
		return "/invsee <playername>";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length > 0) {
			EntityPlayer player = Util.playerByName(args[0]);
			if (player != null) {
				EntityPlayerMP self = getCommandSenderAsPlayer(sender);
				EntityPlayerMP other = (EntityPlayerMP)Util.playerByName(args[0]);
				self.displayGUIChest(new InvSeeInventory(other.inventory, other));
			} else {
				ChatUtil.sendMessage(sender, "Unkown player '"+args[0]+"'", ChatType.LogForJustice);
			}
		} else {
			
		}
	}
	
	@Override
	public int getRequiredPermissionLevel() {
		return 4;
	}
	
	@Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
		if (args.length ==  1) {
			return getListOfStringsMatchingLastWord(args, Util.playersListWithVanished());
		} else {
			return Collections.<String>emptyList();
		}
    }

}
