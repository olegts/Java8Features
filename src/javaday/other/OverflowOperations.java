package javaday.other;

import org.junit.Test;

import java.util.stream.IntStream;

/**
 * @author Oleg Tsal-Tsalko
 */
public class OverflowOperations {

    /**
     * While solving algorithmic problems silent overflow is your greatest enemy...
     * With Java8 you can protect yourself by failing fast!
     */
    @Test
    public void showsHowYouCanSpotOverflowBugEasily() throws Exception {
        int sum = IntStream.range(0,10000000).reduce(0, (a,b)->a+b);
        System.out.println(sum);

        IntStream.range(0,10000000).reduce(0, Math::addExact);
    }
}
