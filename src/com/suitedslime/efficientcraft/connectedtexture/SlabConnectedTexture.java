package com.suitedslime.efficientcraft.connectedtexture;

import net.minecraft.block.Block;
import net.minecraft.world.IBlockAccess;

import com.suitedslime.efficientcraft.util.BlockCoord;

public class SlabConnectedTexture extends CTBase {

    public SlabConnectedTexture(ConnectedTexture texture) {
        super(texture);
    }

    @Override
    public int getTextureFromMap(int map) {
        return this.textureIndexMap[map];
    }

    @Override
    public boolean canConnectOnSide(IBlockAccess blockAccess, BlockCoord coord, int side, int face) {
        BlockCoord copy = coord.copy();

        int thisId = coord.getBlockID(blockAccess);
        int block = coord.offset(side).getBlockID(blockAccess);
        int blockAbove = coord.offset(face).getBlockID(blockAccess);

        int thisMeta = copy.getMeta(blockAccess);
        int blockMeta = copy.offset(side).getMeta(blockAccess);
        int blockAboveMeta = copy.offset(face).getMeta(blockAccess);

        if (Block.blocksList[blockAbove] != null)
            return (block == thisId) && (blockMeta == thisMeta)
                    && ((blockAbove != thisId) || (blockAboveMeta != thisMeta))
                    && (!Block.blocksList[blockAbove].isOpaqueCube());

        return (block == thisId) && (blockMeta == thisMeta);
    }

}
