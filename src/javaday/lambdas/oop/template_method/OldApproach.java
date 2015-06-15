package javaday.lambdas.oop.template_method;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class OldApproach {

    abstract static class LoanApplication {

        public boolean checkLoanApplication() {
            checkIdentity();
            checkCreditHistory();
            checkIncomeHistory();
            reportFindings();
            return true;
        }

        protected abstract void checkIdentity();

        protected abstract void checkIncomeHistory();

        protected abstract void checkCreditHistory();

        private void reportFindings() {}

    }

    static class PersonalLoanApplication extends LoanApplication{
        @Override
        protected void checkIdentity() {

        }
        @Override
        protected void checkIncomeHistory() {

        }
        @Override
        protected void checkCreditHistory() {

        }
    }

    static class CompanyLoanApplication extends LoanApplication{
        @Override
        protected void checkIdentity() {

        }
        @Override
        protected void checkIncomeHistory() {

        }
        @Override
        protected void checkCreditHistory() {

        }
    }
}
