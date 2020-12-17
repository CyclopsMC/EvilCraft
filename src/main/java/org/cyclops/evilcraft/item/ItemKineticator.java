package org.cyclops.evilcraft.item;

import com.google.common.base.Predicate;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.cyclops.cyclopscore.client.particle.ParticleBlurData;
import org.cyclops.cyclopscore.helper.FluidHelpers;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.core.helper.ItemHelpers;
import org.cyclops.evilcraft.core.item.ItemBloodContainer;
import org.cyclops.evilcraft.entity.item.EntityItemUndespawnable;

import java.util.List;
import java.util.Random;

/**
 * Item that can attract items and XP orbs.
 * @author rubensworks
 *
 */
public class ItemKineticator extends ItemBloodContainer {

    private static final int POWER_LEVELS = 5;
    private static final int RANGE_PER_LEVEL = 2;
    private static final double USAGE_PER_D = 0.1;
    private static final int CONTAINER_SIZE = FluidHelpers.BUCKET_VOLUME;

    private final boolean repelling;

    public ItemKineticator(Item.Properties properties, boolean repelling) {
        super(properties, CONTAINER_SIZE);
        this.repelling = repelling;
    }
    
    public boolean isRepelling(ItemStack itemStack) {
    	return repelling;
    }
    
    private int getArea(ItemStack itemStack) {
        return RANGE_PER_LEVEL * (getPower(itemStack) + 1);
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getHeldItem(hand);
        if(!ItemPowerableHelpers.onPowerableItemItemRightClick(itemStack, world, player, POWER_LEVELS, false) && !world.isRemote()) {
            ItemHelpers.toggleActivation(itemStack);
        }
        return MinecraftHelpers.successAction(itemStack);
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
                getTranslationKey() + ".info.attraction");
        list.add(new TranslationTextComponent(getTranslationKey() + ".info.area", getArea(itemStack))
                .applyTextStyle(TextFormatting.BOLD));
    }

    /**
     * Get the power level of the given ItemStack.
     * @param itemStack The item to check.
     * @return The power this Mace currently has.
     */
    public int getPower(ItemStack itemStack) {
        return ItemPowerableHelpers.getPower(itemStack);
    }

    /**
     * Set the power level of the given ItemStack.
     * @param itemStack The item to change.
     * @param power The new power level.
     */
    public void setPower(ItemStack itemStack, int power) {
        ItemPowerableHelpers.setPower(itemStack, power);
    }
    
    @Override
    public void inventoryTick(ItemStack itemStack, World world, Entity entity, int par4, boolean par5) {
        if(entity instanceof PlayerEntity) {
            kineticate(itemStack, world, entity);
        }
        super.inventoryTick(itemStack, world, entity, par4, par5);
    }
    
    @Override
    public boolean onEntityItemUpdate(ItemStack itemStack, ItemEntity entityItem) {
        kineticate(itemStack, entityItem.world, entityItem);
        return super.onEntityItemUpdate(itemStack, entityItem);
    }
    
    @SuppressWarnings("unchecked")
    private void kineticate(ItemStack itemStack, World world, Entity entity) {
        if(ItemHelpers.isActivated(itemStack) &&(FluidUtil.getFluidContained(itemStack) != null ||
                (entity instanceof PlayerEntity && canConsume(1, itemStack, (PlayerEntity) entity))) &&
                (entity != null && !entity.isCrouching())) {
            IFluidHandler fluidHandler = FluidUtil.getFluidHandler(itemStack).orElse(null);
        	boolean repelling = isRepelling(itemStack);
            boolean isPlayer = entity instanceof PlayerEntity;
        	
            // Center of the attraction
            double x = entity.getPosX();
            double y = entity.getPosY();
            double z = entity.getPosZ();

            // Not ticking every tick.
            if(0 == world.getGameTime() % ItemKineticatorConfig.tickHoldoff) {
                // Get items in calculated area.
                int area = getArea(itemStack);
                AxisAlignedBB box = new AxisAlignedBB(x, y, z, x, y, z).grow(area);
                List<Entity> entities = world.getEntitiesInAABBexcluding(entity, box, new Predicate<Entity>() {

                    @Override
                    public boolean apply(Entity entity) {
                        return entity instanceof ItemEntity
                                || (ItemKineticatorConfig.moveXP && entity instanceof ExperienceOrbEntity);
                    }

                });

                // Move all those items in the direction of the player.
                for(Entity moveEntity : entities) {
                    if(repelling ||
                            (moveEntity instanceof ItemEntity && !((ItemEntity) moveEntity).cannotPickup()
                                    && canKineticateItem(((ItemEntity) moveEntity).getItem())) ||
                            (moveEntity instanceof ExperienceOrbEntity)) {
                        double dx = moveEntity.getPosX() - x;
                        double dy = moveEntity.getPosY() - (isPlayer ? (y + (world.isRemote() ? -1 : 1)) : y);
                        double dz = moveEntity.getPosZ() - z;
                        double strength = -0.3;
                        if (isPlayer) {
                            strength = -1;
                        }
                        if (repelling) {
                            strength /= -1;
                            if (isPlayer) {
                                strength = 0.3;
                            }
                        }

                        double d = (double) MathHelper.sqrt(dx * dx + dy * dy + dz * dz);
                        int usage = (int) Math.round(d * USAGE_PER_D);
                        if((repelling || d > 0.5D) && (usage == 0 || (fluidHandler.drain(usage, IFluidHandler.FluidAction.SIMULATE) != null) ||
                                (isPlayer && this.canConsume(usage, itemStack, (PlayerEntity) entity)))) {
                            double m = 1 / (2 * (Math.max(1, d)));
                            dx *= m;
                            dy *= m;
                            dz *= m;
                            if (world.isRemote()) {
                                showEntityMoved(world, entity, moveEntity, dx / 10, dy / 10, dz / 10);
                            } else {
                                if (moveEntity instanceof ItemEntity && d < 5.0D) {
                                    ((ItemEntity) moveEntity).setPickupDelay(repelling ? 5 : 0);
                                }
                                moveEntity.setMotion(new Vec3d(dx, dy, dz)
                                        .mul(strength, strength, strength));
                                if(moveEntity.collidedHorizontally) {
                                    moveEntity.setMotion(new Vec3d(moveEntity.getMotion().x, 0.3, moveEntity.getMotion().z));
                                }
                            }
                            // Not ticking every tick.
                            if(0 == world.getGameTime() % ItemKineticatorConfig.consumeHoldoff) {
                                if(isPlayer) {
                                    this.consume(usage, itemStack, (PlayerEntity) entity);
                                } else {
                                    fluidHandler.drain(usage, IFluidHandler.FluidAction.EXECUTE);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    protected boolean canKineticateItem(ItemStack entityItem) {
        if(entityItem == null) return false;
        return !ItemKineticatorConfig.kineticateBlacklist
                .contains(entityItem.getItem().getRegistryName().toString());
    }

    @OnlyIn(Dist.CLIENT)
    protected void showEntityMoved(World world, Entity player, Entity entity, double dx, double dy, double dz) {
        Random rand = world.rand;
        float scale = 0.05F;
        float red = rand.nextFloat() * 0.03F + 0.5F;
        float green = rand.nextFloat() * 0.03F + (rand.nextBoolean() ? 0.5F : 0.3F);
        float blue = rand.nextFloat() * 0.05F;
        float ageMultiplier = (float) (rand.nextDouble() * 2.5D + 10D);

        world.addParticle(new ParticleBlurData(red, green, blue, scale, ageMultiplier), false,
                entity.getPosX(), entity.getPosY(), entity.getPosZ(),
                -dx, -dy, -dz);
    }
    
    @Override
    public boolean hasCustomEntity(ItemStack itemStack) {
    	return true;
    }
    
    @Override
    public Entity createEntity(World world, Entity location, ItemStack itemStack) {
    	return new EntityItemUndespawnable(world, (ItemEntity) location);
    }

}
