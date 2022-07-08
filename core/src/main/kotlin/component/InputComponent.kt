package component

data class InputComponent(
    var fullscreen: Boolean = false,
    var exitFullscreen: Boolean = false,
    var up: Boolean = false,
    var down: Boolean = false,
    var left: Boolean = false,
    var right: Boolean = false
) {
    val isMoving: Boolean get() = up || down || left || right
}