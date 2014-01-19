package evilcraft.gui.client;

import net.minecraft.entity.player.InventoryPlayer;
import evilcraft.api.gui.client.GuiContainerTankInventory;
import evilcraft.entities.tileentities.TileBloodChest;
import evilcraft.gui.container.ContainerBloodChest;

public class GuiBloodChest extends GuiContainerTankInventory {
    
    private static final int TANKHEIGHT = 58;
    private static final int TANKWIDTH = 16;
    private static final int TANKX = 43;
    private static final int TANKY = 72;
    
    public GuiBloodChest(InventoryPlayer inventory, TileBloodChest tile) {
        super(new ContainerBloodChest(inventory, tile), tile);
        this.setTank(TANKHEIGHT, TANKWIDTH, TANKX, TANKY);
    }

    
}
