package bank;

public class BankService {
    // Transfer funds from a user to another user
    public void transfer(User from, User to, double amount) {
        if (from.getBalance() > amount) {
            from.withdraw(amount);
            to.deposit(amount);
        }
    }
}
