import GameBoot.Companion.assets
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable

fun generateButton(texture: Texture): Button {
    return Button(Button.ButtonStyle().apply {
        up = TextureRegionDrawable(texture)
    })
}

fun generateTextButton(
    text: String,
    textColor: Color = Color.GRAY,
    button: Texture = assets["button.png"],
    padding: Int = 24
): TextButton {
    return TextButton(text, TextButton.TextButtonStyle().apply {
        up = NinePatchDrawable(NinePatch(button, padding, padding, padding, padding))
        font = assets.get<BitmapFont>("open-sans.ttf")
        fontColor = textColor
    })
}
