package co.kaioru.retort.reflection.reflection;

import co.kaioru.retort.ICommandContext;
import co.kaioru.retort.reflection.AbstractReflectionProvider;

public class IntegerReflectionProvider extends AbstractReflectionProvider<ICommandContext, Integer> {

    public IntegerReflectionProvider() {
        super(Integer.class);
    }

    @Override
    public Integer provide(ICommandContext context) {
        return Integer.valueOf(context.getArgs().remove());
    }

}
