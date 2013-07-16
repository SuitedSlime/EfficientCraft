package com.suitedslime.efficientcraft;

import java.util.Arrays;
import java.util.logging.Logger;

import com.suitedslime.efficientcraft.core.helper.TooltipHelper;
import com.suitedslime.efficientcraft.core.helper.TranslationHelper;
import com.suitedslime.efficientcraft.network.PacketHandler;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;

@Mod(modid = EfficientCraft.ID, name = EfficientCraft.NAME, version = EfficientCraft.VERSION, useMetadata = true)
@NetworkMod(clientSideRequired = true, serverSideRequired = false, channels = { EfficientCraft.CHANNEL }, packetHandler = PacketHandler.class)

public class EfficientCraft {
	
	// General variables
	public static final String CHANNEL = "EC";
	public static final String ID = "efficientcraft";
	public static final String NAME = "Efficient Craft";
	public static final String DOMAIN = "efficientcraft";
	public static final String PREFIX = DOMAIN + ":";
	public static final String MAJOR_VERSION = "@MAJOR@";
	public static final String MINOR_VERSION = "@MINOR@";
	public static final String REVISION_VERSION = "@REVIS@";
	public static final String VERSION = MAJOR_VERSION + "." + MINOR_VERSION + "." + REVISION_VERSION;
	
	@Mod.Instance(ID)
	public static EfficientCraft instace;
	@Mod.Metadata(ID)
	public static ModMetadata metadata;
	@SidedProxy(clientSide = "com.suitedslime.efficientcraft.ClientProxy", serverSide = "com.suitedslime.efficientcraft.CommonProxy")
	public static CommonProxy proxy;
	
	public static final Logger LOGGER = Logger.getLogger(NAME);
	
	// Directory constants
	public static final String RESOURCE_DIR = "/assets/efficientcraft/";
	public static final String LANGUAGE_DIR = RESOURCE_DIR + "languages/";
	
	public static final String TEXTURE_DIR = "textures/";
	public static final String BLOCK_DIR = TEXTURE_DIR + "blocks/";
	public static final String ITEM_DIR = TEXTURE_DIR + "item/";
	public static final String MODEL_DIR = TEXTURE_DIR + "models/";
	public static final String GUI_DIR = TEXTURE_DIR + "gui/";
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		
		// General registry
		LOGGER.setParent(FMLLog.getLogger());
		NetworkRegistry.instance().registerGuiHandler(this, proxy);
	}
	
	@EventHandler
	public void load(FMLInitializationEvent event) {
		
		// Load language files
		LOGGER.fine("Language(s) Loaded: " + TranslationHelper.loadLanguages(LANGUAGE_DIR, new String[] { "en_US" }));
		
		// Add fancy tooltips!
		TooltipHelper.initTooltips();
		
		// Write metadata information
		metadata.modId = ID;
		metadata.name = NAME;
		metadata.description = "EfficientCraft adds a bunch of tools to Minecraft to make things much easier and more interesting";
		metadata.url = "http://www.suitedslime.com/";
		metadata.logoFile = "/ec_logo.png";
		metadata.version = VERSION;
		metadata.authorList = Arrays.asList(new String[] { "SuitedSlime" });
		metadata.credits = "Please visit the website for further information.";
		metadata.autogenerated = false;
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		
	}
}