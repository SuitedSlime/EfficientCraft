package com.suitedslime.efficientcraft.core.helper;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class TranslationHelper {
	
	public static int loadLanguages(String languagePath, String[] languageSupported) {
		int languages = 0;
		
		// Load all the languages.
		for (String language : languageSupported) {
			LanguageRegistry.instance().loadLocalization(languagePath + language + ".properties", language, false);
			
			if (LanguageRegistry.instance().getStringLocalization("children", language) != "") {
				try {
					String[] children = LanguageRegistry.instance().getStringLocalization("children", language).split(",");
					
					for (String child : children) {
						if(child != "" || child != null) {
							LanguageRegistry.instance().loadLocalization(languagePath + language + ".properties", child, false);
							languages++;
						}
					}
				} catch (Exception e) {
					FMLLog.severe("Failed to load a child language file.");
					e.printStackTrace();
				}
			}
			languages++;
		}
		return languages;
	}
	
	public static String getLocal(String key) {
		String text = LanguageRegistry.instance().getStringLocalization(key);
		
		if (text == null || text == "") {
			text = LanguageRegistry.instance().getStringLocalization(key, "en_US");
		}
		return text;
	}
}
