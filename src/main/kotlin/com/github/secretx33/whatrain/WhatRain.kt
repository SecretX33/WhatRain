package com.github.secretx33.whatrain

import com.github.secretx33.whatrain.events.NoRainEvent
import com.github.secretx33.whatrain.utils.VersionUtils.serverVersion
import com.github.secretx33.whatrain.utils.VersionUtils.v1_12_2_R01
import com.github.secretx33.whatrain.utils.gameRule
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.bind
import org.koin.dsl.module

@KoinApiExtension
class WhatRain : JavaPlugin(), KoinComponent {

    override fun onEnable() {
        saveDefaultConfig()
        // Our dependency injection library
        startKoin {
            // Makes Koin only print error messages
            printLogger(Level.ERROR)
            modules(module {
                // Tells Koin to provide "this@WhatRain" object (our main class) if someone asks for "Plugin" or for "JavaPlugin"
                single<Plugin> { this@WhatRain } bind JavaPlugin::class
            })
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
}
