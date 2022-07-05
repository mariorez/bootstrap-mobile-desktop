package screen

import Action
import Action.Type.START
import BaseScreen
import Main
import Main.Companion.assets
import Main.Companion.sizes
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.world
import component.InputComponent
import component.PlayerComponent
import component.RenderComponent
import component.TransformComponent
import generateButton
import ktx.actors.onTouchDown
import ktx.app.Platform
import system.InputSystem
import system.MovementSystem
import system.RenderSystem

class GameScreen(
    private val main: Main
) : BaseScreen() {
    private lateinit var touchpad: Touchpad
    private val player: Entity
    private val game = world {
        injectables {
            add(batch)
            add(camera)
        }
        systems {
            add<InputSystem>()
            add<MovementSystem>()
            add<RenderSystem>()
        }
    }

    init {
        buildControls()

        game.apply {
            player = entity {
                val logoTexture = assets.get<Texture>("logo.png")
                add<PlayerComponent>()
                add<InputComponent>()
                add<TransformComponent> {
                    position.x = sizes.worldWidthF() / 2 - logoTexture.width / 2
                    position.y = sizes.worldHeightF() / 2 - logoTexture.height / 2
                    acceleration = 400f
                    deceleration = 250f
                    maxSpeed = 150f
                }
                add<RenderComponent> {
                    sprite = Sprite(logoTexture)
                }
            }

            if (Platform.isMobile) {
                system<InputSystem>().also { it.touchpad = touchpad }
            }
        }
    }

    override fun render(delta: Float) {
        game.update(delta)
        hudStage.draw()
    }

    override fun dispose() {
        super.dispose()
        game.dispose()
    }

    private fun buildControls() {

        val restartButton = generateButton(assets["back-button.png"]).apply {
            onTouchDown { main.setScreen<MenuScreen>() }
        }

        val table = Table().apply {
            setFillParent(true)
            pad(10f)
            add(restartButton).expandX().expandY().right().top()
        }


        if (Platform.isMobile) {
            touchpad = Touchpad(5f, Touchpad.TouchpadStyle().apply {
                background = TextureRegionDrawable(TextureRegion(assets.get<Texture>("touchpad-bg.png")))
                knob = TextureRegionDrawable(TextureRegion(assets.get<Texture>("touchpad-knob.png")))
            })

            table.apply {
                row()
                add(touchpad).expandY().expandX().left().bottom()
            }
        } else {
            registerAction(Input.Keys.UP, Action.Name.UP)
            registerAction(Input.Keys.DOWN, Action.Name.DOWN)
            registerAction(Input.Keys.LEFT, Action.Name.LEFT)
            registerAction(Input.Keys.RIGHT, Action.Name.RIGHT)
        }

        hudStage.addActor(table)
    }

    override fun doAction(action: Action) {
        game.mapper<InputComponent>().getOrNull(player)?.let {
            val isStarting = action.type == START
            when (action.name) {
                Action.Name.UP -> it.up = isStarting
                Action.Name.DOWN -> it.down = isStarting
                Action.Name.LEFT -> it.left = isStarting
                Action.Name.RIGHT -> it.right = isStarting
            }
        }
    }
}
