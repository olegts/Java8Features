package javaday.lambdas.usage;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Oleg Tsal-Tsalko
 */
public class GameOfLifeJava8 {
    @Test
    public void shouldDieIfLessThenTwoNeighbours() throws Exception {
        Grid grid = new Grid();
        grid.setAlive(0,0);
        grid.nextState();
        assertThat(grid.isAlive(0, 0), is(false));
        grid.setAlive(0, 0);
        grid.setAlive(1, 1);
        grid.nextState();
        assertThat(grid.isAlive(0, 0), is(false));
        assertThat(grid.isAlive(1, 1), is(false));
    }

    @Test
    public void shouldDieIfMoreThenThreeNeighbours() throws Exception {
        Grid grid = new Grid();
        grid.setAlive(0, 0);
        grid.setAlive(1, 1);
        grid.setAlive(0, 1);
        grid.setAlive(1, 0);
        grid.setAlive(-1, -1);
        grid.nextState();
        assertThat(grid.isAlive(0, 0), is(false));
    }

    @Test
    public void shouldBecomeAliveIfExactlyThreeNeighbours() throws Exception {
        Grid grid = new Grid();
        grid.setAlive(0, 0);
        grid.setAlive(1, 1);
        grid.setAlive(0, 1);
        grid.nextState();
        assertThat(grid.isAlive(1, 0), is(true));
    }

    static class Grid{

        private Set<Point> alivePoints = new HashSet<>();

        public void setAlive(int x, int y) {
            alivePoints.add(new Point(x,y));
        }

        public boolean isAlive(int x, int y) {
            return alivePoints.contains(new Point(x,y));
        }

        public void nextState() {
            Set<Point> survivors = alivePoints.stream()
                    .filter(p -> numberOfNeighbours(p) >= 2 && numberOfNeighbours(p) < 4)
                    .collect(toSet());
            Set<Point> newborns = alivePoints.stream()
                    .flatMap(this::neighboursOf)
                    .filter(not(alivePoints::contains))
                    .filter(p -> numberOfNeighbours(p) == 3)
                    .collect(toSet());
            alivePoints = survivors;
            alivePoints.addAll(newborns);
        }

        private int numberOfNeighbours(Point p) {
            return (int)neighboursOf(p)
                    .filter(alivePoints::contains)
                    .count();
        }

        private Stream<Point> neighboursOf(Point p) {
            return Stream.of(
                    new Point(p.x + 1, p.y + 1),
                    new Point(p.x + 1, p.y),
                    new Point(p.x + 1, p.y - 1),
                    new Point(p.x, p.y + 1),
                    new Point(p.x, p.y - 1),
                    new Point(p.x - 1, p.y + 1),
                    new Point(p.x - 1, p.y),
                    new Point(p.x - 1, p.y - 1));
        }
    }

    static class Point{

        int x,y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Point)) return false;

            Point point = (Point) o;

            if (x != point.x) return false;
            return y == point.y;

        }
        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            return result;
        }
    }

    static <T> Predicate<T> not(Predicate<T> p) {return p.negate();}
}
