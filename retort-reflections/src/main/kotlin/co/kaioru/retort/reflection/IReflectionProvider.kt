package co.kaioru.retort.reflection

interface IReflectionProvider<I, O : Any> {
    val type: Class<O>

    fun provide(input: I): O
}