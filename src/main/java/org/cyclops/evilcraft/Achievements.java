package org.cyclops.evilcraft;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;
import org.cyclops.evilcraft.block.BoxOfEternalClosure;
import org.cyclops.evilcraft.block.BoxOfEternalClosureConfig;
import org.cyclops.evilcraft.block.SpiritFurnace;
import org.cyclops.evilcraft.block.SpiritFurnaceConfig;
import org.cyclops.evilcraft.item.*;

/**
 * Obtainable achievements in this mod.
 * @author rubensworks
 *
 */
public class Achievements {

	/**
	 * First farting achievement.
	 */
	public static final Achievement FART = new ExtendedAchievement("fart", 4, 1, new ItemStack(Items.fireworks), null);
	/**
	 * Discovering the first age.
	 */
	public static final Achievement FIRST_AGE = new ExtendedAchievement("firstAge", 0, 4, new ItemStack(Configs.isEnabled(DarkGemConfig.class) ? DarkGem.getInstance() : Items.potato), null);
    /**
     * Pick up a copy of Origins of Darkness.
     */
    public static final Achievement EVIL_SOURCE = new ExtendedAchievement("evilSource", 0, 6, new ItemStack(Configs.isEnabled(OriginsOfDarknessConfig.class) ? OriginsOfDarkness.getInstance() : Items.potato), FIRST_AGE);
    /**
	 * Discovering the second age.
	 */
	public static final Achievement SECOND_AGE = new ExtendedAchievement("secondAge", 2, 4, new ItemStack(Configs.isEnabled(BloodExtractorConfig.class) ? BloodExtractor.getInstance() : Items.potato), FIRST_AGE);
	/**
	 * Distort a lot of entities at once.
	 */
	public static final Achievement DISTORTER = new ExtendedAchievement("masterDistorter", 2, 6, new ItemStack(Configs.isEnabled(MaceOfDistortionConfig.class) ? MaceOfDistortion.getInstance() : Items.potato), SECOND_AGE);
	/**
	 * Make a spirit furnace.
	 */
	public static final Achievement SPIRIT_COOKER = new ExtendedAchievement("spiritCooker", 4, 4, new ItemStack(Configs.isEnabled(SpiritFurnaceConfig.class) ? SpiritFurnace.getInstance() : Blocks.anvil), SECOND_AGE);
	/**
	 * Send a beam to a spirit.
	 */
	public static final Achievement CLOSURE = new ExtendedAchievement("closure", -2, 2, new ItemStack(Configs.isEnabled(BoxOfEternalClosureConfig.class) ? BoxOfEternalClosure.getInstance() : Blocks.anvil), FIRST_AGE);
	/**
	 * Distort a player.
	 */
	public static final Achievement PLAYER_DISTORTER = new ExtendedAchievement("playerDistorter", 4, 6, new ItemStack(Items.skull, 1, 3), DISTORTER);
	/**
	 * Attack a player using the necromancer staff.
	 */
	public static final Achievement PLAYER_DEVASTATOR = new ExtendedAchievement("playerDevastator", 6, 6, new ItemStack(Configs.isEnabled(NecromancerStaffConfig.class) ? NecromancerStaff.getInstance() : Items.potato, 1, 3), PLAYER_DISTORTER);
	/**
	 * Make a spirit furnace.
	 */
	public static final Achievement POWER_CRAFTING = new ExtendedAchievement("powerCrafting", 0, 2, new ItemStack(Configs.isEnabled(ExaltedCrafterConfig.class) ? ExaltedCrafter.getInstance() : Items.potato), FIRST_AGE);
    /**
     * Eat some Humanoid Flesh.
     */
    public static final Achievement CANNIBAL = new ExtendedAchievement("cannibal", 4, -1, new ItemStack(Configs.isEnabled(WerewolfFleshConfig.class) ? WerewolfFlesh.getInstance() : Items.potato, 1, 1), null);


    private static final Achievement[] ACHIEVEMENTS = {
        EVIL_SOURCE,
		FART,
		FIRST_AGE,
		SECOND_AGE,
		DISTORTER,
		SPIRIT_COOKER,
		CLOSURE,
		PLAYER_DISTORTER,
		PLAYER_DEVASTATOR,
		POWER_CRAFTING,
        CANNIBAL
	};
	
	/**
	 * Register the achievements.
	 */
	public static void registerAchievements() {
		AchievementPage.registerAchievementPage(new AchievementPage(Reference.MOD_NAME, ACHIEVEMENTS));
	}
	
	static class ExtendedAchievement extends Achievement {

		public ExtendedAchievement(String id,
				int column, int row, ItemStack item,
				Achievement parent) {
			super(Reference.MOD_ID + "." + id, Reference.MOD_ID + "." + id, row, column, item, parent);
			registerStat();
		}
		
	}
	
}
