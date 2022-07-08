import GameBoot.Companion.sizes
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys.ESCAPE
import com.badlogic.gdx.Input.Keys.F11
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.ScreenViewport
import ktx.app.KtxScreen
import ktx.app.Platform
import ktx.assets.disposeSafely

abstract class BaseScreen : KtxScreen {

    private val actionMap = mutableMapOf<Int, Action.Name>()
    protected val batch = SpriteBatch()
    protected val camera = OrthographicCamera().apply {
        setToOrtho(false, sizes.windowWidthF(), sizes.windowHeightF())
    }
    protected val hudStage =
        if (Platform.isMobile) Stage(ScreenViewport().apply { unitsPerPixel = sizes.unitsPerPixel() })
        else Stage(FitViewport(sizes.windowWidthF(), sizes.windowHeightF()), batch)

    init {
        if (Platform.isDesktop) {
            registerAction(F11, Action.Name.FULLSCREEN)
            registerAction(ESCAPE, Action.Name.EXIT_FULLSCREEN)
        }
    }

    fun registerAction(inputKey: Int, actionName: Action.Name) {
        actionMap[inputKey] = actionName
    }

    fun getActionMap(): MutableMap<Int, Action.Name> = actionMap

    open fun doAction(action: Action) {
        if (action.type == Action.Type.START) {
            when (action.name) {
                Action.Name.FULLSCREEN -> Gdx.graphics.setFullscreenMode(Gdx.graphics.displayMode)
                Action.Name.EXIT_FULLSCREEN -> Gdx.graphics.setWindowedMode(sizes.windowWidth, sizes.windowHeight)
                else -> {}
            }
        }
    }

    override fun show() {
        (Gdx.input.inputProcessor as InputMultiplexer).apply {
            addProcessor(hudStage)
        }
    }

    override fun hide() {
        (Gdx.input.inputProcessor as InputMultiplexer).apply {
            removeProcessor(hudStage)
        }
    }

    override fun resize(width: Int, height: Int) {
        hudStage.viewport.update(width, height, true)
    }

    override fun dispose() {
        hudStage.disposeSafely()
        batch.disposeSafely()
    }
}
