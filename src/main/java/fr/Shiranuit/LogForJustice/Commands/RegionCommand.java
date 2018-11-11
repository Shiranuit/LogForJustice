package fr.Shiranuit.LogForJustice.Commands;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

import fr.Shiranuit.LogForJustice.Area.Area;
import fr.Shiranuit.LogForJustice.Area.Flag;
import fr.Shiranuit.LogForJustice.Area.Selection;
import fr.Shiranuit.LogForJustice.Manager.AreaManager;
import fr.Shiranuit.LogForJustice.Utils.ArrayConverter;
import fr.Shiranuit.LogForJustice.Utils.ObjectUtil;
import fr.Shiranuit.LogForJustice.Utils.Util;
import fr.Shiranuit.LogForJustice.Utils.Chat.ChatType;
import fr.Shiranuit.LogForJustice.Utils.Chat.ChatUtil;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class RegionCommand extends CommandBase {

	@Override
	public List<String> getAliases()
    {
        return Arrays.<String>asList("rg");
    }
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "region";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		// TODO Auto-generated method stub
		return "/region help";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length >= 1) {
			if (args[0].equals("add")) {
				if (args.length >= 2) {
					String name = args[1];
					Boolean herit = false;
					if (args.length >= 3) {
						herit = Boolean.valueOf(args[2]);
					}
					if (!AreaManager.Areas.containsKey(name)) {
						Selection selection =  AreaManager.getSelection(sender);
						if (selection.isSet()) {
							if (herit) {
								Area top = AreaManager.findShortestAreaIn(sender.getEntityWorld(), selection.getCenterPoint(), new Area(selection,sender.getCommandSenderEntity().dimension,null));
								if (top != null) {
									AreaManager.writeProtection(name, selection.getStart().getX(), selection.getStart().getY(), selection.getStart().getZ(), selection.getEnd().getX(), selection.getEnd().getY(), selection.getEnd().getZ(), sender.getCommandSenderEntity().dimension,top.flags);
								} else {
									AreaManager.writeProtection(name, selection.getStart().getX(), selection.getStart().getY(), selection.getStart().getZ(), selection.getEnd().getX(), selection.getEnd().getY(), selection.getEnd().getZ(), sender.getCommandSenderEntity().dimension);
								}
							} else {
								AreaManager.writeProtection(name, selection.getStart().getX(), selection.getStart().getY(), selection.getStart().getZ(), selection.getEnd().getX(), selection.getEnd().getY(), selection.getEnd().getZ(), sender.getCommandSenderEntity().dimension);
							}
							ChatUtil.sendMessage(sender, "The area '"+name+"' has been set",ChatType.Region);
						} else {
							ChatUtil.sendMessage(sender, "2 Positions are need to create a region",ChatType.Region);
						}
					} else {
						ChatUtil.sendMessage(sender, "The area '"+name+"' already exist",ChatType.Region);
					}
				} else {
					ChatUtil.sendMessage(sender,"Usage : /region add <regionName> [Flag_Heritance: true/false]",ChatType.LogForJustice);
				}
			}
			if (args[0].equals("remove")) {
				if (args.length >= 2) {
					String name = args[1];
					AreaManager.deleteProtection(name);
					ChatUtil.sendMessage(sender,"The area '"+name+"' has been removed",ChatType.Region);
				} else {
					ChatUtil.sendMessage(sender,"Usage : /region remove <name>",ChatType.LogForJustice);
				}
			}
			
			if (args[0].equals("flag")) {
				if (args.length >= 4) {
					String name = args[1];
					String flag = args[2];
					if (AreaManager.Areas.containsKey(name)) {
						Area area = AreaManager.Areas.get(name);
						if (area.flags.containsKey(flag)) {
							Flag fl = area.flags.get(flag);
							Object obj = fl.convert(args[3]);
							boolean result = fl.setValue(obj);
							if (result) {
								ChatUtil.sendMessage(sender,"The '"+flag+"' flag of the '"+name+"' area has been set to : "+fl,ChatType.Region);
							}
						} else {
							ChatUtil.sendMessage(sender,"Unknown flag named '"+flag+"'",ChatType.Region);
						}
					} else {
						ChatUtil.sendMessage(sender,"Unknown region named '"+name+"'",ChatType.Region);
					}
				} else if (args.length == 3) {
					String name = args[1];
					String flag = args[2];
					if (AreaManager.Areas.containsKey(name)) {
						Area area = AreaManager.Areas.get(name);
						if (area.flags.containsKey(flag)) {
							Flag fl = area.flags.get(flag);
							ChatUtil.sendMessage(sender,"Description of flag '"+flag+"': "+fl.getDescription(),ChatType.Region);
							ChatUtil.sendMessage(sender,"The '"+flag+"' flag of the '"+name+"' area is set to : "+fl,ChatType.Region);
						} else {
							ChatUtil.sendMessage(sender,"Unknown flag named '"+flag+"'",ChatType.Region);
						}
					} else {
						ChatUtil.sendMessage(sender,"Unknown region named '"+name+"'",ChatType.Region);
					}
				}
			}
			if (args[0].equals("getNameHere")) {
				String name = AreaManager.getAreaName(sender.getEntityWorld(), sender.getPositionVector());
				if (name.length() > 0) {
					ChatUtil.sendMessage(sender,"You are in the area named '"+name+"'",ChatType.Region);
				} else {
					ChatUtil.sendMessage(sender,"There is not area here",ChatType.Region);
				}
			}
			if (args[0].equals("getNameAtPosition")) {
				if (args.length >= 4) {
					int x = ObjectUtil.Int(args[1]);
					int y = ObjectUtil.Int(args[2]);
					int z = ObjectUtil.Int(args[3]);
					String name = AreaManager.getAreaName(sender.getEntityWorld(), new BlockPos(x,y,z));
					if (name.length() > 0) {
						ChatUtil.sendMessage(sender,"The area at [X:"+x+", Y:"+y+", Z:"+z+"] is named '"+name+"'",ChatType.Region);
					} else {
						ChatUtil.sendMessage(sender,"There is not area at [X:"+x+", Y:"+y+", Z:"+z+"]",ChatType.Region);
					}
				}
			}
			if (args[0].equals("getPosition")) {
				if (args.length >= 1) {
					String name = args[1];
					if (AreaManager.Areas.containsKey(name)) {
						Area area = AreaManager.Areas.get(name);
						ChatUtil.sendMessage(sender,"Area '"+name+"'Coordinates : [X:"+area.x+", Y:"+area.y+", Z:"+area.z+", DX:"+area.dx+", DY:"+area.dy+", DZ:"+area.dz+"]",ChatType.Region);
					} else {
						ChatUtil.sendMessage(sender,"Area named '"+name+"' not found",ChatType.Region);
					}
				} else {
					ChatUtil.sendMessage(sender,"Usage : /region getPosition <regionName>",ChatType.LogForJustice);
				}
			}
			if (args[0].equals("list")) {
				String list = "[" + String.join(",", ArrayConverter.convert(AreaManager.Areas.keySet()))+"]";
				ChatUtil.sendMessage(sender,"Region List : "+list,ChatType.Region);
			}
			if (args[0].equals("copyFlags")) {
				if (args.length >= 3){
					if (AreaManager.Areas.containsKey(args[1])) {
						if (AreaManager.Areas.containsKey(args[2])) {
							Area src = AreaManager.Areas.get(args[1]);
							Area dst = AreaManager.Areas.get(args[2]);
							AreaManager.writeProtection(args[2],dst.x,dst.y,dst.z,dst.dx,dst.dy,dst.dz,dst.dimensionID,src.flags);
							ChatUtil.sendMessage(sender,"Flags copied from '"+args[1]+"' to '"+args[2]+"'",ChatType.Region);
						} else {
							ChatUtil.sendMessage(sender,"Unknown region named '"+args[2]+"'",ChatType.Region);
						}	
					} else {
						ChatUtil.sendMessage(sender,"Unknown region named '"+args[1]+"'",ChatType.Region);
					}
				} else {
					ChatUtil.sendMessage(sender,"Usage : /region copyFlags <regionSource> <regionDestination>",ChatType.LogForJustice);
				}
			}
			if (args[0].equals("rewrite")) {
				for (String name : AreaManager.Areas.keySet()) {
					AreaManager.writeProtection(AreaManager.Areas.get(name), name);
				}
				ChatUtil.sendMessage(sender, "Areas cleaned up and rewrited",ChatType.Region);
			}
			if (args[0].equals("redefinePosition")) {
				if (args.length >= 2) {
					if (AreaManager.Areas.containsKey(args[1])) {
						Selection selection =  AreaManager.getSelection(sender);
						if (selection.isSet()) {
							Area area = AreaManager.Areas.get(args[1]);
							AreaManager.writeProtection(args[1], selection.getStart().getX(), selection.getStart().getY(), selection.getStart().getZ(), selection.getEnd().getX(), selection.getEnd().getY(), selection.getEnd().getZ(), sender.getCommandSenderEntity().dimension,area.flags);
							ChatUtil.sendMessage(sender, "Region position redefined",ChatType.Region);
						} else {
							ChatUtil.sendMessage(sender, "2 Positions are need to redefine a region",ChatType.Region);
						}
					} else {
						ChatUtil.sendMessage(sender,"Unknown region named '"+args[1]+"'",ChatType.Region);
					}
				}
			}
			if (args[0].equals("reload")) {
				AreaManager.reloadProtection();
				ChatUtil.sendMessage(sender, "Region reloaded",ChatType.Region);
			}
			if (args[0].equals("help")) {
				ITextComponent text = new TextComponentString(TextFormatting.RED+"Usage : /region add <regionName> [Flag_Heritance: true/false]");
				sender.sendMessage(text);
				text = new TextComponentString(TextFormatting.RED+"Usage : /region remove <regionName>");
				sender.sendMessage(text);
				text = new TextComponentString(TextFormatting.RED+"Usage : /region flag <regionName> <flag> <value>");
				sender.sendMessage(text);
				text = new TextComponentString(TextFormatting.RED+"Usage : /region redefinePosition <regionName>");
				sender.sendMessage(text);
				text = new TextComponentString(TextFormatting.RED+"Usage : /region getNameHere");
				sender.sendMessage(text);
				text = new TextComponentString(TextFormatting.RED+"Usage : /region getNameAtPosition <X> <Y> <Z>");
				sender.sendMessage(text);
				text = new TextComponentString(TextFormatting.RED+"Usage : /region copyFlags <regionSource> <regionDestination>");
				sender.sendMessage(text);
				text = new TextComponentString(TextFormatting.RED+"Usage : /region list");
				sender.sendMessage(text);
				text = new TextComponentString(TextFormatting.RED+"Usage : /region getPosition <regionName>");
				sender.sendMessage(text);
				text = new TextComponentString(TextFormatting.RED+"Usage : /region reload");
				sender.sendMessage(text);
				text = new TextComponentString(TextFormatting.RED+"Usage : /region rewrite");
				sender.sendMessage(text);
				text = new TextComponentString(TextFormatting.RED+"Usage : /region help");
				sender.sendMessage(text);
			}
		} else {
			ITextComponent text = new TextComponentString(TextFormatting.RED+"Usage : /region help");
			sender.sendMessage(text);
		}
		
	}
	
	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}
	
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
    	int x = targetPos != null ? targetPos.getX() : sender.getPosition().getX();
    	int y = targetPos != null ? targetPos.getY() : sender.getPosition().getY();
    	int z = targetPos != null ? targetPos.getZ() : sender.getPosition().getZ();
    	switch (args.length) {
	    	case 1: {
	    		return getListOfStringsMatchingLastWord(args, "add","remove","reload","redefinePosition","flag","getNameHere","getNameAtPosition","list","getPosition","copyFlags", "rewrite","help");
	    	}
	    	case 2: {
	    		if (args[0].equals("flag") || args[0].equals("remove") || args[0].equals("getPosition") || args[0].equals("copyFlags") || args[0].equals("redefinePosition")) {
	    			return getListOfStringsMatchingLastWord(args, AreaManager.Areas.keySet());
	    		}
	    		return Collections.<String>emptyList();
	    	}
	    	case 3: {
	    		if (args[0].equals("flag")) {
	    			return getListOfStringsMatchingLastWord(args, AreaManager.flagDefault.keySet());
	    		}
	    		if (args[0].equals("add")) {
	    			return getListOfStringsMatchingLastWord(args, "true","false");
	    		}
	    		if (args[0].equals("copyFlags")) {
	    			return getListOfStringsMatchingLastWord(args, AreaManager.Areas.keySet());
	    		}
	    		return Collections.<String>emptyList();
	    	}
	    	default: {
	    		return Collections.<String>emptyList();
	    	}
    	}
    }

}
