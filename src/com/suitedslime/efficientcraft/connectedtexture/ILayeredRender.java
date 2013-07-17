package com.suitedslime.efficientcraft.connectedtexture;

import net.minecraft.util.Icon;

public interface ILayeredRender {

    /**
     * Return the icon to render underneath for the given side and meta.
     */
    Icon getRenderIcon(int side, int meta);

}
