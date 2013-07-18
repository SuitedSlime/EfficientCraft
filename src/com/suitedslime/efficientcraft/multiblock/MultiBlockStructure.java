package com.suitedslime.efficientcraft.multiblock;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.suitedslime.efficientcraft.network.packet.PacketInitializeMBS;
import com.suitedslime.efficientcraft.tileentity.TileEntityStructure;
import com.suitedslime.efficientcraft.util.WorldBlock;
import com.suitedslime.efficientcraft.util.WorldChunk;
import com.suitedslime.efficientcraft.util.WorldCoordinate;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;

public abstract class MultiBlockStructure {
    
    // Unique string used to identify this multi-block during rendering.
    public final String mbsID;
    
    private int width;
    private int height;
    private int depth;
    private Pattern pattern;
    
    private boolean isSymmetricXZ;
    
    public MultiBlockStructure(String mbsID, Pattern pattern) {
        this(mbsID, pattern, false);
    }

    public MultiBlockStructure(String mbsID, Pattern pattern, boolean isSymmetricXZ) {
        this.mbsID = mbsID;
        this.pattern = pattern;
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.isSymmetricXZ = isSymmetricXZ;
    }
    
    public String getUID() {
        return mbsID;
    }
    
    public Pattern getPattern() {
        return pattern;
    }
    
    public int check(WorldChunk chunk) {
        // Compare dimensions
        if (!compareDimensions(chunk))
            return -1;
        
        // Optimization checks
        boolean isOptimized = isSymmetricXZ || width != depth;
        boolean swapXZ = isOptimized && width == chunk.getDepth();
        
        // Compare each block
        boolean[] angles = new boolean[] { true, true, !isOptimized, !isOptimized }; // 0, 180, 90, 270 degrees.
        
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                for (int z = 0; z < height; z++) {
                    WorldBlock worldBlock = !isOptimized || !swapXZ ? chunk.getBlockAt(x, y, z) : chunk.getBlockAt(z, y, x);
                    
                    if (angles[0] && !getPattern().getBlockAt(x, y, z).isMatchingBlock(worldBlock))
                        angles[0] = false;
                    
                    if (!isSymmetricXZ) {
                        if (angles[1] && !getPattern().getBlockAt(width - x - 1, y, depth - z - 1).isMatchingBlock(worldBlock))
                            angles[1] = false;
                    }
                    
                    if (!isOptimized) {
                        if (angles[2] && !getPattern().getBlockAt(z, y, x).isMatchingBlock(worldBlock))
                            angles[2] = false;
                        
                        if(angles[3] && !getPattern().getBlockAt(depth - z - 1, y, width - x - 1).isMatchingBlock(worldBlock))
                            angles[3] = false;
                    }
                }
            }
        }
        
        if (isSymmetricXZ)
            return angles[0] ? 0 : -1;
        
        else {
            int retValue = angles[0] ? 0 : angles[1] ? 1 : -1;
            if (retValue == -1) {
                if (!isOptimized) {
                    retValue = angles[2] ? 2 : angles[3] ? 3 : -1;
                }
            } else if (swapXZ) {
                retValue += 2;
            }
            return retValue;
        }
    }
    
    protected boolean compareDimensions(WorldChunk chunk) {
        if (chunk.getHeight() != height)
            return false;
        
        return chunk.getWidth() == width && chunk.getDepth() == depth || chunk.getWidth() == depth && chunk.getDepth() == width;
    }
    
    public void initialize(WorldChunk chunk, int rotation) {
        WorldCoordinate coords = getCentralCoordinate(chunk, rotation);
        
        validateTileEntities(chunk, rotation, coords.x, coords.y, coords.z);
        
        if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {
            PacketInitializeMBS packet = new PacketInitializeMBS(chunk);
            PacketDispatcher.sendPacketToAllInDimension(packet.makePacket(), ((World)chunk.getBlockAccess()).provider.dimensionId);
        }
    }

    protected WorldCoordinate getCentralCoordinate(WorldChunk chunk, int rotation) {
        WorldCoordinate coords = new WorldCoordinate(chunk.getBlockAccess(), chunk.getBaseCoordinate().x, chunk.getBaseCoordinate().y, chunk.getBaseCoordinate().z);
        int centerX = chunk.getWidth() / 2;
        int centerZ = chunk.getDepth() / 2;
        return coords.translate(centerX, 0, centerZ);
    }
    
    protected void validateTileEntities(WorldChunk chunk, int rotation, int x, int y, int z) {
        TileEntity tileEntity;
        TileEntityStructure replacement = getNewCentralTileEntity();
        
        for (WorldBlock worldBlock : chunk) {
            if (worldBlock != null) {
                tileEntity = worldBlock.getTileEnity();
                if (tileEntity != null & tileEntity instanceof TileEntityStructure) {
                    ((TileEntityStructure)tileEntity).validateStructure(this, rotation, x, y, z);
                    
                    if (((TileEntityStructure)tileEntity).isCentralTileEntity()) {
                        if (replacement != null) {
                            int subID = ((TileEntityStructure)tileEntity).getSubBlock();
                            replacement.getSubBlock(subID);
                        }
                    }
                }
            }
        }
    }
}
