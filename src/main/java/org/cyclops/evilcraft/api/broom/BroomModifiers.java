package org.cyclops.evilcraft.api.broom;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.ExtendedDamageSource;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.entity.item.EntityBroom;
import org.cyclops.evilcraft.item.DarkSpikeConfig;
import org.cyclops.evilcraft.item.GarmonboziaConfig;

/**
 * A list of broom modifiers.
 * @author rubensworks
 */
public class BroomModifiers {

    public static final IBroomModifierRegistry REGISTRY = EvilCraft._instance.getRegistryManager().getRegistry(IBroomModifierRegistry.class);

    public static BroomModifier MODIFIER_COUNT;
    public static BroomModifier SPEED;
    public static BroomModifier ACCELERATION;
    public static BroomModifier MANEUVERABILITY;
    public static BroomModifier LEVITATION;

    public static BroomModifier DAMAGE;
    public static BroomModifier PARTICLES;
    public static BroomModifier FLAME;
    public static BroomModifier SMASH;

    public static void loadPre() {
        // Base modifiers
        MODIFIER_COUNT = REGISTRY.registerModifier(new BroomModifier(
                new ResourceLocation(Reference.MOD_ID, "modifier_count"),
                BroomModifier.Type.ADDITIVE, 0F, 1F, 3, true));
        SPEED = REGISTRY.registerModifier(new BroomModifier(
                new ResourceLocation(Reference.MOD_ID, "speed"),
                BroomModifier.Type.ADDITIVE, 0F, 100F, 3, true));
        ACCELERATION = REGISTRY.registerModifier(new BroomModifier(
                new ResourceLocation(Reference.MOD_ID, "acceleration"),
                BroomModifier.Type.ADDITIVE, 0F, 100F, 3, true));
        MANEUVERABILITY = REGISTRY.registerModifier(new BroomModifier(
                new ResourceLocation(Reference.MOD_ID, "maneuverability"),
                BroomModifier.Type.ADDITIVE, 0F, 100F, 3, true));
        LEVITATION = REGISTRY.registerModifier(new BroomModifier(
                new ResourceLocation(Reference.MOD_ID, "levitation"),
                BroomModifier.Type.ADDITIVE, 0F, 100F, 3, true));

        // Optional modifiers
        DAMAGE = REGISTRY.registerModifier(new BroomModifier(
                new ResourceLocation(Reference.MOD_ID, "damage"),
                BroomModifier.Type.ADDITIVE, 0F, 100F, 3, false));
        PARTICLES = REGISTRY.registerModifier(new BroomModifier(
                new ResourceLocation(Reference.MOD_ID, "particles"),
                BroomModifier.Type.ADDITIVE, 0F, 10F, 5, false));
        FLAME = REGISTRY.registerModifier(new BroomModifier(
                new ResourceLocation(Reference.MOD_ID, "flame"),
                BroomModifier.Type.ADDITIVE, 0F, 1F, 10, false));
        SMASH = REGISTRY.registerModifier(new BroomModifier(
                new ResourceLocation(Reference.MOD_ID, "smash"),
                BroomModifier.Type.ADDITIVE, 0F, 2F, 10, false));

        // Set modifier events
        DAMAGE.addCollisionListener(new BroomModifier.ICollisionListener() {
            @Override
            public void onCollide(EntityBroom broom, Entity entity, float modifierValue) {
                float damage = (modifierValue * (float) broom.getLastPlayerSpeed()) / 50F;
                if (damage > 0) {
                    entity.attackEntityFrom(ExtendedDamageSource.broomDamage((EntityLivingBase) broom.riddenByEntity), damage);
                }
            }
        });
        FLAME.addCollisionListener(new BroomModifier.ICollisionListener() {
            @Override
            public void onCollide(EntityBroom broom, Entity entity, float modifierValue) {
                if (modifierValue > 0) {
                    entity.setFire((int) modifierValue);
                }
            }
        });
        SMASH.addTickListener(new BroomModifier.ITickListener() {
            @Override
            public void onTick(EntityBroom broom, float modifierValue) {
                if (!broom.worldObj.isRemote) {
                    double pitch = ((broom.rotationPitch + 90) * Math.PI) / 180;
                    double yaw = ((broom.rotationYaw + 90) * Math.PI) / 180;
                    double x = Math.sin(pitch) * Math.cos(yaw);
                    double z = Math.sin(pitch) * Math.sin(yaw);
                    double y = Math.cos(pitch);

                    double r = -0.1D;
                    BlockPos blockpos = new BlockPos(
                            broom.getEntityBoundingBox().minX + x + r,
                            broom.getEntityBoundingBox().minY + y + r,
                            broom.getEntityBoundingBox().minZ + z + r);
                    BlockPos blockpos1 = new BlockPos(
                            broom.getEntityBoundingBox().maxX + x - r,
                            broom.getEntityBoundingBox().maxY + y - r + 1D,
                            broom.getEntityBoundingBox().maxZ + z - r);
                    World world = broom.worldObj;
                    float maxHardness = modifierValue;
                    float breakEfficiency = (SMASH.getMaxTierValue() - modifierValue) / SMASH.getMaxTierValue() + 1F;

                    if (world.isAreaLoaded(blockpos, blockpos1)) {
                        for (int i = blockpos.getX(); i <= blockpos1.getX(); ++i) {
                            for (int j = blockpos.getY(); j <= blockpos1.getY(); ++j) {
                                for (int k = blockpos.getZ(); k <= blockpos1.getZ(); ++k) {
                                    BlockPos pos = new BlockPos(i, j, k);
                                    IBlockState blockState = world.getBlockState(pos);
                                    if (!blockState.getBlock().isAir(world, pos)) {
                                        float hardness = blockState.getBlock().getBlockHardness(world, pos);
                                        if (hardness > 0F && hardness <= maxHardness) {
                                            world.destroyBlock(pos, true);
                                            broom.setLastPlayerSpeed(broom.getLastPlayerSpeed() / breakEfficiency);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    public static void loadPost() {
        REGISTRY.registerModifiersItem(MODIFIER_COUNT, 1F, new ItemStack(Items.nether_star));
        REGISTRY.registerModifiersItem(MODIFIER_COUNT, 1F, new ItemStack(GarmonboziaConfig._instance.getItemInstance()));

        REGISTRY.registerModifiersItem(SPEED, 1F, new ItemStack(Items.redstone));
        REGISTRY.registerModifiersItem(SPEED, 9F, new ItemStack(Blocks.redstone_block));

        REGISTRY.registerModifiersItem(ACCELERATION, 1F, new ItemStack(Items.coal));
        REGISTRY.registerModifiersItem(ACCELERATION, 9F, new ItemStack(Blocks.coal_block));

        REGISTRY.registerModifiersItem(MANEUVERABILITY, 2F, new ItemStack(Items.glowstone_dust));
        REGISTRY.registerModifiersItem(MANEUVERABILITY, 8F, new ItemStack(Blocks.glowstone));

        REGISTRY.registerModifiersItem(LEVITATION, 1F, new ItemStack(Items.feather));

        REGISTRY.registerModifiersItem(DAMAGE, 2F, new ItemStack(DarkSpikeConfig._instance.getItemInstance()));
        REGISTRY.registerModifiersItem(DAMAGE, 1F, new ItemStack(Items.quartz));

        REGISTRY.registerModifiersItem(PARTICLES, 1F, new ItemStack(Items.gunpowder));

        REGISTRY.registerModifiersItem(FLAME, 1F, new ItemStack(Items.blaze_powder));

        REGISTRY.registerModifiersItem(SMASH, 1F, new ItemStack(Items.iron_pickaxe));
        REGISTRY.registerModifiersItem(SMASH, 2F, new ItemStack(Items.diamond_pickaxe));
    }

}
