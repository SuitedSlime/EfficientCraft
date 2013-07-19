package com.suitedslime.efficientcraft.multiblock;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.stream.events.Characters;

public class Pattern {
    
    private int width;
    private int height;
    private int depth;
    private char[][][] pattern;
    private Map<Character, StructureBlock> mappings;
    
    private Pattern(int width, int height, int depth, char[][][] pattern, Map<Character, StructureBlock> mappings) {
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.pattern = pattern;
        this.mappings = mappings;
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
    
    public int getDepth() {
        return depth;
    }
    
    /**
     * Gets the pattern key for the given position. Uses relative positions to the structure's origin.
     * @param x The relative X position.
     * @param y The relative Y position.
     * @param z The relative Z position.
     */
    public char getKeyAt(int x, int y, int z) {
        return pattern[y][x][z];
    }
    
    /**
     * Gets the block representation used to check StructureBlock.isMatchingBlock(WorldBlock).
     * Uses relative positions to the structure's origin.
     * 
     * @param x The relative X position.
     * @param y The relative Y position.
     * @param z The relative Z position.
     */
    public StructureBlock getBlockAt(int x, int y, int z) {
        char key = pattern[y][x][z];
        
        if (key == ' ')
            return StructureBlock.BLOCK_ANY;
        if (key == '-')
            return StructureBlock.BLOCK_AIR;
        return mappings.get(key);
    }
    
    public static class PatternCompiler {
         
        private int width;
        private int depth;
        private List<char[][]> layers = new ArrayList<char[][]>();
        
        private List<Character> keys = new ArrayList<Character>();
        
        public PatternCompiler(int width, int depth) {
            this.width = width;
            this.depth = depth;
        }
        
        public void addLayer(char[][] layer) {
            for (int x = 0; x < width; x++) {
                for (int z = 0; z < depth; z++) {
                    char key = layer[z][z];
                    addKey(key);
                }
            }
            layers.add(layer);
        }
        
        public List<Character> getKeys() {
            return keys;
        }
        
        public Pattern compile(Map<Character, StructureBlock> mappings) {
            if (!checkMappings(mappings))
                throw new IllegalArgumentException("Wrong mapping for this pattern!");
            
            int height = layers.size();
            char[][][] pattern = layers.toArray(new char[height][width][depth]);
            return new Pattern(width, height, depth, pattern, mappings);
        }
        
        private void addKey(char key) {
            if (!getKeys().contains(key) && key != '-' && key != ' ')
                getKeys().add(key);
        }
        
        private boolean checkMappings(Map<Character, StructureBlock> mappings) {
            for (char key : getKeys()) {
                if (!mappings.containsKey(key) || mappings.get(key) == null)
                    return false;
            }
            return true;
        }
    }
}
