package com.github.secretx33.whatrain

import com.github.secretx33.whatrain.events.NoRainEvent
import com.github.secretx33.whatrain.utils.*
import com.github.secretx33.whatrain.utils.VersionUtils.serverVersion
import com.github.secretx33.whatrain.utils.VersionUtils.v1_12_2_R01
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import org.koin.core.component.KoinApiExtension
import org.koin.core.logger.Level
import org.koin.dsl.bind
import org.koin.dsl.module

@KoinApiExtension
class WhatRain : JavaPlugin(), CustomKoinComponent {

    // Module for Koin
    // here you should put every single one of your dependencies that you want to be injected into other classes
    val mod = module {
        // Tells Koin to provide "this@WhatRain" object (our main class) if someone asks for "Plugin" or for "JavaPlugin"
        // 'bind' adds a second type to the object, if we didn't have it here then Koin would only provide 'this@WhatRain' if
        // someone asked specifically for a "Plugin" object
        single<Plugin> { this@WhatRain } bind JavaPlugin::class
        // single { NoRainEvent() } // example of telling koin to use NoRainEvent as dependency, then just use get<NoRainEvent>()
        // in onEnable to make Koin instantiate it and you're done
        // 'single' tells Koin this is singleton, it is instantiated only ONCE and provided to ALL consumers
        // if you want to create a new object every time one asks for it, use 'factory' instead of 'single'
    }

    override fun onEnable() {
        saveDefaultConfig()
        // > Our dependency injection library <
        // make SURE, and I repeat, SURE, that you're importing from utils/Koin.kt file and NOT from default Koin library
        // otherwise you're going to get conflicts, a lot of them if someone else is also using the default Koin instance
        startKoin {
            // Makes Koin only print error messages
            printLogger(Level.ERROR)
            modules(mod)
        }
        if(config.getBoolean("enable-rain-cancel-eventlistener")) {
            // This "get" will tell Koin to inject our Plugin dependency here, get is NOT lazy, the instantiation and injection happens the moment you call "get" (opposed to "inject" which is lazy and will only create/inject the dependency when you use it for the first time)
            // Anyway you cannot use inject here because you need to provide the dependency to instantiate the NoRainEvent class (it's required by the constructor)
            NoRainEvent(get())
        }
        Bukkit.getWorlds().forEach {
            if(config.getBoolean("disable-daylight-cycle"))
                it.gameRule("doDaylightCycle", false)
            if(config.getBoolean("disable-weather-cycle"))
                it.gameRule("doWeatherCycle", false)
            if(config.getBoolean("enable-keep-inventory"))
                it.gameRule("keepInventory", true)
            if(config.getBoolean("run-time-set-zero"))
                it.time = 0
        }
        if(serverVersion > v1_12_2_R01) {
            if(config.getBoolean("set-default-gamemode-to-creative"))
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "defaultgamemode creative")
        }
        if(config.getBoolean("display-loaded-msg"))
            server.consoleSender.sendMessage("${ChatColor.BLUE}[NoMoreRain]${ChatColor.GRAY} Loaded")
    }

    override fun onDisable() {
        // Make sure to stop Koin is the last thing you do in onDisable function
        unloadKoinModules(mod)
        stopKoin()
    }
}
