package fr.Shiranuit.LogForJustice.Utils.Chat;

import java.util.HashMap;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class ChatUtil {
	private static HashMap<ChatType, ChatInfo> chatmanager = new HashMap<ChatType, ChatInfo>();
	static {
		chatmanager.put(ChatType.LogForJustice, new ChatInfo("LogForJustice", TextFormatting.RED));
		chatmanager.put(ChatType.Chunk, new ChatInfo("LFJ Chunk", TextFormatting.DARK_GREEN));
		chatmanager.put(ChatType.Rank, new ChatInfo("LFJ Rank", TextFormatting.DARK_AQUA));
		chatmanager.put(ChatType.Region, new ChatInfo("LFJ Region", TextFormatting.LIGHT_PURPLE));
		chatmanager.put(ChatType.CommandSpy, new ChatInfo("LFJ CommandSpy", TextFormatting.GOLD));
		chatmanager.put(ChatType.CreativeSpy, new ChatInfo("LFJ CreativeSpy", TextFormatting.GOLD));
		chatmanager.put(ChatType.MessageSpy, new ChatInfo("LFJ MessageSpy", TextFormatting.GOLD));
		chatmanager.put(ChatType.Vanish, new ChatInfo("LFJ Vanish", TextFormatting.AQUA));
		chatmanager.put(ChatType.Status, new ChatInfo("LFJ Status", TextFormatting.RED));
		chatmanager.put(ChatType.Plot, new ChatInfo("LFJ Plot", TextFormatting.BLUE));
		chatmanager.put(ChatType.PlayerInfo, new ChatInfo("LFJ PlayerInfo", TextFormatting.RED));
		chatmanager.put(ChatType.Money, new ChatInfo("LFJ Money", TextFormatting.GOLD));
	}
	
	public static void sendMessage(EntityPlayer player, String message, ChatType type) {
		ChatInfo info;
		if (chatmanager.containsKey(type)) {
			info = chatmanager.get(type);
		} else {
			info = chatmanager.get(ChatType.LogForJustice);
		}
		player.sendMessage(new TextComponentString(info.getColor() + "[" + info.getName() + "] "+message));
	}
	
	public static void sendMessage(ICommandSender player, String message, ChatType type) {
		ChatInfo info;
		if (chatmanager.containsKey(type)) {
			info = chatmanager.get(type);
		} else {
			info = chatmanager.get(ChatType.LogForJustice);
		}
		player.sendMessage(new TextComponentString(info.getColor() + "[" + info.getName() + "] "+message));
	}
}
