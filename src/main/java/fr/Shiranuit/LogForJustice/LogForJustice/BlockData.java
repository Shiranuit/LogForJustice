package fr.Shiranuit.LogForJustice.LogForJustice;

import net.minecraft.util.math.BlockPos;

public class BlockData {
	public int dimension;
	public BlockPos pos;
	public BlockInfos info;
	public BlockData(int dimension, BlockPos pos, BlockInfos info) {
		this.dimension=dimension;
		this.pos=pos;
		this.info=info;
	}
}
