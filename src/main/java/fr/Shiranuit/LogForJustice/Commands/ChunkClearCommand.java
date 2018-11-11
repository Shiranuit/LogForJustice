package fr.Shiranuit.LogForJustice.Commands;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import fr.Shiranuit.LogForJustice.Manager.ChunkManager;
import fr.Shiranuit.LogForJustice.Utils.ArrayConverter;
import fr.Shiranuit.LogForJustice.Utils.ObjectUtil;
import fr.Shiranuit.LogForJustice.Utils.Util;
import fr.Shiranuit.LogForJustice.Utils.Chat.ChatType;
import fr.Shiranuit.LogForJustice.Utils.Chat.ChatUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class ChunkClearCommand extends CommandBase {

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "lfjchunk";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		// TODO Auto-generated method stub
		return "/lfjchunk help";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length < 1) {
			throw new WrongUsageException("/lfjchunk help", new Object[0]);
		} else if (args.length >= 1) {
			if (args[0].equals("addID")) {
				if (args.length >= 2) {
					ChatUtil.sendMessage(sender,"Block added : "+ChunkManager.Add(args[1]),ChatType.Chunk);
				} else {
					ChatUtil.sendMessage(sender,"Usage : /lfjchunk addID <ID>",ChatType.LogForJustice);
				}
			} else if (args[0].equals("removeID")) {
				if (args.length >= 2) {
					ChatUtil.sendMessage(sender,"Block removed : "+ChunkManager.Remove(args[1]),ChatType.Chunk);
				} else {
					ChatUtil.sendMessage(sender,"Usage : /lfjchunk removeID <ID>",ChatType.LogForJustice);
				}
			} else if (args[0].equals("listID")) {
				ChatUtil.sendMessage(sender,"ID List: ["+String.join(", ",ChunkManager.block2remove)+"]",ChatType.Chunk);
			} else if (args[0].equals("enable")) {
				ChunkManager.Enable();
				ChatUtil.sendMessage(sender,"ClearMode Enabled",ChatType.Chunk);
			} else if (args[0].equals("disable")) {
				ChunkManager.Disable();
				ChatUtil.sendMessage(sender,"ClearMode Disabled",ChatType.Chunk);
			} else if (args[0].equals("clearWorld")) {
				if (args.length == 1) {
					ChunkManager.Clear(sender);
				} else if (args.length >= 2) {
					int height = ObjectUtil.Int(args[1].toString());
					ChunkManager.Clear(sender, height);
				}
				ChatUtil.sendMessage(sender,"Clear finished",ChatType.Chunk);
			} else if (args[0].equals("clearIDList")) {
				ChunkManager.block2remove.clear();
				ChatUtil.sendMessage(sender,"ID List cleared",ChatType.Chunk);
			} else if (args[0].equals("test")) {
				ChatUtil.sendMessage(sender, sender.getEntityWorld().getTopSolidOrLiquidBlock(sender.getPosition()).getY()+"",ChatType.Chunk);
				ChatUtil.sendMessage(sender, sender.getEntityWorld().getPrecipitationHeight(sender.getPosition()).getY()+"",ChatType.Chunk);
				ChatUtil.sendMessage(sender, sender.getEntityWorld().getHeight(sender.getPosition()).getY()+"",ChatType.Chunk);
			} else if (args[0].equals("clearAnalysed")) {
				ChunkManager.analysed.clear();
				ChatUtil.sendMessage(sender,TextFormatting.RED+"Analysed Chunks cleared",ChatType.Chunk);
			} else if (args[0].equals("help")) {
				ITextComponent text = new TextComponentString(TextFormatting.RED+"Usage : /lfjchunk addID <ID>");
				sender.sendMessage(text);
				text = new TextComponentString(TextFormatting.RED+"Usage : /lfjchunk removeID <ID>");
				sender.sendMessage(text);
				text = new TextComponentString(TextFormatting.RED+"Usage : /lfjchunk listID");
				sender.sendMessage(text);
				text = new TextComponentString(TextFormatting.RED+"Usage : /lfjchunk enable");
				sender.sendMessage(text);
				text = new TextComponentString(TextFormatting.RED+"Usage : /lfjchunk disable");
				sender.sendMessage(text);
				text = new TextComponentString(TextFormatting.RED+"Usage : /lfjchunk clearWorld [height]");
				sender.sendMessage(text);
				text = new TextComponentString(TextFormatting.RED+"Usage : /lfjchunk clearAnalysed");
				sender.sendMessage(text);
				text = new TextComponentString(TextFormatting.RED+"Usage : /lfjchunk clearIDList");
				sender.sendMessage(text);
				text = new TextComponentString(TextFormatting.RED+"Usage : /lfjchunk help");
				sender.sendMessage(text);
			}
		} else {
			throw new WrongUsageException("/lfjchunk help", new Object[0]);
		}
	}
	
	@Override
	public int getRequiredPermissionLevel() {
		return 4;
	}
	
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
    	switch (args.length) {
	    	case 1: {
	    		return getListOfStringsMatchingLastWord(args, "help","addID","removeID","listID","enable","disable","clearWorld","clearAnalysed","clearIDList");
	    	}
	    	case 2: {
	    		if (args[0].equals("addID")) {
	    			return getListOfStringsMatchingLastWord(args, Block.REGISTRY.getKeys());
	    		}
	    		if (args[0].equals("removeID")) {
	    			return getListOfStringsMatchingLastWord(args, ChunkManager.block2remove);
	    		}
	    		return Collections.<String>emptyList();
	    	}
	    	default: {
	    		return Collections.<String>emptyList();
	    	}
    	}
    }
}
