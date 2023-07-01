package org.cyclops.evilcraft.entity.item;

import com.google.common.collect.Sets;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.chunk.ImposterProtoChunk;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.PalettedContainer;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.Triple;
import org.cyclops.cyclopscore.CyclopsCore;
import org.cyclops.cyclopscore.client.particle.ParticleBlurData;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.core.algorithm.OrganicSpread;
import org.cyclops.evilcraft.core.entity.item.EntityThrowable;
import org.cyclops.evilcraft.item.ItemBiomeExtract;
import org.cyclops.evilcraft.network.packet.ResetChunkColorsPacket;

import javax.annotation.Nonnull;
import java.util.Set;

/**
 * Entity for the {@link ItemBiomeExtract}.
 * @author rubensworks
 *
 */
public class EntityBiomeExtract extends EntityThrowable {

    private static final EntityDataAccessor<ItemStack> ITEMSTACK_INDEX = SynchedEntityData.<ItemStack>defineId(EntityBiomeExtract.class, EntityDataSerializers.ITEM_STACK);

    public EntityBiomeExtract(EntityType<? extends EntityThrowable> type, Level world) {
        super(type, world);
    }

    public EntityBiomeExtract(Level world) {
        this(RegistryEntries.ENTITY_BIOME_EXTRACT, world);
    }

    public EntityBiomeExtract(Level world, LivingEntity entity) {
        this(world, entity, new ItemStack(RegistryEntries.ITEM_BIOME_EXTRACT));
    }

    public EntityBiomeExtract(Level world, LivingEntity entity, ItemStack stack) {
        super(RegistryEntries.ENTITY_BIOME_EXTRACT, world, entity);
        setItemStack(stack);
    }

    @Nonnull
    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void onHit(final HitResult movingobjectposition) {
        if (!level().isClientSide() && movingobjectposition.getType() == HitResult.Type.BLOCK) {
            ItemStack itemStack = getItem();

            final Biome biome = ItemBiomeExtract.getBiome(level().registryAccess().registryOrThrow(Registries.BIOME), itemStack);
            if (biome != null) {
                // Update biome in organic spread
                Set<ChunkPos> updatedChunks = Sets.newHashSet();
                OrganicSpread spread = new OrganicSpread(level(), 2, 5, new OrganicSpread.IOrganicSpreadable() {
                    @Override
                    public boolean isDone(Level world, BlockPos location) {
                        return world.getBiome(location).value() == biome;
                    }

                    @Override
                    public void spreadTo(Level world, BlockPos location) {
                        setBiome((ServerLevel) world, location, biome);
                        updatedChunks.add(new ChunkPos(location));
                        int color = biome.getFoliageColor();
                        showChangedBiome((ServerLevel) world, new BlockPos(location.getX(), ((BlockHitResult) movingobjectposition).getBlockPos().getY(),
                                location.getZ()), color);
                    }
                });
                BlockPos pos = BlockPos.containing(movingobjectposition.getLocation());
                for (int i = 0; i < 50; i++) {
                    spread.spreadTick(pos);
                }

                // Send chunk biome data to all players, and reset their grass colors
                if (!level().isClientSide()) {
                    for (ChunkPos chunkPos : updatedChunks) {
                        updateChunkAfterBiomeChange(level(), chunkPos);
                    }
                }
            }

            // Play sound and show particles of splash potion of harming
            this.level().globalLevelEvent(2002, blockPosition(), 16428);

            remove(RemovalReason.DISCARDED);
        }
    }

