package fr.Shiranuit.LogForJustice.Manager;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import fr.Shiranuit.LogForJustice.Main;
import fr.Shiranuit.LogForJustice.Utils.Util;
import fr.Shiranuit.LogForJustice.Utils.Chat.ChatType;
import fr.Shiranuit.LogForJustice.Utils.Chat.ChatUtil;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.registries.GameData;

public class ChunkManager {
	public static boolean check = false;
	public static ArrayList<String> block2remove = new ArrayList<String>();
	public static ArrayList<Chunk> analysed = new ArrayList<Chunk>();
	
	public static void Enable() {
		ChunkManager.check=true;
	}
	
	public static void Disable() {
		ChunkManager.check=false;
		ChunkManager.analysed.clear();
	}
	
	public static String Add(String block) {
		String data[] = block.split("[/]");
		if (data.length >= 2) {
			try {
				int meta = Integer.valueOf(data[1]);
				if (data[1].equals("*")) {
					ChunkManager.block2remove.add(data[0]+"/*");
					return data[0]+"/*";
				} else {
					ChunkManager.block2remove.add(data[0]+"/"+meta);
					return data[0]+"/"+meta;
				}
			} catch (Exception e) {
				int meta = 0;
				if (data[1].equals("*")) {
					ChunkManager.block2remove.add(data[0]+"/*");
					return data[0]+"/*";
				} else {
					ChunkManager.block2remove.add(data[0]+"/"+meta);
					return data[0]+"/"+meta;
				}
			}
		} else {
			int meta = 0;
			ChunkManager.block2remove.add(block+"/"+meta);
			return block+"/"+meta;
		}
	}
	
	public static String Remove(String block) {
		String data[] = block.split("[/]");
		if (data.length >= 2) {
			try {
				int meta = Integer.valueOf(data[1]);
				if (data[1].equals("*")) {
					ChunkManager.block2remove.remove(data[0]+"/*");
					return data[0]+"/*";
				} else {
					ChunkManager.block2remove.remove(data[0]+"/"+meta);
					return data[0]+"/"+meta;
				}
			} catch (Exception e) {
				int meta = 0;
				if (data[1].equals("*")) {
					ChunkManager.block2remove.remove(data[0]+"/*");
					return data[0]+"/*";
				} else {
					ChunkManager.block2remove.remove(data[0]+"/"+meta);
					return data[0]+"/"+meta;
				}
			}
		} else {
			int meta = 0;
			ChunkManager.block2remove.remove(block+"/"+meta);
			return block+"/"+meta;
		}
	}
	
	
	public static void Clear(ICommandSender player, int height) {
		ChatUtil.sendMessage(player, "World number to scan ["+Main.mcserver.worlds.length+"]", ChatType.Chunk);
		int totalchunk = 0;
		for (WorldServer w : Main.mcserver.worlds){
			int chunks = w.getChunkProvider().id2ChunkMap.size();
			String name = w.provider.getDimensionType().getName();
			ChatUtil.sendMessage(player, chunks+" Chunks in world ["+name+"]", ChatType.Chunk);
			totalchunk += w.getChunkProvider().id2ChunkMap.size();
		}
		int before=0;
		ChatUtil.sendMessage(player, "Total chunks to scan : "+totalchunk, ChatType.Chunk);
		int totalblock=0;
		int scanned = 0;
		for (WorldServer w : Main.mcserver.worlds){
			Long2ObjectMap chunks = w.getChunkProvider().id2ChunkMap;
			for (Long l : chunks.keySet()) {
				Chunk c = (Chunk)chunks.get(l);
				if (!analysed.contains(c)) {
					int xs = c.x*16;
					int zs = c.z*16;
					for (int x=xs; x<xs+16; x++) {
						for (int z=zs; z<zs+16; z++) {
							for (int y=0; y<Math.max(height, 256); y++) {
								BlockPos pos = new BlockPos(x,y,z);
								IBlockState iblock = w.getBlockState(pos);
								ResourceLocation loc = Block.REGISTRY.getNameForObject(iblock.getBlock());
								String id = loc.getResourceDomain() + ":" + loc.getResourcePath()+"/"+iblock.getBlock().getMetaFromState(iblock);
								if (block2remove.contains(id)) {
									w.setBlockToAir(pos);
									totalblock++;
								}
							}
						}
					}
					scanned++;
					int percent = (int)Math.floor((double)scanned/(double)totalchunk*100.0);
					if (before != percent) {
						System.out.println("[LJFChunk] Cleaning : "+percent+"%");
						before = percent;
					}
					analysed.add(c);
				}
			}
		}
		ChatUtil.sendMessage(player, "Total block removed : "+totalblock, ChatType.Chunk);
	}
	
	
	public static void Clear(ICommandSender player) {
		ChatUtil.sendMessage(player, "World number to scan ["+Main.mcserver.worlds.length+"]", ChatType.Chunk);
		int totalchunk = 0;
		for (WorldServer w : Main.mcserver.worlds){
			int chunks = w.getChunkProvider().id2ChunkMap.size();
			String name = w.provider.getDimensionType().getName();
			ChatUtil.sendMessage(player, chunks+" Chunks in world ["+name+"]", ChatType.Chunk);
			totalchunk += w.getChunkProvider().id2ChunkMap.size();
		}
		int before=0;
		ChatUtil.sendMessage(player, "Total chunks to scan : "+totalchunk, ChatType.Chunk);
		int totalblock=0;
		int scanned = 0;
		for (WorldServer w : Main.mcserver.worlds){
			Long2ObjectMap chunks = w.getChunkProvider().id2ChunkMap;
			for (Long l : chunks.keySet()) {
				Chunk c = (Chunk)chunks.get(l);
				if (!analysed.contains(c)) {
					int xs = c.x*16;
					int zs = c.z*16;
					for (int x=xs; x<xs+16; x++) {
						for (int z=zs; z<zs+16; z++) {
							for (int y=0; y<Util.maxHeight(w, x, z); y++) {
								BlockPos pos = new BlockPos(x,y,z);
								IBlockState iblock = w.getBlockState(pos);
								ResourceLocation loc = Block.REGISTRY.getNameForObject(iblock.getBlock());
								String id = loc.getResourceDomain() + ":" + loc.getResourcePath();
								if (block2remove.contains(id+"/"+iblock.getBlock().getMetaFromState(iblock)) || block2remove.contains(id+"/*")) {
									w.setBlockToAir(pos);
									totalblock++;
								}
							}
						}
					}
					scanned++;
					int percent = (int)Math.floor((double)scanned/(double)totalchunk*100.0);
					if (before != percent) {
						System.out.println("[LFJChunk] Cleaning : "+percent+"%");
						before = percent;
					}
					analysed.add(c);
				}
			}
		}
		ChatUtil.sendMessage(player, "Total block removed : "+totalblock, ChatType.Chunk);
	}
}
