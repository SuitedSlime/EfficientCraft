package com.suitedslime.efficientcraft.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class IdMetaPair {

    public int id;
    public int meta;

    public IdMetaPair(int id) {
        this(id, 0);
    }

    public IdMetaPair(int id, int meta) {
        this.id = id;
        this.meta = meta;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof IdMetaPair))
            return false;
        IdMetaPair pair = (IdMetaPair) o;
        return (this.id == pair.id) && (this.meta == pair.meta);
    }

    @Override
    public int hashCode() {
        return this.meta | this.id << 16;
    }

    public Block getBlock() {
        if (this.hasValidBlockId()) {
            return Block.blocksList[this.id];
        }
        return null;
    }

    public Item getItem() {
        if (this.hasValidItemId()) {
            return Item.itemsList[this.id];
        }
        return null;
    }

    public boolean hasValidBlockId() {
        return (this.id >= 0) && (this.id < 4096);
    }

    public boolean hasValidItemId() {
        return (this.id >= 4096) && (this.id < 32000);
    }

    public boolean hasValidBlockMeta() {
        return (this.meta >= -1) && (this.meta < 16);
    }

    public boolean isValidBlock() {
        return (this.hasValidItemId()) && (this.hasValidBlockMeta());
    }

    public String toString() {
        return "[ " + this.id + ", " + this.meta + "]";
    }

}
