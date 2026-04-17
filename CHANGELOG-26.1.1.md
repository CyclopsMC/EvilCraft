# Changelog for Minecraft 26.1.1
All notable changes to this project will be documented in this file.

<a name="26.1.1-1.2.86"></a>
## [26.1.1-1.2.86](https://github.com/CyclopsMC/EvilCraft/compare/26.1.1-1.2.85...26.1.1-1.2.86) - 2026-04-17 17:38:31


### Changed
* Add leaf particle colors for Undead Leaves (#1189)

### Fixed
* Fix incorrect Box of Eternal Closure beam rendering direction
* Fix Vengeance Focus beam starting from wrong location
* Fix missing texture on Envir Acc beam rendering
* Fix blood bubble particle sometimes causing crashes
* Fix color of lightning weather container
* Fix weather containers not showing up in creative tab
* Fix division by zero in ParticleBubbleExtended.tick() with pre-decrement
* Fix crash when breaking Box of Eternal Closure (#1191)
* Fix crash when breaking box of eternal closure by checking block type before setValue

<a name="26.1.1-1.2.85"></a>
## [26.1.1-1.2.85](https://github.com/CyclopsMC/EvilCraft/compare/26.1.1-1.2.84...26.1.1-1.2.85) - 2026-04-11 19:59:28 +0200


### Changed
* Restore JEI mod compat
* Optimize images (#1184)

### Fixed
* Fix singleton recipe instances
* Fix crash when equipping spectral glasses
* Fix broken spectral glasses texture when equipped
* Fix CME in EntityVengeanceSpirit.canSpawnNew during dimension unload (#1182), Closes #1180
* Fix IllegalStateException when BiomeExtract contains a biome from a removed/updated mod (#1179), Closes #1178

<a name="26.1.1-1.2.84"></a>
## [26.1.1-1.2.84] - 2026-04-10 14:38:35 +0200


Initial 26.1.1 release
