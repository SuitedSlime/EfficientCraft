package com.suitedslime.efficientcraft.block;

import java.util.Set;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.suitedslime.efficientcraft.CreativeTabEC;
import com.suitedslime.efficientcraft.api.tool.ToolType;
import com.suitedslime.efficientcraft.block.base.BlockGeneric;
import com.suitedslime.efficientcraft.block.base.SubBlock;
import com.suitedslime.efficientcraft.interfaces.IAcceptsTool;
import com.suitedslime.efficientcraft.world.TickHandler;

public class BlockStructure extends BlockGeneric implements IAcceptsTool {

    public BlockStructure(int id) {
        super(id, Material.iron);
        setHardness(2.0F);
    }
    
    @Override
    protected Set<? extends SubBlock> getSubBlocks() {
        return null;
    }
    
    protected void scheduleUpdate(World world, int x, int y, int z, boolean doValidate) {
        TickHandler.instance().scheduleTask(world, x, y, z, doValidate);
    }
    
    public int idPicked(World world, int x, int y, int z) {
        return this.blockID;
    }
    
    public int getDamageValue(World world, int x, int y, int z) {
        return world.getBlockMetadata(x, y, z);
    }
    

    @Override
    public boolean accepts(ToolType tool) {
        return tool == ToolType.WRENCH;
    }
    
    @Override
    public boolean onToolUse(World world, int x, int y, int z, EntityPlayer entityPlayer, ItemStack itemStack) {
        scheduleUpdate(world, x, y, z, true);
        return true;
    }
}
