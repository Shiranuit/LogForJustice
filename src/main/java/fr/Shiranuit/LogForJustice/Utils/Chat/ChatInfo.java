package fr.Shiranuit.LogForJustice.Utils.Chat;

import net.minecraft.util.text.TextFormatting;

public class ChatInfo {
	private String name = "LogForJustice";
	private TextFormatting color = TextFormatting.RED;
	public ChatInfo(String name, TextFormatting color) {
		this.name = name;
		this.color = color;
	}
	
	public String getName() {
		return this.name;
	}
	
	public TextFormatting getColor() {
		return this.color;
	}
}
