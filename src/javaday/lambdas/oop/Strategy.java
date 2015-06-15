package javaday.lambdas.oop;

import javaday.Java8;
import javaday.PriorJava8;
import org.junit.Test;

public class Strategy {

    @Test
    @PriorJava8
    public void shouldInstantiateEachConcreteStrategyObjectBeforeJava8() throws Exception {
        Solver solver = new Solver(new MaxWeightStrategy());
        solver.solveProblem();
    }

    @Test
    @Java8
    public void couldPassReferenceToAnyExistingMethodInJava8() throws Exception {
        Solver solver = new Solver(Strategy::returnRandomMove);
        solver.solveProblem();
    }

    static class Solver{

        private NextMoveStrategy strategy;

        public Solver(NextMoveStrategy strategy) {
            this.strategy = strategy;
        }

        public void solveProblem(){
            //Do some stuff here
            strategy.calculateNextMove("Board encoded state");
        }
    }

    @FunctionalInterface
    interface NextMoveStrategy{
        String calculateNextMove(String board);
    }

    static String returnRandomMove(String board){
        return null;
    }

    static class DummyRandomStrategy implements NextMoveStrategy{
        @Override
        public String calculateNextMove(String board) {
            return null;
        }
    }

    static class MaxWeightStrategy implements NextMoveStrategy{
        @Override
        public String calculateNextMove(String board) {
            return null;
        }
    }
}
