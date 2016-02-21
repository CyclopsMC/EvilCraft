package org.cyclops.evilcraft.modcompat.thaumcraft;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.cyclopscore.config.extendedconfig.MobConfig;
import org.cyclops.evilcraft.Configs;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.block.*;
import org.cyclops.evilcraft.entity.monster.NetherfishConfig;
import org.cyclops.evilcraft.entity.monster.PoisonousLibelleConfig;
import org.cyclops.evilcraft.entity.monster.WerewolfConfig;
import org.cyclops.evilcraft.item.*;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.wands.WandCap;

/**
 * @author rubensworks
 */
public class Thaumcraft {

    public static void register() {
        registerAspects();

        if(Configs.isEnabled(BloodWandCapConfig.class)) {
            WandCap bloodWandCap = new WandCap("blood", 0.95F, 1, new ItemStack(BloodWandCapConfig._instance.getItemInstance()), 3, new ResourceLocation(Reference.MOD_ID, "models/wand_cap_blood")) {
                public String getResearch() {
                    return "CAP_gold";
                }
            };
        }

        if(Configs.isEnabled(DarkGemConfig.class)) {
            ThaumcraftApi.addLootBagItem(new ItemStack(DarkGem.getInstance()), 100, 0, 1, 2);
            ThaumcraftApi.addLootBagItem(new ItemStack(DarkGem.getInstance(), 2), 80, 0, 1, 2);
            ThaumcraftApi.addLootBagItem(new ItemStack(DarkGem.getInstance(), 5), 40, 0, 1, 2);
        }
        if(Configs.isEnabled(DarkGemCrushedConfig.class)) {
            ThaumcraftApi.addLootBagItem(new ItemStack(DarkGemCrushedConfig._instance.getItemInstance()), 80, 0, 1, 2);
            ThaumcraftApi.addLootBagItem(new ItemStack(DarkGemCrushedConfig._instance.getItemInstance(), 2), 60, 0, 1, 2);
            ThaumcraftApi.addLootBagItem(new ItemStack(DarkGemCrushedConfig._instance.getItemInstance(), 5), 20, 0, 1, 2);
        }
        if(Configs.isEnabled(GarmonboziaConfig.class)) {
            ThaumcraftApi.addLootBagItem(new ItemStack(GarmonboziaConfig._instance.getItemInstance()), 5, 1, 2);
            ThaumcraftApi.addLootBagItem(new ItemStack(GarmonboziaConfig._instance.getItemInstance(), 2), 3, 1, 2);
            ThaumcraftApi.addLootBagItem(new ItemStack(GarmonboziaConfig._instance.getItemInstance(), 3), 1, 1, 2);
        }
    }