    /**
     * Set the biome for the given coordinates.
     * Make sure to send updates to clients after calling this using {@link EntityBiomeExtract#updateChunkAfterBiomeChange(Level, ChunkPos)}.
     * @param world The world.
     * @param posIn The position.
     * @param biome The biome to change to.
     */
    public static void setBiome(ServerLevel world, BlockPos posIn, Biome biome) {
        BiomeManager biomeManager = world.getBiomeManager();
        // Worldgen applies some funk "magnifier" position transformation to a "noise position",
        // which can change the pos into some other internal pos.
        // We copied the logic in BiomeManager#getBiome below:
        int i = posIn.getX() - 2;
        int j = posIn.getY() - 2;
        int k = posIn.getZ() - 2;
        int l = i >> 2;
        int i1 = j >> 2;
        int j1 = k >> 2;
        double d0 = (double)(i & 3) / 4.0D;
        double d1 = (double)(j & 3) / 4.0D;
        double d2 = (double)(k & 3) / 4.0D;
        int k1 = 0;
        double d3 = Double.POSITIVE_INFINITY;

        for(int l1 = 0; l1 < 8; ++l1) {
            boolean flag = (l1 & 4) == 0;
            boolean flag1 = (l1 & 2) == 0;
            boolean flag2 = (l1 & 1) == 0;
            int i2 = flag ? l : l + 1;
            int j2 = flag1 ? i1 : i1 + 1;
            int k2 = flag2 ? j1 : j1 + 1;
            double d4 = flag ? d0 : d0 - 1.0D;
            double d5 = flag1 ? d1 : d1 - 1.0D;
            double d6 = flag2 ? d2 : d2 - 1.0D;
            double d7 = BiomeManager.getFiddledDistance(biomeManager.biomeZoomSeed, i2, j2, k2, d4, d5, d6);
            if (d3 > d7) {
                k1 = l1;
                d3 = d7;
            }
        }

        int l2 = (k1 & 4) == 0 ? l : l + 1;
        int i3 = (k1 & 2) == 0 ? i1 : i1 + 1;
        int j3 = (k1 & 1) == 0 ? j1 : j1 + 1;

        // Update biome data in chunk
        ChunkAccess chunk = world.getChunk(QuartPos.toSection(l2), QuartPos.toSection(j3), ChunkStatus.BIOMES, false);
        if (chunk instanceof ImposterProtoChunk) {
            chunk = ((ImposterProtoChunk) chunk).getWrapped();
        }
        if(chunk != null) {
            // HACK
            // Due to some weird thing in MC, different instances of the same biome can exist.
            // This hack allows us to convert to the biome instance that is required for chunk serialization.
            // This avoids weird errors in the form of "Received invalid biome id: -1" (#818)
            Registry<Biome> biomeRegistry = world.registryAccess().registryOrThrow(Registries.BIOME);
            Holder<Biome> biomeHack = biomeRegistry.getHolder(ResourceKey.create(Registries.BIOME, biomeRegistry.getKey(biome)))
                    .orElseGet(() -> biomeRegistry.getHolder(ResourceKey.create(Registries.BIOME, ForgeRegistries.BIOMES.getKey(biome))).get());

            // Update biome in chunk
            // Based on ChunkAccess#getNoiseBiome
            int minBuildHeight = QuartPos.fromBlock(chunk.getMinBuildHeight());
            int maxHeight = minBuildHeight + QuartPos.fromBlock(chunk.getHeight()) - 1;
            int dummyY = Mth.clamp(i3, minBuildHeight, maxHeight);
            int sectionIndex = chunk.getSectionIndex(QuartPos.toBlock(dummyY));
            //chunk.sections[sectionIndex].getNoiseBiome(l2 & 3, dummyY & 3, j3 & 3);
            ((PalettedContainer<Holder<Biome>>) chunk.sections[sectionIndex].getBiomes()).set(l2 & 3, dummyY & 3, j3 & 3, biomeHack);

            chunk.setUnsaved(true);
        } else {
            CyclopsCore.clog(org.apache.logging.log4j.Level.WARN, "Tried changing biome at non-existing chunk for position " + posIn);
        }
    }

    /**
     * This should be called after {@link EntityBiomeExtract#setBiome(ServerLevel, BlockPos, Biome)}}
     * to notify players of biome change.
     * @param world The world.
     * @param chunkPos The chunk position in which one or more biome positions were changed.
     */
    public static void updateChunkAfterBiomeChange(Level world, ChunkPos chunkPos) {
        LevelChunk chunkSafe = world.getChunkSource().getChunk(chunkPos.x, chunkPos.z, false);
        ((ServerChunkCache) world.getChunkSource()).chunkMap.getPlayers(chunkPos, false).forEach((player) -> {
            player.connection.send(new ClientboundLevelChunkWithLightPacket(chunkSafe, ((ServerChunkCache) world.getChunkSource()).chunkMap.getLightEngine(), null, null));
            EvilCraft._instance.getPacketHandler().sendToPlayer(new ResetChunkColorsPacket(chunkPos.x, chunkPos.z), player);
        });
    }

    private void showChangedBiome(ServerLevel world, BlockPos pos, int color) {
        Triple<Float, Float, Float> c = Helpers.intToRGB(color);
        RandomSource rand = world.random;
        for (int j = 0; j < 2 + rand.nextInt(5); j++) {
            float x = pos.getX() + -0.5F + rand.nextFloat();
            float y = pos.getY() + -0.5F + rand.nextFloat();
            float z = pos.getZ() + -0.5F + rand.nextFloat();

            float scale = 0.2F - rand.nextFloat() * 0.2F;
            float red = c.getLeft() + rand.nextFloat() * 0.1F;
            float green = c.getMiddle() + rand.nextFloat() * 0.1F;
            float blue = c.getRight() + rand.nextFloat() * 0.1F;
            float ageMultiplier = 10 + rand.nextInt(15);

            double motionX = -0.1F + rand.nextFloat() * 0.2F;
            double motionY = 0.1F + rand.nextFloat() * 0.2F;
            double motionZ = -0.1F + rand.nextFloat() * 0.2F;

            world.sendParticles(
                    new ParticleBlurData(red, green, blue, scale, ageMultiplier),
                    x, y, z, 1, motionX, motionY, motionZ, 0.1);
        }
    }

    @Override
    protected float getGravity() {
        // The bigger, the faster the entity falls to the ground
        return 0.1F;
    }

    @Override
    public ItemStack getItem() {
        return entityData.get(ITEMSTACK_INDEX);
    }

    private void setItemStack(ItemStack stack) {
        entityData.set(ITEMSTACK_INDEX, stack);
    }

    @Override
    protected void defineSynchedData() {
        entityData.define(ITEMSTACK_INDEX, new ItemStack(RegistryEntries.ITEM_BIOME_EXTRACT));
    }
}
