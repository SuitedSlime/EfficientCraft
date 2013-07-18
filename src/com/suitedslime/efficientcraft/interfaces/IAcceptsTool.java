package com.suitedslime.efficientcraft.interfaces;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.suitedslime.efficientcraft.api.tool.ToolType;

public interface IAcceptsTool {
    
    public boolean accepts(ToolType tool);
    
    public boolean onToolUse(World world, int x, int y, int z, EntityPlayer entityPlayer, ItemStack itemStack);
}
