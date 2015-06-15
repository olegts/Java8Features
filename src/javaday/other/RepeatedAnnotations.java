package javaday.other;

import java.lang.annotation.*;
import java.util.Arrays;

public class RepeatedAnnotations {

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Filters {
        Filter[] value();
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @Repeatable(Filters.class)
    public @interface Filter {
        String value();
    }

    @Filter("filter1")
    @Filter("filter2")
    public interface Filterable {
    }

    public static void main(String[] args) {
        Arrays.stream(Filterable.class.getAnnotationsByType(Filter.class)).forEach(System.out::println);
    }

}
