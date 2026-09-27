package io.github.nexalloy.morphe.music.audio.exclusiveaudio

import de.robv.android.xposed.XposedBridge
import io.github.nexalloy.morphe.music.misc.playservice.is_9_32_or_greater
import io.github.nexalloy.morphe.music.misc.playservice.versionCheckPatch
import io.github.nexalloy.patch

val EnableExclusiveAudioPlayback = patch(
    name = "Enable exclusive audio playback",
    description = "Enables the option to play audio without video.",
) {
    dependsOn(
        versionCheckPatch
    )
    val fingerprint = if (is_9_32_or_greater) {
        AllowExclusiveAudioPlaybackFingerprint
    } else {
        AllowExclusiveAudioPlaybackLegacyFingerprint
    }

    fingerprint.hookMethod {
        after { param ->
            val original = param.result as? Boolean
            XposedBridge.log(
                "NexAlloy: EnableExclusiveAudioPlayback matched " +
                    "${param.method.declaringClass.name}.${param.method.name}, " +
                    "original=${original}"
            )
            if (original == false) {
                param.result = true
                XposedBridge.log(
                    "NexAlloy: EnableExclusiveAudioPlayback changed false -> true"
                )
            }
        }
    }
}