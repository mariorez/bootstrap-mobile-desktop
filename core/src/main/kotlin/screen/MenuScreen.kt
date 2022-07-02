package screen

import Action
import BaseScreen
import Main
import Main.Companion.assets
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Table
import generateTextButton
import ktx.actors.onTouchDown

class MenuScreen(
    private val main: Main
) : BaseScreen() {

    init {
        hudStage.apply {
            addActor(Table().apply {
                setFillParent(true)
                add(Image(assets.get<Texture>("logo.png"))).colspan(2).padBottom(50f)
                row()
                add(generateTextButton("Start").apply {
                    onTouchDown { main.setScreen<GameScreen>() }
                })
                add(generateTextButton("Quit").apply {
                    onTouchDown { Gdx.app.exit() }
                })
            })
        }
    }

    override fun render(delta: Float) {
        hudStage.draw()
    }

    override fun doAction(action: Action) {}
}
