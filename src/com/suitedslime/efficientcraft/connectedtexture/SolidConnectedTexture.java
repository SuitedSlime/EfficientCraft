package com.suitedslime.efficientcraft.connectedtexture;

import net.minecraft.block.Block;
import net.minecraft.world.IBlockAccess;

import com.suitedslime.efficientcraft.util.BlockCoord;

public class SolidConnectedTexture extends CTBase {

    public SolidConnectedTexture(ConnectedTexture texture) {
        super(texture);
    }

    @Override
    public int getTextureFromMap(int map) {
        return this.textureIndexMap[map];
    }

    @Override
    public boolean canConnectOnSide(IBlockAccess block, BlockCoord coord, int side, int face) {
        int meta = coord.getMeta(block);
        BlockCoord self = coord.copy();
        Block neighbor = coord.offset(side).getBlock(block);
        BlockCoord other = coord.copy();
        Block cover = coord.offset(face).getBlock(block);
        ConnectedTexture neighborT = null;
        
        if (neighbor instanceof IConnectedTexture) {
            neighborT = ((IConnectedTexture) neighbor).getTextureType(face, meta);
            
            if (((IConnectedTexture) neighbor).getTextureRenderer(side, meta) instanceof SlabConnectedTexture)
                return false;
        }
        
        if (neighborT != null && cover != null)
            return !cover.isOpaqueCube() && this.texture.name == neighborT.name && self.blockEquals(block, other);
        else if (neighborT != null)
            return this.texture.name == neighborT.name && self.blockEquals(block, other);
        
        return false;
    }
    
}
