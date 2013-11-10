package evilcraft.render;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class ExtendedTileEntity extends TileEntity {
    private boolean init = false;

    public ExtendedTileEntity() {
        

    }

    @Override
    public void updateEntity() {
        if (!init && !isInvalid()) {
            initialize();
            init = true;
        }
    }

    @Override
    public void invalidate() {
        init = false;
        super.invalidate();
    }

    public void initialize() {
        //Utils.handleBufferedDescription(this);
    }

    public void sendNetworkUpdate() {
        /*if (CoreProxy.proxy.isSimulating(worldObj)) {
            CoreProxy.proxy.sendToPlayers(getUpdatePacket(), worldObj, xCoord, yCoord, zCoord, DefaultProps.NETWORK_UPDATE_RANGE);
        }*/
    }

    public World getWorld() {
        return worldObj;
    }
}
