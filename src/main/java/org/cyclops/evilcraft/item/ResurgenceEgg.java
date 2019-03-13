package org.cyclops.evilcraft.item;

import net.minecraft.block.BlockLiquid;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.configurable.ConfigurableItem;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;

import java.util.List;

/**
 * Egg to hold entities.
 * This is an alternative for mob eggs if an entity does not have one.
 * DISABLED FOR NOW.
 * @author rubensworks
 *
 */
public class ResurgenceEgg extends ConfigurableItem {
	
	private static final String NBTKEY_ENTITY = "innerEntity";
    
    private static ResurgenceEgg _instance = null;
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static ResurgenceEgg getInstance() {
        return _instance;
    }

    public ResurgenceEgg(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig);
        this.setMaxStackSize(1);
    }
    
    @Override
    public boolean hasEffect(ItemStack itemStack){
        return !isEmpty(itemStack);
    }
    
    /**
     * Get the ID of an inner entity, can be null.
     * @param itemStack The item stack.
     * @return The ID.
     */
    public ResourceLocation getEntityString(ItemStack itemStack) {
    	NBTTagCompound tag = itemStack.getTagCompound();
		if(tag != null) {
			return new ResourceLocation(tag.getString(NBTKEY_ENTITY));
		}
		return null;
    }
    
    /**
     * Check if the given egg is empty.
     * @param itemStack itemStack The item stack.
     * @return If it is empty.
     */
    public boolean isEmpty(ItemStack itemStack) {
    	return getEntityString(itemStack) == null;
    }
    
    /**
     * Put an entity in this egg.
     * @param itemStack The box.
     * @param entityString The unique string of the entity to set.
     */
    public void setEntity(ItemStack itemStack, String entityString) {
    	NBTTagCompound tag = itemStack.hasTagCompound() ? itemStack.getTagCompound() : new NBTTagCompound();
    	tag.setString(NBTKEY_ENTITY, entityString);
		itemStack.setTagCompound(tag);
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack itemStack, World world, List<String> list, ITooltipFlag flag) {
        super.addInformation(itemStack, world, list, flag);
        String content = TextFormatting.ITALIC + L10NHelpers.localize("general.info.empty");
        ResourceLocation id = getEntityString(itemStack);
		if(id != null) {
			content = L10NHelpers.getLocalizedEntityName(id.toString());
		}
		list.add(TextFormatting.BOLD + L10NHelpers.localize(getTranslationKey() + ".info.content",
                TextFormatting.RESET + content));
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack itemStack = player.getHeldItem(hand);
        if (world.isRemote) {
            return new ActionResult<ItemStack>(EnumActionResult.PASS, itemStack);
        } else {
            RayTraceResult movingobjectposition = this.rayTrace(world, player, true);
            if (movingobjectposition == null) {
                return new ActionResult<ItemStack>(EnumActionResult.PASS, itemStack);
            } else {
                if (movingobjectposition.typeOfHit == RayTraceResult.Type.BLOCK) {
                    BlockPos blockPos = movingobjectposition.getBlockPos();
                    int x = blockPos.getX();
                    int y = blockPos.getY();
                    int z = blockPos.getZ();

                    if (!world.canMineBlockBody(player, blockPos)) {
                        return new ActionResult<ItemStack>(EnumActionResult.PASS, itemStack);
                    }

                    if (world.getBlockState(blockPos).getBlock() instanceof BlockLiquid) {
                        Entity entity = spawnCreature(world, getEntityString(itemStack), (double)x, (double)y, (double)z);
                        if (entity != null) {
                            if (entity instanceof EntityLivingBase && itemStack.hasDisplayName()) {
                                entity.setCustomNameTag(itemStack.getDisplayName());
                            }
                            if (!player.capabilities.isCreativeMode) {
                                itemStack.shrink(1);
                            }
                        }
                    }
                }
                return MinecraftHelpers.successAction(itemStack);
            }
        }
    }
    
    /**
     * Spawn a creature.
     * @param world The world.
     * @param entityString The unique entity string.
     * @param x X
     * @param y Y
     * @param z Z
     * @return The spawned entity, could be null if not spawnable.
     */
    public static Entity spawnCreature(World world, ResourceLocation entityString, double x, double y, double z) {
    	Entity entity = EntityList.createEntityByIDFromName(entityString, world);
        if (entity != null && entity instanceof EntityLivingBase) {
            EntityLiving entityliving = (EntityLiving)entity;
            entity.setLocationAndAngles(x, y, z, MathHelper.wrapDegrees(world.rand.nextFloat() * 360.0F), 0.0F);
            entityliving.rotationYawHead = entityliving.rotationYaw;
            entityliving.renderYawOffset = entityliving.rotationYaw;
            world.spawnEntity(entity);
            entityliving.playLivingSound();
        }
        return entity;
    }

}
