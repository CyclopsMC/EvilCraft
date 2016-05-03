package org.cyclops.evilcraft.modcompat.waila;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaEntityAccessor;
import mcp.mobius.waila.api.IWailaEntityProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import org.cyclops.evilcraft.entity.item.EntityBroom;

import java.util.List;

/**
 * Waila data provider for broom info.
 * @author rubensworks
 *
 */
public class BroomInfoDataProvider implements IWailaEntityProvider {

    @Override
    public Entity getWailaOverride(IWailaEntityAccessor accessor, IWailaConfigHandler config) {
        return null;
    }

    @Override
    public List<String> getWailaHead(Entity entity, List<String> currenttip, IWailaEntityAccessor accessor, IWailaConfigHandler config) {
        return currenttip;
    }

    @Override
    public List<String> getWailaBody(Entity entity, List<String> currenttip, IWailaEntityAccessor accessor, IWailaConfigHandler config) {
        if(entity instanceof EntityBroom && config.getConfig(Waila.getBroomInfoConfigID())) {
            ItemStack broomStack = ((EntityBroom) entity).getBroomStack();
            broomStack.getItem().addInformation(broomStack, accessor.getPlayer(), currenttip, false);
        }
        return currenttip;
    }

    @Override
    public List<String> getWailaTail(Entity entity, List<String> currenttip, IWailaEntityAccessor accessor, IWailaConfigHandler config) {
        return currenttip;
    }

    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, Entity ent, NBTTagCompound tag, World world) {
        return tag;
    }
}
