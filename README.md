# WhatRain
What's rain?

This simple plugin remove and prevents rain/storm/snow in all worlds, set default gamemode to creative, set both "doDaylightCycle" and "doWeatherCycle" gamerules to false and set the world time to 0 each time you start the server.

Should work in all bukkit versions, just download and place in your plugins folder, configs are in the config.yml file.

Useful when testing your plugins, mainly when you need to delete a world and the new world comes without any config. **NO. MORE. RAIN.**

## Notes

Take a second glance on the source code, I've used/showed how to use (or do): 

* Dependency injection at its finest (Koin is such a great library)
* The lastest Gradle KTS (kotlin domain specific language) with Shadow Jar
* Copy a license to inside the jar file
* Exclude certain folders from the shadow jar
* Use replace token to replace the plugin name and version in the resource files
* And the code also include a version check for bukkit which you can easily copy and use it aswell (I got this one from EssentialsX, slighty adapted).
