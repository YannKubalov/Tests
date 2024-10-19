import annotation.AfterSuit;
import annotation.AfterTest;
import annotation.BeforeSuit;
import annotation.BeforeTest;
import annotation.CsvSource;
import annotation.Test;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Example {

    @Test(priority = 1)
    public void firstMethod(){
        var message = "firstMethod completed with priority = 1";
        System.out.println(message);
    }

    @Test
    public void secondMethod(){
        var message = "secondMethod completed with priority = 5";
        System.out.println(message);
    }

    @Test(priority = 9)
    public void thirdMethod(){
        var message = "thirdMethod completed with priority = 9";
        System.out.println(message);
    }

    @Test(priority = 10)
    @CsvSource(params = "10, Java, 20, true")
    public void forthMethod(int a, String b, int c, boolean d){
        var message = "forthMethod completed with priority = 10 and params a = %d, b = %s, c = %d, d = %b\n";
        System.out.printf(message, a, b, c, d);
    }

    @AfterSuit
    public static void afterAll(){
        var message = "afterAll completed";
        System.out.println(message);
    }

    @BeforeSuit
    public static void beforeAll(){
        var message = "beforeAll completed";
        System.out.println(message);
    }
    @AfterTest
    public void afterEach(){
        var message = "afterEach completed";
        System.out.println(message);
    }
    @BeforeTest
    public void beforeEach(){
        var message = "beforeEach completed";
        System.out.println(message);
    }
}
