package javaday.lambdas;

import javaday.lambdas.domain.Album;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This example shows 2 main approaches to test/debug your lambdas code:
 * - Extract business logic into method and test
 * - Use peek() method to intercept methods chain
 */
public class TestingLambdas {

    public static List<String> elementFirstToUpperCaseLambdas(List<String> words) {
        return words.stream()
                .map(value -> {
                    char firstChar = Character.toUpperCase(value.charAt(0));
                    return firstChar + value.substring(1);
                })
                .collect(Collectors.<String>toList());
    }

    /**
     * Separate your business logic from stream functional composition which is proven to work as designed
     */
    public static List<String> elementFirstToUppercase(List<String> words) {
        return words.stream()
                .map(TestingLambdas::firstToUppercase)
                .collect(Collectors.<String>toList());
    }

    /**
     * This method is actual business logic you want to test
     */
    public static String firstToUppercase(String value) {
        char firstChar = Character.toUpperCase(value.charAt(0));
        return firstChar + value.substring(1);
    }

    /**
     * You can use peek() method whenever you want to sneak in between method invocations in your stream pipeline
     */
    public static Set<String> nationalityReportUsingPeek(Album album) {
        Set<String> nationalities = album.getMusicians()
                .filter(artist -> artist.getName().startsWith("The"))
                .map(artist -> artist.getNationality())
                .peek(nation -> System.out.println("Found nationality: " + nation))
                .collect(Collectors.<String>toSet());
        return nationalities;
    }

}
