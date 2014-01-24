package evilcraft.gui.client;

import net.minecraft.entity.player.InventoryPlayer;
import evilcraft.api.gui.client.GuiContainerTankInventory;
import evilcraft.entities.tileentities.TileBloodChest;
import evilcraft.gui.container.ContainerBloodChest;

public class GuiBloodChest extends GuiContainerTankInventory<TileBloodChest> {

    private static final int TEXTUREWIDTH = 176;
    private static final int TEXTUREHEIGHT = 166;
    
    private static final int TANKWIDTH = 16;
    private static final int TANKHEIGHT = 58;
    private static final int TANKX = TEXTUREWIDTH;
    private static final int TANKY = 0;
    private static final int TANKTARGETX = 43;
    private static final int TANKTARGETY = 72;
    
    public GuiBloodChest(InventoryPlayer inventory, TileBloodChest tile) {
        super(new ContainerBloodChest(inventory, tile), tile);
        this.setTank(TANKWIDTH, TANKHEIGHT, TANKX, TANKY, TANKTARGETX, TANKTARGETY);
    }

    
}
