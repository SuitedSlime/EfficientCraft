package com.suitedslime.efficientcraft.tileentity;

import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;

import com.suitedslime.efficientcraft.core.helper.ECHelper;
import com.suitedslime.efficientcraft.multiblock.MultiBlockStructure;
import com.suitedslime.efficientcraft.network.packet.PacketClientData;
import com.suitedslime.efficientcraft.tileentity.base.TileEntityGeneric;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class TileEntityStructure extends TileEntityGeneric {
    
    // The coordinates of the central Tile Entity
    protected int targetX, targetY, targetZ;
    
    // Is this Tile Entity part of the structure?
    protected boolean isValidStructure = false;
    
    // The orientation of the multi-block as a whole.
    protected int rotation;
    
    // Stores the ID of the current multi-block this tile entity is part of.
    protected String mbsID = "";
    
    public void validateStructure(MultiBlockStructure mbs, int rotation, int x, int y, int z) {
        
    }
    
    public void invalidateStructure() {
        isValidStructure = false;
        mbsID = "";
    }
    
    public boolean isValidStructure() {
        return isValidStructure;
    }
    
    public boolean isCentralTileEntity() {
        return isValidStructure() && xCoord == targetX && yCoord == targetY && zCoord == targetZ;
    }
    
    public TileEntityStructure getCentralTileEntity() {
        if (isValidStructure) {
            if (this.isCentralTileEntity()) {
                return this;
            }
            return (TileEntityStructure) worldObj.getBlockTileEntity(targetX, targetY, targetZ);
        }
        return null;
    }
    
    public MultiBlockStructure getMBS() {
        return null;
    }
    
    public int getRotation() {
        return rotation;
    }
    
    public abstract boolean onBlockActivateBy(EntityPlayer enitytPlayer, int side, float xOff, float yOff, float zOff);
    
    //TODO onBlockUpdate
    
    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        NBTTagCompound tag = nbt.getCompoundTag("structure");
        isValidStructure = tag.getBoolean("isPart");
        targetX = tag.getInteger("targetX");
        targetY = tag.getInteger("targetY");
        targetZ = tag.getInteger("targetZ");
        rotation = tag.getInteger("rotation");
        mbsID = tag.getString("mbsID");
    }
    
    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        NBTTagCompound tag = new NBTTagCompound();
        tag.setBoolean("isPart", isValidStructure);
        tag.setInteger("targetX", targetX);
        tag.setInteger("targetY", targetY);
        tag.setInteger("targetZ", targetZ);
        tag.setInteger("rotation", rotation);
        tag.setString("mbsID", mbsID);
        nbt.setTag("structure", tag);
    }
    
    @SuppressWarnings("static-access")
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return this.INFINITE_EXTENT_AABB;
    }
    
    public void sendDataToClient(String key, Object data) {
        PacketClientData packet = new PacketClientData(xCoord, yCoord, zCoord, key, data);
        ECHelper.sendToPlayers(packet.makePacket(), this);
    }
    
    public static TileEntityStructure createNewPlaceholderTE() {
        return new TileStructurePlaceholder();
    }
    
    public static class TileStructurePlaceholder extends TileEntityStructure {
        
        public HashMap<String, Object> fakeDataMappings = new HashMap<String, Object>();
        
        public void readClientData(String key, Object value) {
            fakeDataMappings.put(key, value);
        }

        @Override
        public boolean onBlockActivateBy(EntityPlayer enitytPlayer, int side, float xOff, float yOff, float zOff) {
            return false;
        }
        
    }
    
}
