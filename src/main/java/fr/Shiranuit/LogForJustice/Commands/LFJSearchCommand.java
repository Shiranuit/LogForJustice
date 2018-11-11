package fr.Shiranuit.LogForJustice.Commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fr.Shiranuit.LogForJustice.LogForJustice.BlockInfos;
import fr.Shiranuit.LogForJustice.LogForJustice.EnumPlayerAction;
import fr.Shiranuit.LogForJustice.Manager.LogForJusticeManager;
import fr.Shiranuit.LogForJustice.Utils.Util;
import net.minecraft.block.Block;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class LFJSearchCommand extends CommandBase {
	
	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public String getName() {
		return "lfjsearch";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "/lfjsearch help";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length < 1) {
			throw new WrongUsageException("/lfjsearch help", new Object[0]);
		} else {
			if (args[0].equals("help")) {
				TextComponentString text = new TextComponentString(TextFormatting.RED+"Usage : /lfjsearch param1=... param2=... param...");
				sender.sendMessage(text);
				text = new TextComponentString(TextFormatting.RED+"Parameters : Dimension,dim,X,Y,Z,Player,SameDate,SD,BeforeDate,BD,AfterDate,AD,Action,ID,Meta,ItemID,itemMeta");
				sender.sendMessage(text);
				text = new TextComponentString(TextFormatting.RED+"Actions : BREAK, PLACE, INTERACT");
				sender.sendMessage(text);
				text = new TextComponentString(TextFormatting.RED+"DateFormat : year-month-day-hour-minute-second");
				sender.sendMessage(text);
				//(year-month-day-hour-minute-second)
			} else {
				HashMap<String, Object> arg = new HashMap<String, Object>();
				for (int i=0; i<args.length; i++) {
					String data[] = args[i].toLowerCase().split("=");
					if (data.length == 2) {
						if (data[0].equals("dimension") || data[0].equals("dim") || data[0].equals("x") || data[0].equals("y") || data[0].equals("z") || data[0].equals("id") || data[0].equals("meta") || data[0].equals("itemid") || data[0].equals("itemmeta")) {
							try {
								arg.put(data[0], Integer.valueOf(data[1]));
							} catch (Exception e) {
								TextComponentString text = new TextComponentString(TextFormatting.RED+"Formatting Error : "+data[0]+" must be a number");
								sender.sendMessage(text);
							}
						} else if (data[0].equals("samedata") || data[0].equals("sd") || data[0].equals("beforedata") || data[0].equals("bd") || data[0].equals("afterdate") || data[0].equals("ad")) {
							try {
								arg.put(data[0], Util.parseDate(data[1]));
							}catch (Exception e) {
								TextComponentString text = new TextComponentString(TextFormatting.RED+e.getMessage());
								sender.sendMessage(text);
							}
						} else {
							arg.put(data[0], data[1]);
						}
					}
				}
				HashMap<Integer, HashMap<String,  List<BlockInfos>>> bdata = LogForJusticeManager.search(arg);
				for (Integer dimension : bdata.keySet()) {
					HashMap<String,  List<BlockInfos>> log = bdata.get(dimension);
					if (log != null) {
						for (String b : log.keySet()) {
							BlockPos e = Util.BlockPosFromString(b);
							List<BlockInfos> data = log.get(b);
							if (data != null && data.size() > 0) {
								TextComponentString text = new TextComponentString(TextFormatting.DARK_AQUA+"Block changes at ["+e.getX()+", "+e.getY()+", "+e.getZ()+"] in dimension ["+dimension+"]");
								sender.sendMessage(text);
								for (BlockInfos binfo : data) {	
									if (binfo.action == EnumPlayerAction.PLACE) {
										String blockname = Block.REGISTRY.getNameForObject(Block.getBlockById(binfo.id)) + "/" + binfo.meta;
										text = new TextComponentString(TextFormatting.GOLD+binfo.date+" "+TextFormatting.RED+binfo.playername+TextFormatting.DARK_AQUA+" created "+TextFormatting.DARK_GREEN+blockname);
										sender.sendMessage(text);
									}
									if (binfo.action == EnumPlayerAction.BREAK) {
										String blockname = Block.REGISTRY.getNameForObject(Block.getBlockById(binfo.id)) + "/" + binfo.meta;
										String itemname = Item.REGISTRY.getNameForObject(Item.getItemById(binfo.itemID)) + "/" + binfo.itemMeta;
										text = new TextComponentString(TextFormatting.GOLD+binfo.date+" "+TextFormatting.RED+binfo.playername+TextFormatting.DARK_AQUA+" breaked "+TextFormatting.DARK_GREEN+blockname + TextFormatting.GOLD+" with "+TextFormatting.DARK_GREEN + itemname);
										sender.sendMessage(text);
									}
									if (binfo.action == EnumPlayerAction.INTERACT) {
										String blockname = Block.REGISTRY.getNameForObject(Block.getBlockById(binfo.id)) + "/" + binfo.meta;
										String itemname = Item.REGISTRY.getNameForObject(Item.getItemById(binfo.itemID)) + "/" + binfo.itemMeta;
										text = new TextComponentString(TextFormatting.GOLD+binfo.date+" "+TextFormatting.RED+binfo.playername+TextFormatting.DARK_AQUA+" interacted "+TextFormatting.DARK_GREEN+blockname + TextFormatting.GOLD+" with "+TextFormatting.DARK_GREEN + itemname);
										sender.sendMessage(text);
									}
								}
							}
						}
					}
				}

			}
		}
		
		
	}
}
