## EvilCraft

[![Build Status](https://travis-ci.org/CyclopsMC/EvilCraft.svg?branch=master-1.8)](https://travis-ci.org/rubensworks/EvilCraft)
[![Download](https://api.bintray.com/packages/cyclopsmc/dev/EvilCraft/images/download.svg) ](https://bintray.com/cyclopsmc/dev/EvilCraft/_latestVersion)

All stable releases (including deobfuscated builds) can be found on [CurseForge](http://minecraft.curseforge.com/mc-mods/74610-evilcraft/files).

All downloads (including deobfuscated builds) can be found on [Curse](http://minecraft.curseforge.com/mc-mods/74610-evilcraft/files)

The master branch will always contain the latest released version with possibly some bug fixes for that version that will eventually be released.
New versions will be created in branched named like version-{mc_version}-{next_mod_version}

## Team
Lead developers:
 * rubensworks (kroeserr)
 * immortaleeb (\_EeB\_)

Model/texture artists:
 * Davivs69

Testers:
 * JonaBrackenwood

### Contributing
* Before submitting a pull request containing a new feature, please discuss this first with one of the lead developers.
* When fixing an accepted bug, make sure to declare this in the issue so that no duplicate fixes exist.
* Always make sure you have at least the Minecraft and Forge versions that are specified above.
* All code must comply to our coding conventions, be clean and be well documented.

### Issues
* All bug reports and other issues are appreciated. If the issue is a crash, please include the FULL Forge log.
* Before submission, first check for duplicates, including already closed issues since those can then be re-opened.
* Also add appropriate labels to the issues, preferably the *-pending labels.
* Regular questions don't belong here, please use a contact channel for that.

### Building and setting up a development environment

#### Notes before building
When it comes to minecraft modding we advice you to use either IntelliJ Idea or Eclipse because of their support for customization. Unfortunately we have found eclipse to be rather unreliable when it comes to building projects using ForgeGradle and thus we are unable to provide you with instructions to setup a _reliable_ development environment (reliable, as in _doesn't break every so often_). Should there be anyone who does get EvilCraft working properly in an eclipse environment, then please by all means send us the steps you followed and we will add them to this readme.

EvilCraft uses [Project Lombok](http://projectlombok.org/) -- an annotation processor that allows us you to generate constructors, getters and setters using annotations -- to speed up recurring tasks and keep part of our codebase clean at the same time. Because of this it is advised that you install a plugin for your IDE that supports Project Lombok. Should you encounter any weird errors concerning missing getter or setter methods, it's probably because your code has not been processed by Project Lombok's processor. A list of Project Lombok plugins can be found [here](http://projectlombok.org/download.htm).

#### Creating a lib folder for mod dependencies
EvilCraft provides compatibility with certain mods and because of this, you will require the deobfuscated jars and/or api's of these mods if you want to be able to run EvilCraft from an IDE. Luckely most mods provide a repository from which these required jars will automatically be downloaded when using our gradle build script. Mods that do not provide access to a repository will have to be downloaded manually and stored inside a _lib folder somewhere in your filesystem_ (e.g. `/home/user/mc_libs`).
In order to get EvilCraft to run from you IDE, you will have to download the following mod's unobfuscated jars and manually put them inside your _lib folder_:

 - [BloodMagic](https://www.dropbox.com/sh/0aqvalqobu61t44/AADZq-GuoDeuNzgg6zueVM_Ca)
 - [EE3](http://minecraft.curseforge.com/mc-mods/65509-ee3/files)

#### IntelliJ IDEA
1. Make sure you have read the section on _Creating a lib folder for mod dependencies_ before continuing. In this example we will assume the lib folder is located at `/home/user/mc_libs`.

2. Clone the EvilCraft source code using git and cd inside the EvilCraft directory

  ```
  git clone https://github.com/rubensworks/EvilCraft
  cd EvilCraft/
  ```

3. Make a copy of `gradle.properties_template` and name it `gradle.properties`

  ```
  cp gradle.properties_template gradle.properties
  ```

4. Edit `gradle.properties` and point the value of `libs_path` to a your lib folder (be sure to use an **absolute path**)

  ```
  libs_path=/home/user/mc_libs
  ```

5. Execute the following gradle command to setup your workspace and create the necessary idea project files:

  ```
  ./gradlew setupDecompWorkspace idea genIntellijRuns
  ```

6. Open the `EvilCraft.ipr` project file using IntelliJ IDEA

7. Install the IDEA _Lombok plugin_ by going to `Preferences > Plugins` and searching for `Lombok Plugin`

8. After installing the _Lombok plugin_, go to `Preferences > Build, Execution, Deployment > Compiler` and enable `Enable annotation processing`

9. Select `Minecraft Client` from _Run configurations_ and press the green play button. Minecraft should now launch with EvilCraft and all of its dependencies loaded :)

### License
All code and images are licenced under [Creative Commons 4](http://creativecommons.org/licenses/by/4.0/)
