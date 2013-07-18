package com.suitedslime.efficientcraft.network.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.tileentity.TileEntity;

import com.suitedslime.efficientcraft.network.PacketTypeHandler;
import com.suitedslime.efficientcraft.tileentity.TileEntityStructure.TileStructurePlaceholder;

import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;

public class PacketClientData extends PacketEC {

    public int x, y, z;

    public String dataKey;
    public Object dataValue;

    public PacketClientData() {
        super(PacketTypeHandler.CLIENT_DATA, false);
    }

    public PacketClientData(int x, int y, int z, String dataKey, Object dataValue) {
        super(PacketTypeHandler.CLIENT_DATA, false);

        this.x = x;
        this.y = y;
        this.z = z;
        this.dataKey = dataKey;
        this.dataValue = dataValue;
    }

    @Override
    public void readData(DataInputStream data) throws IOException {
        this.x = data.readInt();
        this.y = data.readInt();
        this.z = data.readInt();
        this.dataKey = data.readUTF();
        this.dataValue = readObject(data);
    }

    @Override
    public void writeData(DataOutputStream dos) throws IOException {
        dos.writeInt(this.x);
        dos.writeInt(this.y);
        dos.writeInt(this.z);
        dos.writeUTF(this.dataKey);
        writeObject(this.dataValue, dos);
    }

    @Override
    public void execute(INetworkManager network, Player player, Side side) {
        if (side == Side.CLIENT) {
            TileEntity tileEntity = ((EntityPlayer)player).worldObj.getBlockTileEntity(x, y, z);
            
           if (tileEntity != null && tileEntity instanceof TileStructurePlaceholder) {
               ((TileStructurePlaceholder)tileEntity).readClientData(dataKey, dataValue);
           }
        }
    }

    public static void writeObject(Object object, DataOutputStream dos) throws IOException {
        if (object instanceof String) {
            dos.writeByte(0);
            dos.writeUTF(object.toString());
        } else if (object instanceof Byte) {
            dos.writeByte(1);
            dos.writeByte(((Byte) object).byteValue());
        } else if (object instanceof Integer) {
            dos.writeByte(2);
            dos.writeInt(((Integer) object).intValue());
        } else if (object instanceof Float) {
            dos.writeByte(3);
            dos.writeFloat(((Float) object).floatValue());
        } else if (object instanceof Double) {
            dos.writeByte(4);
            dos.writeDouble(((Double) object).doubleValue());
        }
    }

    public static Object readObject(DataInputStream data) throws IOException {
        ObjectType type = ObjectType.values()[data.readByte()];

        switch (type) {
            case DOUBLE:
                return data.readDouble();
            case FLOAT:
                return data.readFloat();
            case INTEGER:
                return data.readInt();
            case BYTE:
                return data.readByte();
            case STRING:
                return data.readUTF();
            default:
                return null;
        }
    }

    private static enum ObjectType {
        STRING, BYTE, INTEGER, FLOAT, DOUBLE;
    }
}
