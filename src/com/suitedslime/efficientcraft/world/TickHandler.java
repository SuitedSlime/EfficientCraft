package com.suitedslime.efficientcraft.world;

import java.util.EnumSet;
import java.util.LinkedList;

import net.minecraft.world.IBlockAccess;

import com.suitedslime.efficientcraft.core.helper.ECHelper;
import com.suitedslime.efficientcraft.util.WorldChunk;
import com.suitedslime.efficientcraft.util.WorldCoordinate;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class TickHandler implements ITickHandler {

    private static TickHandler instance;

    public static TickHandler instance() {
        if (instance == null)
            instance = new TickHandler();
        return instance;
    }

    private LinkedList<Task> queue = new LinkedList<Task>();
    private boolean queueEmpty = true;

    @Override
    public void tickStart(EnumSet<TickType> type, Object... tickData) {
        if (queueEmpty)
            return;

        Task task = queue.poll();
        queueEmpty = queue.isEmpty();

        WorldChunk chunk = ECHelper.getChunkSurrounding(task);
        if (chunk != null) {
            if (task.doValidate) {
                return; // TODO
            } else {
                return; // TODO
            }
        }
    }

    @Override
    public void tickEnd(EnumSet<TickType> type, Object... tickData) {

    }

    @Override
    public EnumSet<TickType> ticks() {
        return EnumSet.of(TickType.WORLD);
    }

    @Override
    public String getLabel() {
        return "EC-MBS";
    }

    private void scheculeTask(IBlockAccess world, int x, int y, int z, boolean doValidate) {
        queue.offer(new Task(world, x, y, z, doValidate));
        queueEmpty = false;
    }

    private class Task extends WorldCoordinate {
        final boolean doValidate;

        public Task(IBlockAccess access, int x, int y, int z, boolean doValidate) {
            super(access, x, y, z);
            this.doValidate = doValidate;
        }
    }
}
