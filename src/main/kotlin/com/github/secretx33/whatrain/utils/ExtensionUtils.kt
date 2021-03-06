package com.github.secretx33.whatrain.utils

import com.github.secretx33.whatrain.utils.VersionUtils.serverVersion
import com.github.secretx33.whatrain.utils.VersionUtils.v1_12_2_R01
import org.bukkit.GameRule
import org.bukkit.World

fun World.gameRule(property: String, value: Boolean){
    if(serverVersion.isLowerThanOrEqualTo(v1_12_2_R01)){
        setGameRuleValue(property, value.toString())
    } else {
        when(property) {
            "doDaylightCycle" -> setGameRule(GameRule.DO_DAYLIGHT_CYCLE, value)
            "doWeatherCycleCycle" -> setGameRule(GameRule.DO_WEATHER_CYCLE, value)
        }
    }
}
