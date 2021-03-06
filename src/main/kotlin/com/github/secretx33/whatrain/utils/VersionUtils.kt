package com.github.secretx33.whatrain.utils

import com.google.common.base.Objects
import org.bukkit.Bukkit
import java.util.regex.Pattern

// "object" from kotlin is literally a (function in a similar way to) singleton/static class, you can use its properties everywhere by calling "VersionUtils.<field/method>" or you just import the object and call it directly e.g. "serverVersion < v1_12_2_R01"
object VersionUtils {
    // Version Util (from EssentialsX)
    val v1_8_8_R01  = BukkitVersion.fromString("1.8.8-R0.1-SNAPSHOT")
    val v1_9_R01    = BukkitVersion.fromString("1.9-R0.1-SNAPSHOT")
    val v1_9_4_R01  = BukkitVersion.fromString("1.9.4-R0.1-SNAPSHOT")
    val v1_10_R01   = BukkitVersion.fromString("1.10-R0.1-SNAPSHOT")
    val v1_10_2_R01 = BukkitVersion.fromString("1.10.2-R0.1-SNAPSHOT")
    val v1_11_R01   = BukkitVersion.fromString("1.11-R0.1-SNAPSHOT")
    val v1_11_2_R01 = BukkitVersion.fromString("1.11.2-R0.1-SNAPSHOT")
    val v1_12_2_R01 = BukkitVersion.fromString("1.12.2-R0.1-SNAPSHOT")
    val v1_13_0_R01 = BukkitVersion.fromString("1.13.0-R0.1-SNAPSHOT")
    val v1_13_2_R01 = BukkitVersion.fromString("1.13.2-R0.1-SNAPSHOT")
    val v1_14_R01   = BukkitVersion.fromString("1.14-R0.1-SNAPSHOT")
    val v1_14_4_R01 = BukkitVersion.fromString("1.14.4-R0.1-SNAPSHOT")
    val v1_15_R01   = BukkitVersion.fromString("1.15-R0.1-SNAPSHOT")
    val v1_15_2_R01 = BukkitVersion.fromString("1.15.2-R0.1-SNAPSHOT")
    val v1_16_1_R01 = BukkitVersion.fromString("1.16.1-R0.1-SNAPSHOT")
    val v1_16_5_R01 = BukkitVersion.fromString("1.16.5-R0.1-SNAPSHOT")

    val nmsVersion: String = run {
        val name = Bukkit.getServer().javaClass.name
        val parts = name.split("\\.").toTypedArray()
        if (parts.size > 3) {
            parts[3]
        } else ""
    }
    val serverVersion = BukkitVersion.fromString(Bukkit.getServer().bukkitVersion)

    class BukkitVersion private constructor(
        val major: Int,
        val minor: Int,
        val patch: Int,
        val revision: Double,
        val prerelease: Int
    ) : Comparable<BukkitVersion> {
        fun isHigherThan(o: BukkitVersion): Boolean = compareTo(o) > 0

        fun isHigherThanOrEqualTo(o: BukkitVersion): Boolean = compareTo(o) >= 0

        fun isLowerThan(o: BukkitVersion): Boolean = compareTo(o) < 0

        fun isLowerThanOrEqualTo(o: BukkitVersion): Boolean = compareTo(o) <= 0

        override fun equals(other: Any?): Boolean {
            if (this === other) {
                return true
            }
            if (other == null || javaClass != other.javaClass) {
                return false
            }
            val that = other as BukkitVersion
            return major == that.major && minor == that.minor && patch == that.patch && revision == that.revision && prerelease == that.prerelease
        }

        override fun hashCode(): Int {
            return Objects.hashCode(major, minor, patch, revision, prerelease)
        }

        override fun toString(): String {
            val sb = StringBuilder("$major.$minor")
            if (patch != 0) {
                sb.append(".").append(patch)
            }
            if (prerelease != -1) {
                sb.append("-pre").append(prerelease)
            }
            return sb.append("-R").append(revision).toString()
        }

        override fun compareTo(other: BukkitVersion): Int {
            return if (major < other.major) {
                -1
            } else if (major > other.major) {
                1
            } else { // equal major
                if (minor < other.minor) {
                    -1
                } else if (minor > other.minor) {
                    1
                } else { // equal minor
                    if (patch < other.patch) {
                        -1
                    } else if (patch > other.patch) {
                        1
                    } else { // equal patch
                        if (prerelease < other.prerelease) {
                            -1
                        } else if (prerelease > other.prerelease) {
                            1
                        } else { // equal prerelease
                            revision.compareTo(other.revision)
                        }
                    }
                }
            }
        }

        companion object {
            private val VERSION_PATTERN = Pattern.compile("^(\\d+)\\.(\\d+)\\.?([0-9]*)?(?:-pre(\\d))?(?:-?R?([\\d.]+))?(?:-SNAPSHOT)?")

            fun fromString(string: String): BukkitVersion {
                var matcher = VERSION_PATTERN.matcher(string)
                if (!matcher.matches()) {
                    matcher = VERSION_PATTERN.matcher(v1_14_R01.toString())
                    require(matcher.matches()) { "$string is not in valid version format. e.g. 1.8.8-R0.1" }
                }
                return from(
                    matcher.group(1),
                    matcher.group(2),
                    matcher.group(3),
                    if (matcher.groupCount() < 5) "" else matcher.group(5),
                    matcher.group(4)
                )
            }

            private fun from(major: String, minor: String, patch: String?, revision: String?, prerelease: String?): BukkitVersion {
                var patch = patch
                var revision = revision
                var prerelease = prerelease
                if(patch == null || patch.isEmpty()) patch = "0"
                if(revision == null || revision.isEmpty()) revision = "0"
                if(prerelease == null || prerelease.isEmpty()) prerelease = "-1"
                return BukkitVersion(
                    major.toInt(),
                    minor.toInt(),
                    patch.toInt(),
                    revision.toDouble(),
                    prerelease.toInt()
                )
            }
        }
    }
}
