package com.suitedslime.efficientcraft.tileentity.base;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.suitedslime.efficientcraft.block.base.BlockGeneric;

public abstract class TileEntityGeneric extends TileEntity {
    
    private int subBlock = -1;
    
    public void setSubBlock(int subBlock) {
        this.subBlock = subBlock;
    }
    
    public int getSubBlock() {
        return subBlock;
    }
    
    @Override
    public boolean shouldRefresh(int oldId, int newId, int oldMeta, int newMeta, World world, int x, int y, int z) {
        return oldId != newId;
    }
    
    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        subBlock = nbt.getInteger("subBlock");
    }
    
    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger("subBlock", subBlock);
    }
    
    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        this.writeToNBT(nbtTagCompound);
        return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 0, nbtTagCompound);
    }
    
    @Override
    public void onDataPacket(INetworkManager nm, Packet132TileEntityData pkt) {
        readFromNBT(pkt.customParam1);
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord); // Re-render.
    }
    
    public static TileEntityGeneric createReplaceableTE() {
        return new TileGenericReplaceable();
    }
    
    static class TileGenericReplaceable extends TileEntityGeneric {
        static {
            TileEntity.addMapping(TileEntityGeneric.TileGenericReplaceable.class, "tile.replaceable.generic");
        }
        
        @Override
        public void onDataPacket(INetworkManager nm, Packet132TileEntityData pkt) {
            // Get the correct tile entity top replace this one.
            BlockGeneric block = (BlockGeneric) this.getBlockType();
            int subBlock = pkt.customParam1.getInteger("subBlock");
            TileEntity tileEntity = block.createSpecificTileEntity(this.worldObj, xCoord, yCoord, zCoord, pkt.customParam1, subBlock);
            if (tileEntity == null)
                return;
            
            worldObj.setBlockTileEntity(xCoord, yCoord, zCoord, tileEntity); // Replace the tileEntity
            tileEntity.onDataPacket(nm, pkt); // Load the data into the tileEntity
            worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord); // Re-render
        }
    }
}
