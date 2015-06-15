package javaday.lambdas.niceties;

import java.util.function.Supplier;

/**
 * Example shows how lambdas influence APIs
 */
public class Logger {

    private boolean debug = true;

    public boolean isDebugEnabled() {
        return debug;
    }

    /*
     * Old style API...
     */
    public void debug(String message) {
        if (isDebugEnabled()) {
            System.out.println(message);
        }
    }

    /**
     * Ugly live hack we all hate!
     */
    public void example() {
        Logger logger = new Logger();
        if (logger.isDebugEnabled()) {
            logger.debug("Look at this: " + expensiveOperation());
        }
    }

    private String expensiveOperation() {
        return "";
    }

    /**
     * We can do better:
     */
    public void debug(Supplier<String> message) {
        if (isDebugEnabled()) {
            debug(message.get());
        }
    }

    /**
     * It makes our life better))
     */
    public void exampleWithLambda() {
        Logger logger = new Logger();
        logger.debug(() -> "Look at this: " + expensiveOperation());
    }
}
