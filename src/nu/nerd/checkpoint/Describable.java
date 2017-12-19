package nu.nerd.checkpoint;

public abstract class Describable {
    public final String getName() {
        return getMeta().name();
    }

    public final String getDescription() {
        return getMeta().description();
    }

    public final String getUsage() {
        return getMeta().usage();
    }

    private DescribableMeta getMeta() {
        return getMeta(this.getClass());
    }

    public static DescribableMeta getMeta(Class<? extends Describable> describableType) {
        return describableType.getAnnotation(DescribableMeta.class);
    }
}
