package javaday.lambdas;

import javaday.Java8;
import javaday.PriorJava8;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static javaday.lambdas.ParalelismExample.Shape.Color.BLUE;
import static javaday.lambdas.ParalelismExample.Shape.Color.RED;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Oleg Tsal-Tsalko
 */
public class ParalelismExample {

    /**
     * Paralelism is now part of the platform bringing power of multithreading for free
     */
    @Java8
    public static int calcSumOfWeights(List<Shape> shapes) {
        return shapes.parallelStream()
                .filter(s -> s.getColor() == BLUE)
                .mapToInt(Shape::getWeight)
                .sum();
    }

    /**
     * You were lucky too cause you had a chance to get an understanding of FJPool
     */
    @PriorJava8
    public static int calcSumOfWeightsBeforeJava8(List<Shape> shapes) {
        ForkJoinPool fjp = ForkJoinPool.commonPool();
        SumOfWeightsSolver task = new SumOfWeightsSolver(shapes);
        fjp.invoke(task);
        return task.sum;
    }

    static class SumOfWeightsSolver extends RecursiveAction{

        private static final int THRESHOLD = 100;

        private List<Shape> shapes;
        private int sum;

        public SumOfWeightsSolver(List<Shape> shapes) {
            this.shapes = shapes;
        }

        public int calcSumOfWeights(){
            int sum = 0;
            for (Shape shape : shapes){
                if (shape.color==BLUE){
                    sum += shape.getWeight();
                }
            }
            return sum;
        }

        @Override
        protected void compute() {
            if (shapes.size()<THRESHOLD){
                sum = calcSumOfWeights();
            }else{
                SumOfWeightsSolver left = new SumOfWeightsSolver(shapes.subList(0,shapes.size()/2));
                SumOfWeightsSolver right = new SumOfWeightsSolver(shapes.subList(shapes.size()/2,shapes.size()));
                invokeAll(left, right);
                sum = left.sum + right.sum;
            }
        }
    }

    static class Shape{

        enum Color{BLUE,RED}

        private int weight;
        private Color color;

        public Shape(int weight, Color color) {
            this.weight = weight;
            this.color = color;
        }

        public int getWeight() {
            return weight;
        }

        public Color getColor() {
            return color;
        }
    }

    @Test
    public void testCalculatingSumOfWeightsUsingParallelStreams() throws Exception {
        List<Shape> shapes = generateListOfShapes();
        assertThat(calcSumOfWeights(shapes), is(20));
    }

    @Test
    public void testCalculatingSumOfWeightsUsingForkJoinPool() throws Exception {
        List<Shape> shapes = generateListOfShapes();
        assertThat(calcSumOfWeightsBeforeJava8(shapes), is(20));
    }

    private List<Shape> generateListOfShapes() {
        return IntStream.range(0,10).mapToObj(i -> new Shape(i, ((i & 1) == 0) ? BLUE : RED)).collect(toList());
    }
}
