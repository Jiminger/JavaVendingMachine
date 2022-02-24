import com.ritchie.james.Item;
import com.ritchie.james.UserInterface;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;



import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;

public class UserInterfaceTest {

    public static UserInterface testUI = new UserInterface();

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    public void printFileLoadMessageTest() {
        testUI.printWelcomeMessage();
        String result = outContent.toString().trim();

        assertEquals("""
                ******************************************
                *                                        *
                *  Welcome to the Java Vending machine!  *
                *                                        *
                ******************************************""",result);
    }

    @Test
    public void printFileLoadMessageTrue(){
        testUI.printFileLoadMessage(true);
        String result = outContent.toString().trim();
        assertEquals("File Loaded Successfully.",result);
    }

    @Test
    public void printFileLoadMessageFalse(){
        testUI.printFileLoadMessage(false);
        String result = outContent.toString().trim();
        assertEquals("Error Loading File.",result);
    }

    @Test
    public void printNotEnoughMoneyTest(){
        testUI.printNotEnoughMoney();
        String result = outContent.toString().trim();
        assertEquals("Insufficient Funds.",result);
    }

    @Test
    public void printInvalidInputTest(){
        testUI.printInvalidInput();
        String result = outContent.toString().trim();
        assertEquals("ERROR: INVALID INPUT",result);
    }

    @Test
    public void printMoneyAddedMessageTest(){
        testUI.printMoneyAddedMessage();
        String result = outContent.toString().trim();
        assertEquals("Money Accepted",result);
    }

    @Test
    public void printZeroStatePromptTest(){
        testUI.printZeroStatePrompt();
        String result = outContent.toString().stripTrailing();
        assertEquals("\nState: Ready\nOptions:\n1: View Menu.\n2: Insert Money.\n3: Load New Item List.\nX: Exit",result);
    }

    @Test
    public void printOneStatePromptTest(){
        testUI.printOneStatePrompt(2.00);
        String result = outContent.toString().trim();
        assertEquals("""
                State: Accepting Money
                Current Money: $ 2.00
                Enter Dollar Amount, 'Select' to Choose an Item, or 'X' to Cancel Transaction:""",result);
    }

    @Test
    public void printThreeStatePromptTest(){
        testUI.printThreeStatePrompt(new Item("Item", 2.55, 10));
        String result = outContent.toString().stripTrailing();
        assertEquals("""

                State: Dispensing Item
                Dispensing : Item""", result);
    }

    @Test
    public void printFourStatePromptTest(){
        testUI.printFourStatePrompt(2.00);
        String result = outContent.toString();
        assertEquals("\nState: Dispensing Change\nDispensing Change : $ 2.00",result);
    }




}
