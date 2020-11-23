package org.cyclops.evilcraft.api.broom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.play.client.CPlayerDiggingPacket;
import net.minecraft.network.play.server.SChangeBlockPacket;
import net.minecraft.potion.Effect;
import net.minecraft.potion.Effects;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.ExtendedDamageSource;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.core.broom.BroomParts;
import org.cyclops.evilcraft.core.broom.PotionEffectBroomCollision;
import org.cyclops.evilcraft.entity.item.EntityBroom;
import org.cyclops.evilcraft.item.ItemBroomConfig;
import org.cyclops.evilcraft.item.ItemMaceOfDistortion;

/**
 * A list of broom modifiers.
 * @author rubensworks
 */
public class BroomModifiers {

    public static final IBroomModifierRegistry REGISTRY = EvilCraft._instance.getRegistryManager().getRegistry(IBroomModifierRegistry.class);

    public static void init() {
        FMLJavaModLoadingContext.get().getModEventBus().register(BroomModifiers.class);
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
    public static BroomModifier STICKY;

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void afterItemsRegistered(RegistryEvent<Item> event) {
        BroomParts.loadPre();
        loadPre();
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void afterAfterItemsRegistered(RegistryEvent<Effect> event) {
        BroomParts.loadPost();
    }

    @SubscribeEvent
    public static void onTagsUpdated(TagsUpdatedEvent event) {
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
        STICKY = REGISTRY.registerModifier(new BroomModifier(
                new ResourceLocation(Reference.MOD_ID, "sticky"),
                BroomModifier.Type.ADDITIVE, 0F, 10F, 3, false,
                TextFormatting.GOLD, Helpers.RGBToInt(78, 58, 12)));

        // Set modifier events
        DAMAGE.addCollisionListener(new BroomModifier.ICollisionListener() {
            @Override
            public void onCollide(EntityBroom broom, Entity entity, float modifierValue) {
                float damage = (modifierValue * (float) broom.getLastPlayerSpeed()) / 50F;
                if (damage > 0) {
                    entity.attackEntityFrom(ExtendedDamageSource.broomDamage((LivingEntity) broom.getControllingPassenger()), damage);
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
                        broom.getBoundingBox().minX + x + r,
                        broom.getBoundingBox().minY + y + r,
                        broom.getBoundingBox().minZ + z + r);
                BlockPos blockpos1 = new BlockPos(
                        broom.getBoundingBox().maxX + x - r,
                        broom.getBoundingBox().maxY + y - r + 1D,
                        broom.getBoundingBox().maxZ + z - r);
                World world = broom.world;
                float maxHardness = modifierValue;
                float toughnessModifier = Math.min(1F, 0.5F + (broom.getModifier(BroomModifiers.STURDYNESS) / (BroomModifiers.STURDYNESS.getMaxTierValue() * 1.5F) / 2F));
                LivingEntity ridingEntity = broom.getControllingPassenger() instanceof LivingEntity ? (LivingEntity) broom.getControllingPassenger() : null;
                PlayerEntity player = broom.getControllingPassenger() instanceof PlayerEntity ? (PlayerEntity) broom.getControllingPassenger() : null;

                if (world.isAreaLoaded(blockpos, blockpos1)) {
                    for (int i = blockpos.getX(); i <= blockpos1.getX(); ++i) {
                        for (int j = blockpos.getY(); j <= blockpos1.getY(); ++j) {
                            for (int k = blockpos.getZ(); k <= blockpos1.getZ(); ++k) {
                                BlockPos pos = new BlockPos(i, j, k);
                                BlockState blockState = world.getBlockState(pos);
                                IFluidState fluidState = world.getFluidState(pos);
                                Block block = blockState.getBlock();
                                if (!blockState.getBlock().isAir(blockState, world, pos) && broom.canConsume(ItemBroomConfig.bloodUsageBlockBreak, ridingEntity)) {
                                    float hardness = blockState.getBlockHardness(world, pos);
                                    if (hardness > 0F && hardness <= maxHardness) {
                                        broom.consume(ItemBroomConfig.bloodUsageBlockBreak, ridingEntity);
                                        if (player == null) {
                                            // The mounted entity is no player, do regular block breaking
                                            world.destroyBlock(pos, true);
                                        } else {
                                            // The mounted entity is a player, apply fortune and drop xp
                                            // Inspired by TCon's block breaking code

                                            // Destroy the block
                                            if (!broom.world.isRemote()) {
                                                ServerPlayerEntity playerMp = (ServerPlayerEntity) player;
                                                int expToDrop = ForgeHooks.onBlockBreakEvent(world, playerMp.interactionManager.getGameType(), (ServerPlayerEntity) player, pos);
                                                if (expToDrop >= 0) {
                                                    // Block breaking sequence
                                                    block.onBlockHarvested(world, pos, blockState, player);
                                                    if(block.removedByPlayer(blockState, world, pos, player, true, fluidState)) {
                                                        block.onPlayerDestroy(world, pos, blockState);
                                                        block.harvestBlock(world, player, pos, blockState, world.getTileEntity(pos), ItemStack.EMPTY);
                                                        block.dropXpOnBlockBreak(world, pos, expToDrop);
                                                    }

                                                    // Send block change packet to the client
                                                    playerMp.connection.sendPacket(new SChangeBlockPacket(world, pos));
                                                }
                                            } else if (Minecraft.getInstance().objectMouseOver.getType() == RayTraceResult.Type.BLOCK) {
                                                // Play sound and client-side block breaking sequence
                                                world.playBroadcastSound(2001, pos, Block.getStateId(blockState));
                                                if(block.removedByPlayer(blockState, world, pos, player, true, fluidState)) {
                                                    block.onPlayerDestroy(world, pos, blockState);
                                                }

                                                // Tell the server we are done with breaking this block
                                                Minecraft.getInstance().getConnection().sendPacket(new CPlayerDiggingPacket(
                                                        CPlayerDiggingPacket.Action.STOP_DESTROY_BLOCK, pos,
                                                        ((BlockRayTraceResult) Minecraft.getInstance().objectMouseOver).getFace()));
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
                    double dx = entity.getPosX() - broom.getPosX();
                    double dy = entity.getPosY() + (double)entity.getEyeHeight() - broom.getPosY();
                    double dz = entity.getPosZ() - broom.getPosZ();
                    double d = (double) MathHelper.sqrt(dx * dx + dy * dy + dz * dz);
                    if (d != 0.0D) {
                        dx /= d;
                        dy /= d;
                        dz /= d;
                        entity.setMotion(entity.getMotion()
                                .add(dx, dy, dz)
                                .mul(power, power, power));
                        if (broom.world.isRemote()) {
                            ItemMaceOfDistortion.showEntityDistored(broom.world, null, entity, (int) (power / 10F));
                        }
                    }
                }
            }
        });
        WITHERER.addCollisionListener(new PotionEffectBroomCollision(Effects.WITHER));
        HUNGERER.addCollisionListener(new PotionEffectBroomCollision(Effects.HUNGER));
        KAMIKAZE.addCollisionListener(new BroomModifier.ICollisionListener() {
            @Override
            public void onCollide(EntityBroom broom, Entity entity, float modifierValue) {
                World world = broom.world;
                float power = (modifierValue * (float) broom.getLastPlayerSpeed()) / 5F;
                if (power > 0 && broom.getControllingPassenger() != null) {
                    broom.stopRiding();
                    world.createExplosion(null, broom.getPosX(), broom.getPosY(), broom.getPosZ(), power, Explosion.Mode.DESTROY);
                }
            }
        });
        ICY.addCollisionListener(new PotionEffectBroomCollision(Effects.SLOWNESS, 2));
        STICKY.addCollisionListener(new BroomModifier.ICollisionListener() {
            @Override
            public void onCollide(EntityBroom broom, Entity entity, float modifierValue) {
                if (!entity.world.isRemote() && !entity.isPassenger() && entity.canBeRidden(broom)) {
                    // Will fail if max passenger count is exceeded
                    entity.startRiding(broom);
                }
            }
        });
    }

    protected static void loadPost() {
        // Clear the registry, as this can be called multiple times
        REGISTRY.clearModifierItems();

        REGISTRY.registerModifiersItem(MODIFIER_COUNT, 1F, new ItemStack(Items.NETHER_STAR));
        REGISTRY.registerModifiersItem(MODIFIER_COUNT, 1F, new ItemStack(RegistryEntries.ITEM_GARMONBOZIA));

        REGISTRY.registerModifiersItem(SPEED, 1F, new ItemStack(Items.REDSTONE));
        REGISTRY.registerModifiersItem(SPEED, 9F, new ItemStack(Blocks.REDSTONE_BLOCK));

        REGISTRY.registerModifiersItem(ACCELERATION, 1F, new ItemStack(Items.COAL));
        REGISTRY.registerModifiersItem(ACCELERATION, 9F, new ItemStack(Blocks.COAL_BLOCK));

        REGISTRY.registerModifiersItem(MANEUVERABILITY, 2F, new ItemStack(Items.GLOWSTONE_DUST));
        REGISTRY.registerModifiersItem(MANEUVERABILITY, 8F, new ItemStack(Blocks.GLOWSTONE));

        REGISTRY.registerModifiersItem(LEVITATION, 1F, new ItemStack(Items.FEATHER));
        REGISTRY.registerModifiersItem(LEVITATION, 50F, new ItemStack(Items.PHANTOM_MEMBRANE));

        REGISTRY.registerModifiersItem(DAMAGE, 2F, new ItemStack(RegistryEntries.ITEM_DARK_SPIKE));
        REGISTRY.registerModifiersItem(DAMAGE, 1F, new ItemStack(Items.QUARTZ));

        REGISTRY.registerModifiersItem(PARTICLES, 1F, new ItemStack(Items.GUNPOWDER));

        REGISTRY.registerModifiersItem(FLAME, 1F, new ItemStack(Items.BLAZE_POWDER));

        REGISTRY.registerModifiersItem(SMASH, 1F, new ItemStack(Items.IRON_PICKAXE));
        REGISTRY.registerModifiersItem(SMASH, 5F, new ItemStack(Items.DIAMOND_PICKAXE));

        REGISTRY.registerModifiersItem(BOUNCY, 1F, new ItemStack(Items.SLIME_BALL));
        REGISTRY.registerModifiersItem(BOUNCY, 9F, new ItemStack(Blocks.SLIME_BLOCK));

        registerModifierTagItem(STURDYNESS, 1F, new ResourceLocation("forge", "stone"));
        REGISTRY.registerModifiersItem(STURDYNESS, 10F, new ItemStack(Blocks.OBSIDIAN));

        registerModifierTagItem(EFFICIENCY, 1F, new ResourceLocation(Reference.MOD_ID, "gems/dark_power"));

        REGISTRY.registerModifiersItem(SWIMMING, 1F, new ItemStack(Items.PRISMARINE_SHARD));
        REGISTRY.registerModifiersItem(SWIMMING, 4F, new ItemStack(Blocks.PRISMARINE));
        REGISTRY.registerModifiersItem(SWIMMING, 9F, new ItemStack(Blocks.DARK_PRISMARINE));
        REGISTRY.registerModifiersItem(SWIMMING, 20F, new ItemStack(Items.NAUTILUS_SHELL));
        REGISTRY.registerModifiersItem(SWIMMING, 25F, new ItemStack(Items.TURTLE_HELMET));

        REGISTRY.registerModifiersItem(ICY, 1F, new ItemStack(Blocks.ICE));
        REGISTRY.registerModifiersItem(ICY, 5F, new ItemStack(Blocks.PACKED_ICE));
        REGISTRY.registerModifiersItem(ICY, 10F, new ItemStack(Blocks.BLUE_ICE));

        REGISTRY.registerModifiersItem(STICKY, 1F, new ItemStack(Items.SEAGRASS));
        REGISTRY.registerModifiersItem(STICKY, 2F, new ItemStack(Items.KELP));
        REGISTRY.registerModifiersItem(STICKY, 4F, new ItemStack(Items.HONEY_BOTTLE));
        REGISTRY.registerModifiersItem(STICKY, 16F, new ItemStack(Items.HONEY_BLOCK));

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

    public static void registerModifierTagItem(BroomModifier modifier, float value, ResourceLocation name) {
        Tag<Item> tag = ItemTags.getCollection().get(name);
        if (tag != null) {
            for (Item item : tag.getAllElements()) {
                REGISTRY.registerModifiersItem(modifier, value, new ItemStack(item));
            }
        } else {
            EvilCraft.clog(String.format("Broom modifiers could not find a tag instance for %s", name), Level.WARN);
        }
    }

}
