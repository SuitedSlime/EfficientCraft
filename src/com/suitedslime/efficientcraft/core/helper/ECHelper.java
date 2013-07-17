package com.suitedslime.efficientcraft.core.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.world.IBlockAccess;

import com.suitedslime.efficientcraft.util.IdMetaPair;
import com.suitedslime.efficientcraft.util.WorldChunk;
import com.suitedslime.efficientcraft.util.WorldCoordinate;

public abstract class ECHelper {

    /**
     * If a block can be considered able to be part of the MBS.
     * 
     * @param blockID
     *            The ID of the block. Must be lower than 4096
     * @see Block#blocksList#length
     */
    public static boolean isStructureBlock(int blockID) {
        return blockID != 0 && Block.blocksList[blockID] instanceof BlockStructure;
    }

    public static boolean isStructureBlock(IBlockAccess access, int x, int y, int z) {
        int id = access.getBlockId(x, y, z);
        return isStructureBlock(id);
    }

    public static boolean containsStructureBlock(WorldCoordinate coords, int deltaX, int deltaY, int deltaZ, int width,
            int height, int depth) {
        for (int i = 0; i < width; i++) {
            int x = coords.x + deltaX + i;
            for (int j = 0; j < height; j++) {
                int y = coords.y + deltaY + j;
                for (int k = 0; k < depth; k++) {
                    int z = coords.z + deltaZ + k;

                    if (ECHelper.isStructureBlock(coords.getBlockAccess(), x, y, z))
                        return true;
                }
            }
        }
        return false;
    }

    /**
     * Gets the region that encloses a structure that is potentially a MBS.
     * 
     * The region grows in width, height and depth recursively as long as their
     * is a structure block within the immediate surroundings of the chunk.
     * 
     * @param coords
     *            The initial coordinates
     */
    private static final int lowerX = 0;
    private static final int upperX = 1;
    private static final int lowerY = 2;
    private static final int upperY = 3;
    private static final int lowerZ = 4;
    private static final int upperZ = 5;

    public static WorldChunk getChunkSurrounding(WorldCoordinate coords) {
        int[] measures = new int[] { 0, 0, 0, 0, 0, 0 };
        int[] newMeasures;
        int i = 0; // This is to prevent infinite loops. Max dimensions:
                   // 25*25*25

        // Get the measures by recursion
        while (i++ < 25 && !Arrays.equals(measures, newMeasures = getNewMeasures(coords, measures))) {
            measures = newMeasures;
        }

        WorldCoordinate start = coords.translate(-measures[lowerX], -measures[lowerY], -measures[lowerZ]);
        WorldCoordinate end = coords.translate(-measures[upperX], -measures[upperY], -measures[upperZ]);

        return WorldChunk.getChunk(start, end);
    }

    private static int[] getNewMeasures(WorldCoordinate coords, int[] measures) {
        int width = measures[lowerX] + measures[upperZ] + 1;
        int height = measures[lowerY] + measures[upperY] + 1;
        int depth = measures[lowerZ] + measures[upperZ] + 1;

        int[] array = measures.clone();

        // Check X-Axis (lower)
        if (containsStructureBlock(coords, -measures[lowerX] - 1, -measures[lowerY], -measures[lowerZ], 1, height,
                depth))
            array[lowerX]++;

        // Check X-Axis (upper)
        if (containsStructureBlock(coords, -measures[upperX] + 1, -measures[lowerY], -measures[lowerZ], 1, height,
                depth))
            array[upperX]++;

        // Check Y-Axis (lower)
        if (containsStructureBlock(coords, -measures[lowerX], -measures[lowerY] - 1, -measures[lowerZ], width, 1, depth))
            array[lowerY]++;

        // Check Y-Axis (upper)
        if (containsStructureBlock(coords, -measures[lowerX], -measures[upperY] + 1, -measures[lowerZ], width, 1, depth))
            array[upperY]++;

        // Check Z-Axis (lower)
        if (containsStructureBlock(coords, -measures[lowerX], -measures[lowerY], -measures[lowerZ] - 1, width, height,
                1))
            array[lowerZ]++;

        // Check Z-Axis (upper)
        if (containsStructureBlock(coords, -measures[lowerX], -measures[upperY], -measures[upperZ] + 1, width, height,
                1))
            array[upperZ]++;

        return array;
    }

    /**
     * Register a tooltip.
     * 
     * @return The tooltip map.
     */
    private static Map<IdMetaPair, List<String>> tooltipMap = new HashMap<IdMetaPair, List<String>>();

    public static void registerTooltip(int id, int meta, String line) {
        IdMetaPair pair = new IdMetaPair(id, meta);

        if (tooltipMap.get(pair) == null) {
            List<String> temp = new ArrayList<String>();
            temp.add(line);
            tooltipMap.put(pair, temp);
        } else {
            tooltipMap.get(pair).add(line);
        }
    }

    public static Map<IdMetaPair, List<String>> getTooltipMap() {
        return tooltipMap;
    }
}
