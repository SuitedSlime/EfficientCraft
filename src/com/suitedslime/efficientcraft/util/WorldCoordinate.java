package com.suitedslime.efficientcraft.util;

import net.minecraft.world.IBlockAccess;

public class WorldCoordinate {
	
	public final int x;
	public final int y;
	public final int z;
	private IBlockAccess access;
	
	public WorldCoordinate(IBlockAccess access, int x, int y, int z) {
		this.access = access;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public IBlockAccess getBlockAccess() {
		return access;
	}
	
	public WorldCoordinate translate(int deltaX, int deltaY, int deltaZ) {
		return new WorldCoordinate(access, x + deltaX, y + deltaY, z + deltaZ);
	}
	
	public String toString() {
		return("World Coordinate: " + x + ":" + y + ":" + z);
	}
}