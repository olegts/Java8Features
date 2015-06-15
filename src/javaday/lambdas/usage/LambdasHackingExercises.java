package javaday.lambdas.usage;

import javaday.Java8;
import javaday.PriorJava8;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class LambdasHackingExercises {

    // Exercise 1: Print out all the words in wordList, which is
    // a static List<String> defined at the bottom of this file.

    @Test
    @PriorJava8
    public void printAllWords() {
        for (String word : wordList){
            System.out.println(word);
        }
    }

    @Test
    @Java8
    public void printAllWordsInJava8() {
        wordList.forEach(System.out::println);
    }

    // Exercise 2: Convert all words in wordList to upper case,
    // and gather the result into an output list.

    @Test
    @PriorJava8
    public void upperCaseWords() {
        List<String> output = new ArrayList<>();
        for (String word : wordList){
            output.add(word.toUpperCase());
        }

        System.out.println(output);
    }

    @Test
    @Java8
    public void upperCaseWordsInJava8() {
        List<String> output =
                wordList.stream()
                        .map(String::toUpperCase)
                        .collect(Collectors.toList());

        System.out.println(output);
    }

    // Exercise 3: Join lines 3-4 from the text file into a single string.

    @Test
    @PriorJava8
    public void joinLineRange() throws IOException {
        for (int i=0;i<2;i++) {
            reader.readLine();
        }
        StringBuilder builder = new StringBuilder();
        for (int i=0;i<2;i++) {
            builder.append(reader.readLine());
        }
        String output = builder.toString();

        System.out.println(output);
    }

    @Test
    @Java8
    public void joinLineRangeInJava8() throws IOException {

        String output = reader.lines()
                .skip(2)
                .limit(2)
                .collect(joining());

        System.out.println(output);
    }

    // Exercise 4: Find the length of the longest line in the file.

    @Test
    @PriorJava8
    public void lengthOfLongestLine() throws IOException {
        int longest = 0;
        String line = reader.readLine();
        while(line!=null){
            if (line.length()>longest){
                longest = line.length();
            }
            line = reader.readLine();
        }

        assertEquals(longest, 53);
    }

    @Test
    @Java8("Mind shift")
    public void lengthOfLongestLineInJava8() throws IOException {

        int longest = reader.lines()
                .mapToInt(String::length)
                .max()
                .orElse(0);

        assertEquals(longest, 53);
    }

    // Exercise 5: Sort unique, lower-cased words by length, then alphabetically
    // within length, and place the result into an output list.

    @Test
    @PriorJava8
    public void sortedLowerCaseDistinctByLengthThenAlphabetically() throws IOException {
        Set<String> words = new HashSet<>();
        String line = reader.readLine();
        while(line!=null){
            String[] tokens = line.split(REGEXP);
            for (String token : tokens){
                if (token.length()>0){
                    words.add(token.toLowerCase());
                }
            }
            line = reader.readLine();
        }
        String[] array = words.toArray(new String[words.size()]);
        Arrays.sort(array, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                int c = Integer.compare(o1.length(),o2.length());
                if (c == 0){
                    return o1.compareTo(o2);
                }else{
                    return c;
                }
            }
        });
        List<String> output = Arrays.asList(array);

        System.out.println(output);
    }

    @Test
    @Java8
    public void sortedLowerCaseDistinctByLengthThenAlphabeticallyInJava8() throws IOException {
        List<String> output =
                reader.lines()
                        .flatMap(line -> Stream.of(line.split(REGEXP)))
                        .filter(word -> word.length() > 0)
                        .map(String::toLowerCase)
                        .distinct()
                        .sorted(Comparator.comparingInt(String::length)
                                .thenComparing(Comparator.naturalOrder()))
                        .collect(toList());

        System.out.println(output);
    }

    // Exercise 6: Gather the words into a map, accumulating a count of the
    // number of occurrences of each word. Don't worry about upper case and
    // lower case. Extra challenge: implement two solutions, one that uses
    // groupingBy() and the other that uses toMap().

    @Test
    @PriorJava8
    public void wordFrequencies() throws IOException {
        Map<String, Long> map = new HashMap<>();
        String line = reader.readLine();
        while(line!=null){
            String[] tokens = line.split(REGEXP);
            for (String token : tokens){
                if (token.length()>0){
                    Long count = map.get(token);
                    if (count==null){
                        count = 0L;
                    }
                    map.put(token, count + 1);
                }
            }
            line = reader.readLine();
        }

        assertEquals(2L, (long) map.get("tender"));
        assertEquals(6L, (long) map.get("the"));
        assertEquals(1L, (long) map.get("churl"));
        assertEquals(2L, (long) map.get("thine"));
        assertEquals(3L, (long) map.get("world"));
        assertFalse(map.containsKey("lambda"));
    }

    @Test
    @Java8("In Java8 there are always number of ways to solve same problem")
    public void wordFrequenciesInJava8() throws IOException {
        //Combinator example
        Map<String, Long> map =
                reader.lines()
                        .flatMap(line -> Stream.of(line.split(REGEXP)))
                        .filter(word -> word.length() > 0)
                        .collect(Collectors.groupingBy(Function.identity(),
                                Collectors.counting()));

        //Collector implementation
        /*Map<String, Long> map =
                reader.lines()
                        .flatMap(line -> Stream.of(line.split(REGEXP)))
                        .filter(word -> word.length() > 0)
                        .collect(toMap(Function.identity(),
                                       w -> 1L,
                                       Long::sum));*/

        assertEquals(2L, (long) map.get("tender"));
        assertEquals(6L, (long) map.get("the"));
        assertEquals(1L, (long) map.get("churl"));
        assertEquals(2L, (long) map.get("thine"));
        assertEquals(3L, (long) map.get("world"));
        assertFalse(map.containsKey("lambda"));
    }

    // ===== TEST INFRASTRUCTURE ==================================================

    static List<String> wordList = Arrays.asList(
            "every", "problem", "in", "computer", "science",
            "can", "be", "solved", "by", "adding", "another",
            "level", "of", "indirection");
    // Butler Lampson

    static final String REGEXP = "\\W+"; // for splitting into words

    private BufferedReader reader;

    @Before
    public void setUpBufferedReader() throws IOException {
        reader = Files.newBufferedReader(
                Paths.get("SonnetI.txt"), StandardCharsets.UTF_8);
    }

    @After
    public void closeBufferedReader() throws IOException {
        reader.close();
    }
}
