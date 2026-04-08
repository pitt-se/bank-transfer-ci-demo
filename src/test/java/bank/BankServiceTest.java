package bank;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BankServiceTest {

    @Test
    void testNormalTransfer() {
        User alice = new User("Alice", 500);
        User bob   = new User("Bob",   100);
        new BankService().transfer(alice, bob, 200);
        assertEquals(300, alice.getBalance());
        assertEquals(300, bob.getBalance());
    }

    @Test
    void testInsufficientFunds() {
        User alice = new User("Alice", 100);
        User bob   = new User("Bob",   100);
        new BankService().transfer(alice, bob, 200); // should do nothing
        assertEquals(100, alice.getBalance());       // ✅ passes
        assertEquals(100, bob.getBalance());
    }

    @Test
    void testExactBalance() {
        User alice = new User("Alice", 200);
        User bob   = new User("Bob",   0);
        new BankService().transfer(alice, bob, 200); // should succeed
        // ❌ This test FAILS because of the > bug (should be >=)
        assertEquals(0,   alice.getBalance());
        assertEquals(200, bob.getBalance());
    }

    @Test
    void testNegativeAmount() {
        User alice = new User("Alice", 100);
        User bob   = new User("Bob",   100);
        new BankService().transfer(alice, bob, -50); // should be rejected
        // ❌ This test FAILS — negative amounts are not validated
        assertEquals(100, alice.getBalance());
        assertEquals(100, bob.getBalance());
    }
}