package javaday.lambdas.niceties;

import javaday.lambdas.domain.Artist;
import org.junit.Test;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static javaday.lambdas.domain.SampleData.georgeHarrison;
import static javaday.lambdas.domain.SampleData.johnLennon;
import static javaday.lambdas.domain.SampleData.paulMcCartney;
import static org.junit.Assert.assertEquals;

public class CollectionsOutputFormat {

    /**
     * Imagine you need to print artists...
     * 
     * This is how you did it before Java 8:
     */
    public static String printArtists(List<Artist> artists) {
        StringBuilder builder = new StringBuilder("[");
        for (Artist  artist : artists) {
            if (builder.length() > 1)
                builder.append(", ");

            String name = artist.getName();
            builder.append(name);
        }
        builder.append("]");
        String result = builder.toString();
        return result;
    }

    /**
     * Lets evalutionize to proper Java 8 version through number of refactor steps...
     */
    public static String printArtistsRefactor1(List<Artist> artists) {
        StringBuilder builder = new StringBuilder("[");
        artists.stream()
                .map(Artist::getName)
                .forEach(name -> {
                    if (builder.length() > 1)
                        builder.append(", ");

                    builder.append(name);
                });
        builder.append("]");
        String result = builder.toString();
        return result;
    }

    public static String printArtistsRefactor2(List<Artist> artists) {
        StringBuilder reduced =
                artists.stream()
                        .map(Artist::getName)
                        .reduce(new StringBuilder(), (builder, name) -> {
                            if (builder.length() > 0)
                                builder.append(", ");

                            builder.append(name);
                            return builder;
                        }, (left, right) -> left.append(right));

        reduced.insert(0, "[");
        reduced.append("]");
        String result = reduced.toString();
        return result;
    }

    public static String printArtistsRefactor3(List<Artist> artists) {
        StringCombiner combined =
                artists.stream()
                        .map(Artist::getName)
                        .reduce(new StringCombiner(", ", "[", "]"),
                                StringCombiner::add,
                                StringCombiner::merge);

        String result = combined.toString();
        return result;
    }

    public static String printArtistsRefactor4(List<Artist> artists) {
        String result =
                artists.stream()
                        .map(Artist::getName)
                        .reduce(new StringCombiner(", ", "[", "]"),
                                StringCombiner::add,
                                StringCombiner::merge)
                        .toString();
        return result;
    }

    public static String printArtistsRefactor5(List<Artist> artists) {
        String result =
                artists.stream()
                        .map(Artist::getName)
                        .collect(new StringCollector(", ", "[", "]"));
        return result;
    }

    /**
     * Hopefully we shouldn't implement all of this, cause someone already did it for us!!!
     */
    public static String printArtistsInJava8(List<Artist> artists) {
        String result =
                artists.stream()
                        .map(Artist::getName)
                        .collect(Collectors.joining(", ", "[", "]"));
        return result;
    }
    
    static class StringCollector implements Collector<String, StringCombiner, String> {

        private static final Set<Characteristics> characteristics = Collections.emptySet();

        private final String delim;
        private final String prefix;
        private final String suffix;

        public StringCollector(String delim, String prefix, String suffix) {
            this.delim = delim;
            this.prefix = prefix;
            this.suffix = suffix;
        }

        @Override
        public Supplier<StringCombiner> supplier() {
            return () -> new StringCombiner(delim, prefix, suffix);
        }

        @Override
        public BiConsumer<StringCombiner, String> accumulator() {
            return StringCombiner::add;
        }

        @Override
        public BinaryOperator<StringCombiner> combiner() {
            return StringCombiner::merge;
        }

        @Override
        public Function<StringCombiner, String> finisher() {
            return StringCombiner::toString;
        }

        @Override
        public Set<Characteristics> characteristics() {
            return characteristics;
        }

    }

    static class StringCombiner {

        private final String delim;
        private final String prefix;
        private final String suffix;
        private final StringBuilder builder;

        public StringCombiner(String delim, String prefix, String suffix) {
            this.delim = delim;
            this.prefix = prefix;
            this.suffix = suffix;
            builder = new StringBuilder();
        }

        public StringCombiner add(String element) {
            if (areAtStart()) {
                builder.append(prefix);
            } else {
                builder.append(delim);
            }
            builder.append(element);
            return this;
        }

        private boolean areAtStart() {
            return builder.length() == 0;
        }

        public StringCombiner merge(StringCombiner other) {
            if (other.builder.length() > 0) {
                if (areAtStart()) {
                    builder.append(prefix);
                } else {
                    builder.append(delim);
                }
                builder.append(other.builder, prefix.length(), other.builder.length());
            }
            return this;
        }

        @Override
        public String toString() {
            if (areAtStart()) {
                builder.append(prefix);
            }
            builder.append(suffix);
            return builder.toString();
        }

    }

    @Test
    public void beatlesExample() {
        StringJoiner joiner = new StringJoiner(", ", "[", "]");
        joiner.add("John");
        joiner.add("Paul");
        joiner.add("Ringo");
        assertEquals("[John, Paul, Ringo]", joiner.toString());
    }

    @Test
    public void allStringJoins() {
        List<Function<List<Artist>, String>> formatters = Arrays.<Function<List<Artist>, String>>asList(
                CollectionsOutputFormat::printArtists,
                CollectionsOutputFormat::printArtistsInJava8,
                CollectionsOutputFormat::printArtistsRefactor1,
                CollectionsOutputFormat::printArtistsRefactor2,
                CollectionsOutputFormat::printArtistsRefactor3,
                CollectionsOutputFormat::printArtistsRefactor4,
                CollectionsOutputFormat::printArtistsRefactor5
        );

        formatters.forEach(formatter -> {
            System.out.println("Testing: " + formatter.toString());
            String result = formatter.apply(Arrays.asList(johnLennon, paulMcCartney, georgeHarrison));
            assertEquals("[John Lennon, Paul McCartney, George Harrison]", result);

            result = formatter.apply(Collections.emptyList());
            assertEquals("[]", result);
        });
    }

}
