package evilcraft.entities.tileentities.tickaction.bloodinfuser;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.ItemFluidContainer;
import evilcraft.entities.tileentities.IConsumeProduceWithTankTile;

public class FluidContainerItemTickAction extends BloodInfuserTickAction{

    @Override
    public void onTick(IConsumeProduceWithTankTile tile, int tick) {
        if(tick >= getRequiredTicks(tile)) {
            ItemStack infuseStack = getInfuseStack(tile);
            ItemFluidContainer container = (ItemFluidContainer) infuseStack.getItem();
            int filled = container.fill(infuseStack, tile.getTank().getFluid(), true);
            tile.getTank().drain(filled, true);
        }
    }

    private int getRequiredTicks(IConsumeProduceWithTankTile tile) {
        return 10;
    }
    
}
