package bank;

public class BankService {

    /**
     * Transfers {@code amount} from {@code from} to {@code to}.
     *
     * Known bugs (intentional for teaching):
     *
     *   BUG 1 – Wrong comparison operator:
     *     Uses  >  instead of  >=  in the balance guard, so a transfer of
     *     exactly the sender's remaining balance is incorrectly rejected.
     *     Fix: change  >  to  >=  on the marked line.
     *
     *   BUG 2 – No negative-amount validation:
     *     A negative amount is never rejected.  It silently runs
     *     withdraw(-x) and deposit(-x), which *increases* the sender's
     *     balance and *decreases* the receiver's – the opposite of intended.
     *     Fix: add  if (amount < 0) throw new IllegalArgumentException(...)
     *          before the balance check.
     *
     *   BUG 3 – No atomicity:
     *     withdraw() is called before deposit().  If an unchecked exception
     *     were thrown between the two calls the sender's balance would already
     *     be reduced while the receiver never receives the funds.
     *     Fix: save the old balances, attempt both operations, and roll back
     *          on failure (or use a transactional data store).
     */
    public void transfer(User from, User to, double amount) {

        // BUG 1: > should be >= so that transferring exactly the available
        //        balance is allowed (e.g. alice has 500 and sends 500).
        if (from.getBalance() > amount) {           // ← BUG 1  (fix: >= )
            // BUG 2: negative amounts sneak past here; add a guard above.

            // BUG 3: non-atomic – a crash between these two lines leaves
            //        the ledger in an inconsistent state.
            from.withdraw(amount);   // step 1 – sender debited
            to.deposit(amount);      // step 2 – receiver credited
        } else {
            throw new IllegalArgumentException(
                "Insufficient funds: " + from.getName()
                + " has " + from.getBalance()
                + " but tried to transfer " + amount);
        }
    }
}
