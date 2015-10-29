package org.cyclops.evilcraft.entity.item;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.Configs;
import evilcraft.client.particle.EntityBlurFX;
import evilcraft.core.algorithm.OrganicSpread;
import evilcraft.core.config.configurable.IConfigurable;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.core.entity.item.EntityThrowable;
import evilcraft.core.helper.WorldHelpers;
import org.cyclops.evilcraft.item.BiomeExtract;
import org.cyclops.evilcraft.item.BiomeExtractConfig;
import evilcraft.item.WeatherContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import org.apache.commons.lang3.tuple.Triple;

import java.util.Random;

/**
 * Entity for the {@link WeatherContainer}.
 * @author rubensworks
 *
 */
public class EntityBiomeExtract extends EntityThrowable implements IConfigurable {

    private static final int ITEMSTACK_INDEX = 15;

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
     * @param damage The damage value for the {@link WeatherContainer} to be rendered.
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
    protected void onImpact(final MovingObjectPosition movingobjectposition) {
        ItemStack itemStack = getItemStack();

        final BiomeGenBase biome = BiomeExtract.getInstance().getBiome(itemStack);
        if(biome != null) {
            OrganicSpread spread = new OrganicSpread(worldObj, 2, 5, new OrganicSpread.IOrganicSpreadable() {
                @Override
                public boolean isDone(World world, ILocation location) {
                    return world.getBiomeGenForCoords(location.getCoordinates()[0], location.getCoordinates()[1]) == biome;
                }

                @Override
                public void spreadTo(World world, ILocation location) {
                    if(worldObj.isRemote) {
                        showChangedBiome(worldObj, location.getCoordinates()[0], movingobjectposition.blockY,
                                location.getCoordinates()[1], biome.color);
                    } else {
                        WorldHelpers.setBiome(worldObj, location.getCoordinates()[0], location.getCoordinates()[1], biome);
                    }
                }
            });
            for(int i = 0; i < 50; i++) {
                spread.spreadTick(new Location(movingobjectposition.blockX, movingobjectposition.blockZ));
            }
        }
        
        // Play sound and show particles of splash potion of harming
        this.worldObj.playAuxSFX(2002, (int) Math.round(this.posX), (int) Math.round(this.posY), (int) Math.round(this.posZ), 16428);
        
        setDead();
    }

    @SideOnly(Side.CLIENT)
    private void showChangedBiome(World world, int xStart, int yStart, int zStart, int color) {
        Triple<Float, Float, Float> c = RenderHelpers.intToRGB(color);
        Random rand = world.rand;
        for (int j = 0; j < 2 + rand.nextInt(5); j++) {
            float x = xStart + -0.5F + rand.nextFloat();
            float y = yStart + -0.5F + rand.nextFloat();
            float z = zStart + -0.5F + rand.nextFloat();

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
        return 0.01F;
    }

    @Override
    protected float func_70182_d() {
        // Determines the distance of the throw
        return 1.0F;
    }

    @Override
    protected float func_70183_g() {
        // Offset for the start height at which the entity is thrown
        return 0.0F;
    }

    @Override
    public ItemStack getItemStack() {
        return dataWatcher.getWatchableObjectItemStack(ITEMSTACK_INDEX);
    }
    
    private void setItemStack(ItemStack stack) {
        dataWatcher.updateObject(ITEMSTACK_INDEX, stack);
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        
        dataWatcher.addObject(ITEMSTACK_INDEX, BiomeExtract.getInstance().createItemStack(null, 1));
    }

    @Override
    public ExtendedConfig<?> getConfig() {
        return null;
    }
}
