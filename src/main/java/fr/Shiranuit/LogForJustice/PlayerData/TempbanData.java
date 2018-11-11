package fr.Shiranuit.LogForJustice.PlayerData;

import java.util.Date;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class TempbanData {
	
	public Date time;
	
	public TempbanData(Date time) {
		this.time = time;
	}

}
