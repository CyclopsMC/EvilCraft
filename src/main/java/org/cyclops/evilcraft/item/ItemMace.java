package org.cyclops.evilcraft.item;

import com.google.common.collect.Multimap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.cyclops.cyclopscore.capability.fluid.IFluidHandlerItemCapacity;
import org.cyclops.cyclopscore.helper.FluidHelpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.client.particle.ParticleBlurTargettedEntityData;
import org.cyclops.evilcraft.client.particle.ParticleDistortData;
import org.cyclops.evilcraft.client.particle.ParticleExplosionExtendedData;
import org.cyclops.evilcraft.core.item.ItemBloodContainer;

import java.util.List;

/**
 * An abstract powerable mace.
 * @author rubensworks
 *
 */
public abstract class ItemMace extends ItemBloodContainer {

    private final int hitUsage;
    private final int maximumCharge;
    private final int powerLevels;
    private final float meleeDamage;

    public ItemMace(Properties builder, int containerSize, int hitUsage, int maximumCharge,
                    int powerLevels, float meleeDamage) {
        super(builder, containerSize);
        this.hitUsage = hitUsage;
        this.maximumCharge = maximumCharge;
        this.powerLevels = powerLevels;
        this.meleeDamage = meleeDamage;
    }
    
    protected boolean isUsable(ItemStack itemStack, PlayerEntity player) {
        return canConsume(1, itemStack, player);
    }
    
    @Override
    public boolean hitEntity(ItemStack itemStack, LivingEntity attacked, LivingEntity attacker) {
        if(attacker instanceof PlayerEntity && isUsable(itemStack, (PlayerEntity) attacker)) {
            FluidUtil.getFluidHandler(itemStack).orElseGet(null).drain(hitUsage, IFluidHandler.FluidAction.EXECUTE);
        }
        return true;
    }
    
    @Override
    public boolean onLeftClickEntity(ItemStack itemStack, PlayerEntity player, Entity entity) {
        return !isUsable(itemStack, player);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }
    
    @Override
    public int getUseDuration(ItemStack itemStack) {
        return this.maximumCharge * (this.powerLevels - getPower(itemStack));
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getHeldItem(hand);
        if(ItemPowerableHelpers.onPowerableItemItemRightClick(itemStack, world, player, this.powerLevels, true)) {
            return MinecraftHelpers.successAction(itemStack);
        } else {
            if(isUsable(itemStack, player)) {
                player.setActiveHand(hand);
                return MinecraftHelpers.successAction(itemStack);
            } else {
                if(world.isRemote()) {
                    animateOutOfEnergy(world, player);
                    return new ActionResult<ItemStack>(ActionResultType.FAIL, itemStack);
                }
            }
        }
        return new ActionResult<ItemStack>(ActionResultType.PASS, itemStack);
    }
    
    @Override
    public void onUsingTick(ItemStack itemStack, LivingEntity player, int duration) {
        World world = player.world;
        if(world.isRemote() && duration % 2 == 0) {
            showUsingItemTick(world, itemStack, player, duration);
        }
        super.onUsingTick(itemStack, player, duration);
    }
    
