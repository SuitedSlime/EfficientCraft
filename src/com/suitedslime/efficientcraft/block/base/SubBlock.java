package com.suitedslime.efficientcraft.block.base;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.suitedslime.efficientcraft.tileentity.base.TileEntityGeneric;

public interface SubBlock {
    
    /**
     * Gets the unlocalized name for this sub-block. 
     */
    String getUnlocalizedName();
    
    /**
     * Gets the texture files for this sub-block.
     * 
     * The array must contain the files for the files register.
     * The textures in the following order: bottom, top, front, right, back, left.
     * 
     * @return An string array with the length of 6.
     */
    String[] getTextureFiles();
    
    /**
     * Creates a new TileEntityGeneric for thsi sub-block.
     * Must not return null.
     * 
     * @param world The world in which the tileEntity will be placed.
     */
    TileEntityGeneric createNewTileEntity(World world);
    
    ItemStack toItemStack();
}
