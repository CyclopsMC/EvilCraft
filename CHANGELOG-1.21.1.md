# Changelog for Minecraft 1.21.1
All notable changes to this project will be documented in this file.

<a name="1.21.1-1.2.87"></a>
## [1.21.1-1.2.87](https://github.com/CyclopsMC/EvilCraft/compare/1.21.1-1.2.86...1.21.1-1.2.87) - 2026-05-16 07:31:39


### Fixed
* Fix Dead Bush recipe not handling third-party shears, Closes #1210

<a name="1.21.1-1.2.86"></a>
## [1.21.1-1.2.86](https://github.com/CyclopsMC/EvilCraft/compare/1.21.1-1.2.85...1.21.1-1.2.86) - 2026-04-25 17:06:20 +0200


### Fixed
* Fix broken Curios integration, Closes CyclopsMC/EvilCraft#1203

<a name="1.21.1-1.2.85"></a>
## [1.21.1-1.2.85](https://github.com/CyclopsMC/EvilCraft/compare/1.21.1-1.2.84...1.21.1-1.2.85) - 2026-04-11 19:55:28 +0200


### Fixed
* Fix CME in EntityVengeanceSpirit.canSpawnNew during dimension unload (#1182), Closes #1180
* Fix IllegalStateException when BiomeExtract contains a biome from a removed/updated mod (#1179), Closes #1178

<a name="1.21.1-1.2.84"></a>
## [1.21.1-1.2.84](https://github.com/CyclopsMC/EvilCraft/compare/1.21.1-1.2.83...1.21.1-1.2.84) - 2026-03-28 10:00:53 +0100


### Added
* Add enchantmentIdBlacklist config for the Purifier (#1175), Closes #855

<a name="1.21.1-1.2.83"></a>
## [1.21.1-1.2.83](https://github.com/CyclopsMC/EvilCraft/compare/1.21.1-1.2.82...1.21.1-1.2.83) - 2026-03-09 16:18:50 +0100


### Added
* Add config option to only spawn spirits on player kills
  By default, the old behaviour (also spawning on non-player kills) is kept,
  but can be changed by setting `spawnOnNonPlayerKills` to `false`.

<a name="1.21.1-1.2.82"></a>
## [1.21.1-1.2.82](https://github.com/CyclopsMC/EvilCraft/compare/1.21.1-1.2.81...1.21.1-1.2.82) - 2026-02-17 11:32:37 +0100


### Added
* Add auto-output toggle for Eternal Water Block, Closes #630 (#1157)
* Add config option to disable broom smash modifier, Closes #737 (#1155)

### Fixed
* Fix race condition in testVengeanceSpiritSpawn causing NPE (#1156)

<a name="1.21.1-1.2.81"></a>
## [1.21.1-1.2.81](https://github.com/CyclopsMC/EvilCraft/compare/1.21.1-1.2.80...1.21.1-1.2.81) - 2026-02-04 15:31:52 +0100


### Fixed
* Fix Broom duping when changing dimensions, Closes #1154

<a name="1.21.1-1.2.80"></a>
## [1.21.1-1.2.80](https://github.com/CyclopsMC/EvilCraft/compare/1.21.1-1.2.79...1.21.1-1.2.80) - 2025-12-22 14:17:57 +0100


### Added
* Add Werewolvian villager house to villages, Closes #1146

<a name="1.21.1-1.2.79"></a>
## [1.21.1-1.2.79](https://github.com/CyclopsMC/EvilCraft/compare/1.21.1-1.2.78...1.21.1-1.2.79) - 2025-11-15 06:36:47 +0100


### Added
* Add translations through Crowdin (#1126)

### Changed
* Restore Jade compatibility, Closes CyclopsMC/EvilCraft#1134
* Accept more damageable items in Blood Chest, Closes #1133

### Fixed
* Fix Box Of Eternal Closure breaking when placing neighbour
* Fetch spirit furnace recipes from server, Closes CyclopsMC/EvilCraft#1130
* Fix JEI Spirit Furnace and Reanimator icons, Closes CyclopsMC/EvilCraft#1128

<a name="1.21.1-1.2.78"></a>
## [1.21.1-1.2.78](https://github.com/CyclopsMC/EvilCraft/compare/1.21.1-1.2.77...1.21.1-1.2.78) - 2025-08-14 19:57:46 +0200


### Fixed
* Fetch spirit furnace recipes from server, Closes CyclopsMC/EvilCraft#1130

<a name="1.21.1-1.2.77"></a>
## [1.21.1-1.2.77](https://github.com/CyclopsMC/EvilCraft/compare/1.21.1-1.2.76...1.21.1-1.2.77) - 2025-08-10 12:59:11 +0200


### Fixed
* Fix JEI Spirit Furnace and Reanimator icons, Closes CyclopsMC/EvilCraft#1128

<a name="1.21.1-1.2.76"></a>
## [1.21.1-1.2.76](https://github.com/CyclopsMC/EvilCraft/compare/1.21.1-1.2.75...1.21.1-1.2.76) - 2025-08-08 16:27:40 +0200


### Added
* Add Spirit Furnace and Reanimator JEI support

<a name="1.21.1-1.2.75"></a>
## [1.21.1-1.2.75](https://github.com/CyclopsMC/EvilCraft/compare/1.21.1-1.2.74...1.21.1-1.2.75) - 2025-07-30 16:06:08 +0200


### Changed
* Set correct position within spiked plate fake player, Closes #1127

<a name="1.21.1-1.2.74"></a>
## [1.21.1-1.2.74](https://github.com/CyclopsMC/EvilCraft/compare/1.21.1-1.2.73...1.21.1-1.2.74) - 2025-07-29 20:15:57 +0200


### Changed
* Optimize broom part tooltip event handler, Closes #1125

### Fixed
* Fix broom parts not showing in creative tabs

<a name="1.21.1-1.2.73"></a>
## [1.21.1-1.2.73](https://github.com/CyclopsMC/EvilCraft/compare/1.21.1-1.2.72...1.21.1-1.2.73) - 2025-07-29 17:04:49 +0200


### Added
* Add translations through Crowdin (#1116)
* Add PT_BR localization (#1120)

### Fixed
* Fix broken off-hand block interactions, Closes #1123
* Fix broom crash when mounted by non-living entity, Closes #1124
* Fix typo in unusing book description, Closes #1122
* Fix some spelling and grammar typos in lang (#1119)

<a name="1.21.1-1.2.72"></a>
## [1.21.1-1.2.72](https://github.com/CyclopsMC/EvilCraft/compare/1.21.1-1.2.71...1.21.1-1.2.72) - 2025-05-29 16:38:50 +0200


### Fixed
* Fix Vengeance Spirits not killable by command, Closes #1114

<a name="1.21.1-1.2.71"></a>
## [1.21.1-1.2.71](https://github.com/CyclopsMC/EvilCraft/compare/1.21.1-1.2.70...1.21.1-1.2.71) - 2025-05-11 14:32:24 +0200


### Changed
* Add translations through Crowdin

### Fixed
* Fix unusing stopping unbreakable tools, Closes #1113

<a name="1.21.1-1.2.70"></a>
## [1.21.1-1.2.70](https://github.com/CyclopsMC/EvilCraft/compare/1.21.1-1.2.69...1.21.1-1.2.70) - 2025-05-03 15:48:14 +0200


### Added
* Add lectern and bookshelf support for infobook, Closes #1110

<a name="1.21.1-1.2.69"></a>
## [1.21.1-1.2.69](https://github.com/CyclopsMC/EvilCraft/compare/1.21.1-1.2.68...1.21.1-1.2.69) - 2025-04-12 17:54:19 +0200


### Fixed
* Fix tanks with fluids not being combinable

<a name="1.21.1-1.2.68"></a>
## [1.21.1-1.2.68](https://github.com/CyclopsMC/EvilCraft/compare/1.21.1-1.2.67...1.21.1-1.2.68) - 2025-03-12 14:44:53 +0100


### Fixed
* Fix dead bush recipe not breaking shears during autocrafting

<a name="1.21.1-1.2.67"></a>
## [1.21.1-1.2.67](https://github.com/CyclopsMC/EvilCraft/compare/1.21.1-1.2.66...1.21.1-1.2.67) - 2025-03-11 07:36:02 +0100


### Added
* Add translations through Crowdin (#1094)

### Fixed
* Fix dead bush recipe not showing up in JEI

<a name="1.21.1-1.2.66"></a>
## [1.21.1-1.2.66](https://github.com/CyclopsMC/EvilCraft/compare/1.21.1-1.2.65...1.21.1-1.2.66) - 2025-03-10 06:08:22 +0100


### Fixed
* Fix brooms having a null particle icon, Closes #1104
* Fix wrong getIngredients in dead bush recipe

<a name="1.21.1-1.2.65"></a>
## [1.21.1-1.2.65](https://github.com/CyclopsMC/EvilCraft/compare/1.21.1-1.2.64...1.21.1-1.2.65) - 2025-02-03 19:55:39 +0100


### Added
* Add nl_nl translations through Crowdin (#1091)

### Fixed
* Fix inconsistent NBT saving of disableable items
  Related to CyclopsMC/EvilCraft#1089
* Fix Gem Stone Torch crash in SpongeNeo, Closes #1092

<a name="1.21.1-1.2.64"></a>
## [1.21.1-1.2.64](https://github.com/CyclopsMC/EvilCraft/compare/1.21.1-1.2.63...1.21.1-1.2.64) - 2025-01-19 09:45:10 +0100


### Fixed
* Fix broom crash when controlling entity is null, Closes #1087

<a name="1.21.1-1.2.63"></a>
## [1.21.1-1.2.63](https://github.com/CyclopsMC/EvilCraft/compare/1.21.1-1.2.62...1.21.1-1.2.63) - 2025-01-12 07:10:31 +0100


### Added
* Add ja_jp translations
* Add vi_vn translation
* Add cs_cz translations

### Fixed
* Fix unable to cook player spirits, and possible crash, Closes #1085

<a name="1.21.1-1.2.62"></a>
## [1.21.1-1.2.62](https://github.com/CyclopsMC/EvilCraft/compare/1.21.1-1.2.61...1.21.1-1.2.62) - 2024-12-19 10:41:37 +0100


### Fixed
* Fix Eternal Water Bucket vanishing after world placement, Closes #1076

<a name="1.21.1-1.2.61"></a>
## [1.21.1-1.2.61](https://github.com/CyclopsMC/EvilCraft/compare/1.21.1-1.2.60...1.21.1-1.2.61) - 2024-12-18 10:01:04 +0100


### Fixed
* Fix timeout when producing skull of player in box, Closes #1075

<a name="1.21.1-1.2.60"></a>
## [1.21.1-1.2.60](https://github.com/CyclopsMC/EvilCraft/compare/1.21.1-1.2.59...1.21.1-1.2.60) - 2024-12-13 10:52:28 +0100


### Added
* Add various item and block tags, Closes #1065

### Fixed
* Don't invoke WorldHelpers.efficientTick client-side

<a name="1.21.1-1.2.59"></a>
## [1.21.1-1.2.59](https://github.com/CyclopsMC/EvilCraft/compare/1.21.1-1.2.58...1.21.1-1.2.59) - 2024-10-24 14:48:00 +0200


### Fixed
* Fix Undead Trees producing Blood Stains at wrong location, Closes #1068
* Fix spawned Weather Containers not working in Envir Acc, Closes #1067

<a name="1.21.1-1.2.58"></a>
## [1.21.1-1.2.58](https://github.com/CyclopsMC/EvilCraft/compare/1.21.1-1.2.57...1.21.1-1.2.58) - 2024-10-02 17:17:52 +0200


### Fixed
* Fix VengeanceSpirit using wrong RandomSource, Closes #1064

<a name="1.21.1-1.2.57"></a>
## [1.21.1-1.2.57](https://github.com/CyclopsMC/EvilCraft/compare/1.21.1-1.2.56...1.21.1-1.2.57) - 2024-09-23 17:52:19 +0200


### Fixed
* Reduce default spawn rate of Silverfish and Netherfish
  Netherfish will now only spawn in Soul Sand Valleys,
  but can be overriden via datapacks.
* Fix Primed Pendant not working for most potions, Closes #1062

<a name="1.21.1-1.2.56"></a>
## [1.21.1-1.2.56](https://github.com/CyclopsMC/EvilCraft/compare/1.21.1-1.2.55...1.21.1-1.2.56) - 2024-09-09 17:39:48 +0200


### Fixed
* Fix Spirit Furnace not resuming after drained by hopper, Closes #1058

<a name="1.21.1-1.2.55"></a>
## [1.21.1-1.2.55](https://github.com/CyclopsMC/EvilCraft/compare/1.21.1-1.2.54...1.21.1-1.2.55) - 2024-09-05 17:00:03 +0200


### Fixed
* Fix crash when copy_tank_data loot function is applied to wrong block, Closes #1056

<a name="1.21.1-1.2.54"></a>
## [1.21.1-1.2.54](https://github.com/CyclopsMC/EvilCraft/compare/1.21.1-1.2.53...1.21.1-1.2.54) - 2024-09-01 17:39:29 +0200


### Added
* Add config option to disable villager lightning conversion, Closes #1053
* Allow random bad enchant chance in Blood Chest to be configured

### Fixed
* Fix lightning-struck villagers always becoming werewolves, Closes #1052
* Fix crash when adding bad enchant in Blood Chest, Closes #1054
* Fix memory leak in heal-from-damage effects, Closes #1051

<a name="1.21.1-1.2.53"></a>
## [1.21.1-1.2.53](https://github.com/CyclopsMC/EvilCraft/compare/1.21.1-1.2.52...1.21.1-1.2.53) - 2024-08-21 18:55:05 +0200


### Fixed
* Fix JEI memory leak, Closes CyclopsMC/EvilCraft#1049

<a name="1.21.1-1.2.52"></a>
## [1.21.1-1.2.52](https://github.com/CyclopsMC/EvilCraft/compare/1.21.1-1.2.51...1.21.1-1.2.52) - 2024-08-19 20:33:13 +0200


### Fixed
* Fix broken Blook rendering in Purifier
* Fix missing RegisterSpawnPlacementsEvent
* Fix crash with Enchanted Books in Purifier, Closes #1048
* Refer to NeoForge's updateJSONURL instead of Forge's

<a name="1.21.1-1.2.51"></a>
## [1.21.1-1.2.51] - 2024-08-11 08:54:51 +0200


### Fixed
* Fix crash when vengeance enchantment is active on bow
