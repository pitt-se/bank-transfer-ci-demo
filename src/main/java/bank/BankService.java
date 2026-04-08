package bank;

public class BankService {

    public void transfer(User from, User to, double amount) {
        if (from.getBalance() > amount) {
            from.withdraw(amount);
            to.deposit(amount);
        }
    }
}
