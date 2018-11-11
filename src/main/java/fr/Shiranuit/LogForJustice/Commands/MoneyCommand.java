package fr.Shiranuit.LogForJustice.Commands;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import fr.Shiranuit.LogForJustice.Manager.MoneyManager;
import fr.Shiranuit.LogForJustice.Manager.RankManager;
import fr.Shiranuit.LogForJustice.Utils.ArrayConverter;
import fr.Shiranuit.LogForJustice.Utils.ObjectUtil;
import fr.Shiranuit.LogForJustice.Utils.Util;
import fr.Shiranuit.LogForJustice.Utils.Chat.ChatType;
import fr.Shiranuit.LogForJustice.Utils.Chat.ChatUtil;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class MoneyCommand extends CommandBase {

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "money";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		// TODO Auto-generated method stub
		return "/money help";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length > 0) {
			if (args[0].equals("count")) {
				if (args.length >= 2) {
					EntityPlayer player = Util.playerByName(args[1]);
					if (player != null) {
						double amount = MoneyManager.calcMoney(player);
						ChatUtil.sendMessage(sender,"Money of '"+args[1]+"' : "+amount, ChatType.Money);
					} else {
						ChatUtil.sendMessage(sender,"Unknown player '"+args[1]+"'", ChatType.Money);
					}
				} else {
					ChatUtil.sendMessage(sender,"/money count <playername>", ChatType.LogForJustice);
				}
			} else if (args[0].equals("add")) {
				if (args.length >= 3) {
					EntityPlayer player = Util.playerByName(args[1]);
					if (player != null) {
						double amount = ObjectUtil.Double(args[2]);
						if (amount > 0) {
							MoneyManager.add(player, amount);
							ChatUtil.sendMessage(sender,amount+" have been added to "+args[1]+"'s money", ChatType.Money);
						} else {
							ChatUtil.sendMessage(sender,"The amount need to be superior to 0", ChatType.Money);
						}
					} else {
						ChatUtil.sendMessage(sender,"Unknown player '"+args[1]+"'", ChatType.Money);
					}
				} else {
					ChatUtil.sendMessage(sender,"/money add <playername> <amount>", ChatType.LogForJustice);
				}
			} else if (args[0].equals("remove")) {
				if (args.length >= 3) {
					EntityPlayer player = Util.playerByName(args[1]);
					if (player != null) {
						double amount = ObjectUtil.Double(args[2]);
						if (amount > 0) {
							MoneyManager.remove(player, amount);
							ChatUtil.sendMessage(sender,amount+" have been removed from "+args[1]+"'s money", ChatType.Money);
						} else {
							ChatUtil.sendMessage(sender,"The amount need to be superior to 0", ChatType.Money);
						}
					} else {
						ChatUtil.sendMessage(sender,"Unknown player '"+args[1]+"'", ChatType.Money);
					}
				} else {
					ChatUtil.sendMessage(sender,"/money remove <playername> <amount>", ChatType.LogForJustice);
				}
			} else if (args[0].equals("clear")) {
				if (args.length >= 2) {
					EntityPlayer player = Util.playerByName(args[1]);
					if (player != null) {
						MoneyManager.clear(player);
						ChatUtil.sendMessage(sender,args[1]+"'s money have been cleared", ChatType.Money);
					} else {
						ChatUtil.sendMessage(sender,"Unknown player '"+args[1]+"'", ChatType.Money);
					}
				} else {
					ChatUtil.sendMessage(sender,"/money clear <playername>", ChatType.LogForJustice);
				}
			} else if (args[0].equals("set")) {
				if (args.length >= 3) {
					EntityPlayer player = Util.playerByName(args[1]);
					if (player != null) {
						double amount = ObjectUtil.Double(args[2]);
						if (amount >= 0) {
							MoneyManager.set(player, amount);
							ChatUtil.sendMessage(sender,args[1]+"'s money set to "+amount, ChatType.Money);
						} else {
							ChatUtil.sendMessage(sender,"The amount need to be superior to 0", ChatType.Money);
						}
					} else {
						ChatUtil.sendMessage(sender,"Unknown player '"+args[1]+"'", ChatType.Money);
					}
				} else {
					ChatUtil.sendMessage(sender,"/money set <playername> <amount>", ChatType.LogForJustice);
				}
			} else if (args[0].equals("help")) {
				ITextComponent text = new TextComponentString(TextFormatting.RED+"Usage : /money add <playername> <amount>");
				sender.sendMessage(text);
				text = new TextComponentString(TextFormatting.RED+"Usage : /money remove <playername> <amount>");
				sender.sendMessage(text);
				text = new TextComponentString(TextFormatting.RED+"Usage : /money clear <playername>");
				sender.sendMessage(text);
				text = new TextComponentString(TextFormatting.RED+"Usage : /money set <playername> <amount>");
				sender.sendMessage(text);
			}
		} else {
			ChatUtil.sendMessage(sender,"/money help", ChatType.LogForJustice);
		}
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}
	
	@Override
    public List getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
		if (args.length == 1) {
			return getListOfStringsMatchingLastWord(args,"help","add","remove","count","clear","set");
		} else if (args.length == 2) {
			if (args[0].equals("add") || args[0].equals("remove") || args[0].equals("clear") || args[0].equals("set") || args[0].equals("count")) {
				return getListOfStringsMatchingLastWord(args, Util.playersListWithVanished());
			}
		}
        return Collections.<String>emptyList();
    }
	
}
