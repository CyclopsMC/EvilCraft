package evilcraft.modcompat.thaumcraft;

import evilcraft.Configs;
import evilcraft.IInitListener;
import evilcraft.Reference;
import evilcraft.block.*;
import evilcraft.entity.monster.NetherfishConfig;
import evilcraft.entity.monster.PoisonousLibelleConfig;
import evilcraft.entity.monster.WerewolfConfig;
import evilcraft.item.*;
import evilcraft.modcompat.IModCompat;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.wands.WandCap;

/**
 * Compatibility plugin for Waila.
 * @author rubensworks
 *
 */
public class ThaumcraftModCompat implements IModCompat {

    @Override
    public String getModID() {
        return Reference.MOD_THAUMCRAFT;
    }

    @Override
    public void onInit(Step step) {
        if(step == IInitListener.Step.PREINIT) {
            Configs.getInstance().configs.add(new VeinedScribingToolsConfig());
            Configs.getInstance().configs.add(new BloodWandCapConfig());
        } else if(step == Step.INIT) {
            registerAspects();
            if(Configs.isEnabled(BloodWandCapConfig.class)) {
                WandCap bloodWandCap = new WandCap("blood", 0.95F, new ItemStack(BloodWandCap.getInstance()), 3) {
                    public String getResearch() {
                        return "CAP_gold";
                    }
                };
                bloodWandCap.setTexture(new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_MODELS + "wand_cap_blood.png"));
            }
    	}
    }

    private void registerAspects() {
        // Entities
        ThaumcraftApi.registerEntityTag(NetherfishConfig._instance.getNamedId(), new AspectList().add(Aspect.FIRE, 4).add(Aspect.BEAST, 1).add(Aspect.EARTH, 1), new ThaumcraftApi.EntityTagsNBT[0]);
        ThaumcraftApi.registerEntityTag(PoisonousLibelleConfig._instance.getNamedId(), new AspectList().add(Aspect.POISON, 4).add(Aspect.BEAST, 1).add(Aspect.AIR, 1), new ThaumcraftApi.EntityTagsNBT[0]);
        ThaumcraftApi.registerEntityTag(WerewolfConfig._instance.getNamedId(), new AspectList().add(Aspect.HUNGER, 4).add(Aspect.BEAST, 4), new ThaumcraftApi.EntityTagsNBT[0]);

        // Blood
        AspectList bloodLists = new AspectList().add(Aspect.ENERGY, 2).add(Aspect.LIFE, 4);
        ThaumcraftApi.registerObjectTag(new ItemStack(FluidBlockBlood.getInstance()), bloodLists);
        ThaumcraftApi.registerObjectTag(new ItemStack(HardenedBlood.getInstance()), bloodLists.copy());
        ThaumcraftApi.registerObjectTag(new ItemStack(BucketBloodConfig._instance.getItemInstance()), bloodLists.copy().add(Aspect.METAL, 3));
        ThaumcraftApi.registerObjectTag(new ItemStack(HardenedBloodShardConfig._instance.getItemInstance()), new AspectList().add(Aspect.LIFE, 1));
        ThaumcraftApi.registerObjectTag(new ItemStack(BloodStainedBlock.getInstance()), bloodLists.copy());
        ThaumcraftApi.registerObjectTag(new ItemStack(BloodyCobblestoneConfig._instance.getBlockInstance()), bloodLists.copy().add(Aspect.EARTH, 1).add(Aspect.ENTROPY, 1));

        // Dark Gem
        ThaumcraftApi.registerObjectTag(new ItemStack(DarkGem.getInstance()), new AspectList().add(Aspect.CRYSTAL, 2).add(Aspect.DARKNESS, 4));
        ThaumcraftApi.registerObjectTag("gemDark", new AspectList().add(Aspect.CRYSTAL, 2).add(Aspect.DARKNESS, 4));
        ThaumcraftApi.registerObjectTag(new ItemStack(DarkOre.getInstance()), new AspectList().add(Aspect.CRYSTAL, 2).add(Aspect.DARKNESS, 2).add(Aspect.EARTH, 1));
        ThaumcraftApi.registerObjectTag(new ItemStack(DarkGemCrushedConfig._instance.getItemInstance()), new AspectList().add(Aspect.SOUL, 2).add(Aspect.DARKNESS, 2).add(Aspect.EARTH, 1));
        ThaumcraftApi.registerObjectTag(new ItemStack(DarkPowerGemConfig._instance.getItemInstance()), new AspectList(new ItemStack(DarkGem.getInstance())).add(bloodLists.copy()).add(Aspect.MAGIC, 2));

        // Poison
        ThaumcraftApi.registerObjectTag(new ItemStack(FluidBlockPoison.getInstance()), new AspectList().add(Aspect.POISON, 4));
        ThaumcraftApi.registerObjectTag(new ItemStack(BucketPoisonConfig._instance.getItemInstance()), new AspectList().add(Aspect.POISON, 4).add(Aspect.METAL, 3));
        ThaumcraftApi.registerObjectTag(new ItemStack(PoisonSacConfig._instance.getItemInstance()), new AspectList().add(Aspect.POISON, 4).add(Aspect.FLESH, 2));

        // Undead tree
        ThaumcraftApi.registerObjectTag(new ItemStack(UndeadSapling.getInstance()), new AspectList().add(Aspect.TREE, 1).add(Aspect.PLANT, 2).add(Aspect.UNDEAD, 2));
        ThaumcraftApi.registerObjectTag(new ItemStack(UndeadPlankConfig._instance.getBlockInstance()), new AspectList().add(Aspect.TREE, 1).add(Aspect.UNDEAD, 1));
        ThaumcraftApi.registerObjectTag(new ItemStack(UndeadLogConfig._instance.getBlockInstance()), new AspectList().add(Aspect.TREE, 4).add(Aspect.UNDEAD, 4));
        ThaumcraftApi.registerObjectTag(new ItemStack(UndeadLeaves.getInstance()), new AspectList().add(Aspect.PLANT, 1).add(Aspect.UNDEAD, 1));

        // Werewolf drops
        ThaumcraftApi.registerObjectTag(new ItemStack(WerewolfBoneConfig._instance.getItemInstance()), new AspectList().add(Aspect.FLESH, 2).add(Aspect.DEATH, 2));
        ThaumcraftApi.registerObjectTag(new ItemStack(WerewolfFleshConfig._instance.getItemInstance()), new AspectList().add(Aspect.MAN, 8));
        ThaumcraftApi.registerObjectTag(new ItemStack(WerewolfFurConfig._instance.getItemInstance()), new AspectList().add(Aspect.BEAST, 8));

        // Weather containers
        AspectList weatherContainerList = new AspectList(new ItemStack(WeatherContainer.getInstance()));
        ThaumcraftApi.registerObjectTag(new ItemStack(WeatherContainer.getInstance()), new AspectList().add(weatherContainerList).add(Aspect.WEATHER, 4).add(Aspect.MAGIC, 2));
        ThaumcraftApi.registerObjectTag(new ItemStack(WeatherContainer.getInstance(), 1, WeatherContainer.WeatherContainerTypes.CLEAR.ordinal()), new AspectList().add(weatherContainerList).add(Aspect.ORDER, 6));
        ThaumcraftApi.registerObjectTag(new ItemStack(WeatherContainer.getInstance(), 1, WeatherContainer.WeatherContainerTypes.RAIN.ordinal()), new AspectList().add(weatherContainerList).add(Aspect.WATER, 6));
        ThaumcraftApi.registerObjectTag(new ItemStack(WeatherContainer.getInstance(), 1, WeatherContainer.WeatherContainerTypes.LIGHTNING.ordinal()), new AspectList().add(weatherContainerList).add(Aspect.ENERGY, 6));

        // Other
        ThaumcraftApi.registerObjectTag(new ItemStack(EnvironmentalAccumulator.getInstance()), new AspectList().add(Aspect.MAGIC, 10).add(Aspect.ENTROPY, 5).add(Aspect.EXCHANGE, 4).add(Aspect.MECHANISM, 3).add(Aspect.WEATHER, 10));
        ThaumcraftApi.registerObjectTag(new ItemStack(SpiritPortal.getInstance()), new AspectList().add(Aspect.MAGIC, 10).add(Aspect.TRAVEL, 10).add(Aspect.UNDEAD, 10));
        ThaumcraftApi.registerObjectTag(new ItemStack(DarkBloodBrick.getInstance()), new AspectList(new ItemStack(DarkBrickConfig._instance.getBlockInstance())).add(new AspectList(new ItemStack(DarkPowerGemConfig._instance.getItemInstance()))));
        ThaumcraftApi.registerObjectTag(new ItemStack(Broom.getInstance()), new AspectList().add(Aspect.MOTION, 4).add(Aspect.MAGIC, 2).add(Aspect.FLIGHT, 6).add(Aspect.TRAVEL, 2));
        ThaumcraftApi.registerObjectTag(new ItemStack(BlookConfig._instance.getItemInstance()), new AspectList().add(bloodLists).add(Aspect.MIND, 1));
        ThaumcraftApi.registerObjectTag(new ItemStack(InvertedPotentia.getInstance(), 1, InvertedPotentia.EMPOWERED_META), new AspectList(new ItemStack(InvertedPotentia.getInstance())).add(Aspect.ENERGY, 4));
        for(int i = 0; i < 4; i++) {
            ThaumcraftApi.registerObjectTag(new ItemStack(ExaltedCrafter.getInstance(), 1, i), new AspectList(new ItemStack(ExaltedCrafter.getInstance(), 1, i)).add(Aspect.CRAFT, 10));
        }
        ThaumcraftApi.registerObjectTag(new ItemStack(CorruptedTearConfig._instance.getItemInstance()), new AspectList().add(Aspect.DEATH, 4).add(Aspect.SOUL, 4).add(Aspect.ENTROPY, 4).add(Aspect.EXCHANGE, 8).add(Aspect.TRAVEL, 2).add(Aspect.MAGIC, 4));
        for(int i = 0; i < 3; i++) {
            ThaumcraftApi.registerObjectTag(new ItemStack(PromiseAcceptor.getInstance(), 1, i), new AspectList().add(Aspect.METAL, 10).add(Aspect.MECHANISM, 10));
        }
        ThaumcraftApi.registerObjectTag(new ItemStack(BloodWaxedCoalConfig._instance.getItemInstance()), new AspectList().add(Aspect.FIRE, 4).add(Aspect.ENERGY, 4).add(bloodLists));
        ThaumcraftApi.registerObjectTag(new ItemStack(BloodPotash.getInstance()), new AspectList().add(Aspect.SENSES, 2).add(bloodLists));
        ThaumcraftApi.registerObjectTag(new ItemStack(EnderTearConfig._instance.getItemInstance()), new AspectList().add(Aspect.TRAVEL, 8).add(Aspect.MAGIC, 8).add(Aspect.ELDRITCH, 4));
        ThaumcraftApi.registerObjectTag(new ItemStack(BloodOrb.getInstance(), 1, 1), new AspectList().add(Aspect.ORDER, 1).add(bloodLists));
        ThaumcraftApi.registerObjectTag(new ItemStack(OriginsOfDarkness.getInstance()), new AspectList(new ItemStack(Items.book)).add(Aspect.DARKNESS, 4));
        ThaumcraftApi.registerObjectTag(DarkSpikeConfig._instance.getOreDictionaryId(), new AspectList().add(Aspect.METAL, 2).add(Aspect.DARKNESS, 2).add(Aspect.DEATH, 1));
        ThaumcraftApi.registerObjectTag(new ItemStack(ObscuredGlass.getInstance()), new AspectList().add(Aspect.DARKNESS, 4).add(Aspect.LIGHT, 4));
        ThaumcraftApi.registerObjectTag(new ItemStack(BurningGemStone.getInstance()), new AspectList().add(Aspect.CRYSTAL, 10).add(Aspect.DARKNESS, 10).add(Aspect.FIRE, 4).add(Aspect.SOUL, 2));
        ThaumcraftApi.registerObjectTag(new ItemStack(GemStoneTorchConfig._instance.getBlockInstance()), new AspectList().add(Aspect.CRYSTAL, 2).add(Aspect.DARKNESS, 2).add(Aspect.FIRE, 2).add(Aspect.LIGHT, 2).add(Aspect.SOUL, 1));
        ThaumcraftApi.registerObjectTag(new ItemStack(PotentiaSphereConfig._instance.getItemInstance()), new AspectList().add(Aspect.SLIME, 2).add(Aspect.MAGIC, 4).add(Aspect.CRYSTAL, 2).add(Aspect.LIGHT, 2).add(Aspect.MECHANISM, 1));

    }

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String getComment() {
		return "Adds Thaumcraft aspects to EvilCraft items and blocks.";
	}

}
