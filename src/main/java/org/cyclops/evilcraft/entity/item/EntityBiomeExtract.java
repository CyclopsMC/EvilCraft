package org.cyclops.evilcraft.entity.item;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.lang3.tuple.Triple;
import org.cyclops.cyclopscore.client.particle.ParticleBlurData;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.cyclopscore.helper.WorldHelpers;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.core.algorithm.OrganicSpread;
import org.cyclops.evilcraft.core.entity.item.EntityThrowable;
import org.cyclops.evilcraft.item.ItemBiomeExtract;

import java.util.Random;

/**
 * Entity for the {@link ItemBiomeExtract}.
 * @author rubensworks
 *
 */
public class EntityBiomeExtract extends EntityThrowable {

    private static final DataParameter<ItemStack> ITEMSTACK_INDEX = EntityDataManager.<ItemStack>createKey(EntityWeatherContainer.class, DataSerializers.ITEMSTACK);

    public EntityBiomeExtract(EntityType<? extends EntityThrowable> type, World world) {
        super(type, world);
    }

    public EntityBiomeExtract(World world) {
        this(RegistryEntries.ENTITY_BIOME_EXTRACT, world);
    }

    public EntityBiomeExtract(World world, LivingEntity entity) {
        this(world, entity, new ItemStack(RegistryEntries.ITEM_BIOME_EXTRACT));
    }

    public EntityBiomeExtract(World world, LivingEntity entity, ItemStack stack) {
        super(RegistryEntries.ENTITY_BIOME_EXTRACT, world, entity);
        setItemStack(stack);
    }

    @Override
    protected void onImpact(final RayTraceResult movingobjectposition) {
        if (movingobjectposition.getType() == RayTraceResult.Type.BLOCK) {
            ItemStack itemStack = getItem();

            final Biome biome = ItemBiomeExtract.getBiome(itemStack);
            if (biome != null) {
                OrganicSpread spread = new OrganicSpread(world, 2, 5, new OrganicSpread.IOrganicSpreadable() {
                    @Override
                    public boolean isDone(World world, BlockPos location) {
                        return world.getBiome(location) == biome;
                    }

                    @Override
                    public void spreadTo(World world, BlockPos location) {
                        if (world.isRemote()) {
                            showChangedBiome(world, new BlockPos(location.getX(), ((BlockRayTraceResult) movingobjectposition).getPos().getY(),
                                    location.getZ()), biome.getFoliageColor());
                        }
                        WorldHelpers.setBiome(world, location, biome);
                    }
                });
                BlockPos pos = new BlockPos(movingobjectposition.getHitVec());
                for (int i = 0; i < 50; i++) {
                    spread.spreadTick(pos);
                }
            }

            // Play sound and show particles of splash potion of harming
            this.world.playBroadcastSound(2002, getPosition(), 16428);

            remove();
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void showChangedBiome(World world, BlockPos pos, int color) {
        Triple<Float, Float, Float> c = Helpers.intToRGB(color);
        Random rand = world.rand;
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

            Minecraft.getInstance().worldRenderer.addParticle(
                    new ParticleBlurData(red, green, blue, scale, ageMultiplier), false,
                    x, y, z, motionX, motionY, motionZ);
        }
    }

    @Override
    protected float getGravityVelocity() {
        // The bigger, the faster the entity falls to the ground
        return 0.1F;
    }

    @Override
    public ItemStack getItem() {
        return dataManager.get(ITEMSTACK_INDEX);
    }
    
    private void setItemStack(ItemStack stack) {
        dataManager.set(ITEMSTACK_INDEX, stack);
    }
    
    @Override
    protected void registerData() {
        dataManager.register(ITEMSTACK_INDEX, new ItemStack(RegistryEntries.ITEM_BIOME_EXTRACT));
    }
}
