# Changelog for Minecraft 1.20.1
All notable changes to this project will be documented in this file.

<a name="1.20.1-1.2.60"></a>
## [1.20.1-1.2.60](/compare/1.20.1-1.2.59...1.20.1-1.2.60) - 2026-05-16 07:30:30


### Fixed
* Fix Dead Bush recipe not handling third-party shears, Closes #1210

<a name="1.20.1-1.2.59"></a>
## [1.20.1-1.2.59](/compare/1.20.1-1.2.58...1.20.1-1.2.59) - 2025-11-15 06:34:08 +0100


### Changed
* Accept more damageable items in Blood Chest, Closes #1133
* Restore Jade compatibility, Closes CyclopsMC/EvilCraft#1134

### Fixed
* Fix Box Of Eternal Closure breaking when placing neighbour
* Fix JEI Spirit Furnace and Reanimator icons, Closes CyclopsMC/EvilCraft#1128

<a name="1.20.1-1.2.58"></a>
## [1.20.1-1.2.58](/compare/1.20.1-1.2.57...1.20.1-1.2.58) - 2025-08-14 19:57:35 +0200


### Fixed
* Fetch spirit furnace recipes from server, Closes CyclopsMC/EvilCraft#1130

<a name="1.20.1-1.2.57"></a>
## [1.20.1-1.2.57](/compare/1.20.1-1.2.56...1.20.1-1.2.57) - 2025-08-10 12:58:55 +0200


### Fixed
* Fix JEI Spirit Furnace and Reanimator icons, Closes CyclopsMC/EvilCraft#1128

<a name="1.20.1-1.2.56"></a>
## [1.20.1-1.2.56](/compare/1.20.1-1.2.55...1.20.1-1.2.56) - 2025-08-08 16:27:24 +0200


### Added
* Add Spirit Furnace and Reanimator JEI support

<a name="1.20.1-1.2.55"></a>
## [1.20.1-1.2.55](/compare/1.20.1-1.2.54...1.20.1-1.2.55) - 2025-07-29 17:03:19 +0200


### Fixed
* Fix broom crash when mounted by non-living entity, Closes #1124
* Fix typo in unusing book description, Closes #1122

<a name="1.20.1-1.2.54"></a>
## [1.20.1-1.2.54](/compare/1.20.1-1.2.53...1.20.1-1.2.54) - 2025-05-29 16:37:52 +0200


### Fixed
* Fix Vengeance Spirits not killable by command, Closes #1114

<a name="1.20.1-1.2.53"></a>
## [1.20.1-1.2.53](/compare/1.20.1-1.2.52...1.20.1-1.2.53) - 2025-05-03 15:39:28 +0200


### Added
* Add lectern and bookshelf support for infobook, Closes #1110

<a name="1.20.1-1.2.52"></a>
## [1.20.1-1.2.52](/compare/1.20.1-1.2.51...1.20.1-1.2.52) - 2025-03-10 06:06:50 +0100


### Fixed
* Fix brooms having a null particle icon, Closes #1104

<a name="1.20.1-1.2.51"></a>
## [1.20.1-1.2.51](/compare/1.20.1-1.2.50...1.20.1-1.2.51) - 2025-02-03 19:53:05 +0100


### Fixed
* Fix inconsistent NBT saving of disableable items
  Related to CyclopsMC/EvilCraft#1089

<a name="1.20.1-1.2.50"></a>
## [1.20.1-1.2.50](/compare/1.20.1-1.2.49...1.20.1-1.2.50) - 2025-01-19 09:43:05 +0100


### Fixed
* Fix broom crash when controlling entity is null, Closes #1087

<a name="1.20.1-1.2.49"></a>
## [1.20.1-1.2.49](/compare/1.20.1-1.2.48...1.20.1-1.2.49) - 2024-12-13 10:51:28 +0100


### Fixed
* Don't invoke WorldHelpers.efficientTick client-side, Related to #961

<a name="1.20.1-1.2.48"></a>
## [1.20.1-1.2.48](/compare/1.20.1-1.2.47...1.20.1-1.2.48) - 2024-10-02 17:15:10 +0200


