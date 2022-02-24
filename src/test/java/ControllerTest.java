import com.ritchie.james.Controller;
import com.ritchie.james.UserInterface;
import com.ritchie.james.VendingMachine;
import org.junit.Test;


import static org.junit.Assert.*;

public class ControllerTest {

    Controller testController = new Controller(new UserInterface(), new VendingMachine());

    @Test
    public void testCleanUserStringInput(){
        String testString = "          ThiS IS a TEst      ";
        String result = testController.cleanUserStringInput(testString);
        assertEquals("THIS IS A TEST",result);
    }

    @Test
    public void testCleanFileName(){
        String fileName = "   testFile.txt ";
        String result = testController.cleanFileName(fileName);
        assertEquals("testFile.txt", result);
    }

    @Test
    public void testAddMoneyTrue(){
        boolean result = this.testController.addMoney("5.00");
        assertTrue(result);
    }

    @Test
    public void testAddMoneyFalse(){
        boolean result = this.testController.addMoney("ABC");
        assertFalse(result);
    }


    @Test
    public void testLoadMachineTrue(){
        boolean result = this.testController.loadMachine("input.json");
        assertTrue(result);
    }

    @Test
    public void testLoadMachineFalse(){
        boolean result = this.testController.loadMachine("NOT_REAL.json");
        assertFalse(result);
    }

    @Test
    public void inputToItemIDTest(){
        int[] result = this.testController.inputToItemID("A14");
        System.out.println(result[0]);
        assertArrayEquals(result,new int[]{0,14});
    }

    @Test
    public void validItemId(){
        this.testController.loadMachine("input.json");
        String itemID = "B0";
        boolean result = this.testController.validItemID(itemID);
        assertTrue(result);
    }

    @Test
    public void enoughMoneyToBuyTrue(){
        this.testController.loadMachine("input.json");
        this.testController.addMoney("5.00");
        boolean result = this.testController.enoughMoneyToBuy(new int[]{1,0});
        assertTrue(result);
    }

    @Test
    public void enoughMoneyToBuyFalse(){
        this.testController.loadMachine("input.json");
        this.testController.addMoney(".05");
        boolean result = this.testController.enoughMoneyToBuy(new int[]{1,0});
        assertFalse(result);
    }

}