    @OnlyIn(Dist.CLIENT)
    protected void showUsingItemTick(World world, ItemStack itemStack, LivingEntity entity, int duration) {
        int itemUsedCount = getUseDuration(itemStack) - duration;
        double area = getArea(itemUsedCount);
        int points = (int) (Math.pow(area, 0.55)) * 2 + 1;
        int particleChance = 5 * (this.powerLevels - getPower(itemStack));
        for(double point = -points; point <= points; point++) {
            for(double pointHeight = -points; pointHeight <= points; pointHeight+=0.5F) {
                if(random.nextInt(particleChance) == 0) {
                    double u = Math.PI * (point / points);
                    double v = -2 * Math.PI * (pointHeight / points);

                    double xOffset = Math.cos(u) * Math.sin(v) * area;
                    double yOffset = Math.sin(u) * area;
                    double zOffset = Math.cos(v) * area;

                    double xCoord = entity.getPosX();
                    double yCoord = entity.getPosY() + entity.getEyeHeight()
                            - (Minecraft.getInstance().player == entity ? 0.5D : 1.5D);
                    double zCoord = entity.getPosZ();

                    double particleX = xCoord + xOffset - world.rand.nextFloat() * area / 4 - 0.5F;
                    double particleY = yCoord + yOffset - world.rand.nextFloat() * area / 4 - 0.5F;
                    double particleZ = zCoord + zOffset - world.rand.nextFloat() * area / 4 - 0.5F;

                    float particleMotionX = (float) (xOffset * 10);
                    float particleMotionY = (float) (yOffset * 10);
                    float particleMotionZ = (float) (zOffset * 10);

                    Minecraft.getInstance().worldRenderer.addParticle(
                            new ParticleDistortData((float) area * 3), false,
                            particleX, particleY, particleZ,
                            particleMotionX, particleMotionY, particleMotionZ);

                    if(world.rand.nextInt(10) == 0) {
                        int spread = 10;
                        float scale2 = 0.3F - world.rand.nextFloat() * 0.2F;
                        float r = 1.0F * world.rand.nextFloat();
                        float g = 0.2F + 0.01F * world.rand.nextFloat();
                        float b = 0.1F + 0.5F * world.rand.nextFloat();
                        float ageMultiplier2 = 20;

                        double motionX = spread - world.rand.nextDouble() * 2 * spread;
                        double motionY = spread - world.rand.nextDouble() * 2 * spread;
                        double motionZ = spread - world.rand.nextDouble() * 2 * spread;

                        Minecraft.getInstance().worldRenderer.addParticle(
                                new ParticleBlurTargettedEntityData(r, g, b, scale2, ageMultiplier2, entity.getEntityId()), false,
                                particleX, particleY, particleZ,
                                motionX, motionY, motionZ);
                    }
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    protected void showUsedItemTick(World world, LivingEntity player, int power) {
        int particles = (power + 1) * (power + 1) * (power + 1) * 10;
        for(int i = 0; i < particles; i++) {
            double x = player.getPosX() - 0.5F + world.rand.nextDouble();
            double y = player.getPosY() + player.getEyeHeight() - 1F + world.rand.nextDouble();
            double z = player.getPosZ() - 0.5F + world.rand.nextDouble();

            double particleMotionX = (-1 + world.rand.nextDouble() * 2) * (power + 1) / 2;
            double particleMotionY = (-1 + world.rand.nextDouble() * 2) * (power + 1) / 2;
            double particleMotionZ = (-1 + world.rand.nextDouble() * 2) * (power + 1) / 2;

            float r = 1.0F * world.rand.nextFloat();
            float g = 0.2F + 0.01F * world.rand.nextFloat();
            float b = 0.1F + 0.5F * world.rand.nextFloat();

            Minecraft.getInstance().worldRenderer.addParticle(
                    new ParticleExplosionExtendedData(r, g, b, 0.3F), false,
                    x, y, z, particleMotionX, particleMotionY, particleMotionZ);
        }
    }
    
    /**
     * The area of effect for the given in use count (counting up per tick).
     * @param itemUsedCount The amount of ticks the item was active.
     * @return The area of effect.
     */
    protected double getArea(int itemUsedCount) {
        return itemUsedCount / 5 + 2.0D;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack itemStack, World world, LivingEntity entity, int itemInUseCount) {
        if(entity instanceof PlayerEntity) {
            IFluidHandlerItemCapacity fluidHandler = FluidHelpers.getFluidHandlerItemCapacity(itemStack).orElse(null);
            PlayerEntity player = (PlayerEntity) entity;
            // Actual usage length
            int itemUsedCount = getUseDuration(itemStack) - itemInUseCount;

            // Calculate how much blood to drain
            int toDrain = itemUsedCount * fluidHandler.getCapacity() * (getPower(itemStack) + 1)
                    / (getUseDuration(itemStack) * this.powerLevels);
            FluidStack consumed = consume(toDrain, itemStack, player);
            int consumedAmount = consumed == null ? 0 : consumed.getAmount();

            // Recalculate the itemUsedCount depending on how much blood is available
            itemUsedCount = consumedAmount * getUseDuration(itemStack) / fluidHandler.getCapacity();

            // Only do something if there is some blood left
            if (consumedAmount > 0) {
                // This will perform an effect to entities in a certain area,
                // depending on the itemUsedCount.
                use(world, entity, itemUsedCount, getPower(itemStack));
                if (world.isRemote()) {
                    showUsedItemTick(world, entity, getPower(itemStack));
                }
            } else if (world.isRemote()) {
                animateOutOfEnergy(world, entity);
            }
        }
    }

    /**
     * The usage action after charging the mace.
     * @param world The world
     * @param entity The using entity
     * @param itemUsedCount The charge count
     * @param power The configured power level
     */
    protected abstract void use(World world, LivingEntity entity, int itemUsedCount, int power);
    
    @OnlyIn(Dist.CLIENT)
    protected void animateOutOfEnergy(World world, LivingEntity entity) {
        double xCoord = entity.getPosX();
        double yCoord = entity.getPosY();
        double zCoord = entity.getPosZ();

        float particleMotionX = world.rand.nextFloat() * 0.2F - 0.1F;
        float particleMotionY = 0.2F;
        float particleMotionZ = world.rand.nextFloat() * 0.2F - 0.1F;
        world.addParticle(ParticleTypes.SMOKE, xCoord, yCoord, zCoord, particleMotionX, particleMotionY, particleMotionZ);
        
        world.playSound(null, entity.getPosX(), entity.getPosY(), entity.getPosZ(), SoundEvents.BLOCK_NOTE_BLOCK_BASEDRUM,
                SoundCategory.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
    }
    
    @Override
    public int getItemEnchantability() {
        return 15;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public Multimap getAttributeModifiers(EquipmentSlotType slot, ItemStack itemStack) {
        Multimap multimap = super.getAttributeModifiers(slot, itemStack);
        if (slot == EquipmentSlotType.MAINHAND) {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", this.meleeDamage, AttributeModifier.Operation.ADDITION));
        }
        return multimap;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack itemStack, World world, List<ITextComponent> list, ITooltipFlag flag) {
        ItemPowerableHelpers.addPreInformation(itemStack, list);
        super.addInformation(itemStack, world, list, flag);
        ItemPowerableHelpers.addPostInformation(itemStack, list);
    }

    /**
     * Get the power level of the given ItemStack.
     * @param itemStack The item to check.
     * @return The power this Mace currently has.
     */
    public int getPower(ItemStack itemStack) {
        return ItemPowerableHelpers.getPower(itemStack);
    }

}
