package org.cyclops.evilcraft.api.broom;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.evilcraft.Configs;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.ExtendedDamageSource;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.core.broom.BroomParts;
import org.cyclops.evilcraft.core.broom.PotionEffectBroomCollision;
import org.cyclops.evilcraft.entity.item.EntityBroom;
import org.cyclops.evilcraft.item.BroomConfig;
import org.cyclops.evilcraft.item.DarkSpikeConfig;
import org.cyclops.evilcraft.item.GarmonboziaConfig;
import org.cyclops.evilcraft.item.MaceOfDistortion;

/**
 * A list of broom modifiers.
 * @author rubensworks
 */
public class BroomModifiers {

    public static final IBroomModifierRegistry REGISTRY = EvilCraft._instance.getRegistryManager().getRegistry(IBroomModifierRegistry.class);

    public static void init() {
        MinecraftForge.EVENT_BUS.register(BroomModifiers.class);
    }

    public static BroomModifier MODIFIER_COUNT;
    public static BroomModifier SPEED;
    public static BroomModifier ACCELERATION;
    public static BroomModifier MANEUVERABILITY;
    public static BroomModifier LEVITATION;

    public static BroomModifier DAMAGE;
    public static BroomModifier PARTICLES;
    public static BroomModifier FLAME;
    public static BroomModifier SMASH;
    public static BroomModifier BOUNCY;
    public static BroomModifier WITHERER;
    public static BroomModifier HUNGERER;
    public static BroomModifier KAMIKAZE;
    public static BroomModifier WITHERSHIELD;
    public static BroomModifier STURDYNESS;
    //public static BroomModifier LUCK;
    public static BroomModifier EFFICIENCY;
    public static BroomModifier SWIMMING;
    public static BroomModifier ICY;

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void afterItemsRegistered(RegistryEvent<Item> event) {
        BroomParts.loadPre();
        loadPre();
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void afterAfterItemsRegistered(RegistryEvent<Potion> event) {
        BroomParts.loadPost();
        loadPost();
    }

    protected static void loadPre() {
        // Base modifiers
        MODIFIER_COUNT = REGISTRY.registerModifier(new BroomModifier(
                new ResourceLocation(Reference.MOD_ID, "modifier_count"),
                BroomModifier.Type.ADDITIVE, 0F, 1F, 3, true,
                TextFormatting.BOLD, Helpers.RGBToInt(0, 0, 0)));
        REGISTRY.overrideDefaultModifierPart(MODIFIER_COUNT, null);
        SPEED = REGISTRY.registerModifier(new BroomModifier(
                new ResourceLocation(Reference.MOD_ID, "speed"),
                BroomModifier.Type.ADDITIVE, 0F, 100F, 3, true,
                TextFormatting.RED, Helpers.RGBToInt(230, 20, 20)));
        ACCELERATION = REGISTRY.registerModifier(new BroomModifier(
                new ResourceLocation(Reference.MOD_ID, "acceleration"),
                BroomModifier.Type.ADDITIVE, 0F, 100F, 3, true,
                TextFormatting.DARK_GRAY, Helpers.RGBToInt(20, 20, 20)));
        MANEUVERABILITY = REGISTRY.registerModifier(new BroomModifier(
                new ResourceLocation(Reference.MOD_ID, "maneuverability"),
                BroomModifier.Type.ADDITIVE, 0F, 100F, 3, true,
                TextFormatting.YELLOW, Helpers.RGBToInt(160, 160, 20)));
        LEVITATION = REGISTRY.registerModifier(new BroomModifier(
                new ResourceLocation(Reference.MOD_ID, "levitation"),
                BroomModifier.Type.ADDITIVE, 0F, 100F, 3, true,
                TextFormatting.WHITE, Helpers.RGBToInt(230, 230, 230)));

        // Optional modifiers
        DAMAGE = REGISTRY.registerModifier(new BroomModifier(
                new ResourceLocation(Reference.MOD_ID, "damage"),
                BroomModifier.Type.ADDITIVE, 0F, 100F, 3, false,
                TextFormatting.GRAY, Helpers.RGBToInt(100, 100, 100)));
        PARTICLES = REGISTRY.registerModifier(new BroomModifier(
                new ResourceLocation(Reference.MOD_ID, "particles"),
                BroomModifier.Type.ADDITIVE, 0F, 50F, 1, false,
                TextFormatting.LIGHT_PURPLE, Helpers.RGBToInt(160, 20, 160)));
        FLAME = REGISTRY.registerModifier(new BroomModifier(
                new ResourceLocation(Reference.MOD_ID, "flame"),
                BroomModifier.Type.ADDITIVE, 0F, 4F, 3, false,
                TextFormatting.GOLD, Helpers.RGBToInt(100, 100, 0)));
        SMASH = REGISTRY.registerModifier(new BroomModifier(
                new ResourceLocation(Reference.MOD_ID, "smash"),
                BroomModifier.Type.ADDITIVE, 0F, 10F, 2, false,
                TextFormatting.AQUA, Helpers.RGBToInt(20, 60, 60)));
        BOUNCY = REGISTRY.registerModifier(new BroomModifier(
                new ResourceLocation(Reference.MOD_ID, "bouncy"),
                BroomModifier.Type.ADDITIVE, 0F, 10F, 3, false,
                TextFormatting.GREEN, Helpers.RGBToInt(20, 200, 60)));
        WITHERER = REGISTRY.registerModifier(new BroomModifier(
                new ResourceLocation(Reference.MOD_ID, "witherer"),
                BroomModifier.Type.ADDITIVE, 0F, 10F, 3, false,
                TextFormatting.DARK_GRAY, Helpers.RGBToInt(20, 20, 20)));
        HUNGERER = REGISTRY.registerModifier(new BroomModifier(
                new ResourceLocation(Reference.MOD_ID, "hungerer"),
                BroomModifier.Type.ADDITIVE, 0F, 10F, 3, false,
                TextFormatting.DARK_GREEN, Helpers.RGBToInt(20, 120, 20)));
        KAMIKAZE = REGISTRY.registerModifier(new BroomModifier(
                new ResourceLocation(Reference.MOD_ID, "kamikaze"),
                BroomModifier.Type.ADDITIVE, 0F, 10F, 3, false,
                TextFormatting.DARK_GREEN, Helpers.RGBToInt(20, 120, 20)));
        WITHERSHIELD = REGISTRY.registerModifier(new BroomModifier(
                new ResourceLocation(Reference.MOD_ID, "withershield"),
                BroomModifier.Type.ADDITIVE, 0F, 5F, 4, false,
                TextFormatting.DARK_BLUE, Helpers.RGBToInt(20, 20, 120)));
        STURDYNESS = REGISTRY.registerModifier(new BroomModifier(
                new ResourceLocation(Reference.MOD_ID, "sturdyness"),
                BroomModifier.Type.ADDITIVE, 0F, 100F, 3, false,
                TextFormatting.GRAY, Helpers.RGBToInt(100, 100, 100)));
        /*LUCK = REGISTRY.registerModifier(new BroomModifier(
                new ResourceLocation(Reference.MOD_ID, "luck"),
                BroomModifier.Type.ADDITIVE, 0F, 100F, 3, false,
                TextFormatting.BLUE, Helpers.RGBToInt(30, 20, 210)));*/
        EFFICIENCY = REGISTRY.registerModifier(new BroomModifier(
                new ResourceLocation(Reference.MOD_ID, "efficiency"),
                BroomModifier.Type.ADDITIVE, 0F, 10F, 3, false,
                TextFormatting.DARK_RED, Helpers.RGBToInt(92, 29, 29)));
        SWIMMING = REGISTRY.registerModifier(new BroomModifier(
                new ResourceLocation(Reference.MOD_ID, "swimming"),
                BroomModifier.Type.ADDITIVE, 0F, 100F, 3, false,
                TextFormatting.AQUA, Helpers.RGBToInt(150, 150, 235)));
        ICY = REGISTRY.registerModifier(new BroomModifier(
                new ResourceLocation(Reference.MOD_ID, "icy"),
                BroomModifier.Type.ADDITIVE, 0F, 10F, 3, false,
                TextFormatting.WHITE, Helpers.RGBToInt(220, 220, 240)));

        // Set modifier events
        DAMAGE.addCollisionListener(new BroomModifier.ICollisionListener() {
            @Override
            public void onCollide(EntityBroom broom, Entity entity, float modifierValue) {
                float damage = (modifierValue * (float) broom.getLastPlayerSpeed()) / 50F;
                if (damage > 0) {
                    entity.attackEntityFrom(ExtendedDamageSource.broomDamage((EntityLivingBase) broom.getControllingPassenger()), damage);
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
                World world = broom.world;
                float maxHardness = modifierValue;
                float toughnessModifier = Math.min(1F, 0.5F + (broom.getModifier(BroomModifiers.STURDYNESS) / (BroomModifiers.STURDYNESS.getMaxTierValue() * 1.5F) / 2F));
                EntityLivingBase ridingEntity = broom.getControllingPassenger() instanceof EntityLivingBase ? (EntityLivingBase) broom.getControllingPassenger() : null;
                EntityPlayer player = broom.getControllingPassenger() instanceof EntityPlayer ? (EntityPlayer) broom.getControllingPassenger() : null;

                if (world.isAreaLoaded(blockpos, blockpos1)) {
                    for (int i = blockpos.getX(); i <= blockpos1.getX(); ++i) {
                        for (int j = blockpos.getY(); j <= blockpos1.getY(); ++j) {
                            for (int k = blockpos.getZ(); k <= blockpos1.getZ(); ++k) {
                                BlockPos pos = new BlockPos(i, j, k);
                                IBlockState blockState = world.getBlockState(pos);
                                Block block = blockState.getBlock();
                                if (!blockState.getBlock().isAir(blockState, world, pos) && broom.canConsume(BroomConfig.bloodUsageBlockBreak, ridingEntity)) {
                                    float hardness = blockState.getBlockHardness(world, pos);
                                    if (hardness > 0F && hardness <= maxHardness) {
                                        broom.consume(BroomConfig.bloodUsageBlockBreak, ridingEntity);
                                        if (player == null) {
                                            // The mounted entity is no player, do regular block breaking
                                            world.destroyBlock(pos, true);
                                        } else {
                                            // The mounted entity is a player, apply fortune and drop xp
                                            // Inspired by TCon's block breaking code

                                            // Destroy the block
                                            if (!broom.world.isRemote) {
                                                EntityPlayerMP playerMp = (EntityPlayerMP) player;
                                                int expToDrop = ForgeHooks.onBlockBreakEvent(world, playerMp.interactionManager.getGameType(), (EntityPlayerMP) player, pos);
                                                if (expToDrop >= 0) {
                                                    // Block breaking sequence
                                                    block.onBlockHarvested(world, pos, blockState, player);
                                                    if(block.removedByPlayer(blockState, world, pos, player, true)) {
                                                        block.onBlockDestroyedByPlayer(world, pos, blockState);
                                                        block.harvestBlock(world, player, pos, blockState, world.getTileEntity(pos), null);
                                                        block.dropXpOnBlockBreak(world, pos, expToDrop);
                                                    }

                                                    // Send block change packet to the client
                                                    playerMp.connection.sendPacket(new SPacketBlockChange(world, pos));
                                                }
                                            } else if (Minecraft.getMinecraft().objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK) {
                                                // Play sound and client-side block breaking sequence
                                                world.playBroadcastSound(2001, pos, Block.getStateId(blockState));
                                                if(block.removedByPlayer(blockState, world, pos, player, true)) {
                                                    block.onBlockDestroyedByPlayer(world, pos, blockState);
                                                }

                                                // Tell the server we are done with breaking this block
                                                Minecraft.getMinecraft().getConnection().sendPacket(new CPacketPlayerDigging(
                                                        CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, pos,
                                                        Minecraft.getMinecraft().objectMouseOver.sideHit));
                                            }
                                        }

                                        // Slow the broom down a bit
                                        broom.setLastPlayerSpeed(broom.getLastPlayerSpeed() * toughnessModifier);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });
        BOUNCY.addCollisionListener(new BroomModifier.ICollisionListener() {
            @Override
            public void onCollide(EntityBroom broom, Entity entity, float modifierValue) {
                float power = (modifierValue * (float) broom.getLastPlayerSpeed()) / 20F;
                if (power > 0) {
                    double dx = entity.posX - broom.posX;
                    double dy = entity.posY + (double)entity.getEyeHeight() - broom.posY;
                    double dz = entity.posZ - broom.posZ;
                    double d = (double) MathHelper.sqrt(dx * dx + dy * dy + dz * dz);
                    if (d != 0.0D) {
                        dx /= d;
                        dy /= d;
                        dz /= d;
                        entity.motionX += dx * power;
                        entity.motionY += dy * power;
                        entity.motionZ += dz * power;
                        if (broom.world.isRemote) {
                            MaceOfDistortion.showEntityDistored(broom.world, null, entity, (int) (power / 10F));
                        }
                    }
                }
            }
        });
        WITHERER.addCollisionListener(new PotionEffectBroomCollision(MobEffects.WITHER));
        HUNGERER.addCollisionListener(new PotionEffectBroomCollision(MobEffects.HUNGER));
        KAMIKAZE.addCollisionListener(new BroomModifier.ICollisionListener() {
            @Override
            public void onCollide(EntityBroom broom, Entity entity, float modifierValue) {
                World world = broom.world;
                float power = (modifierValue * (float) broom.getLastPlayerSpeed()) / 5F;
                if (power > 0 && broom.getControllingPassenger() != null) {
                    broom.dismountRidingEntity();
                    world.createExplosion(null, broom.posX, broom.posY, broom.posZ, power, true);
                }
            }
        });
        ICY.addCollisionListener(new PotionEffectBroomCollision(MobEffects.SLOWNESS, 2));
    }

    protected static void loadPost() {
        REGISTRY.registerModifiersItem(MODIFIER_COUNT, 1F, new ItemStack(Items.NETHER_STAR));
        if (Configs.isEnabled(GarmonboziaConfig.class)) {
            REGISTRY.registerModifiersItem(MODIFIER_COUNT, 1F, new ItemStack(GarmonboziaConfig._instance.getItemInstance()));
        }

        REGISTRY.registerModifiersItem(SPEED, 1F, new ItemStack(Items.REDSTONE));
        REGISTRY.registerModifiersItem(SPEED, 9F, new ItemStack(Blocks.REDSTONE_BLOCK));

        REGISTRY.registerModifiersItem(ACCELERATION, 1F, new ItemStack(Items.COAL));
        REGISTRY.registerModifiersItem(ACCELERATION, 9F, new ItemStack(Blocks.COAL_BLOCK));

        REGISTRY.registerModifiersItem(MANEUVERABILITY, 2F, new ItemStack(Items.GLOWSTONE_DUST));
        REGISTRY.registerModifiersItem(MANEUVERABILITY, 8F, new ItemStack(Blocks.GLOWSTONE));

        REGISTRY.registerModifiersItem(LEVITATION, 1F, new ItemStack(Items.FEATHER));

        if (Configs.isEnabled(DarkSpikeConfig.class)) {
            REGISTRY.registerModifiersItem(DAMAGE, 2F, new ItemStack(DarkSpikeConfig._instance.getItemInstance()));
        }
        REGISTRY.registerModifiersItem(DAMAGE, 1F, new ItemStack(Items.QUARTZ));

        REGISTRY.registerModifiersItem(PARTICLES, 1F, new ItemStack(Items.GUNPOWDER));

        REGISTRY.registerModifiersItem(FLAME, 1F, new ItemStack(Items.BLAZE_POWDER));

        REGISTRY.registerModifiersItem(SMASH, 1F, new ItemStack(Items.IRON_PICKAXE));
        REGISTRY.registerModifiersItem(SMASH, 5F, new ItemStack(Items.DIAMOND_PICKAXE));

        REGISTRY.registerModifiersItem(BOUNCY, 1F, new ItemStack(Items.SLIME_BALL));
        REGISTRY.registerModifiersItem(BOUNCY, 9F, new ItemStack(Blocks.SLIME_BLOCK));

        registerModifierOredictItem(STURDYNESS, 1F, "stone");
        REGISTRY.registerModifiersItem(STURDYNESS, 10F, new ItemStack(Blocks.OBSIDIAN));

        registerModifierOredictItem(EFFICIENCY, 1F, Reference.DICT_GEMDARKPOWER);

        REGISTRY.registerModifiersItem(SWIMMING, 1F, new ItemStack(Items.PRISMARINE_SHARD));
        REGISTRY.registerModifiersItem(SWIMMING, 4F, new ItemStack(Blocks.PRISMARINE, 1, 0));
        REGISTRY.registerModifiersItem(SWIMMING, 9F, new ItemStack(Blocks.PRISMARINE, 1, 1));

        REGISTRY.registerModifiersItem(ICY, 1F, new ItemStack(Blocks.ICE, 1, 0));
        REGISTRY.registerModifiersItem(ICY, 5F, new ItemStack(Blocks.PACKED_ICE, 1, 0));

        EvilCraft.clog(String.format("%s Broom modifiers can be applied!", REGISTRY.getModifiers().size()));
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (event.getEntityLiving() != null && event.getEntityLiving().getRidingEntity() instanceof EntityBroom
                && event.getSource().getImmediateSource() instanceof IProjectile) {
            EntityBroom broom = (EntityBroom) event.getEntityLiving().getRidingEntity();
            float modifierValue = broom.getModifier(BroomModifiers.WITHERSHIELD);
            if (modifierValue > 0 && modifierValue > broom.world.rand.nextInt((int) BroomModifiers.WITHERSHIELD.getMaxTierValue())) {
                event.setCanceled(true);
            }
        }
    }

    public static void registerModifierOredictItem(BroomModifier modifier, float value, String name) {
        for (ItemStack itemStack : OreDictionary.getOres(name)) {
            REGISTRY.registerModifiersItem(modifier, value, itemStack);
        }
    }

}
