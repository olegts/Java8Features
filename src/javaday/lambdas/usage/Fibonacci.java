package javaday.lambdas.usage;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

public class Fibonacci {

    private final Map<Integer,Long> cache;

    public Fibonacci() {
        cache = new HashMap<>();
        cache.put(0, 0L);
        cache.put(1, 1L);
    }

    public long fibonacci(int x) {
        return cache.computeIfAbsent(x, n -> fibonacci(n-1) + fibonacci(n-2));
    }

    @Test
    public void fibonacciMatchesOpeningSequence() {
        List<Long> fibonacciSequence = Arrays.asList(0L, 1L, 1L, 2L, 3L, 5L, 8L, 13L, 21L, 34L);

        IntStream.range(0, fibonacciSequence.size())
                .forEach(x -> {
                    Fibonacci fibonacci = new Fibonacci();
                    long result = fibonacci.fibonacci(x);
                    long expectedResult = fibonacciSequence.get(x);
                    assertEquals(expectedResult, result);
                });
    }

}
