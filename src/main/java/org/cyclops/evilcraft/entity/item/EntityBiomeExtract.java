package org.cyclops.evilcraft.entity.item;

import com.google.common.base.Optional;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Triple;
import org.cyclops.cyclopscore.config.configurable.IConfigurable;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.cyclopscore.helper.WorldHelpers;
import org.cyclops.evilcraft.Configs;
import org.cyclops.evilcraft.client.particle.EntityBlurFX;
import org.cyclops.evilcraft.core.algorithm.OrganicSpread;
import org.cyclops.evilcraft.core.entity.item.EntityThrowable;
import org.cyclops.evilcraft.item.BiomeExtract;
import org.cyclops.evilcraft.item.BiomeExtractConfig;

import java.util.Random;

/**
 * Entity for the {@link BiomeExtract}.
 * @author rubensworks
 *
 */
public class EntityBiomeExtract extends EntityThrowable implements IConfigurable {

    private static final DataParameter<Optional<ItemStack>> ITEMSTACK_INDEX = EntityDataManager.<Optional<ItemStack>>createKey(EntityWeatherContainer.class, DataSerializers.OPTIONAL_ITEM_STACK);

    /**
     * Make a new instance in the given world.
     * @param world The world to make it in.
     */
    public EntityBiomeExtract(World world) {
        super(world);
    }

    /**
     * Make a new instance in a world by a placer {@link EntityLivingBase}.
     * @param world The world.
     * @param entity The {@link EntityLivingBase} that placed this {@link Entity}.
     * @param damage The damage value for the {@link BiomeExtract} to be rendered.
     */
    public EntityBiomeExtract(World world, EntityLivingBase entity, int damage) {
        this(world, entity, new ItemStack(Configs.isEnabled(BiomeExtractConfig.class) ? BiomeExtract.getInstance() : Items.coal, 1, damage));
    }

    /**
     * Make a new instance at the given location in a world.
     * @param world The world.
     * @param entity The entity
     * @param stack The {@link ItemStack} inside this entity.
     */
    public EntityBiomeExtract(World world, EntityLivingBase entity, ItemStack stack) {
        super(world, entity);
        setItemStack(stack);
    }

    @Override
    protected void onImpact(final RayTraceResult movingobjectposition) {
        ItemStack itemStack = getItemStack();

        final BiomeGenBase biome = BiomeExtract.getInstance().getBiome(itemStack);
        if(biome != null) {
            OrganicSpread spread = new OrganicSpread(worldObj, 2, 5, new OrganicSpread.IOrganicSpreadable() {
                @Override
                public boolean isDone(World world, BlockPos location) {
                    return world.getBiomeGenForCoords(location) == biome;
                }

                @Override
                public void spreadTo(World world, BlockPos location) {
                    if(worldObj.isRemote) {
                        showChangedBiome(worldObj, new BlockPos(location.getX(), movingobjectposition.getBlockPos().getY(),
                                location.getZ()), biome.getFoliageColorAtPos(new BlockPos(0, 0, 0)));
                    }
                    WorldHelpers.setBiome(worldObj, location, biome);
                }
            });
            BlockPos pos = movingobjectposition.typeOfHit == RayTraceResult.Type.BLOCK
                    ? movingobjectposition.getBlockPos()
                    : movingobjectposition.entityHit.getPosition();
            for(int i = 0; i < 50; i++) {
                spread.spreadTick(pos);
            }
        }
        
        // Play sound and show particles of splash potion of harming
        this.worldObj.playAuxSFX(2002, getPosition(), 16428);
        
        setDead();
    }

    @SideOnly(Side.CLIENT)
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

            FMLClientHandler.instance().getClient().effectRenderer.addEffect(
                    new EntityBlurFX(world, x, y, z, scale, motionX, motionY, motionZ, red, green, blue, ageMultiplier)
            );
        }
    }

    @Override
    protected float getGravityVelocity() {
        // The bigger, the faster the entity falls to the ground
        return 0.1F;
    }

    @Override
    public ItemStack getItemStack() {
        Optional<ItemStack> optional = dataWatcher.get(ITEMSTACK_INDEX);
        return optional.isPresent() ? optional.get() : null;
    }
    
    private void setItemStack(ItemStack stack) {
        dataWatcher.set(ITEMSTACK_INDEX, Optional.of(stack));
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        
        dataWatcher.register(ITEMSTACK_INDEX, Optional.of(BiomeExtract.getInstance().createItemStack(null, 1)));
    }

    @Override
    public ExtendedConfig<?> getConfig() {
        return null;
    }
}
