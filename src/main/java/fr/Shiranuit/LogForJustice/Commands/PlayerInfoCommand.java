package fr.Shiranuit.LogForJustice.Commands;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import javax.annotation.Nullable;

import com.mojang.authlib.GameProfile;

import fr.Shiranuit.LogForJustice.Main;
import fr.Shiranuit.LogForJustice.Grade.RankInfo;
import fr.Shiranuit.LogForJustice.Manager.RankManager;
import fr.Shiranuit.LogForJustice.Utils.Util;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class PlayerInfoCommand extends CommandBase {

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "playerinfo";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		// TODO Auto-generated method stub
		return "/playerinfo <playername>";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length >= 1) {
			EntityPlayer player = Util.playerByName(args[0]);
			if (player != null) {
				String rankName = RankManager.rankName(player);
				RankInfo info = RankManager.grades.get(rankName);
				NumberFormat formatter = new DecimalFormat("#0.00");
				sender.sendMessage(new TextComponentString(""));
				sender.sendMessage(new TextComponentString(TextFormatting.GOLD+"==================[ Player Infos ]=================="));
				sender.sendMessage(new TextComponentString(TextFormatting.GOLD+"Name: "+TextFormatting.WHITE+player.getName()));
				sender.sendMessage(new TextComponentString(TextFormatting.GOLD+"Health: "+TextFormatting.RED+player.getHealth() + "/" + player.getMaxHealth()));
				sender.sendMessage(new TextComponentString(TextFormatting.GOLD+"Food: "+TextFormatting.DARK_GREEN+player.getFoodStats().getFoodLevel()));
				sender.sendMessage(new TextComponentString(TextFormatting.GOLD+"Coordinates: "+TextFormatting.LIGHT_PURPLE+"[X:"+formatter.format(player.posX)+" Y:"+formatter.format(player.posY)+" Z:"+formatter.format(player.posZ)+"]"));
				sender.sendMessage(new TextComponentString(TextFormatting.GOLD+"Chunk Coordinates: "+TextFormatting.LIGHT_PURPLE+"[X:"+player.chunkCoordX+" Z:"+player.chunkCoordZ+"]"));
				sender.sendMessage(new TextComponentString(TextFormatting.GOLD+"Dimension: "+TextFormatting.GREEN+""+player.dimension));
				sender.sendMessage(new TextComponentString(TextFormatting.GOLD+"UUID: "+TextFormatting.WHITE+player.getGameProfile().getId().toString()));
				sender.sendMessage(new TextComponentString(TextFormatting.GOLD+"OP: "+(Util.isOp(player) ? TextFormatting.DARK_GREEN+"true" : TextFormatting.DARK_RED+"false")));
				sender.sendMessage(new TextComponentString(TextFormatting.GOLD+"Vanished: "+(Main.VanishedPlayers.contains(player.getName()) ? TextFormatting.DARK_GREEN+"true" : TextFormatting.DARK_RED+"false")));
				sender.sendMessage(new TextComponentString(TextFormatting.GOLD+"Invulnerable: "+((rankName.equals("default") && RankManager.invulnerable) ? TextFormatting.DARK_GREEN+"true" : TextFormatting.DARK_RED+"false")));
				sender.sendMessage(new TextComponentString(TextFormatting.GOLD+"RankName: "+TextFormatting.WHITE+rankName));
				sender.sendMessage(new TextComponentString(TextFormatting.GOLD+"RankDisplayname: "+info.displayname.replace("&", "\247")));
				sender.sendMessage(new TextComponentString(TextFormatting.GOLD+"RankPower: "+TextFormatting.GREEN+info.power));
				sender.sendMessage(new TextComponentString(TextFormatting.GOLD+"Gamemode: "+(player.isSpectator() ? TextFormatting.GRAY+"Spectator" : (player.isCreative() ? TextFormatting.RED+"Creative" : TextFormatting.GREEN+"Survival"))));
				try {
				sender.sendMessage(new TextComponentString(TextFormatting.GOLD+"IP Address: "+TextFormatting.WHITE+((EntityPlayerMP)player).getPlayerIP()));
				} catch (Exception e) {

				}
			}
		}
	}
	
	@Override
	public int getRequiredPermissionLevel() {
		return 4;
	}

	@Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
		return getListOfStringsMatchingLastWord(args, Util.playersListWithVanished());
    }
	
}
