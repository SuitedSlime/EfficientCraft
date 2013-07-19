package com.suitedslime.efficientcraft.block.base;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.suitedslime.efficientcraft.tileentity.base.TileEntityGeneric;

public enum StructureComponent implements SubBlock {
    
    @Override
    public String getUnlocalizedName() {
        return null;
    }

    @Override
    public String[] getTextureFiles() {
        return null;
    }

    @Override
    public TileEntityGeneric createNewTileEntity(World world) {
        return null;
    }

    @Override
    public ItemStack toItemStack() {
        return null;
    }

}
