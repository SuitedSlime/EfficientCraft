package com.suitedslime.efficientcraft.network.packet;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;

import com.suitedslime.efficientcraft.network.PacketTypeHandler;

import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;

public abstract class PacketEC {

    public final PacketTypeHandler packetType;

    public final boolean isChunkPacket;

    public PacketEC(PacketTypeHandler type, boolean chunk) {
        this.packetType = type;
        this.isChunkPacket = chunk;
    }

    public byte[] writeToByteStrean() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        try {
            dos.writeByte(packetType.ordinal());
            this.writeData(dos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bos.toByteArray();
    }

    public void readByteStream(DataInputStream data) {
        try {
            this.readData(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Packet makePacket() {
        return PacketTypeHandler.fillPacket(this);
    }

    public abstract void readData(DataInputStream data) throws IOException;

    public abstract void writeData(DataOutputStream dos) throws IOException;

    public abstract void execute(INetworkManager network, Player player, Side side);
}