### Changed
* Reduce default spawn rate of Silverfish and Netherfish
  Netherfish will now only spawn in Soul Sand Valleys,
  but can be overriden via datapacks.
  Closes #1059

### Fixed
* Fix VengeanceSpirit using wrong RandomSource, Closes #1064

<a name="1.20.1-1.2.47"></a>
## [1.20.1-1.2.47](/compare/1.20.1-1.2.46...1.20.1-1.2.47) - 2024-09-09 17:34:31 +0200


### Fixed
* Fix Spirit Furnace not resuming after drained by hopper, Closes #1058

<a name="1.20.1-1.2.46"></a>
## [1.20.1-1.2.46](/compare/1.20.1-1.2.45...1.20.1-1.2.46) - 2024-09-05 16:57:10 +0200


### Fixed
* Fix crash when copy_tank_data loot function is applied to wrong block, Closes #1056

<a name="1.20.1-1.2.45"></a>
## [1.20.1-1.2.45](/compare/1.20.1-1.2.44...1.20.1-1.2.45) - 2024-09-01 17:36:14 +0200


### Added
* Add config option to disable villager lightning conversion, Closes #1053

### Fixed
* Fix lightning-struck villagers always becoming werewolves, Closes #1052

<a name="1.20.1-1.2.44"></a>
## [1.20.1-1.2.44](/compare/1.20.1-1.2.43...1.20.1-1.2.44) - 2024-08-03 08:24:07 +0200


### Fixed
* Fix Blood Stains not being replaceable

<a name="1.20.1-1.2.43"></a>
## [1.20.1-1.2.43](/compare/1.20.1-1.2.42...1.20.1-1.2.43) - 2024-07-24 15:37:15 +0200


### Changed
* Restore spawning random brooms in loot chests

<a name="1.20.1-1.2.42"></a>
## [1.20.1-1.2.42](/compare/1.20.1-1.2.41...1.20.1-1.2.42) - 2024-07-22 13:27:18 +0200


### Fixed
* Remove unused chalice texture, Closes #1035

<a name="1.20.1-1.2.41"></a>
## [1.20.1-1.2.41](/compare/1.20.1-1.2.40...1.20.1-1.2.41) - 2024-07-16 16:11:18 +0200


### Fixed
* Fix missing blook entity texture, Closes #1030
* Fix Promise of Tenacity III not showing in book, Closes #1031
* Remove unsupported compats from infobook, Closes CyclopsMC/EvilCraft#1028

<a name="1.20.1-1.2.40"></a>
## [1.20.1-1.2.40](/compare/1.20.1-1.2.39...1.20.1-1.2.40) - 2024-06-24 08:44:51 +0200


### Changed
* Remove unsupported compats from infobook, Closes CyclopsMC/EvilCraft#1028

<a name="1.20.1-1.2.39"></a>
## [1.20.1-1.2.39](/compare/1.20.1-1.2.38...1.20.1-1.2.39) - 2024-05-04 09:02:32 +0200


### Fixed
* Fix Effortless Ring step assist conflicting with other mods, Closes #1026

<a name="1.20.1-1.2.38"></a>
## [1.20.1-1.2.38](/compare/1.20.1-1.2.37...1.20.1-1.2.38) - 2024-04-07 13:57:24 +0200


### Fixed
* Fix item IO side mapping of Colossal Blood Chest, Closes #1025
* Catch exceptions when getting vengeance spirit sounds, Closes #1023

<a name="1.20.1-1.2.37"></a>
## [1.20.1-1.2.37](/compare/1.20.1-1.2.36...1.20.1-1.2.37) - 2024-02-05 17:21:56 +0100


### Fixed
* Fix spirit portal not spawning anymore with darkened apple, Closes #1020

<a name="1.20.1-1.2.36"></a>
## [1.20.1-1.2.36](/compare/1.20.1-1.2.35...1.20.1-1.2.36) - 2023-12-27 15:41:09 +0100


### Changed
* Increase spawn rate of Dark Ore, Closes #1018
  Regular Dark Ore spawned too infrequently, and the Deepslate version never spawned at all.

### Fixed
* Fix missing texture for Colossal Blood Chest, Closes #1017

