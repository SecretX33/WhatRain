package com.github.secretx33.whatrain.utils

import com.github.secretx33.whatrain.utils.VersionUtils.serverVersion
import com.github.secretx33.whatrain.utils.VersionUtils.v1_12_2_R01
import org.bukkit.GameRule
import org.bukkit.World

@Suppress("DEPRECATION")
fun World.gameRule(property: String, value: Boolean){
    if(serverVersion <= v1_12_2_R01){
        setGameRuleValue(property, value.toString())
    } else {
        when(property) {
            "doDaylightCycle" -> setGameRule(GameRule.DO_DAYLIGHT_CYCLE, value)
            "doWeatherCycle" -> setGameRule(GameRule.DO_WEATHER_CYCLE, value)
            "keepInventory" -> setGameRule(GameRule.KEEP_INVENTORY, value)
            else -> throw IllegalArgumentException("Unknown property: $property")
        }
    }
}
