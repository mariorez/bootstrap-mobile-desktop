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
    private val transformMap: ComponentMapper<TransformComponent>,
    private val renderMap: ComponentMapper<RenderComponent>,
) : IteratingSystem(
    compareEntity { entA, entB -> transformMap[entA].zIndex.compareTo(transformMap[entB].zIndex) }
) {

    override fun onTick() {
        batch.use(camera) {
            super.onTick()
        }
    }

    override fun onTickEntity(entity: Entity) {
        renderMap[entity].apply {
            rendered = true
            sprite.apply {
                transformMap[entity].also {
                    rotation = it.rotation
                    setBounds(it.position.x, it.position.y, width, height)
                }
                draw(batch)
            }
        }
    }
}
