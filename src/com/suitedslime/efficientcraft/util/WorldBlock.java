package com.suitedslime.efficientcraft.util;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;

public class WorldBlock {

    private final IBlockAccess access;
    private final int x, y, z;

    public WorldBlock(IBlockAccess access, int x, int y, int z) {
        this.access = access;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Block getBlock() {
        return Block.blocksList[access.getBlockId(x, y, z)];
    }

    public int getMetadata() {
        return access.getBlockMetadata(x, y, z);
    }

    public boolean isAirBlock() {
        return access.isAirBlock(x, y, z);
    }

    public TileEntity getTileEnity() {
        return access.getBlockTileEntity(x, z, z);
    }
}