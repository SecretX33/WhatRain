package com.github.secretx33.whatrain.events

import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.weather.WeatherChangeEvent
import org.bukkit.plugin.Plugin

class NoRainEvent(plugin: Plugin) : Listener {

    init { Bukkit.getPluginManager().registerEvents(this, plugin) }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private fun onRain(event: WeatherChangeEvent) {
        event.isCancelled = event.toWeatherState()
    }
}
