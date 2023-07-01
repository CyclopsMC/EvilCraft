package org.cyclops.evilcraft.item;

import com.google.common.base.Predicate;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.registries.ForgeRegistries;
import org.cyclops.cyclopscore.client.particle.ParticleBlurData;
import org.cyclops.cyclopscore.helper.FluidHelpers;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.core.helper.ItemHelpers;
import org.cyclops.evilcraft.core.item.ItemBloodContainer;
import org.cyclops.evilcraft.entity.item.EntityItemUndespawnable;

import java.util.List;

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
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if(!ItemPowerableHelpers.onPowerableItemItemRightClick(itemStack, world, player, POWER_LEVELS, false) && !world.isClientSide()) {
            ItemHelpers.toggleActivation(itemStack);
        }
        return MinecraftHelpers.successAction(itemStack);
    }

    @Override
    public boolean isFoil(ItemStack itemStack){
        return ItemHelpers.isActivated(itemStack);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack itemStack, Level world, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemStack, world, list, flag);
        L10NHelpers.addStatusInfo(list, ItemHelpers.isActivated(itemStack),
                getDescriptionId() + ".info.attraction");
        list.add(Component.translatable(getDescriptionId() + ".info.area", getArea(itemStack))
                .withStyle(ChatFormatting.BOLD));
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
    public void inventoryTick(ItemStack itemStack, Level world, Entity entity, int par4, boolean par5) {
        if(entity instanceof Player) {
            kineticate(itemStack, world, entity);
        }
        super.inventoryTick(itemStack, world, entity, par4, par5);
    }

    @Override
    public boolean onEntityItemUpdate(ItemStack itemStack, ItemEntity entityItem) {
        kineticate(itemStack, entityItem.level(), entityItem);
        return super.onEntityItemUpdate(itemStack, entityItem);
    }

    @SuppressWarnings("unchecked")
    private void kineticate(ItemStack itemStack, Level world, Entity entity) {
        if(ItemHelpers.isActivated(itemStack) &&(FluidUtil.getFluidContained(itemStack) != null ||
                (entity instanceof Player && canConsume(1, itemStack, (Player) entity))) &&
                (entity != null && !entity.isCrouching())) {
            IFluidHandler fluidHandler = FluidUtil.getFluidHandler(itemStack).orElse(null);
            boolean repelling = isRepelling(itemStack);
            boolean isPlayer = entity instanceof Player;

            // Center of the attraction
            double x = entity.getX();
            double y = entity.getY();
            double z = entity.getZ();

            // Not ticking every tick.
            if(0 == world.getGameTime() % ItemKineticatorConfig.tickHoldoff) {
                // Get items in calculated area.
                int area = getArea(itemStack);
                AABB box = new AABB(x, y, z, x, y, z).inflate(area);
                List<Entity> entities = world.getEntities(entity, box, new Predicate<Entity>() {

                    @Override
                    public boolean apply(Entity entity) {
                        return entity instanceof ItemEntity
                                || (ItemKineticatorConfig.moveXP && entity instanceof ExperienceOrb);
                    }

                });

                // Move all those items in the direction of the player.
                for(Entity moveEntity : entities) {
                    if(repelling ||
                            (moveEntity instanceof ItemEntity && !((ItemEntity) moveEntity).hasPickUpDelay()
                                    && canKineticateItem(((ItemEntity) moveEntity).getItem())) ||
                            (moveEntity instanceof ExperienceOrb)) {
                        double dx = moveEntity.getX() - x;
                        double dy = moveEntity.getY() - (isPlayer ? (y + (world.isClientSide() ? -1 : 1)) : y);
                        double dz = moveEntity.getZ() - z;
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

                        double d = (double) Mth.sqrt((float) (dx * dx + dy * dy + dz * dz));
                        int usage = (int) Math.round(d * USAGE_PER_D);
                        if((repelling || d > 0.5D) && (usage == 0 || (fluidHandler.drain(usage, IFluidHandler.FluidAction.SIMULATE) != null) ||
                                (isPlayer && this.canConsume(usage, itemStack, (Player) entity)))) {
                            double m = 1 / (2 * (Math.max(1, d)));
                            dx *= m;
                            dy *= m;
                            dz *= m;
                            if (world.isClientSide()) {
                                showEntityMoved(world, entity, moveEntity, dx / 10, dy / 10, dz / 10);
                            } else {
                                if (moveEntity instanceof ItemEntity && d < 5.0D) {
                                    ((ItemEntity) moveEntity).setPickUpDelay(repelling ? 5 : 0);
                                }
                                moveEntity.setDeltaMovement(new Vec3(dx, dy, dz)
                                        .multiply(strength, strength, strength));
                                if(moveEntity.horizontalCollision) {
                                    moveEntity.setDeltaMovement(new Vec3(moveEntity.getDeltaMovement().x, 0.3, moveEntity.getDeltaMovement().z));
                                }
                            }
                            // Not ticking every tick.
                            if(0 == world.getGameTime() % ItemKineticatorConfig.consumeHoldoff) {
                                if(isPlayer) {
                                    this.consume(usage, itemStack, (Player) entity);
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
                .contains(ForgeRegistries.ITEMS.getKey(entityItem.getItem()).toString());
    }

    @OnlyIn(Dist.CLIENT)
    protected void showEntityMoved(Level world, Entity player, Entity entity, double dx, double dy, double dz) {
        RandomSource rand = world.random;
        float scale = 0.05F;
        float red = rand.nextFloat() * 0.03F + 0.5F;
        float green = rand.nextFloat() * 0.03F + (rand.nextBoolean() ? 0.5F : 0.3F);
        float blue = rand.nextFloat() * 0.05F;
        float ageMultiplier = (float) (rand.nextDouble() * 2.5D + 10D);

        world.addParticle(new ParticleBlurData(red, green, blue, scale, ageMultiplier), false,
                entity.getX(), entity.getY(), entity.getZ(),
                -dx, -dy, -dz);
    }

    @Override
    public boolean hasCustomEntity(ItemStack itemStack) {
        return true;
    }

    @Override
    public Entity createEntity(Level world, Entity location, ItemStack itemStack) {
        return new EntityItemUndespawnable(world, (ItemEntity) location);
    }

}
