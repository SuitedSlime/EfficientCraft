package com.suitedslime.efficientcraft.multiblock;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import com.suitedslime.efficientcraft.util.WorldBlock;

public abstract class StructureBlock {
    
    public abstract boolean isMatchingBlock(WorldBlock worldBlock);
    
    public static final StructureBlock BLOCK_AIR = new StructureBlock() {
        @Override
        public boolean isMatchingBlock(WorldBlock worldBlock) {
            return worldBlock.isAirBlock();
        }
    };
    
    public static final StructureBlock BLOCK_ANY = new StructureBlock() {
        @Override
        public boolean isMatchingBlock(WorldBlock worldBlock) {
            return !worldBlock.isAirBlock();
        }
    };
    
    public static StructureBlock wrapBlock(Block block) {
        return new GenericStructureBlock(block.blockID, 0);
    }
    
    public static StructureBlock wrapBlock(Block block, int metadata) {
        return new GenericStructureBlock(block.blockID, metadata & 0xF);
    }
    
    public static StructureBlock wrapBlock(ItemStack itemStack) {
        if (itemStack != null && itemStack.itemID < Block.blocksList.length) {
            int metadata = itemStack.getItem().getMetadata(itemStack.getItemDamage());
            return new GenericStructureBlock(itemStack.itemID, metadata);
        }
        return null;
    }
    
    public static class GenericStructureBlock extends StructureBlock {
        
        private int blockID = 0;
        private int blockMetadata = 0;
        
        public GenericStructureBlock(int blockID, int metadata) {
            this.blockID = blockID;
            this.blockMetadata = metadata;
        }
        
        @Override
        public boolean isMatchingBlock(WorldBlock worldBlock) {
            return worldBlock.getBlock() != null && worldBlock.getBlock().blockID == blockID && worldBlock.getMetadata() == blockMetadata;
        }
    }
}
