package fr.Shiranuit.LogForJustice.Commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

import fr.Shiranuit.LogForJustice.Area.Area;
import fr.Shiranuit.LogForJustice.Area.Flag;
import fr.Shiranuit.LogForJustice.Manager.AreaManager;
import fr.Shiranuit.LogForJustice.Manager.MoneyManager;
import fr.Shiranuit.LogForJustice.Utils.ArrayConverter;
import fr.Shiranuit.LogForJustice.Utils.Util;
import fr.Shiranuit.LogForJustice.Utils.Chat.ChatType;
import fr.Shiranuit.LogForJustice.Utils.Chat.ChatUtil;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class PlotCommand extends CommandBase {

	@Override
	public String getName() {
		return "plot";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "/plot help";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length > 0) {
			if (args[0].equals("getName")) {
				String name = AreaManager.getAreaName(sender.getCommandSenderEntity());
				if (name.length() > 0) {
					ChatUtil.sendMessage(sender,"This plot is named '"+name+"'",ChatType.Plot);
				} else {
					ChatUtil.sendMessage(sender,"There is not plot here",ChatType.Plot);
				}
			} else if (args[0].equals("getCost")) {
				Area area = AreaManager.getArea(sender.getCommandSenderEntity());
				Flag isBuyable = AreaManager.getFlag(area, "is_buyable");
				if (!isBuyable.isNull() && isBuyable.isEqual(true)) {
					Flag cost = AreaManager.getFlag(area, "cost");
					ChatUtil.sendMessage(sender,"This plot cost is "+cost.getDouble(),ChatType.Plot);
				} else {
					ChatUtil.sendMessage(sender,"This plot can not be purchased",ChatType.Plot);
				}
			} else if (args[0].equals("getOwner")) {
				Flag owner = AreaManager.getFlag(sender.getCommandSenderEntity(), "owner");
				if (!owner.isNull() && owner.getString().length() > 0) {
					ChatUtil.sendMessage(sender,"The owner of this plot is '" + owner.getString() + "'",ChatType.Plot);
				} else {
					ChatUtil.sendMessage(sender,"This plot has no owner",ChatType.Plot);
				}
			} else if (args[0].equals("buy")) {
				Area area = AreaManager.getArea(sender.getCommandSenderEntity());
				Flag isBuyable = AreaManager.getFlag(area, "is_buyable");
				if (!isBuyable.isNull() && isBuyable.isEqual(true)) {
					Flag owner = AreaManager.getFlag(area, "owner");
					if (owner.length() <= 0) {
						Flag cost = AreaManager.getFlag(area, "cost");
						Double money = MoneyManager.removeIfCan(Util.playerByName(sender.getName()),  cost.getDouble());
						if (money!=null) {
							ChatUtil.sendMessage(sender,"You don't have enought money to buy this plot, you have : "+money,ChatType.Plot);
						} else {
							owner.setValue(sender.getName());
							ChatUtil.sendMessage(sender,"Congratulations, this plot is your's",ChatType.Plot);
						}
					} else {
						ChatUtil.sendMessage(sender,"This plot is owned by '"+owner+"'",ChatType.Plot);
					}
				} else {
					ChatUtil.sendMessage(sender,"This plot can not be purchased",ChatType.Plot);
				}
			} else if (args[0].equals("flag")) {
				if (args.length >= 2) {
					Area area = AreaManager.getArea(sender.getCommandSenderEntity());
					Flag owner = AreaManager.getFlag(area, "owner");
					if (!owner.isNull()) {
						Flag members = AreaManager.getFlag(area, "members");
						boolean isOwner = owner.isEqual(sender.getName());
						boolean isMember = members.contains(sender.getName());
						boolean isOp = Util.isOp(sender.getName());
						if (args.length >= 3) {
							String name = AreaManager.getAreaName(sender.getCommandSenderEntity());
							String flag = args[1];
							if (AreaManager.Areas.containsKey(name)) {
								if (area.flags.containsKey(flag)) {
									Flag fl = area.flags.get(flag);
									if (fl.canModifyFlag(isOp, isOwner, isMember)) {
										Object obj = fl.convert(args[2]);
										boolean result = fl.setValue(obj);
										if (result) {
											ChatUtil.sendMessage(sender,"The '"+flag+"' flag of this plot has been set to : "+fl,ChatType.Plot);
										}
									} else {
										ChatUtil.sendMessage(sender,"You can't modify the '"+flag+"' flag of this plot",ChatType.Plot);
									}
								}
							}
						} else if (args.length == 2) {
							String name = AreaManager.getAreaName(sender.getCommandSenderEntity());
							String flag = args[1];
							if (AreaManager.Areas.containsKey(name)) {
								if (area.flags.containsKey(flag)) {
									Flag fl = area.flags.get(flag);
									if (fl.canModifyFlag(isOp, isOwner, isMember)) {
										ChatUtil.sendMessage(sender,"The '"+flag+"' flag of this plot is set to : "+fl,ChatType.Plot);
									} else {
										ChatUtil.sendMessage(sender,"You can't read the '"+flag+"' flag of this plot",ChatType.Plot);
									}
								}
							}
						}
					} else {
						ChatUtil.sendMessage(sender,"You don't own this plot, you can't change his flags",ChatType.Plot);
					}
				} else {
					ChatUtil.sendMessage(sender,"Usage : /plot flag <flagname> <value>",ChatType.LogForJustice);
				}
			} else if (args[0].equals("listMembers")) {
				Area area = AreaManager.getArea(sender.getCommandSenderEntity());
				Flag owner = AreaManager.getFlag(area, "owner");
				if (!owner.isNull() && (owner.isEqual(sender.getName()) || Util.isOp(sender.getName()))) {
					Flag members = AreaManager.getFlag(area, "members");
					ChatUtil.sendMessage(sender,"Members List : "+members,ChatType.Plot);
				} else {
					ChatUtil.sendMessage(sender,"You don't own this plot, you can't have the members list",ChatType.Plot);
				}
			} else if (args[0].equals("removeMember")) {
				if (args.length >= 1) {
					Area area = AreaManager.getArea(sender.getCommandSenderEntity());
					Flag owner = AreaManager.getFlag(area, "owner");
					if (!owner.isNull() && (owner.isEqual(sender.getName()) || Util.isOp(sender.getName()))) {
						Flag members = AreaManager.getFlag(area, "members");
						if (members.contains(args[1])) {
							Object success = members.remove(args[1]);
							if (success != null) {
								ChatUtil.sendMessage(sender,"Player '"+args[1]+"' has been removed from this plot",ChatType.Plot);
							} else {
								ChatUtil.sendMessage(sender,"Can't remove player '"+args[1]+"' from this plot",ChatType.Plot);
							}
						} else {
							ChatUtil.sendMessage(sender,"Player '"+args[1]+"' is not member of this plot",ChatType.Plot);
						}
					} else {
						ChatUtil.sendMessage(sender,"You don't own this plot, you can't remove members from it",ChatType.Plot);
					}
				} else {
					ChatUtil.sendMessage(sender,"Usage : /plot removeMember <playername>",ChatType.LogForJustice);
				}
			} else if (args[0].equals("addMember")) {
				if (args.length >= 1) {
					Area area = AreaManager.getArea(sender.getCommandSenderEntity());
					Flag owner = AreaManager.getFlag(area, "owner");
					if (!owner.isNull() && (owner.isEqual(sender.getName()) || Util.isOp(sender.getName()))) {
						Flag members = AreaManager.getFlag(area, "members");
						if (!members.contains(args[1])) {
							boolean success = members.add(args[1]);
							if (success) {
								ChatUtil.sendMessage(sender,"Player '"+args[1]+"' is now member of this plot",ChatType.Plot);
							} else {
								ChatUtil.sendMessage(sender,"Can't add player '"+args[1]+"' to this plot",ChatType.Plot);
							}
						} else {
							ChatUtil.sendMessage(sender,"Player '"+args[1]+"' is already member of this plot",ChatType.Plot);
						}
					} else {
						ChatUtil.sendMessage(sender,"You don't own this plot, you can't add members to it",ChatType.Plot);
					}
				} else {
					ChatUtil.sendMessage(sender,"Usage : /plot addMember <playername>",ChatType.LogForJustice);
				}
			}  else if (args[0].equals("setOwner")) {
				if (args.length >= 1) {
					Flag owner = AreaManager.getFlag(sender.getCommandSenderEntity(), "owner");
					if (!owner.isNull() && (owner.isEqual(sender.getName()) || Util.isOp(sender.getName()))) {
						if (Util.isConnected(args[1].toString())) {
							owner.setValue(args[1].toString());
							ChatUtil.sendMessage(sender,"Player '"+args[1]+"' is now the owner of this plot",ChatType.Plot);
						} else {
							ChatUtil.sendMessage(sender,"Unknown player '"+args[1]+"'",ChatType.Plot);
						}
					} else {
						ChatUtil.sendMessage(sender,"You don't own this plot, you can't change the owner",ChatType.Plot);
					}
				} else {
					ChatUtil.sendMessage(sender,"Usage : /plot setOwner <playername>",ChatType.LogForJustice);
				}
			} else if (args[0].equals("help")) {
				ITextComponent text = new TextComponentString(TextFormatting.RED+"Usage : /plot getName");
				sender.sendMessage(text);
				text = new TextComponentString(TextFormatting.RED+"Usage : /plot getOwner");
				sender.sendMessage(text);
				text = new TextComponentString(TextFormatting.RED+"Usage : /plot getCost");
				sender.sendMessage(text);
				text = new TextComponentString(TextFormatting.RED+"Usage : /plot buy");
				sender.sendMessage(text);
				text = new TextComponentString(TextFormatting.RED+"Usage : /plot flag <flagname> <value>");
				sender.sendMessage(text);
				text = new TextComponentString(TextFormatting.RED+"Usage : /plot addMember <playername>");
				sender.sendMessage(text);
				text = new TextComponentString(TextFormatting.RED+"Usage : /plot removeMember <playername>");
				sender.sendMessage(text);
				text = new TextComponentString(TextFormatting.RED+"Usage : /plot listMembers");
				sender.sendMessage(text);
				text = new TextComponentString(TextFormatting.RED+"Usage : /plot setOwner <playername>");
				sender.sendMessage(text);
				text = new TextComponentString(TextFormatting.RED+"Usage : /plot help");
				sender.sendMessage(text);
			}
		}
	}
	
	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}
	
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
    	Area area = AreaManager.getArea(sender.getCommandSenderEntity());
    	Flag owner = AreaManager.getFlag(area, "owner");
    	switch (args.length) {
	    	case 1: {
	    		return getListOfStringsMatchingLastWord(args,"getName","getOwner","getCost","buy","flag","addMember","removeMember","listMembers","setOwner","help");
	    	}
	    	case 2: {
	    		if (args[0].equals("removeMember")) {
	    			if (!owner.isNull() && (owner.isEqual(sender.getName()) || Util.isOp(sender.getName()))) {
	    				Flag members = AreaManager.getFlag(area, "members");
	    				return getListOfStringsMatchingLastWord(args, (String[])members.getValue());
	    			}
	    		} else if (args[0].equals("addMember") || args[0].equals("setOwner")) {
	    			if (!owner.isNull() && (owner.isEqual(sender.getName()) || Util.isOp(sender.getName()))) {
	    				Flag members = AreaManager.getFlag(area, "members");
	    				return getListOfStringsMatchingLastWord(args, Util.playersList());
	    			}
	    		} else if (args[0].equals("flag")) {
	    			if (!owner.isNull()) {
	    				Flag members = AreaManager.getFlag(area, "members");
	    				boolean isOwner = owner.isEqual(sender.getName());
	    				boolean isMember = members.contains(sender.getName());
	    				boolean isOp = Util.isOp(sender.getName());
	    				ArrayList<String> lst = new ArrayList<String>();
		    			for (String key : AreaManager.flagDefault.keySet()) {
		    				Flag flag =  AreaManager.flagDefault.get(key);
		    				if (flag.canModifyFlag(isOp, isOwner, isMember)) {
		    					lst.add(key);
		    				}
		    			}
		    			return getListOfStringsMatchingLastWord(args, lst);
	    			}
	    		}
	    	}
	    	default: {
	    		return Collections.<String>emptyList();
	    	}
    	}
    }
}
