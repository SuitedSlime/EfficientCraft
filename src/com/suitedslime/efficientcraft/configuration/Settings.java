package com.suitedslime.efficientcraft.configuration;

import java.io.File;

import net.minecraftforge.common.Configuration;

import com.suitedslime.efficientcraft.EfficientCraft;

import cpw.mods.fml.common.Loader;

public class Settings {

    public static final Configuration CONFIGURATION = new Configuration(new File(Loader.instance().getConfigDir(),
            EfficientCraft.NAME + ".cfg"));

    // Auto increment configuration IDs.
    public static final int BLOCK_ID_PREFIX = 1600;
    public static final int ITEM_ID_PREFIX = 11300;

    private static int NEXT_BLOCK_ID = BLOCK_ID_PREFIX;
    private static int NEXT_ITEM_ID = ITEM_ID_PREFIX;

    public static int getNextBlockID() {
        NEXT_BLOCK_ID++;
        return NEXT_BLOCK_ID;
    }

    public static int getNextItemID() {
        NEXT_ITEM_ID++;
        return NEXT_ITEM_ID;
    }

    // Efficient Craft Configuration Settings.
    public static void load() {
        CONFIGURATION.load();

        CONFIGURATION.save();
    }
}
