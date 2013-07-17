package com.suitedslime.efficientcraft.api.tool;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface ITool {

    public ToolType getToolType();

    public void onToolUsed(ItemStack itemStack, World world, int x, int y, int z, EntityPlayer entityPlayer);
}
