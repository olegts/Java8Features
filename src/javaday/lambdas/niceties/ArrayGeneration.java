package javaday.lambdas.niceties;

import java.util.Arrays;
import java.util.stream.IntStream;

public class ArrayGeneration {

    /**
     * This is how you did it all the time before...
     */
    public static int[] imperativeInitilize(int size) {
        int[] values = new int[size];
        for(int i = 0; i < values.length;i++) {
            values[i] = i;
        }
        return values;
    }

    public static String[] sequentialInitialize(int size){
        return (String[])IntStream.range(0,size)
                .mapToObj(String::valueOf)
                .toArray();
    }

    /**
     * This is how you can do it in Java 8 even parallel
     */
    public static int[] parallelInitialize(int size) {
        int[] values = new int[size];
        Arrays.parallelSetAll(values, i -> i);
        return values;
    }
}
