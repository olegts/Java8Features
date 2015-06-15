package javaday.lambdas.oop.template_method;

import org.junit.Test;

import java.util.stream.Collector;

/**
 * Here you mainly use delegation instead of heavy usage of inheritance, which makes design more flexible
 * However you still can mix two like in {@link CompanyLoanApplication}
 * It's very similar to how {@link Collector} designed in Java 8
 */
public class NewApproach {

    @Test
    public void couldBeUsedImMoreObjectOrientedWay() throws Exception {
        new CompanyLoanApplication(new Company())
                .checkLoanApplication();
    }

    @Test
    public void couldBeUsedImMoreFunctionalWay() throws Exception {
        Company company = new Company();
        new LoanApplication(company::checkIdentity,
                            company::checkHistoricalDebt,
                            company::checkProfitAndLoss)
                .checkLoanApplication();
    }

    static class Company {

        public void checkIdentity() {

        }
        public void checkProfitAndLoss() {

        }
        public void checkHistoricalDebt() {

        }

    }

    static class CompanyLoanApplication extends LoanApplication {

        public CompanyLoanApplication(Company company) {
            super(company::checkIdentity,
                    company::checkHistoricalDebt,
                    company::checkProfitAndLoss);
        }

    }

    static class LoanApplication {

        private final Criteria identity;
        private final Criteria creditHistory;
        private final Criteria incomeHistory;

        public LoanApplication(Criteria identity,
                               Criteria creditHistory,
                               Criteria incomeHistory) {

            this.identity = identity;
            this.creditHistory = creditHistory;
            this.incomeHistory = incomeHistory;
        }

        public void checkLoanApplication() {
            identity.check();
            creditHistory.check();
            incomeHistory.check();
            reportFindings();
        }

        private void reportFindings() {
            //Do smth standard for all types of clients here
        }
    }

    @FunctionalInterface
    interface Criteria {
        void check();
    }
}
