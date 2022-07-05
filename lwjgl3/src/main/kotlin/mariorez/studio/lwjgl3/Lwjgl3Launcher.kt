@file:JvmName("Lwjgl3Launcher")

package mariorez.studio.lwjgl3

import Main
import Main.Companion.sizes
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration

/** Launches the desktop (LWJGL3) application. */
fun main() {
    Lwjgl3Application(Main(), Lwjgl3ApplicationConfiguration().apply {
        setTitle("GameDev Bootstrap")
        setWindowedMode(sizes.windowWidth, sizes.windowHeight)
        setWindowIcon(*(arrayOf(128, 64, 32, 16).map { "libgdx$it.png" }.toTypedArray()))
    })
}
