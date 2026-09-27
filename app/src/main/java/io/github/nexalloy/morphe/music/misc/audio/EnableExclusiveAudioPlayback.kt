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
            val method = param.method
            XposedBridge.log(
                "NexAlloy: ExclusiveAudio matched " +
                    "${method.declaringClass.name}.${method.name}" +
                    " return=${method.returnType}" +
                    " params=${method.parameterTypes.joinToString(",")}" +
                    " original=${original}"
            )

            if (original == false) {
                val trace = Throwable().stackTrace
                    .take(8)
                    .joinToString(" <- ") { "${it.className}.${it.methodName}:${it.lineNumber}" }
                XposedBridge.log(
                    "NexAlloy: ExclusiveAudio false-call trace: " + trace
                )
                param.result = true
            }
        }
    }
}