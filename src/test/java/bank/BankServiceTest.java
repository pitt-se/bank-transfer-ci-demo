package bank;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Four tests for BankService.transfer().
 *
 *   PASS  testNormalTransfer          – straightforward happy path
 *   PASS  testInsufficientFundsFails  – genuinely over-limit transfer
 *   FAIL  testExactBalanceTransfer    – exposes BUG 1 (> vs >=)
 *   FAIL  testNegativeAmountRejected  – exposes BUG 2 (no negative guard)
 */
class BankServiceTest {

    private BankService bankService;
    private User alice;
    private User bob;

    @BeforeEach
    void setUp() {
        bankService = new BankService();
        alice = new User("Alice", 1000.0);
        bob   = new User("Bob",      0.0);
    }

    // ------------------------------------------------------------------ //
    //  PASSING TEST 1                                                      //
    //  Normal transfer: sender has more than enough funds.                 //
    //  Guard:  1000 > 500  →  true  →  transfer executes.                 //
    // ------------------------------------------------------------------ //
    @Test
    void testNormalTransfer() {
        bankService.transfer(alice, bob, 500.0);

        assertEquals(500.0, alice.getBalance(), 0.001,
            "Alice should have 500 after sending 500");
        assertEquals(500.0, bob.getBalance(), 0.001,
            "Bob should have 500 after receiving 500");
    }

    // ------------------------------------------------------------------ //
    //  PASSING TEST 2                                                      //
    //  Genuinely over-limit: alice has 1000, tries to send 1500.          //
    //  Guard:  1000 > 1500  →  false  →  exception thrown  →  expected.   //
    //  (BUG 1 happens to produce the correct outcome here.)               //
    // ------------------------------------------------------------------ //
    @Test
    void testInsufficientFundsFails() {
        assertThrows(IllegalArgumentException.class,
            () -> bankService.transfer(alice, bob, 1500.0),
            "Transfer exceeding balance should throw IllegalArgumentException");
    }

    // ------------------------------------------------------------------ //
    //  FAILING TEST 3  –  exposes BUG 1                                   //
    //  Sending exactly the available balance must succeed.                 //
    //                                                                      //
    //  Correct behaviour  (>=):  1000 >= 1000  →  true  →  transfer OK    //
    //  Buggy  behaviour   ( >):  1000 >  1000  →  false →  exception! ✗  //
    //                                                                      //
    //  The test expects balances of 0 / 1000 but instead gets an          //
    //  IllegalArgumentException → test FAILS.                             //
    // ------------------------------------------------------------------ //
    @Test
    void testExactBalanceTransfer() {
        // Should succeed – alice transfers her entire balance.
        bankService.transfer(alice, bob, 1000.0);

        assertEquals(0.0, alice.getBalance(), 0.001,
            "Alice should have 0 after transferring her entire balance");
        assertEquals(1000.0, bob.getBalance(), 0.001,
            "Bob should have 1000 after receiving Alice's entire balance");
    }

    // ------------------------------------------------------------------ //
    //  FAILING TEST 4  –  exposes BUG 2                                   //
    //  A negative amount should be rejected immediately.                   //
    //                                                                      //
    //  Correct behaviour: throw IllegalArgumentException before touching   //
    //                     any balance.                                     //
    //  Buggy  behaviour:  1000 > -200  →  true  →  transfer "executes":   //
    //                     alice.withdraw(-200) raises her balance to 1200, //
    //                     bob.deposit(-200) lowers his balance to -200. ✗  //
    //                                                                      //
    //  The test expects an exception but none is thrown → test FAILS.     //
    // ------------------------------------------------------------------ //
    @Test
    void testNegativeAmountRejected() {
        assertThrows(IllegalArgumentException.class,
            () -> bankService.transfer(alice, bob, -200.0),
            "Negative transfer amount should throw IllegalArgumentException");
    }
}
