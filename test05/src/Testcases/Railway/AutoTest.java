package Railway;

import Railways.Ticket;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.openqa.selenium.Alert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import Constant.Constant;

import java.util.List;
import java.util.Random;
import java.net.URI;
import java.net.URISyntaxException;

public class AutoTest {
    @BeforeMethod
    public void beforeMethod() {
//        System.out.println("Pre-condition");
//        System.setProperty("webdriver.chrome.driver", Utilities.getProjectPath()
//        + "\\Executables\\chromedriver.exe");
        WebDriverManager.chromedriver().setup();
        Constant.WEBDRIVER = new ChromeDriver();
        Constant.WEBDRIVER.manage().window().maximize();
    }

    @AfterMethod
    public void afterMethod() {
        Constant.WEBDRIVER.quit();
    }

    private void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (Exception ex) {
            System.out.printf(ex.getMessage());
        }
    }

    @Test
    public void TC01() {
        System.out.println("TC01 - User can log into Railway with valid username and password");
        HomePage homePage = new HomePage();
        homePage.open();

        LoginPage loginPage = homePage.gotoLoginPage();

        String actualMsg = loginPage.login(Constant.USERNAME, Constant.PASSWORD).getWelcomeMessage();
        String expectedMsg = "Welcome " + Constant.USERNAME;

        Assert.assertEquals(actualMsg, expectedMsg);
    }

    @Test
    public void TC02() {
        System.out.println("TC02 - User can't login with blank \"Username\" textbox");
        HomePage homePage = new HomePage();
        homePage.open();

        LoginPage loginPage = homePage.gotoLoginPage();

        String actualMsg = String.valueOf(loginPage.login("", Constant.PASSWORD).getMessageClass("message error LoginForm"));
        String expectedMsg = "There was a problem with your login and/or errors exist in your form.";


        Assert.assertEquals(actualMsg, expectedMsg);
    }

    @Test
    public void TC03() {
        System.out.println("TC03 - User cannot log into Railway with invalid password ");
        HomePage homePage = new HomePage();
        homePage.open();

        LoginPage loginPage = homePage.gotoLoginPage();

        String actualMsg = String.valueOf(loginPage.login(Constant.USERNAME, "123").getMessageClass("message error LoginForm"));
        String expectedMsg = "There was a problem with your login and/or errors exist in your form.";

        Assert.assertEquals(actualMsg, expectedMsg);
    }

    @Test
    public void TC04() {
        System.out.println("TC04 - Login page displays when un-logged User clicks on \"Book ticket\" tab");
        HomePage homePage = new HomePage();
        homePage.open();

        Constant.WEBDRIVER.findElement(By.xpath("//div[@id='menu']//a[@href='/Page/BookTicketPage.cshtml']")).click();

        String actualMsg = Constant.WEBDRIVER.findElement(By.xpath("//div[@id = 'content']//h1")).getText();
        String expectedMsg = "Login Page";

        Assert.assertEquals(actualMsg, expectedMsg);
    }

    @Test
    public void TC05() {
        System.out.println("TC05 - System shows message when user enters wrong password several times");
        HomePage homePage = new HomePage();
        homePage.open();

        LoginPage loginPage = homePage.gotoLoginPage();
        loginPage.login(Constant.USERNAME, "");
        System.out.println("Login 1 time.");

        // Login 3 more times.
        for (int i = 0; i < 3; i++) {
            // Tạo đối tượng Actions
            Actions actions = new Actions(Constant.WEBDRIVER);

            // Bấm phím "End"
            actions.sendKeys(Keys.END).perform();
            sleep(1000);
            System.out.println("Login " + (i+2) + " times.");
            Constant.WEBDRIVER.findElement(By.xpath("//p[@class='form-actions']/input[@type='submit']")).click();
        }

        String actualMsg = Constant.WEBDRIVER.findElement(By.xpath("//p[@class='message error LoginForm']")).getText();
        String expectedMsg = "You have used 4 out of 5 login attempts. After all 5 have been used, you will be unable to login for 15 minutes.";

        Assert.assertEquals(actualMsg, expectedMsg);
    }

    @Test
    public void TC06() {
        System.out.println("TC06 - Additional pages display once user logged in");
        HomePage homePage = new HomePage();
        homePage.open();

        LoginPage loginPage = homePage.gotoLoginPage();

        loginPage.login(Constant.USERNAME, Constant.PASSWORD);

        // My ticket
        Constant.WEBDRIVER.findElement(By.xpath("//div[@id='menu']//a[@href='/Page/ManageTicket.cshtml']")).click();
        String actualMsg1 = Constant.WEBDRIVER.findElement(By.xpath("//div[@id = 'content']//h1")).getText();
        String expectedMsg1 = "Manage Tickets";
        Assert.assertEquals(actualMsg1, expectedMsg1);

        // Change password
        Constant.WEBDRIVER.findElement(By.xpath("//div[@id='menu']//a[@href='/Account/ChangePassword.cshtml']")).click();
        String actualMsg2 = Constant.WEBDRIVER.findElement(By.xpath("//div[@id = 'content']//h1")).getText();
        String expectedMsg2 = "Change password";
        Assert.assertEquals(actualMsg2, expectedMsg2);

        // Logout
        Constant.WEBDRIVER.findElement(By.xpath("//div[@id='menu']//a[@href='/Account/Logout']")).click();
        String actualMsg3 = Constant.WEBDRIVER.findElement(By.xpath("//div[@id = 'content']//h1")).getText();
        String expectedMsg3 = "Welcome to Safe Railway";

        Assert.assertEquals(actualMsg3, expectedMsg3);
    }

    @Test
    public void TC07() {
        System.out.println("TC07 - User can create new account");
        HomePage homePage = new HomePage();
        homePage.open();

        Constant.WEBDRIVER.findElement(By.xpath("//div[@id='menu']//a[@href='/Account/Register.cshtml']")).click();


        String characters = "abcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder randomString = new StringBuilder();

        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            char randomChar = characters.charAt(random.nextInt(characters.length()));
            randomString.append(randomChar);
        }

        String registerEmail = randomString.toString() + "@gmail.com";

        // Register
        Constant.WEBDRIVER.findElement(By.xpath("//input[@id='email']")).sendKeys(registerEmail);
        Constant.WEBDRIVER.findElement(By.xpath("//input[@id='password']")).sendKeys("12345678");
        Constant.WEBDRIVER.findElement(By.xpath("//input[@id='confirmPassword']")).sendKeys("12345678");
        Constant.WEBDRIVER.findElement(By.xpath("//input[@id='pid']")).sendKeys("12345678");

        // Press Enter
        Constant.WEBDRIVER.findElement(By.xpath("//input[@title='Register']")).sendKeys(Keys.ENTER);

        sleep(500);

        String actualMsg = Constant.WEBDRIVER.findElement(By.xpath("//div[@id = 'content']//p")).getText();
        String expectedMsg = "Thank you for registering your account";

        Assert.assertEquals(actualMsg, expectedMsg);
    }

    @Test
    public void TC08() {
        System.out.println("TC08 - User can't login with an account hasn't been activated");
        HomePage homePage = new HomePage();
        homePage.open();

        LoginPage loginPage = homePage.gotoLoginPage();

        String actualMsg = String.valueOf(loginPage.login("a3@gmail.com", "12345678").getMessageClass("message error LoginForm"));
        String expectedMsg = "Invalid username or password. Please try again.";

        Assert.assertEquals(actualMsg, expectedMsg);
    }

    @Test
    public void TC09() {
        System.out.println("TC09 - User can change password");
        HomePage homePage = new HomePage();
        homePage.open();

        LoginPage loginPage = homePage.gotoLoginPage();
        loginPage.login("tuan@gmail.com", "12345678");

        // Change password
        Constant.WEBDRIVER.findElement(By.xpath("//div[@id='menu']//a[@href='/Account/ChangePassword.cshtml']")).click();

        Constant.WEBDRIVER.findElement(By.xpath("//input[@id='currentPassword']")).sendKeys("12345678");
        Constant.WEBDRIVER.findElement(By.xpath("//input[@id='newPassword']")).sendKeys("12345678");
        Constant.WEBDRIVER.findElement(By.xpath("//input[@id='confirmPassword']")).sendKeys("12345678");

        // Press Enter
        Constant.WEBDRIVER.findElement(By.xpath("//input[@title='Change password']")).sendKeys(Keys.ENTER);

        String actualMsg = Constant.WEBDRIVER.findElement(By.xpath("//p[@class='message success']")).getText();

        String expectedMsg = "Your password has been updated!";
        Assert.assertEquals(actualMsg, expectedMsg);
    }

    @Test
    public void TC10() {
        System.out.println("TC10 - User can't create account with \"Confirm password\" is not the same with \"Password\"");
        HomePage homePage = new HomePage();
        homePage.open();

        Constant.WEBDRIVER.findElement(By.xpath("//div[@id='menu']//a[@href='/Account/Register.cshtml']")).click();

        // Register
        Constant.WEBDRIVER.findElement(By.xpath("//input[@id='email']")).sendKeys("a@gmail.com");
        Constant.WEBDRIVER.findElement(By.xpath("//input[@id='password']")).sendKeys("12345678");
        Constant.WEBDRIVER.findElement(By.xpath("//input[@id='confirmPassword']")).sendKeys("123456789");
        Constant.WEBDRIVER.findElement(By.xpath("//input[@id='pid']")).sendKeys("12345678");

        // Press Enter
        Constant.WEBDRIVER.findElement(By.xpath("//input[@title='Register']")).sendKeys(Keys.ENTER);

        String actualMsg = Constant.WEBDRIVER.findElement(By.xpath("//div[@id = 'content']//p[@class = 'message error']")).getText();
        String expectedMsg = "There're errors in the form. Please correct the errors and try again.";

        Assert.assertEquals(actualMsg, expectedMsg);
    }

    @Test
    public void TC11() {
        System.out.println("TC11 - User can't create account while password and PID fields are empty");
        HomePage homePage = new HomePage();
        homePage.open();

        Constant.WEBDRIVER.findElement(By.xpath("//div[@id='menu']//a[@href='/Account/Register.cshtml']")).click();

        // Register
        Constant.WEBDRIVER.findElement(By.xpath("//input[@id='email']")).sendKeys("a@gmail.com");

        // Press Enter
        Constant.WEBDRIVER.findElement(By.xpath("//input[@title='Register']")).sendKeys(Keys.ENTER);

        String actualMsg1 = Constant.WEBDRIVER.findElement(By.xpath("//div[@id = 'content']//p[@class = 'message error']")).getText();
        String expectedMsg1 = "There're errors in the form. Please correct the errors and try again.";
        Assert.assertEquals(actualMsg1, expectedMsg1, "Welcome message is not displayed as expected");

        String actualMsg2 = Constant.WEBDRIVER.findElement(By.xpath("//ol//label[@for='password' and @class]")).getText();
        String expectedMsg2 = "Invalid password length";
        Assert.assertEquals(actualMsg2, expectedMsg2, "Welcome message is not displayed as expected");

        String actualMsg3 = Constant.WEBDRIVER.findElement(By.xpath("//ol//label[@for='pid' and @class]")).getText();
        String expectedMsg3 = "Invalid ID length";
        Assert.assertEquals(actualMsg3, expectedMsg3);
    }

    @Test
    public void TC12() {
        System.out.println("TC12 - Errors display when password reset token is blank");
        HomePage homePage = new HomePage();
        homePage.open();

        LoginPage loginPage = homePage.gotoLoginPage();

        Constant.WEBDRIVER.findElement(By.xpath("//ul/li[contains(text(), 'forgot your password')]/a")).click();

        // Tạo đối tượng Actions
        Actions actions = new Actions(Constant.WEBDRIVER);

        // Bấm phím "End"
        actions.sendKeys(Keys.END).perform();

        Constant.WEBDRIVER.findElement(By.xpath("//li[@class='email']/input[@type='text']")).sendKeys(Constant.USERNAME);
        Constant.WEBDRIVER.findElement(By.xpath("//p[@class='form-actions']/input[@type='submit']")).click();
    }

    @Test
    public void TC13() {
        System.out.println("TC13 - Errors display if password and confirm password don't match when resetting password");
        HomePage homePage = new HomePage();
        homePage.open();

        LoginPage loginPage = homePage.gotoLoginPage();

        Constant.WEBDRIVER.findElement(By.xpath("//ul/li[contains(text(), 'forgot your password')]/a")).click();

        // Tạo đối tượng Actions
        Actions actions = new Actions(Constant.WEBDRIVER);

        // Bấm phím "End"
        actions.sendKeys(Keys.END).perform();

        Constant.WEBDRIVER.findElement(By.xpath("//li[@class='email']/input[@type='text']")).sendKeys(Constant.USERNAME);
        Constant.WEBDRIVER.findElement(By.xpath("//p[@class='form-actions']/input[@type='submit']")).click();
    }

    @Test
    public void TC14() {
        System.out.println("TC14 - User can book 1 ticket at a time");
        HomePage homePage = new HomePage();
        homePage.open();

        LoginPage loginPage = homePage.gotoLoginPage();

        loginPage.login(Constant.USERNAME, Constant.PASSWORD);

        sleep(3000);

        Ticket ticket = new Ticket();
        ticket.setId("1");
        ticket.bookTicket("Sài Gòn", "Nha Trang", "Soft bed with air conditioner", "1");

        String book = ticket.getDepartDate();
        String booked = Constant.WEBDRIVER.findElement(By.xpath("//table[@class='MyTable WideTable']//tr[@class='OddRow']/td[count(//table[@class='MyTable WideTable']//tr/th[text()='Depart Date']/preceding-sibling::th)+1]")).getText();
        Assert.assertEquals(book, booked);

        book = ticket.getDepartStation();
        booked = Constant.WEBDRIVER.findElement(By.xpath("//table[@class='MyTable WideTable']//tr[@class='OddRow']/td[count(//table[@class='MyTable WideTable']//tr/th[text()='Depart Station']/preceding-sibling::th)+1]")).getText();
        Assert.assertEquals(book, booked);

        book = ticket.getArriveStation();
        booked = Constant.WEBDRIVER.findElement(By.xpath("//table[@class='MyTable WideTable']//tr[@class='OddRow']/td[count(//table[@class='MyTable WideTable']//tr/th[text()='Arrive Station']/preceding-sibling::th)+1]")).getText();
        Assert.assertEquals(book, booked);

        book = ticket.getSeatType();
        booked = Constant.WEBDRIVER.findElement(By.xpath("//table[@class='MyTable WideTable']//tr[@class='OddRow']/td[count(//table[@class='MyTable WideTable']//tr/th[text()='Seat Type']/preceding-sibling::th)+1]")).getText();
        Assert.assertEquals(book, booked);

        book = ticket.getAmount();
        booked = Constant.WEBDRIVER.findElement(By.xpath("//table[@class='MyTable WideTable']//tr[@class='OddRow']/td[count(//table[@class='MyTable WideTable']//tr/th[text()='Amount']/preceding-sibling::th)+1]")).getText();
        Assert.assertEquals(book, booked);
    }


    @Test
    public void TC15() {
        System.out.println("TC15 - User can open \"Book ticket\" page by clicking on \"Book ticket\" link in \"Train timetable\" page");
        HomePage homePage = new HomePage();
        homePage.open();

        LoginPage loginPage = homePage.gotoLoginPage();
        loginPage.login(Constant.USERNAME, Constant.PASSWORD);

        Constant.WEBDRIVER.findElement(By.xpath("//div[@id='menu']//a[@href='TrainTimeListPage.cshtml']")).click();

        //table[@class='MyTable WideTable']//tr/td[count(//table[@class='MyTable WideTable']//tr/th[text()='Depart Station']/preceding-sibling::th)+1]

        List<WebElement> departStation = Constant.WEBDRIVER.findElements(By.xpath("//table[@class='MyTable WideTable']//tr/td[count(//table[@class='MyTable WideTable']//tr/th[text()='Depart Station']/preceding-sibling::th)+1]"));
        List<WebElement> arriveStation = Constant.WEBDRIVER.findElements(By.xpath("//table[@class='MyTable WideTable']//tr/td[count(//table[@class='MyTable WideTable']//tr/th[text()='Arrive Station']/preceding-sibling::th)+1]"));

        for (int i = 0; i < departStation.size(); i++) {
            if ("Huế".equals(departStation.get(i).getText()) && "Sài Gòn".equals(arriveStation.get(i).getText())) {
                String rowIndex = String.valueOf(i + 1);
                sleep(3000);

                String link = Constant.WEBDRIVER.findElement(By.xpath("//table[@class='MyTable WideTable']//tr["+rowIndex+"]/td[count(//table[@class='MyTable WideTable']//tr/th[text()='Book ticket']/preceding-sibling::th)+1]/a[contains(text(), 'book ticket')]")).getAttribute("href");

                Constant.WEBDRIVER.navigate().to(link);

                break;
            }
        }

        sleep(3000);

        String departFrom_value = Constant.WEBDRIVER.findElement(By.xpath("//select[@name='DepartStation']")).getAttribute("value");
        String departFrom = Constant.WEBDRIVER.findElement(By.xpath("//select[@name='DepartStation']/option[@value="+departFrom_value+"]")).getText();
        Assert.assertEquals(departFrom, "Huế");

        String arriveAt_value = Constant.WEBDRIVER.findElement(By.xpath("//select[@name='ArriveStation']")).getAttribute("value");
        String arriveAt = Constant.WEBDRIVER.findElement(By.xpath("//select[@name='ArriveStation']/option[@value="+arriveAt_value+"]")).getText();
        Assert.assertEquals(arriveAt, "Sài Gòn");
    }

    @Test
    public void TC16() {
        System.out.println("TC16 - User can cancel a ticket");
        HomePage homePage = new HomePage();
        homePage.open();

        LoginPage loginPage = homePage.gotoLoginPage();

        loginPage.login(Constant.USERNAME, Constant.PASSWORD);

        sleep(3000);

        Ticket ticket = new Ticket();
        ticket.bookTicket("Sài Gòn", "Nha Trang", "Soft seat", "1");

        String currentURL = Constant.WEBDRIVER.getCurrentUrl();

        sleep(3000);

        try {
            URI uri = new URI(currentURL);
            String id = uri.getQuery().split("/?id=")[1];

            ticket.setId(id);

            Constant.WEBDRIVER.findElement(By.xpath("//div[@id='menu']//a[@href='/Page/ManageTicket.cshtml']")).click();

            // Tạo đối tượng Actions
            Actions actions = new Actions(Constant.WEBDRIVER);

            // Bấm phím "End"
            actions.sendKeys(Keys.END).perform();

            sleep(1000);

            // Click 'Cancel' button of chosen ticket
            Constant.WEBDRIVER.findElement(By.xpath("//table[@class='MyTable']//tr/td[count(//table[@class='MyTable']//tr/th[text()='Operation']/preceding-sibling::th)+1]/input[@onclick='DeleteTicket("+id+");']")).click();

            Alert alert = Constant.WEBDRIVER.switchTo().alert();
            alert.accept();

            // Check that the ticket is canceled.
            List<WebElement> inputElement = Constant.WEBDRIVER.findElements(By.xpath("//table[@class='MyTable']//tr/td[count(//table[@class='MyTable']//tr/th[text()='Operation']/preceding-sibling::th)+1]/input"));

            String isCanceled = "True";

            for (int i = 0; i < inputElement.size(); i++) {
                if (("DeleteTicket("+id+");").equals(inputElement.get(i).getAttribute("DeleteTicket(8250);"))) {
                    isCanceled = "False";
                    break;
                }
            }

            Assert.assertEquals(isCanceled, "True");

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
