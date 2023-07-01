package org.cyclops.evilcraft.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
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

    protected boolean isUsable(ItemStack itemStack, Player player) {
        return canConsume(1, itemStack, player);
    }

    @Override
    public boolean hurtEnemy(ItemStack itemStack, LivingEntity attacked, LivingEntity attacker) {
        if(attacker instanceof Player && isUsable(itemStack, (Player) attacker)) {
            FluidUtil.getFluidHandler(itemStack).orElseGet(null).drain(hitUsage, IFluidHandler.FluidAction.EXECUTE);
        }
        return true;
    }

    @Override
    public boolean onLeftClickEntity(ItemStack itemStack, Player player, Entity entity) {
        return !isUsable(itemStack, player);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(ItemStack itemStack) {
        return this.maximumCharge * (this.powerLevels - getPower(itemStack));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if(ItemPowerableHelpers.onPowerableItemItemRightClick(itemStack, world, player, this.powerLevels, true)) {
            return MinecraftHelpers.successAction(itemStack);
        } else {
            if(isUsable(itemStack, player)) {
                player.startUsingItem(hand);
                return MinecraftHelpers.successAction(itemStack);
            } else {
                if(world.isClientSide()) {
                    animateOutOfEnergy(world, player);
                    return new InteractionResultHolder<ItemStack>(InteractionResult.FAIL, itemStack);
                }
            }
        }
        return new InteractionResultHolder<ItemStack>(InteractionResult.PASS, itemStack);
    }

    @Override
    public void onUseTick(Level level, LivingEntity player, ItemStack itemStack, int duration) {
        Level world = player.level();
        if(world.isClientSide() && duration % 2 == 0) {
            showUsingItemTick(world, itemStack, player, duration);
        }
        super.onUseTick(level, player, itemStack, duration);
    }

    @OnlyIn(Dist.CLIENT)
    protected void showUsingItemTick(Level world, ItemStack itemStack, LivingEntity entity, int duration) {
        int itemUsedCount = getUseDuration(itemStack) - duration;
        double area = getArea(itemUsedCount);
        int points = (int) (Math.pow(area, 0.55)) * 2 + 1;
        int particleChance = 5 * (this.powerLevels - getPower(itemStack));
        for(double point = -points; point <= points; point++) {
            for(double pointHeight = -points; pointHeight <= points; pointHeight+=0.5F) {
                if(world.random.nextInt(particleChance) == 0) {
                    double u = Math.PI * (point / points);
                    double v = -2 * Math.PI * (pointHeight / points);

                    double xOffset = Math.cos(u) * Math.sin(v) * area;
                    double yOffset = Math.sin(u) * area;
                    double zOffset = Math.cos(v) * area;

                    double xCoord = entity.getX();
                    double yCoord = entity.getY() + entity.getEyeHeight()
                            - (Minecraft.getInstance().player == entity ? 0.5D : 1.5D);
                    double zCoord = entity.getZ();

                    double particleX = xCoord + xOffset - world.random.nextFloat() * area / 4 - 0.5F;
                    double particleY = yCoord + yOffset - world.random.nextFloat() * area / 4 - 0.5F;
                    double particleZ = zCoord + zOffset - world.random.nextFloat() * area / 4 - 0.5F;

                    float particleMotionX = (float) (xOffset * 10);
                    float particleMotionY = (float) (yOffset * 10);
                    float particleMotionZ = (float) (zOffset * 10);

                    Minecraft.getInstance().levelRenderer.addParticle(
                            new ParticleDistortData((float) area * 3), false,
                            particleX, particleY, particleZ,
                            particleMotionX, particleMotionY, particleMotionZ);

                    if(world.random.nextInt(10) == 0) {
                        int spread = 10;
                        float scale2 = 0.3F - world.random.nextFloat() * 0.2F;
                        float r = 1.0F * world.random.nextFloat();
                        float g = 0.2F + 0.01F * world.random.nextFloat();
                        float b = 0.1F + 0.5F * world.random.nextFloat();
                        float ageMultiplier2 = 20;

                        double motionX = spread - world.random.nextDouble() * 2 * spread;
                        double motionY = spread - world.random.nextDouble() * 2 * spread;
                        double motionZ = spread - world.random.nextDouble() * 2 * spread;

                        Minecraft.getInstance().levelRenderer.addParticle(
                                new ParticleBlurTargettedEntityData(r, g, b, scale2, ageMultiplier2, entity.getId()), false,
                                particleX, particleY, particleZ,
                                motionX, motionY, motionZ);
                    }
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    protected void showUsedItemTick(Level world, LivingEntity player, int power) {
        int particles = (power + 1) * (power + 1) * (power + 1) * 10;
        for(int i = 0; i < particles; i++) {
            double x = player.getX() - 0.5F + world.random.nextDouble();
            double y = player.getY() + player.getEyeHeight() - 1F + world.random.nextDouble();
            double z = player.getZ() - 0.5F + world.random.nextDouble();

            double particleMotionX = (-1 + world.random.nextDouble() * 2) * (power + 1) / 2;
            double particleMotionY = (-1 + world.random.nextDouble() * 2) * (power + 1) / 2;
            double particleMotionZ = (-1 + world.random.nextDouble() * 2) * (power + 1) / 2;

            float r = 1.0F * world.random.nextFloat();
            float g = 0.2F + 0.01F * world.random.nextFloat();
            float b = 0.1F + 0.5F * world.random.nextFloat();

            Minecraft.getInstance().levelRenderer.addParticle(
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
    public void releaseUsing(ItemStack itemStack, Level world, LivingEntity entity, int itemInUseCount) {
        if(entity instanceof Player) {
            IFluidHandlerItemCapacity fluidHandler = FluidHelpers.getFluidHandlerItemCapacity(itemStack).orElse(null);
            Player player = (Player) entity;
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
                if (world.isClientSide()) {
                    showUsedItemTick(world, entity, getPower(itemStack));
                }
            } else if (world.isClientSide()) {
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
    protected abstract void use(Level world, LivingEntity entity, int itemUsedCount, int power);

    @OnlyIn(Dist.CLIENT)
    protected void animateOutOfEnergy(Level world, LivingEntity entity) {
        double xCoord = entity.getX();
        double yCoord = entity.getY();
        double zCoord = entity.getZ();

        float particleMotionX = world.random.nextFloat() * 0.2F - 0.1F;
        float particleMotionY = 0.2F;
        float particleMotionZ = world.random.nextFloat() * 0.2F - 0.1F;
        world.addParticle(ParticleTypes.SMOKE, xCoord, yCoord, zCoord, particleMotionX, particleMotionY, particleMotionZ);

        world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.NOTE_BLOCK_BASEDRUM.value(),
                SoundSource.NEUTRAL, 0.5F, 0.4F / (world.random.nextFloat() * 0.4F + 0.8F));
    }

    @Override
    public int getEnchantmentValue() {
        return 15;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack itemStack) {
        if (slot == EquipmentSlot.MAINHAND) {
            return ImmutableMultimap.of(Attributes.ATTACK_DAMAGE,
                    new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", this.meleeDamage, AttributeModifier.Operation.ADDITION));
        }
        return super.getAttributeModifiers(slot, itemStack);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack itemStack, Level world, List<Component> list, TooltipFlag flag) {
        ItemPowerableHelpers.addPreInformation(itemStack, list);
        super.appendHoverText(itemStack, world, list, flag);
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
