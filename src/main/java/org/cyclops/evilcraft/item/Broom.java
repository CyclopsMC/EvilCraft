package org.cyclops.evilcraft.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.config.configurable.ConfigurableItem;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.api.broom.BroomModifier;
import org.cyclops.evilcraft.api.broom.BroomModifiers;
import org.cyclops.evilcraft.api.broom.IBroomPart;
import org.cyclops.evilcraft.core.broom.BroomParts;
import org.cyclops.evilcraft.entity.item.EntityBroom;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Item for the {@link EntityBroom}.
 * @author rubensworks
 *
 */
public class Broom extends ConfigurableItem {
    
    private static Broom _instance = null;
    
    private static final float Y_SPAWN_OFFSET = 1.5f;
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static Broom getInstance() {
        return _instance;
    }

    public Broom(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig);
        this.maxStackSize = 1;
    }
    
    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (!world.isRemote && player.ridingEntity == null) {
            player.posY += Y_SPAWN_OFFSET;
            
            EntityBroom broom = new EntityBroom(world, player.posX, player.posY, player.posZ);
            broom.setBroomStack(stack);
            // Spawn and mount the broom
            world.spawnEntityInWorld(broom);
            broom.mountEntity(player);
            
            stack.stackSize--;
        }
        
        return stack;
    }
    
    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos blockPos, EnumFacing side, float hitX, float hitY, float hitZ) {
    	if (!world.isRemote && player.isSneaking()) {
            EntityBroom entityBroom = new EntityBroom(world, blockPos.getX() + 0.5, blockPos.getY() + Y_SPAWN_OFFSET, blockPos.getZ() + 0.5);
            entityBroom.setBroomStack(stack);
    		world.spawnEntityInWorld(entityBroom);
    		
    		// We don't consume the broom when in creative mode
    		if (!player.capabilities.isCreativeMode)
    		    stack.stackSize--;
    		
    		return true;
    	}
    	
    	return false;
    }

    @Override
    public EnumRarity getRarity(ItemStack itemStack) {
        return EnumRarity.RARE;
    }

    public Collection<IBroomPart> getParts(ItemStack itemStack) {
        return BroomParts.REGISTRY.getBroomParts(itemStack);
    }

    public Map<BroomModifier, Float> getModifiers(ItemStack itemStack) {
        return BroomModifiers.REGISTRY.getModifiers(itemStack);
    }

    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean par4) {
        super.addInformation(itemStack, entityPlayer, list, par4);
        if(MinecraftHelpers.isShifted()) {
            list.add(EnumChatFormatting.ITALIC + L10NHelpers.localize("broom.parts.evilcraft.types.name"));
            for (IBroomPart part : getParts(itemStack)) {
                list.add("  " + L10NHelpers.localize(part.getUnlocalizedName() + ".name"));
            }
            list.add(EnumChatFormatting.ITALIC + L10NHelpers.localize("broom.modifiers.evilcraft.name"));
            for (Map.Entry<BroomModifier, Float> entry : getModifiers(itemStack).entrySet()) {
                list.add("  " + L10NHelpers.localize(entry.getKey().getUnlocalizedName()) + ": " + entry.getValue());
            }

        } else {
            list.add(EnumChatFormatting.ITALIC + L10NHelpers.localize("broom.evilcraft.shiftinfo"));
        }
    }
}
