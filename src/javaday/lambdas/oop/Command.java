package javaday.lambdas.oop;

import javaday.Java8;
import javaday.PriorJava8;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class Command {

    @Test
    @PriorJava8
    public void shouldCreateSeparateInstanceForEachActionPriorJava8() throws Exception {
        Processor processor = new Processor();
        Dao dao = new Dao();
        processor.addAction(new Create(dao));
        processor.addAction(new Update(dao));
        processor.addAction(new Delete(dao));



        processor.processChainOfActions();
    }

    @Test
    @Java8
    public void shouldCreateSeparateInstanceForEachActionInJava8() throws Exception {
        Processor processor = new Processor();
        Dao dao = new Dao();

        processor.addAction(new Action() {
            @Override
            public void perform() {
                dao.create();
            }
        });

        processor.addAction(dao::create);


        processor.addAction(dao::update);
        processor.addAction(dao::delete);
        processor.processChainOfActions();
    }

    static class Processor{

        private List<Action> actions = new ArrayList<>();

        public void addAction(Action action){
            actions.add(action);
        }

        public void processChainOfActions(){
            actions.forEach(Action::perform);
        }
    }

    @FunctionalInterface
    interface Action{

        void perform();

    }

    static class Create implements Action{

        private Dao dao;

        public Create(Dao dao) {
            this.dao = dao;
        }

        @Override
        public void perform() {
            dao.create();
        }
    }

    static class Update implements Action{

        private Dao dao;

        public Update(Dao dao) {
            this.dao = dao;
        }

        @Override
        public void perform() {
            dao.update();
        }
    }

    static class Delete implements Action{

        private Dao dao;

        public Delete(Dao dao) {
            this.dao = dao;
        }

        @Override
        public void perform() {
            dao.delete();
        }
    }

    static class Dao{

        public void create(){
            System.out.println("Created data set");
        }

        public void update(){
            System.out.println("Updated data set");
        }

        public void delete(){
            System.out.println("Deleted data set");
        }
    }
}
