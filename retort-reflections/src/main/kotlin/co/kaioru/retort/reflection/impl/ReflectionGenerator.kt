package co.kaioru.retort.reflection.impl

import co.kaioru.retort.ICommand
import co.kaioru.retort.ICommandExecutable
import co.kaioru.retort.annotation.Command
import co.kaioru.retort.annotation.exceptions.AnnotationAdapterException
import co.kaioru.retort.impl.CommandContext
import co.kaioru.retort.inline.command
import co.kaioru.retort.reflection.IReflectionGenerator
import co.kaioru.retort.reflection.IReflectionProvider
import co.kaioru.retort.reflection.Optional
import co.kaioru.retort.reflection.Reflect
import co.kaioru.retort.reflection.exceptions.ReflectionProviderException
import java.lang.reflect.Method

open class ReflectionGenerator<I : CommandContext, O : Any> : IReflectionGenerator<I, O> {
    override val providers: MutableMap<Class<*>, IReflectionProvider<I, *>> = HashMap()

    @Suppress("UNCHECKED_CAST")
    override fun generate(any: Any, method: Method): ICommand<I, O> {
        if (method.isAnnotationPresent(Command::class.java) && method.isAnnotationPresent(Reflect::class.java)) {
            val name: String = method.getAnnotation(Command::class.java).value

            return command(name) {
                build(object : ICommandExecutable<I, O> {
                    override fun execute(input: I): O {
                        val params: MutableCollection<Any> = ArrayList()

                        method.parameters.forEach {
                            val provider: IReflectionProvider<I, *>? = providers[it.type]

                            if (provider != null) params.add(provider.provide(input))
                            else {
                                if (!it.isAnnotationPresent(Optional::class.java))
                                    throw ReflectionProviderException()
                                else params.add(null!!)
                            }
                        }

                        method.isAccessible = true
                        return method.invoke(any, *params.toTypedArray()) as O
                    }
                })
            }
        }
        throw AnnotationAdapterException()
    }
}