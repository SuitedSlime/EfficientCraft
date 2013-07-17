package com.suitedslime.efficientcraft.connectedtexture;

import net.minecraft.util.Icon;

public enum ConnectedTexture {
    
    // Connected Textures go here.
    ClearGlass("clear_glass");
    
    public String name;
    public Icon[] textureList;
    public boolean isRegistered;
    
    private ConnectedTexture(String s) {
        this.name = s;
        this.textureList = new Icon[47];
        this.isRegistered = false;
    }
}
