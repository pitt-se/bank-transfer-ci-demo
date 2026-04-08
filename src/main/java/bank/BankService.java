package bank;

public class BankService {

    /**
     * Transfers {@code amount} from {@code from} to {@code to}.
     *
     * Known bugs (intentional for teaching):
     *
     * BUG 1 – Wrong comparison operator:
     * Uses > instead of >= in the balance guard, so a transfer of
     * exactly the sender's remaining balance is incorrectly rejected.
     * Fix: change > to >= on the marked line.
     *
     * BUG 2 – No negative-amount validation:
     * A negative amount is never rejected. It silently runs
     * withdraw(-x) and deposit(-x), which *increases* the sender's
     * balance and *decreases* the receiver's – the opposite of intended.
     * Fix: add if (amount < 0) throw new IllegalArgumentException(...)
     * before the balance check.
     *
     * BUG 3 – No atomicity:
     * withdraw() is called before deposit(). If an unchecked exception
     * were thrown between the two calls the sender's balance would already
     * be reduced while the receiver never receives the funds.
     * Fix: save the old balances, attempt both operations, and roll back
     * on failure (or use a transactional data store).
     */
    public void transfer(User from, User to, double amount) {

        // Add negative amount validation FIRST
        if (amount < 0) {
            throw new IllegalArgumentException("Transfer amount cannot be negative");
        }

        // Fix: change > to >=
        if (from.getBalance() >= amount) {
            from.withdraw(amount);
            to.deposit(amount);
        } else {
            throw new IllegalArgumentException(
                    "Insufficient funds: " + from.getName()
                            + " has " + from.getBalance()
                            + " but tried to transfer " + amount);
        }
    }
}
