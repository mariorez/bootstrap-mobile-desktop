import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.Texture.TextureFilter.Linear
import ktx.app.KtxGame
import ktx.app.KtxInputAdapter
import ktx.app.KtxScreen
import ktx.app.Platform
import ktx.assets.async.AssetStorage
import ktx.async.KtxAsync
import screen.GameScreen
import screen.MenuScreen

class GameBoot : KtxGame<KtxScreen>() {

    companion object {
        val assets = AssetStorage()
        val gameSizes = GameSizes(
            windowWidth = 960,
            windowHeight = 540
        )
    }

    override fun create() {
        Gdx.app.logLevel = Application.LOG_DEBUG // TODO: how to disable log in prod

        Gdx.input.inputProcessor = if (Platform.isMobile) InputMultiplexer()
        else InputMultiplexer(object : KtxInputAdapter {
            override fun keyDown(keycode: Int): Boolean {
                (currentScreen as BaseScreen).apply {
                    getActionMap()[keycode]?.let { doAction(Action(it, Action.Type.START)) }
                }
                return super.keyDown(keycode)
            }

            override fun keyUp(keycode: Int): Boolean {
                (currentScreen as BaseScreen).apply {
                    getActionMap()[keycode]?.let { doAction(Action(it, Action.Type.END)) }
                }
                return super.keyUp(keycode)
            }
        })

        KtxAsync.initiate()

        assets.apply {
            loadSync<Texture>("logo.png").setFilter(Linear, Linear)
            loadSync<Texture>("button.png").setFilter(Linear, Linear)
            loadSync<Texture>("back-button.png").setFilter(Linear, Linear)
            if (Platform.isMobile) {
                loadSync<Texture>("touchpad-bg.png").setFilter(Linear, Linear)
                loadSync<Texture>("touchpad-knob.png").setFilter(Linear, Linear)
            }
        }

        addScreen(MenuScreen(this))
        addScreen(GameScreen(this))
        setScreen<MenuScreen>()
    }
}