<a name="1.20.1-1.2.35"></a>
## [1.20.1-1.2.35](/compare/1.20.1-1.2.34...1.20.1-1.2.35) - 2023-12-07 17:43:05 +0100


### Fixed
* Fix biome extract crash on pack reload, Closes #1016

<a name="1.20.1-1.2.34"></a>
## [1.20.1-1.2.34](/compare/1.20.1-1.2.33...1.20.1-1.2.34) - 2023-11-27 14:08:32 +0100


### Changed
* Remove chance of loading panorama override, Closes #1014

### Fixed
* Fix temple generation hanging in void worlds, Closes #1013

<a name="1.20.1-1.2.33"></a>
## [1.20.1-1.2.33](/compare/1.20.1-1.2.32...1.20.1-1.2.33) - 2023-10-29 08:53:42 +0100


### Fixed
* Fix Effortless Ring crash, Closes #1011

<a name="1.20.1-1.2.32"></a>
## [1.20.1-1.2.32](/compare/1.20.1-1.2.31...1.20.1-1.2.32) - 2023-10-26 16:34:18 +0200


### Fixed
* Fix incorrect tooltip offsets, Closes #1009

<a name="1.20.1-1.2.31"></a>
## [1.20.1-1.2.31](/compare/1.20.1-1.2.30...1.20.1-1.2.31) - 2023-10-10 17:17:32 +0200


### Fixed
* Fix broom dupe when broken and mounted by players, Closes #1007

<a name="1.20.1-1.2.30"></a>
## [1.20.1-1.2.30](/compare/1.20.1-1.2.29...1.20.1-1.2.30) - 2023-09-25 14:19:25 +0200


### Fixed
* Fix crash when reanimating mobs without spawn egg, Closes #1005

<a name="1.20.1-1.2.29"></a>
## [1.20.1-1.2.29](/compare/1.20.1-1.2.28...1.20.1-1.2.29) - 2023-09-09 11:09:48 +0200


### Changed
* Don't play sounds on Entangled Chalice auto-fill, Closes #1003

### Fixed
* Add Farming for Blockheads merchant to spirit blacklist
  It causes crashes due to rendering issues.
  Closes #1004

<a name="1.20.1-1.2.28"></a>
## [1.20.1-1.2.28](/compare/1.20.1-1.2.27...1.20.1-1.2.28) - 2023-08-27 11:46:32 +0200


### Fixed
* Fix Spirit Furnace not always producing player drops, Closes #1001
* Fix villagers not turning to witches on struck by lightning

<a name="1.20.1-1.2.27"></a>
## [1.20.1-1.2.27](/compare/1.20.1-1.2.26...1.20.1-1.2.27) - 2023-08-01 13:00:46 +0200


### Fixed
* Fix Envir Acc crash when degrading biome, Closes #998

<a name="1.20.1-1.2.26"></a>
## [1.20.1-1.2.26](/compare/1.20.1-1.2.25...1.20.1-1.2.26) - 2023-07-30 12:35:57 +0200


### Fixed
* Fix crash when activating Effortless Ring, Closes #995
* Fix villagers not turning to witches on struck by lightning

<a name="1.20.1-1.2.25"></a>
## [1.20.1-1.2.25](/compare/1.20.1-1.2.24...1.20.1-1.2.25) - 2023-07-04 18:08:29 +0200


### Fixed
* Fix player not being dismounted from kamikaze broom, Closes #989
* Fix acceleration modifier listed twice in book, Closes #988

<a name="1.20.1-1.2.24"></a>
## [1.20.1-1.2.24](/compare/1.20.1-1.2.23...1.20.1-1.2.24) - 2023-07-02 16:28:32 +0200


### Fixed
* Fix crash when Vengeance Spirit is spawned

Closes #991
Closes #992

<a name="1.20.1-1.2.23"></a>
## [1.20.1-1.2.23](/compare/1.20.1-1.2.22...1.20.1-1.2.23) - 2023-07-02 09:28:56 +0200


### Fixed
* Fix keymappings being loaded twice, causing invalid options.txt on first load.

<a name="1.20.1-1.2.22"></a>
## [1.20.1-1.2.22] - 2023-07-02 08:11:22 +0200


Initial 1.20.1 release
