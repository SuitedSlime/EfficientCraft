package com.suitedslime.efficientcraft.connectedtexture;

public interface IConnectedTexture {

    /**
     * Return the textureset to use for this side and metadata.
     */
    ConnectedTexture getTextureType(int side, int meta);

    /**
     * Returns the connected texture renderer used by this block.
     */
    CTBase getTextureRenderer(int side, int meta);
}
