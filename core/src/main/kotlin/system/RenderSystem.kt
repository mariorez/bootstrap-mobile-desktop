package system

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.github.quillraven.fleks.AllOf
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.collection.compareEntity
import component.RenderComponent
import component.TransformComponent
import ktx.graphics.use

@AllOf([TransformComponent::class, RenderComponent::class])
class RenderSystem(
    private val batch: SpriteBatch,
    private val camera: OrthographicCamera,
    private val transformMapper: ComponentMapper<TransformComponent>,
    private val renderMapper: ComponentMapper<RenderComponent>,
) : IteratingSystem(
    compareEntity { entA, entB -> transformMapper[entA].zIndex.compareTo(transformMapper[entB].zIndex) }
) {

    override fun onTick() {
        batch.use(camera) {
            super.onTick()
        }
    }

    override fun onTickEntity(entity: Entity) {
        renderMapper[entity].apply {
            rendered = true
            sprite.apply {
                transformMapper[entity].also {
                    rotation = it.rotation
                    setBounds(it.position.x, it.position.y, width, height)
                }
                draw(batch)
            }
        }
    }
}
