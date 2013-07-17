package com.suitedslime.efficientcraft.network;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;

import com.suitedslime.efficientcraft.EfficientCraft;
import com.suitedslime.efficientcraft.network.packet.PacketEC;
import com.suitedslime.efficientcraft.network.packet.PacketInitializeMBS;
import com.suitedslime.efficientcraft.network.packet.PacketInvalidateMBS;

public enum PacketTypeHandler {

    MBS_INIT(PacketInitializeMBS.class), MBS_INVALIDATE(PacketInvalidateMBS.class);

    private Class<? extends PacketEC> clazz;

    private PacketTypeHandler(Class<? extends PacketEC> clazz) {
        this.clazz = clazz;
    }

    public static PacketEC buildPacket(byte[] data) {
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        int id = bis.read();
        DataInputStream dis = new DataInputStream(bis);
        PacketEC packet = null;

        try {
            packet = values()[id].clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        packet.readByteStream(dis);
        return packet;
    }

    public static PacketEC buildPacket(PacketTypeHandler type) {
        PacketEC packet = null;

        try {
            packet = type.clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return packet;
    }

    public static Packet fillPacket(PacketEC packet) {
        byte[] data = packet.writeToByteStrean();

        Packet250CustomPayload packet250 = new Packet250CustomPayload();
        packet250.channel = EfficientCraft.CHANNEL;
        packet250.data = data;
        packet250.length = data.length;
        packet250.isChunkDataPacket = packet.isChunkPacket;

        return packet250;
    }

    public static NBTTagCompound readNBTTagCompound(DataInputStream dis) throws IOException {
        short short1 = dis.readShort();
        if (short1 < 0) {
            return null;
        } else {
            byte[] abyte = new byte[short1];
            dis.readFully(abyte);
            return CompressedStreamTools.decompress(abyte);
        }
    }

    public static void writeNBTTagCompound(NBTTagCompound nbt, DataOutputStream dos) throws IOException {
        if (nbt == null) {
            dos.writeShort(-1);
        } else {
            byte[] abyte = CompressedStreamTools.compress(nbt);
            dos.writeShort((short) abyte.length);
            dos.write(abyte);
        }
    }
}
