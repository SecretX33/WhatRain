package com.github.secretx33.whatrain

import com.github.secretx33.whatrain.events.NoRainEvent
import com.github.secretx33.whatrain.utils.gameRule
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.context.startKoin
import org.koin.dsl.bind
import org.koin.dsl.module

@KoinApiExtension
class Main : JavaPlugin(), KoinComponent {

    override fun onEnable() {
        saveDefaultConfig()
        startKoin {
            printLogger()
            modules(module {
                single<Plugin> { this@Main } bind JavaPlugin::class
            })
        }
        val noRainEvent: NoRainEvent
        if(config.getBoolean("enable-rain-cancel-eventlistener")){
            noRainEvent = NoRainEvent(get())
        }
        Bukkit.getWorlds().forEach {
            if(config.getBoolean("disable-daylight-cycle"))
                it.gameRule("doDaylightCycle", false)
            if(config.getBoolean("disable-weather-cycle"))
                it.gameRule("doWeatherCycle", false)
        }
        if(config.getBoolean("set-default-gamemode-to-creative"))
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "defaultgamemode creative")
        if(config.getBoolean("run-time-set-zero"))
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "time set 0")
        if(config.getBoolean("display-loaded-msg"))
            server.consoleSender.sendMessage("${ChatColor.BLUE}[NoMoreRain]${ChatColor.GRAY} Loaded")
    }
}
