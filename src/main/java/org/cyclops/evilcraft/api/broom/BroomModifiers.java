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
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.tuple.Triple;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.ExtendedDamageSource;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.client.particle.EntityColoredSmokeFX;
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
    public static BroomModifier LUCK;
    public static BroomModifier EFFICIENCY;
    public static BroomModifier SWIMMING;
    public static BroomModifier ICY;

    public static void loadPre() {
        MinecraftForge.EVENT_BUS.register(new BroomModifiers());

        // Base modifiers
        MODIFIER_COUNT = REGISTRY.registerModifier(new BroomModifier(
                new ResourceLocation(Reference.MOD_ID, "modifier_count"),
                BroomModifier.Type.ADDITIVE, 0F, 1F, 3, true,
                EnumChatFormatting.BOLD, Helpers.RGBToInt(0, 0, 0)));
        REGISTRY.overrideDefaultModifierPart(MODIFIER_COUNT, null);
        SPEED = REGISTRY.registerModifier(new BroomModifier(
                new ResourceLocation(Reference.MOD_ID, "speed"),
                BroomModifier.Type.ADDITIVE, 0F, 100F, 3, true,
                EnumChatFormatting.RED, Helpers.RGBToInt(230, 20, 20)));
        ACCELERATION = REGISTRY.registerModifier(new BroomModifier(
                new ResourceLocation(Reference.MOD_ID, "acceleration"),
                BroomModifier.Type.ADDITIVE, 0F, 100F, 3, true,
                EnumChatFormatting.DARK_GRAY, Helpers.RGBToInt(20, 20, 20)));
        MANEUVERABILITY = REGISTRY.registerModifier(new BroomModifier(
                new ResourceLocation(Reference.MOD_ID, "maneuverability"),
                BroomModifier.Type.ADDITIVE, 0F, 100F, 3, true,
                EnumChatFormatting.YELLOW, Helpers.RGBToInt(160, 160, 20)));
        LEVITATION = REGISTRY.registerModifier(new BroomModifier(
                new ResourceLocation(Reference.MOD_ID, "levitation"),
                BroomModifier.Type.ADDITIVE, 0F, 100F, 3, true,
                EnumChatFormatting.WHITE, Helpers.RGBToInt(230, 230, 230)));

        // Optional modifiers
        DAMAGE = REGISTRY.registerModifier(new BroomModifier(
                new ResourceLocation(Reference.MOD_ID, "damage"),
                BroomModifier.Type.ADDITIVE, 0F, 100F, 3, false,
                EnumChatFormatting.GRAY, Helpers.RGBToInt(100, 100, 100)));
        PARTICLES = REGISTRY.registerModifier(new BroomModifier(
                new ResourceLocation(Reference.MOD_ID, "particles"),
                BroomModifier.Type.ADDITIVE, 0F, 10F, 5, false,
                EnumChatFormatting.LIGHT_PURPLE, Helpers.RGBToInt(160, 20, 160)));
        FLAME = REGISTRY.registerModifier(new BroomModifier(
                new ResourceLocation(Reference.MOD_ID, "flame"),
                BroomModifier.Type.ADDITIVE, 0F, 1F, 10, false,
                EnumChatFormatting.GOLD, Helpers.RGBToInt(100, 100, 0)));
        SMASH = REGISTRY.registerModifier(new BroomModifier(
                new ResourceLocation(Reference.MOD_ID, "smash"),
                BroomModifier.Type.ADDITIVE, 0F, 2F, 10, false,
                EnumChatFormatting.AQUA, Helpers.RGBToInt(20, 60, 60)));
        BOUNCY = REGISTRY.registerModifier(new BroomModifier(
                new ResourceLocation(Reference.MOD_ID, "bouncy"),
                BroomModifier.Type.ADDITIVE, 0F, 10F, 3, false,
                EnumChatFormatting.GREEN, Helpers.RGBToInt(20, 200, 60)));
        WITHERER = REGISTRY.registerModifier(new BroomModifier(
                new ResourceLocation(Reference.MOD_ID, "witherer"),
                BroomModifier.Type.ADDITIVE, 0F, 10F, 3, false,
                EnumChatFormatting.DARK_GRAY, Helpers.RGBToInt(20, 20, 20)));
        HUNGERER = REGISTRY.registerModifier(new BroomModifier(
                new ResourceLocation(Reference.MOD_ID, "hungerer"),
                BroomModifier.Type.ADDITIVE, 0F, 10F, 3, false,
                EnumChatFormatting.DARK_GREEN, Helpers.RGBToInt(20, 120, 20)));
        KAMIKAZE = REGISTRY.registerModifier(new BroomModifier(
                new ResourceLocation(Reference.MOD_ID, "kamikaze"),
                BroomModifier.Type.ADDITIVE, 0F, 10F, 3, false,
                EnumChatFormatting.DARK_GREEN, Helpers.RGBToInt(20, 120, 20)));
        WITHERSHIELD = REGISTRY.registerModifier(new BroomModifier(
                new ResourceLocation(Reference.MOD_ID, "withershield"),
                BroomModifier.Type.ADDITIVE, 0F, 5F, 4, false,
                EnumChatFormatting.DARK_BLUE, Helpers.RGBToInt(20, 20, 120)));
        STURDYNESS = REGISTRY.registerModifier(new BroomModifier(
                new ResourceLocation(Reference.MOD_ID, "sturdyness"),
                BroomModifier.Type.ADDITIVE, 0F, 100F, 3, false,
                EnumChatFormatting.GRAY, Helpers.RGBToInt(100, 100, 100)));
        LUCK = REGISTRY.registerModifier(new BroomModifier(
                new ResourceLocation(Reference.MOD_ID, "luck"),
                BroomModifier.Type.ADDITIVE, 0F, 100F, 3, false,
                EnumChatFormatting.BLUE, Helpers.RGBToInt(30, 20, 210)));
        EFFICIENCY = REGISTRY.registerModifier(new BroomModifier(
                new ResourceLocation(Reference.MOD_ID, "efficiency"),
                BroomModifier.Type.ADDITIVE, 0F, 10F, 3, false,
                EnumChatFormatting.DARK_RED, Helpers.RGBToInt(92, 29, 29)));
        SWIMMING = REGISTRY.registerModifier(new BroomModifier(
                new ResourceLocation(Reference.MOD_ID, "swimming"),
                BroomModifier.Type.ADDITIVE, 0F, 100F, 3, false,
                EnumChatFormatting.AQUA, Helpers.RGBToInt(150, 150, 235)));
        ICY = REGISTRY.registerModifier(new BroomModifier(
                new ResourceLocation(Reference.MOD_ID, "icy"),
                BroomModifier.Type.ADDITIVE, 0F, 10F, 3, false,
                EnumChatFormatting.WHITE, Helpers.RGBToInt(220, 220, 240)));

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
        if(MinecraftHelpers.isClientSide()) {
            PARTICLES.addTickListener(new BroomModifier.ITickListener() {
                @Override
                public void onTick(EntityBroom broom, float modifierValue) {
                    World world = broom.worldObj;
                    if (world.isRemote && broom.lastMounted.moveForward != 0) {
                        // Emit particles
                        int particles = (int) (broom.getModifier(BroomModifiers.PARTICLES) * (float) broom.getLastPlayerSpeed());
                        Triple<Float, Float, Float> color = BroomModifier.getAverageColor(broom.getModifiers());
                        for (int i = 0; i < particles; i++) {
                            float r = color.getLeft();
                            float g = color.getMiddle();
                            float b = color.getRight();
                            EntityColoredSmokeFX smoke = new EntityColoredSmokeFX(world,
                                    broom.posX - broom.motionX * 1.5D + Math.random() * 0.4D - 0.2D,
                                    broom.posY - broom.motionY * 1.5D + Math.random() * 0.4D - 0.2D,
                                    broom.posZ - broom.motionZ * 1.5D + Math.random() * 0.4D - 0.2D,
                                    r, g, b,
                                    broom.motionX / 10, broom.motionY / 10, broom.motionZ / 10);
                            Minecraft.getMinecraft().effectRenderer.addEffect(smoke);
                        }
                    }
                }
            });
        }
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
                World world = broom.worldObj;
                float maxHardness = modifierValue;
                float toughnessModifier = Math.min(1F, 0.5F + (broom.getModifier(BroomModifiers.STURDYNESS) / (BroomModifiers.STURDYNESS.getMaxTierValue() * 1.5F) / 2F));
                EntityLivingBase ridingEntity = broom.ridingEntity instanceof EntityLivingBase ? (EntityLivingBase) broom.ridingEntity : null;
                EntityPlayer player = broom.ridingEntity instanceof EntityPlayer ? (EntityPlayer) broom.ridingEntity : null;

                if (world.isAreaLoaded(blockpos, blockpos1)) {
                    for (int i = blockpos.getX(); i <= blockpos1.getX(); ++i) {
                        for (int j = blockpos.getY(); j <= blockpos1.getY(); ++j) {
                            for (int k = blockpos.getZ(); k <= blockpos1.getZ(); ++k) {
                                BlockPos pos = new BlockPos(i, j, k);
                                IBlockState blockState = world.getBlockState(pos);
                                Block block = blockState.getBlock();
                                if (!blockState.getBlock().isAir(world, pos) && broom.canConsume(BroomConfig.bloodUsageBlockBreak, ridingEntity)) {
                                    float hardness = blockState.getBlock().getBlockHardness(world, pos);
                                    if (hardness > 0F && hardness <= maxHardness && (player == null || ForgeHooks.canHarvestBlock(block, player, world, pos))) {
                                        broom.consume(BroomConfig.bloodUsageBlockBreak, ridingEntity);
                                        if (player == null) {
                                            // The mounted entity is no player, do regular block breaking
                                            world.destroyBlock(pos, true);
                                        } else {
                                            // The mounted entity is a player, apply fortune and drop xp
                                            // Inspired by TCon's block breaking code

                                            // Destroy the block
                                            if (!broom.worldObj.isRemote) {
                                                EntityPlayerMP playerMp = (EntityPlayerMP) player;
                                                int expToDrop = ForgeHooks.onBlockBreakEvent(world, playerMp.theItemInWorldManager.getGameType(), (EntityPlayerMP) player, pos);
                                                if (expToDrop >= 0) {
                                                    // Block breaking sequence
                                                    block.onBlockHarvested(world, pos, blockState, player);
                                                    if(block.removedByPlayer(world, pos, player, true)) {
                                                        block.onBlockDestroyedByPlayer(world, pos, blockState);
                                                        block.harvestBlock(world, player, pos, blockState, world.getTileEntity(pos));
                                                        block.dropXpOnBlockBreak(world, pos, expToDrop);
                                                    }

                                                    // Send block change packet to the client
                                                    playerMp.playerNetServerHandler.sendPacket(new S23PacketBlockChange(world, pos));
                                                }
                                            } else {
                                                // Play sound and client-side block breaking sequence
                                                world.playAuxSFX(2001, pos, Block.getStateId(blockState));
                                                if(block.removedByPlayer(world, pos, player, true)) {
                                                    block.onBlockDestroyedByPlayer(world, pos, blockState);
                                                }

                                                // Tell the server we are done with breaking this block
                                                Minecraft.getMinecraft().getNetHandler().addToSendQueue(new C07PacketPlayerDigging(
                                                        C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, pos,
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
                    double d = (double) MathHelper.sqrt_double(dx * dx + dy * dy + dz * dz);
                    if (d != 0.0D) {
                        dx /= d;
                        dy /= d;
                        dz /= d;
                        entity.motionX += dx * power;
                        entity.motionY += dy * power;
                        entity.motionZ += dz * power;
                        if (broom.worldObj.isRemote) {
                            MaceOfDistortion.showEntityDistored(broom.worldObj, null, entity, (int) (power / 10F));
                        }
                    }
                }
            }
        });
        WITHERER.addCollisionListener(new PotionEffectBroomCollision(Potion.wither));
        HUNGERER.addCollisionListener(new PotionEffectBroomCollision(Potion.hunger));
        KAMIKAZE.addCollisionListener(new BroomModifier.ICollisionListener() {
            @Override
            public void onCollide(EntityBroom broom, Entity entity, float modifierValue) {
                World world = broom.worldObj;
                float power = (modifierValue * (float) broom.getLastPlayerSpeed()) / 5F;
                if (power > 0 && broom.riddenByEntity != null) {
                    broom.riddenByEntity.mountEntity(null);
                    world.createExplosion(null, broom.posX, broom.posY, broom.posZ, power, true);
                }
            }
        });
        ICY.addCollisionListener(new PotionEffectBroomCollision(Potion.moveSlowdown, 2));
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

        REGISTRY.registerModifiersItem(BOUNCY, 1F, new ItemStack(Items.slime_ball));
        REGISTRY.registerModifiersItem(BOUNCY, 9F, new ItemStack(Blocks.slime_block));

        registerModifierOredictItem(STURDYNESS, 1F, "stone");
        REGISTRY.registerModifiersItem(STURDYNESS, 10F, new ItemStack(Blocks.obsidian));

        registerModifierOredictItem(EFFICIENCY, 1F, Reference.DICT_GEMDARKPOWER);

        REGISTRY.registerModifiersItem(SWIMMING, 1F, new ItemStack(Items.prismarine_shard));
        REGISTRY.registerModifiersItem(SWIMMING, 4F, new ItemStack(Blocks.prismarine, 1, 0));
        REGISTRY.registerModifiersItem(SWIMMING, 9F, new ItemStack(Blocks.prismarine, 1, 1));

        REGISTRY.registerModifiersItem(ICY, 1F, new ItemStack(Blocks.ice, 1, 1));
        REGISTRY.registerModifiersItem(ICY, 5F, new ItemStack(Blocks.packed_ice, 1, 1));

        EvilCraft.clog(String.format("%s Broom modifiers can be applied!", REGISTRY.getModifiers().size()));
    }

    @SubscribeEvent
    public void onLivingHurt(LivingHurtEvent event) {
        if (event.entityLiving != null && event.entityLiving.ridingEntity instanceof EntityBroom
                && event.source.getSourceOfDamage() instanceof IProjectile) {
            EntityBroom broom = (EntityBroom) event.entityLiving.ridingEntity;
            float modifierValue = broom.getModifier(BroomModifiers.WITHERSHIELD);
            if (modifierValue > 0 && modifierValue > broom.worldObj.rand.nextInt((int) BroomModifiers.WITHERSHIELD.getMaxTierValue())) {
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
