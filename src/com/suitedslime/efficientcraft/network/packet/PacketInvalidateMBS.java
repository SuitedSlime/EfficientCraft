package com.suitedslime.efficientcraft.network.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;

import com.suitedslime.efficientcraft.network.PacketTypeHandler;
import com.suitedslime.efficientcraft.util.WorldChunk;

import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;

public class PacketInvalidateMBS extends PacketEC {

    public int x;
    public int y;
    public int z;
    public int width;
    public int length;
    public int height;

    public PacketInvalidateMBS(WorldChunk worldChunk) {
        this(worldChunk.getBaseCoordinate().x, worldChunk.getBaseCoordinate().y, worldChunk.getBaseCoordinate().z,
                worldChunk.getWidth(), worldChunk.getHeight(), worldChunk.getDepth());
    }

    public PacketInvalidateMBS(int x, int y, int z, int width, int length, int height) {
        super(PacketTypeHandler.MBS_INVALIDATE, false);

        this.x = x;
        this.y = y;
        this.z = z;
        this.width = width;
        this.length = length;
        this.height = height;
    }

    public PacketInvalidateMBS() {
        super(PacketTypeHandler.MBS_INVALIDATE, false);
    }

    @Override
    public void readData(DataInputStream data) throws IOException {
        this.x = data.readInt();
        this.y = data.readInt();
        this.z = data.readInt();
        this.width = data.readInt();
        this.length = data.readInt();
        this.height = data.readInt();
    }

    @Override
    public void writeData(DataOutputStream dos) throws IOException {
        dos.writeInt(x);
        dos.writeInt(y);
        dos.writeInt(z);
        dos.writeInt(width);
        dos.writeInt(length);
        dos.writeInt(height);
    }

    @Override
    public void execute(INetworkManager network, Player player, Side side) {
        WorldChunk worldChunk = new WorldChunk(((EntityPlayer) player).worldObj, x, y, z, width, length, height);
    }
}
