package org.cyclops.evilcraft.tileentity;

import lombok.experimental.Delegate;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.BookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.client.particle.ParticleBlur;
import org.cyclops.cyclopscore.client.particle.ParticleBlurData;
import org.cyclops.cyclopscore.persist.nbt.NBTPersist;
import org.cyclops.cyclopscore.tileentity.CyclopsTileEntity;
import org.cyclops.evilcraft.EvilCraftSoundEvents;
import org.cyclops.evilcraft.RegistryEntries;

import java.util.Random;

/**
 * Machine that can accumulate the weather and put it in a bottle.
 * @author immortaleeb
 *
 */
public class TileSpiritPortal extends CyclopsTileEntity implements CyclopsTileEntity.ITickingTile {

    @Delegate
    private final ITickingTile tickingTileComponent = new TickingTileComponent(this);

    @NBTPersist
    private Float progress = 0f;

    public TileSpiritPortal() {
        super(RegistryEntries.TILE_ENTITY_SPIRIT_PORTAL);
    }

    @SuppressWarnings("unchecked")
    @Override
	public void updateTileEntity() {
		super.updateTileEntity();
        progress += 0.005f;
        if(progress > 1) {
            world.removeBlock(getPos(), false);
        }
        if(world.isRemote()) {
            int progressModifier = (int) (getProgress() * 40f) + 1;
            if(world.rand.nextInt(5) == 0) {
                world.playSound(getPos().getX() + 0.5F, getPos().getY() + 0.5F, getPos().getZ() + 0.5F,
                        EvilCraftSoundEvents.effect_vengeancebeam_base, SoundCategory.AMBIENT,
                        0.5F + world.rand.nextFloat() * 0.2F, 1.0F, false);
            }
            for(int i = 0; i < world.rand.nextInt(progressModifier); i++) {
                showNewBlurParticle();
            }
        }

        // transform book if thrown in.
        if(!world.isRemote()) {
            for (ItemEntity entityItem : world.getEntitiesWithinAABB(ItemEntity.class,
                    new AxisAlignedBB(
                            this.getPos().getX() - 0.5D, this.getPos().getY() - 0.5D, this.getPos().getZ() - 0.5D,
                            this.getPos().getX() + 1.5D, this.getPos().getY() + 1.5D, this.getPos().getZ() + 1.5D))) {
                if (entityItem.getItem().getItem() instanceof BookItem) {
                    Entity entity = new ItemEntity(world, entityItem.getPosX(), entityItem.getPosY(), entityItem.getPosZ(),
                            new ItemStack(RegistryEntries.ITEM_ORIGINS_OF_DARKNESS, entityItem.getItem().getCount()));
                    entity.setMotion(entityItem.getMotion());
                    entityItem.remove();
                    world.addEntity(entity);
                }
            }
        }
	}

    @OnlyIn(Dist.CLIENT)
    private void showNewBlurParticle() {
        Random rand = world.rand;
        float scale = 0.6F - rand.nextFloat() * 0.3F;
        float red = rand.nextFloat() * 0.03F + 0.01F;
        float green = rand.nextFloat() * 0.03F;
        float blue = rand.nextFloat() * 0.05F + 0.05F;
        float ageMultiplier = (float) (rand.nextDouble() * 6.5D + 10D);

        Minecraft.getInstance().worldRenderer.addParticle(
                new ParticleBlurData(red, green, blue, scale, ageMultiplier), false,
                getPos().getX() + 0.5F, getPos().getY() + 0.5F, getPos().getZ() + 0.5F,
                rand.nextFloat() * 0.2F - 0.1F, rand.nextFloat() * 0.2F - 0.1F, rand.nextFloat() * 0.2F - 0.1F);
    }

    public float getProgress() {
        return progress;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return TileEntity.INFINITE_EXTENT_AABB;
    }

}
