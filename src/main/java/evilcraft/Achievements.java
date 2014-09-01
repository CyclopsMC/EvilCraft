package evilcraft;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;
import evilcraft.blocks.BoxOfEternalClosure;
import evilcraft.blocks.BoxOfEternalClosureConfig;
import evilcraft.blocks.SpiritFurnace;
import evilcraft.blocks.SpiritFurnaceConfig;
import evilcraft.items.BloodExtractor;
import evilcraft.items.BloodExtractorConfig;
import evilcraft.items.Broom;
import evilcraft.items.BroomConfig;
import evilcraft.items.DarkGem;
import evilcraft.items.DarkGemConfig;
import evilcraft.items.MaceOfDistortion;
import evilcraft.items.MaceOfDistortionConfig;

/**
 * Obtainable achievements in this mod.
 * @author rubensworks
 *
 */
public class Achievements {

	/**
	 * First farting achievement.
	 */
	public static final Achievement FART = new ExtendedAchievement("fart", 2, 0, new ItemStack(Items.fireworks), null);
	/**
	 * Discovering the first age.
	 */
	public static final Achievement FIRST_AGE = new ExtendedAchievement("firstAge", 0, 0, new ItemStack(Configs.isEnabled(DarkGemConfig.class) ? DarkGem.getInstance() : Items.potato), null);
	/**
	 * Discovering the second age.
	 */
	public static final Achievement SECOND_AGE = new ExtendedAchievement("secondAge", 0, 2, new ItemStack(Configs.isEnabled(BloodExtractorConfig.class) ? BloodExtractor.getInstance() : Items.potato), FIRST_AGE);
	/**
	 * Distort a lot of entities at once.
	 */
	public static final Achievement DISTORTER = new ExtendedAchievement("masterDistorter", 2, 4, new ItemStack(Configs.isEnabled(MaceOfDistortionConfig.class) ? MaceOfDistortion.getInstance() : Items.potato), SECOND_AGE);
	/**
	 * Make a spirit furnace.
	 */
	public static final Achievement SPIRIT_COOKER = new ExtendedAchievement("spiritCooker", 4, 4, new ItemStack(Configs.isEnabled(SpiritFurnaceConfig.class) ? SpiritFurnace.getInstance() : Blocks.anvil), SECOND_AGE);
	/**
	 * Send a beam to a spirit.
	 */
	public static final Achievement CLOSURE = new ExtendedAchievement("closure", 0, 4, new ItemStack(Configs.isEnabled(BoxOfEternalClosureConfig.class) ? BoxOfEternalClosure.getInstance() : Blocks.anvil), FIRST_AGE);
	/**
	 * Tidy up excrement pile with a broom.
	 */
	public static final Achievement TIDY = new ExtendedAchievement("tidy", 4, 0, new ItemStack(Configs.isEnabled(BroomConfig.class) ? Broom.getInstance() : Items.potato), null);
	/**
	 * Distort a player.
	 */
	public static final Achievement PLAYER_DISTORTER = new ExtendedAchievement("playerDistorter", 2, 6, new ItemStack(Items.skull, 1, 3), DISTORTER);
	
	private static final Achievement[] ACHIEVEMENTS = {
		FART,
		FIRST_AGE,
		SECOND_AGE,
		DISTORTER,
		SPIRIT_COOKER,
		CLOSURE,
		TIDY,
		PLAYER_DISTORTER
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
		}
		
	}
	
}
