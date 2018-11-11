package fr.Shiranuit.LogForJustice.Commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import fr.Shiranuit.LogForJustice.Grade.RankInfo;
import fr.Shiranuit.LogForJustice.Manager.ConfigManager;
import fr.Shiranuit.LogForJustice.Manager.RankManager;
import fr.Shiranuit.LogForJustice.Utils.ArrayConverter;
import fr.Shiranuit.LogForJustice.Utils.ObjectUtil;
import fr.Shiranuit.LogForJustice.Utils.Util;
import fr.Shiranuit.LogForJustice.Utils.Chat.ChatType;
import fr.Shiranuit.LogForJustice.Utils.Chat.ChatUtil;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class RankCommand extends CommandBase {

	private void error() throws WrongUsageException {
		throw new WrongUsageException(this.getUsage(null), new Object[0]);
	}
	
	private void error(String err) throws WrongUsageException {
		throw new WrongUsageException(err, new Object[0]);
	}
	
	private void send(ICommandSender sender, String message) {
		ChatUtil.sendMessage(sender, message, ChatType.Rank);
	}
	
	@Override
    public List getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
		if (args.length == 1) {
			return getListOfStringsMatchingLastWord(args,"help","add","remove","list","info","setPlayerRank","setPower","setDisplayname","reload");
		} else if (args.length == 2) {
			if (args[0].equals("remove")) {
				return getListOfStringsMatchingLastWord(args, ArrayConverter.convert(RankManager.grades.keySet()));
			} else if (args[0].equals("info")) {
				return getListOfStringsMatchingLastWord(args, ArrayConverter.convert(RankManager.grades.keySet()));
			} else if (args[0].equals("setPlayerRank")) {
				return getListOfStringsMatchingLastWord(args, Util.playersListWithVanished());
			} else if (args[0].equals("setPower")) {
				return getListOfStringsMatchingLastWord(args, ArrayConverter.convert(RankManager.grades.keySet()));
			} else if (args[0].equals("setDisplayname")) {
				return getListOfStringsMatchingLastWord(args, ArrayConverter.convert(RankManager.grades.keySet()));
			}
		} else if(args.length == 3) {
			if (args[0].equals("setPlayerRank")) {
				return getListOfStringsMatchingLastWord(args, ArrayConverter.convert(RankManager.grades.keySet()));
			}
		}
        return Collections.<String>emptyList();
    }

	@Override
	public int getRequiredPermissionLevel() {
		return 4;
	}
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "lfjrank";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		// TODO Auto-generated method stub
		return "/lfjrank help";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length < 1) {
			error();
		} else if (args.length >= 1) {
			if (args[0].equals("help")) {
				send(sender, "/lfjrank help");
				send(sender, "/lfjrank add <RankName> <Power> [Displayname]");
				send(sender, "/lfjrank remove <RankName>");
				send(sender, "/lfjrank list");
				send(sender, "/lfjrank info <RankName>");
				send(sender, "/lfjrank setPlayerRank <PlayerName> <RankName>");
				send(sender, "/lfjrank setPower <RankName> <Power>");
				send(sender, "/lfjrank setDisplayname <RankName> <Displayname>");
				send(sender, "/lfjrank reload");
			} else if (args[0].equals("add")) {
				if (args.length == 3) {
					int power = ObjectUtil.Int(args[2]);
					power = power < RankManager.getPlayerPower(sender) ? power : RankManager.getPlayerPower(sender)-1; 
					boolean result = RankManager.createRank(args[1], power, "");
					if (result) {
						send(sender, "New rank named '"+args[1]+"' with a power of "+power+" created");
					} else {
						send(sender, "Rank '"+args[1]+"' already exist");
					}
				} else if (args.length >= 4) {
					int power = ObjectUtil.Int(args[2]);
					power = power < RankManager.getPlayerPower(sender) ? power : RankManager.getPlayerPower(sender)-1;
					boolean result = RankManager.createRank(args[1], power, args[3]);
					if (result) {
						send(sender, "New rank named '"+args[1]+"' with a power of "+power+" created");
					} else {
						send(sender, "Rank '"+args[1]+"' already exist");
					}
				} else {
					error("/lfjrank add <RankName> <Power> [Displayname]");
				}
			} else if (args[0].equals("remove")) {
				if (args.length >= 2) {
					if (RankManager.existRank(args[1]) && RankManager.rankPower(args[1]) < RankManager.getPlayerPower(sender)) {
						if (args[1].equals("default")) {
							send(sender, "Could not remove the 'default' rank");
						} else {
							RankManager.removeRank(args[1]);
							send(sender, "Rank '"+args[1]+"' removed");
						}
					} else {
						send(sender, "Rank '"+args[1]+"' does not exist");
					}
				} else {
					error("/lfjrank remove <RankName>");
				}
			} else if (args[0].equals("list")) {
				send(sender, "Existing ranks : ["+String.join(", ", RankManager.grades.keySet())+"]");
			} else if (args[0].equals("info")) {
				if (args.length >= 2) {
					if (RankManager.existRank(args[1])) {
						RankInfo info = RankManager.grades.get(args[1]);
						send(sender, "RankName: " + args[1]+", Power: "+info.power + ", Displayname: "+info.displayname.replace("&", "\247"));
					} else {
						send(sender, "Rank '"+args[1]+"' does not exist");
					}
				} else {
					error("/lfjrank info <RankName>");
				}
			} else if (args[0].equals("setPlayerRank")) {
				if (args.length >= 3) {
					if (RankManager.existRank(args[2])) {
						if (RankManager.rankPower(args[2]) < RankManager.getPlayerPower(sender)) {
							if (RankManager.getPlayerPower(args[1]) < RankManager.getPlayerPower(sender)) {
								RankManager.setRank(args[1], args[2]);
								RankManager.saveRank(args[2]);
								send(sender, args[1]+" has been promoted to the rank '"+args[2]+"'");	
							} else {
								send(sender, "You can't demote a player with the same or higher rank than your's");
							}
							RankManager.setRank(args[1], args[2]);
							RankManager.saveRank(args[2]);
							send(sender, args[1]+" has been promoted to the rank '"+args[2]+"'");	
						} else {
							send(sender, "You can't promote a player to a higher or equal rank than your's");
						}
					} else {
						send(sender, "Rank '"+args[1]+"' does not exist");
					}
				} else {
					error("/lfjrank setPlayerRank <PlayerName> <RankName>");
				}
			} else if (args[0].equals("setPower")) {
				if (args.length >= 3) {
					if (RankManager.existRank(args[1])) {
						RankInfo info = RankManager.grades.get(args[1]);
						if (info.power < RankManager.getPlayerPower(sender)) {
							int power = ObjectUtil.Int(args[2]);
							power = power < RankManager.getPlayerPower(sender) ? power : RankManager.getPlayerPower(sender)-1;
							info.power = power;
							RankManager.grades.put(args[1], info);
							info.save();
							send(sender, "Power of rank '"+args[1]+"' has been set to " + power);
						} else {
							send(sender, "You can't edit a equal or higher rank than your's");
						}
					} else {
						send(sender, "Rank '"+args[1]+"' does not exist");
					}
				} else {
					error("/lfjrank setPower <RankName> <Power>");
				}
			} else if (args[0].equals("setDisplayname")) {
				if (args.length >= 3) {
					if (RankManager.existRank(args[1])) {
						RankInfo info = RankManager.grades.get(args[1]);
						if (info.power < RankManager.getPlayerPower(sender)) {
							info.displayname = args[2];
							RankManager.grades.put(args[1], info);
							info.save();
							send(sender, "Displayname of rank '"+args[1]+"' has been set to '" + args[2].replace("&", "\247")+"\247f"+TextFormatting.RED+"'");
						} else {
							send(sender, "You can't edit a equal or higher rank than your's");
						}
						
					} else {
						send(sender, "Rank '"+args[1]+"' does not exist");
					}
				} else {
					error("/lfjrank setDisplayname <RankName> <Displayname>");
				}
			} else if (args[0].equals("reload")) {
				RankManager.loadRanks();
				ConfigManager.sync();
				send(sender, "Commands Power levels have been reloaded");
			} else {
				error();
			}
		}
	}

}
