package com.suitedslime.efficientcraft.block.base;

import java.util.List;
import java.util.Random;
import java.util.Set;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

import com.suitedslime.efficientcraft.tileentity.base.TileEntityGeneric;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class BlockGeneric extends BlockContainer {

    public BlockGeneric(int id, Material material) {
        super(id, material);
    }
    
    /**
     * A set containing all the SubBlock objects for this block.
     */
    protected abstract Set<? extends SubBlock> getSubBlocks();
    
    /**
     * Gets the sub-blocks as an array of SubBlock
     * @return
     */
    public SubBlock[] getSubBlocksArray() {
        Set<? extends SubBlock> subBlocks = getSubBlocks();
        return subBlocks.toArray(new SubBlock[subBlocks.size()]);
    }
    
    /**
     * Gets the sub-block index from an ItemStack damage value. 
     */
    public int getSubBlockIndex(ItemStack itemStack) {
        return itemStack.getItemDamage();
    }
    
    @Override
    public final TileEntity createNewTileEntity(World world) {
        return TileEntityGeneric.createReplaceableTE();
    }
    
    public TileEntity createSpecificTileEntity(World world, int x, int y, int z, NBTTagCompound nbt, int subBlock) {
        if (subBlock == -1)
            return null;
        
        SubBlock sub = getSubBlocksArray()[subBlock];
        return sub.createNewTileEntity(world);
    }
    
    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLivingBase, ItemStack itemStack) {
        // Set the front side to the meta-data
        ForgeDirection front = null;
        int metadata = front.ordinal();
        world.setBlockMetadataWithNotify(x, y, z, metadata, 3);
        
        // Determine sub-block
        int subBlock = getSubBlockIndex(itemStack);
        
        // Create and set the tileEntity
        TileEntityGeneric tileEntity = getSubBlocksArray()[subBlock].createNewTileEntity(world);
        world.setBlockTileEntity(x, y, z, tileEntity);
        
        // Set the sub-block to the tileEntity
        tileEntity.setSubBlock(subBlock);
    }
    
    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void getSubBlocks(int id, CreativeTabs creativeTabs, List list) {
        // Register all the sub-blocks
        for (SubBlock subBlock : getSubBlocks()) {
            list.add(subBlock.toItemStack());
        }
    }
    
    @Override
    public void onBlockHarvested(World world, int x, int y, int z, int meta, EntityPlayer entityPlayer) {
        if (entityPlayer.capabilities.isCreativeMode)
            return;
        
        TileEntityGeneric tileEntity = (TileEntityGeneric) world.getBlockTileEntity(x, y, z);
        
        if (tileEntity != null) {
            int sub = tileEntity.getSubBlock();
            SubBlock subBlock = getSubBlocksArray()[sub];
            this.dropBlockAsItem_do(world, x, y, z, subBlock.toItemStack());
        }
    }
    
    @Override
    public int quantityDropped(Random random) {
        return 0;
    }
    
    @SideOnly(Side.CLIENT)
    Icon[][] icons;
    
    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int side, int metadata) {
        // Used only for rendering the block in inventory
        return icons[metadata][side];
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public Icon getBlockTexture(IBlockAccess blockAccess, int x, int y, int z, int side) {
        TileEntityGeneric tileEntity = (TileEntityGeneric) blockAccess.getBlockTileEntity(x, y, z);
        
        if (tileEntity == null)
            return null; // To prevent null pointers. Shouldn't be needed.
        
        int subBlock = tileEntity.getSubBlock();
        if (subBlock == -1)
            return null; // Invalid sub-block, ahead of rendering the 'correct' TE
        int front = blockAccess.getBlockMetadata(y, y, z);
        
        // Assumes that no component will ever be facing up/down.
        if (side == 0 || side == 1)
            return getIcon(side, subBlock);
        
        // Iterate through the possible orientations
        ForgeDirection currentSide = ForgeDirection.getOrientation(front);
        
        for (int i = 2; i < 6; i++) {
            if (currentSide.ordinal() == side)
                return getIcon(i, subBlock);
            
            currentSide = currentSide.getRotation(ForgeDirection.DOWN); // Rotate to the right.
        }
        
        return null; // Texture not found.
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister) {
        SubBlock[] subBlocks = getSubBlocksArray();
        icons = new Icon[subBlocks.length][6];
        
        int i = 0;
        for (SubBlock subBlock : subBlocks) {
            String[] textures = subBlock.getTextureFiles();
            for (int e = 0; e < textures.length; e++) {
                if (textures[e] != null)
                    icons[i][e] = iconRegister.registerIcon(textures[e]);
            }
            i++;
        }
    }
}
