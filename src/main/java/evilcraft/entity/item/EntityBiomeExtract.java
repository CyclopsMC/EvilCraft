package evilcraft.entity.item;

import evilcraft.Configs;
import evilcraft.api.ILocation;
import evilcraft.core.algorithm.Location;
import evilcraft.core.algorithm.OrganicSpread;
import evilcraft.core.config.configurable.IConfigurable;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.core.entity.item.EntityThrowable;
import evilcraft.core.helper.WorldHelpers;
import evilcraft.item.BiomeExtract;
import evilcraft.item.BiomeExtractConfig;
import evilcraft.item.WeatherContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

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
    protected void onImpact(MovingObjectPosition movingobjectposition) {
        ItemStack itemStack = getItemStack();

        if(!worldObj.isRemote) {
            final BiomeGenBase biome = BiomeExtract.getInstance().getBiome(itemStack);
            if(biome != null) {
                OrganicSpread spread = new OrganicSpread(worldObj, 2, 5, new OrganicSpread.IOrganicSpreadable() {
                    @Override
                    public boolean isDone(World world, ILocation location) {
                        return world.getBiomeGenForCoords(location.getCoordinates()[0], location.getCoordinates()[1]) == biome;
                    }

                    @Override
                    public void spreadTo(World world, ILocation location) {
                        WorldHelpers.setBiome(worldObj, location.getCoordinates()[0], location.getCoordinates()[1], biome);
                    }
                });
                for(int i = 0; i < 50; i++) {
                    spread.spreadTick(new Location(movingobjectposition.blockX, movingobjectposition.blockZ));
                }
            }
        }

        worldObj.playSound(movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ,
                "mob.ghast.moan", 0.5F, 0.4F / (worldObj.rand.nextFloat() * 0.4F + 0.8F), true);
        
        // Play sound and show particles of splash potion of harming
        this.worldObj.playAuxSFX(2002, (int)Math.round(this.posX), (int)Math.round(this.posY), (int)Math.round(this.posZ), 16428);
        
        setDead();
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
