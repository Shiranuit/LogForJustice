package fr.Shiranuit.LogForJustice.Fake;

import java.util.ArrayList;
import java.util.List;

import fr.Shiranuit.LogForJustice.Main;
import fr.Shiranuit.LogForJustice.Dimension.TeleporterDim;
import fr.Shiranuit.LogForJustice.Manager.CommandSpyManager;
import fr.Shiranuit.LogForJustice.Manager.HomeManager;
import fr.Shiranuit.LogForJustice.Manager.PlayerManager;
import fr.Shiranuit.LogForJustice.Manager.RankManager;
import fr.Shiranuit.LogForJustice.PlayerData.Position;
import fr.Shiranuit.LogForJustice.Utils.ArrayConverter;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.CommandBlockBaseLogic;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class AlterateCommand implements ICommand{
	public ICommand cmd;
	
	public AlterateCommand(ICommand cmd) {
		this.cmd = cmd;
	}

	@Override
	public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_) {
		return this.cmd.isUsernameIndex(p_82358_1_, p_82358_2_);
	}

	@Override
	public int compareTo(ICommand o) {
		return this.cmd.compareTo(o);
	}

	@Override
	public String getName() {
		return this.cmd.getName();
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return this.cmd.getUsage(sender);
	}

	@Override
	public List<String> getAliases() {
		return this.cmd.getAliases();
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (sender.getCommandSenderEntity() instanceof EntityPlayer && HomeManager.back.contains(this.cmd.getName())) {
			EntityPlayer player = (EntityPlayer)sender.getCommandSenderEntity();
			Position previous = new Position(player);
			this.cmd.execute(server, sender, args);
			if (RankManager.hasPermission("back", "tp", "", -1, player)) {
				PlayerManager.setPrevious(player, previous);	
			}
		} else {
			this.cmd.execute(server, sender, args);
		}
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		if (Main.enabled) {
			if (sender instanceof CommandBlockBaseLogic) {
				return this.cmd.checkPermission(server, sender);
			} else if (sender instanceof MinecraftServer) {
				return true;
			} else if (sender.getCommandSenderEntity() instanceof EntityPlayer) {
				return RankManager.canUseCommand(this.cmd, sender);
			} else {
				return true;
			}
		} else {
			return this.cmd.checkPermission(server, sender);
		}
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
			BlockPos targetPos) {
		return this.cmd.getTabCompletions(server, sender, args, targetPos);
	}

}
