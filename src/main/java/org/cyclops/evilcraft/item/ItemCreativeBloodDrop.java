package org.cyclops.evilcraft.item;

import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.cyclops.cyclopscore.capability.fluid.FluidHandlerItemCapacity;
import org.cyclops.cyclopscore.helper.FluidHelpers;
import org.cyclops.cyclopscore.helper.ItemStackHelpers;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.helper.TileHelpers;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.client.particle.ParticleBloodSplash;
import org.cyclops.evilcraft.core.helper.ItemHelpers;
import org.cyclops.evilcraft.core.item.ItemBloodContainer;
import org.cyclops.evilcraft.tileentity.TileBloodStain;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Containers that holds an infinite amount of blood.
 * @author rubensworks
 *
 */
public class ItemCreativeBloodDrop extends ItemBloodContainer {
    
    private static final int MB_FILL_PERTICK = 1000;

    public ItemCreativeBloodDrop(Item.Properties properties) {
        super(properties, MB_FILL_PERTICK);
        setPlaceFluids(true);
    }
    
    @Override
    public boolean hasEffect(ItemStack itemStack){
        return ItemHelpers.isActivated(itemStack);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack itemStack, World world, List<ITextComponent> list, ITooltipFlag flag) {
        super.addInformation(itemStack, world, list, flag);
        L10NHelpers.addStatusInfo(list, ItemHelpers.isActivated(itemStack),
                getTranslationKey() + ".info.auto_supply");
    }
    
    @Override
    public void inventoryTick(ItemStack itemStack, World world, Entity entity, int par4, boolean par5) {
    	updateAutoFill(itemStack, world, entity);
        super.inventoryTick(itemStack, world, entity, par4, par5);
    }
    
    /**
     * Run an auto-fill tick for filling currently held container items from this item.
     * @param itemStack The item stack to fill from.
     * @param world The world.
     * @param entity The entity that holds this item.
     */
    public static void updateAutoFill(ItemStack itemStack, World world, Entity entity) {
        IFluidHandler source = FluidUtil.getFluidHandler(itemStack).orElse(null);
    	if(source != null && entity instanceof PlayerEntity && !world.isRemote() && ItemHelpers.isActivated(itemStack)) {
            FluidStack tickFluid = source.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE);
            if(tickFluid != null && tickFluid.getAmount() > 0) {
                PlayerEntity player = (PlayerEntity) entity;
                for(Hand hand : Hand.values()) {
                    ItemStack held = player.getHeldItem(hand);
                    IFluidHandler fluidHandler = FluidUtil.getFluidHandler(held).orElse(null);
                    if (!held.isEmpty() && held != itemStack && fluidHandler != null && player.getItemInUseCount() == 0) {
                        if (fluidHandler.fill(tickFluid, IFluidHandler.FluidAction.SIMULATE) > 0) {
                            int filled = fluidHandler.fill(new FluidStack(tickFluid.getFluid(), MB_FILL_PERTICK), IFluidHandler.FluidAction.EXECUTE);
                            source.drain(filled, IFluidHandler.FluidAction.EXECUTE);
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public double getDurabilityForDisplay(ItemStack itemStack) {
        return 1;
    }

    @Override
    public void fillItemGroup(ItemGroup itemGroup, NonNullList<ItemStack> items) {
        if (ItemStackHelpers.isValidCreativeTab(this, itemGroup)) {
            if (this.isInGroup(this.group)) {
                items.add(new ItemStack(this));
            }
        }
    }
    
    @Override
    public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
        if (context.getPlayer().isCrouching()) {
            BlockPos pos = context.getPos().add(0, 1, 0);
	        if (RegistryEntries.BLOCK_BLOOD_STAIN
                    .isValidPosition(RegistryEntries.BLOCK_BLOOD_STAIN.getDefaultState(), context.getWorld(), pos)) {
		        if (context.getWorld().isRemote()) {
		        	ParticleBloodSplash.spawnParticles(context.getWorld(), pos, 5, 1 + context.getWorld().rand.nextInt(2));
		        } else {
		            if (context.getWorld().isAirBlock(pos)) {
		                // Add new stain
		                context.getWorld().setBlockState(pos, RegistryEntries.BLOCK_BLOOD_STAIN.getDefaultState());
                    }
		            if (context.getWorld().getBlockState(pos).getBlock() == RegistryEntries.BLOCK_BLOOD_STAIN) {
                        // Add blood to existing block
                        TileHelpers.getSafeTile(context.getWorld(), pos, TileBloodStain.class)
                                .ifPresent(tile -> tile.addAmount(FluidHelpers.BUCKET_VOLUME));
                    }
                }
		        return ActionResultType.PASS;
	        }
	    }
        return super.onItemUseFirst(stack, context);
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getHeldItem(hand);
        if(!player.isCrouching()) {
            return super.onItemRightClick(world, player, hand);
        } else {
            BlockRayTraceResult target = (BlockRayTraceResult) this.rayTrace(world, player, RayTraceContext.FluidMode.ANY);
        	if(target == null || target.getType() == Type.MISS) {
        		if(!world.isRemote()) {
		            ItemHelpers.toggleActivation(itemStack);
		    	}
        	}
        }
        return MinecraftHelpers.successAction(itemStack);
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {
        return new FluidHandlerItemCapacity(stack, MB_FILL_PERTICK) {
            @Override
            public FluidStack getFluid() {
                return new FluidStack(ItemCreativeBloodDrop.this.getFluid(), MB_FILL_PERTICK / 2);
            }

            @Nonnull
            @Override
            public FluidStack drain(int maxDrain, FluidAction action) {
                return new FluidStack(getFluid(), maxDrain);
            }

            @Override
            public int fill(FluidStack resource, FluidAction doFill) {
                return resource.getAmount();
            }
        };
    }
}