    private static void registerAspects() {
        // Entities
        registerEntityTagSafe(NetherfishConfig._instance, new AspectList().add(Aspect.FIRE, 4).add(Aspect.BEAST, 1).add(Aspect.EARTH, 1), new ThaumcraftApi.EntityTagsNBT[0]);
        registerEntityTagSafe(PoisonousLibelleConfig._instance, new AspectList().add(Aspect.BEAST, 1).add(Aspect.AIR, 1), new ThaumcraftApi.EntityTagsNBT[0]);
        registerEntityTagSafe(WerewolfConfig._instance, new AspectList().add(Aspect.BEAST, 4), new ThaumcraftApi.EntityTagsNBT[0]);

        // Blood
        AspectList bloodLists = new AspectList().add(Aspect.ENERGY, 2).add(Aspect.LIFE, 4);
        registerObjectTagSafe(FluidBlockBloodConfig._instance, bloodLists.copy());
        registerObjectTagSafe(HardenedBloodConfig._instance, bloodLists.copy());
        registerObjectTagSafe(BucketBloodConfig._instance, bloodLists.copy().add(Aspect.METAL, 3));
        registerObjectTagSafe(HardenedBloodShardConfig._instance, new AspectList().add(Aspect.LIFE, 1));
        registerObjectTagSafe(BloodStainedBlockConfig._instance, bloodLists.copy());
        registerObjectTagSafe(BloodyCobblestoneConfig._instance, bloodLists.copy().add(Aspect.EARTH, 1).add(Aspect.ENTROPY, 1));
        registerObjectTagSafe(CondensedBloodConfig._instance, bloodLists.copy());

        // Dark Gem
        ThaumcraftApi.registerObjectTag("gemDark", new AspectList().add(Aspect.CRYSTAL, 2).add(Aspect.DARKNESS, 4));
        registerObjectTagSafe(DarkOreConfig._instance, new AspectList().add(Aspect.CRYSTAL, 2).add(Aspect.DARKNESS, 2).add(Aspect.EARTH, 1));
        registerObjectTagSafe(DarkGemCrushedConfig._instance, new AspectList().add(Aspect.SOUL, 2).add(Aspect.DARKNESS, 2).add(Aspect.EARTH, 1));
        registerObjectTagSafe(DarkPowerGemConfig._instance, new AspectList().add(Aspect.CRYSTAL, 2).add(Aspect.DARKNESS, 4).add(bloodLists.copy()));

        // Poison
        registerObjectTagSafe(FluidBlockPoisonConfig._instance, new AspectList().add(Aspect.EARTH, 4));
        registerObjectTagSafe(BucketPoisonConfig._instance, new AspectList().add(Aspect.EARTH, 4).add(Aspect.METAL, 3));
        registerObjectTagSafe(PoisonSacConfig._instance, new AspectList().add(Aspect.MOTION, 4).add(Aspect.EARTH, 2));

        // Undead tree
        registerObjectTagSafe(UndeadSaplingConfig._instance, new AspectList().add(Aspect.EARTH, 1).add(Aspect.PLANT, 2).add(Aspect.UNDEAD, 2));
        registerObjectTagSafe(UndeadPlankConfig._instance, new AspectList().add(Aspect.EARTH, 1).add(Aspect.UNDEAD, 1));
        registerObjectTagSafe(UndeadLogConfig._instance, new AspectList().add(Aspect.EARTH, 4).add(Aspect.UNDEAD, 4));
        registerObjectTagSafe(UndeadLeavesConfig._instance, new AspectList().add(Aspect.PLANT, 1).add(Aspect.UNDEAD, 1));

        // Werewolf drops
        registerObjectTagSafe(WerewolfBoneConfig._instance, new AspectList().add(Aspect.DEATH, 2));
        registerObjectTagSafe(WerewolfFleshConfig._instance, new AspectList().add(Aspect.MAN, 8));
        registerObjectTagSafe(WerewolfFurConfig._instance, new AspectList().add(Aspect.BEAST, 8));

        // Weather containers
        AspectList weatherContainerList = new AspectList().add(Aspect.DARKNESS, 3).add(Aspect.CRYSTAL, 1);
        registerObjectTagSafe(WeatherContainerConfig._instance, new AspectList().add(weatherContainerList.copy()));
        registerObjectTagSafe(WeatherContainerConfig._instance, WeatherContainer.WeatherContainerTypes.CLEAR.ordinal(), new AspectList().add(weatherContainerList.copy()).add(Aspect.ORDER, 6));
        registerObjectTagSafe(WeatherContainerConfig._instance, WeatherContainer.WeatherContainerTypes.RAIN.ordinal(), new AspectList().add(weatherContainerList.copy()).add(Aspect.WATER, 6));
        registerObjectTagSafe(WeatherContainerConfig._instance, WeatherContainer.WeatherContainerTypes.LIGHTNING.ordinal(), new AspectList().add(weatherContainerList.copy()).add(Aspect.ENERGY, 6));

        // Other
        registerObjectTagSafe(EnvironmentalAccumulatorConfig._instance, new AspectList().add(Aspect.ENTROPY, 5).add(Aspect.EXCHANGE, 4).add(Aspect.MECHANISM, 3));
        registerObjectTagSafe(SpiritPortalConfig._instance, new AspectList().add(Aspect.FLUX, 10).add(Aspect.MOTION, 10).add(Aspect.UNDEAD, 10));
        registerObjectTagSafe(DarkBloodBrickConfig._instance, new AspectList(new ItemStack(DarkBrickConfig._instance.getBlockInstance())).add(new AspectList(new ItemStack(DarkPowerGemConfig._instance.getItemInstance()))));
        registerObjectTagSafe(BroomConfig._instance, new AspectList().add(Aspect.MOTION, 4).add(Aspect.FLIGHT, 6).add(Aspect.MOTION, 2));
        registerObjectTagSafe(BlookConfig._instance, new AspectList().add(bloodLists.copy()).add(Aspect.MIND, 1));
        registerObjectTagSafe(InvertedPotentiaConfig._instance, InvertedPotentia.EMPOWERED_META, new AspectList(new ItemStack(InvertedPotentia.getInstance())).add(Aspect.ENERGY, 4));
        registerObjectTagSafe(CorruptedTearConfig._instance, new AspectList().add(Aspect.DEATH, 4).add(Aspect.SOUL, 4).add(Aspect.ENTROPY, 4).add(Aspect.EXCHANGE, 8).add(Aspect.MOTION, 2).add(Aspect.FLUX, 4));
        for(int i = 0; i < 3; i++) {
            registerObjectTagSafe(PromiseAcceptorConfig._instance, i, new AspectList().add(Aspect.METAL, 10).add(Aspect.MECHANISM, 10));
        }
        registerObjectTagSafe(BloodWaxedCoalConfig._instance, new AspectList().add(Aspect.FIRE, 4).add(Aspect.ENERGY, 4).add(bloodLists.copy()));
        registerObjectTagSafe(BloodPotashConfig._instance, new AspectList().add(Aspect.SENSES, 2).add(bloodLists.copy()));
        registerObjectTagSafe(EnderTearConfig._instance, new AspectList().add(Aspect.MOTION, 8).add(Aspect.ELDRITCH, 8).add(Aspect.ELDRITCH, 4));
        registerObjectTagSafe(BloodOrbConfig._instance, 1, new AspectList().add(Aspect.ORDER, 1).add(bloodLists.copy()));
        registerObjectTagSafe(OriginsOfDarknessConfig._instance, new AspectList().add(Aspect.MIND, 4).add(Aspect.DARKNESS, 4));
        ThaumcraftApi.registerObjectTag(DarkSpikeConfig._instance.getOreDictionaryId(), new AspectList().add(Aspect.METAL, 2).add(Aspect.DARKNESS, 2).add(Aspect.DEATH, 1));
        registerObjectTagSafe(ObscuredGlassConfig._instance, new AspectList().add(Aspect.DARKNESS, 4).add(Aspect.LIGHT, 4));
        registerObjectTagSafe(BurningGemStoneConfig._instance, new AspectList().add(Aspect.CRYSTAL, 10).add(Aspect.DARKNESS, 10).add(Aspect.FIRE, 4).add(Aspect.SOUL, 2));
        registerObjectTagSafe(GemStoneTorchConfig._instance, new AspectList().add(Aspect.CRYSTAL, 2).add(Aspect.DARKNESS, 2).add(Aspect.FIRE, 2).add(Aspect.LIGHT, 2).add(Aspect.SOUL, 1));
        registerObjectTagSafe(PotentiaSphereConfig._instance, new AspectList().add(Aspect.EARTH, 2).add(Aspect.CRYSTAL, 2).add(Aspect.LIGHT, 2).add(Aspect.MECHANISM, 1));
        registerObjectTagSafe(BiomeExtractConfig._instance, new AspectList().add(Aspect.EARTH, 2).add(Aspect.EXCHANGE, 4));
        registerObjectTagSafe(EnvironmentalAccumulationCoreConfig._instance, new AspectList().add(Aspect.EXCHANGE, 10).add(Aspect.MECHANISM, 2));

    }

    private static void registerEntityTagSafe(MobConfig mobConfig, AspectList aspects, ThaumcraftApi.EntityTagsNBT... nbt) {
        if(mobConfig != null) {
            ThaumcraftApi.registerEntityTag(mobConfig.getNamedId(), aspects, nbt);
        }
    }

    private static void registerObjectTagSafe(ItemConfig itemConfig, AspectList aspects) {
        if(itemConfig != null) {
            ThaumcraftApi.registerObjectTag(new ItemStack(itemConfig.getItemInstance()), aspects);
        }
    }

    private static void registerObjectTagSafe(ItemConfig itemConfig, int meta, AspectList aspects) {
        if(itemConfig != null) {
            ThaumcraftApi.registerObjectTag(new ItemStack(itemConfig.getItemInstance(), 1, meta), aspects);
        }
    }

    private static void registerObjectTagSafe(BlockConfig blockConfig, AspectList aspects) {
        if(blockConfig != null) {
            ThaumcraftApi.registerObjectTag(new ItemStack(blockConfig.getBlockInstance()), aspects);
        }
    }

}
