package Railways;

import Constant.Constant;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import org.openqa.selenium.By;
import org.openqa.selenium.interactions.Actions;

import org.openqa.selenium.chrome.ChromeDriver;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;

public class Ticket {
    private String departDate;
    private String departStation;
    private String arriveStation;
    private String seatType	;
    private String amount;
    private String id;
    public Ticket() {
        this.departDate = "";
        this.departStation = "";
        this.arriveStation = "";
        this.seatType = "";
        this.amount = "";
        this.id = "";
    }

    private void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (Exception ex) {
            System.out.printf(ex.getMessage());
        }
    }

    // Tạo getter và setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDepartDate() {
        return departDate;
    }

    public void setDepartDate(String departDate) {
        this.departDate = departDate;
    }

    public String getDepartStation() {
        return departStation;
    }

    public void setDepartStation(String departStation) {
        this.departStation = departStation;
    }

    public String getArriveStation() {
        return arriveStation;
    }

    public void setArriveStation(String arriveStation) {
        this.arriveStation = arriveStation;
    }

    public String getSeatType() {
        return seatType;
    }

    public void setSeatType(String seatType) {
        this.seatType = seatType;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    // Tạo bookTicket để đặt vé
    public void bookTicket(String departStation, String arriveStation, String seatType, String amount) {
        Constant.WEBDRIVER.findElement(By.xpath("//div[@id='menu']//a[@href='/Page/BookTicketPage.cshtml']")).click();

        // Tạo đối tượng Actions
        Actions actions = new Actions(Constant.WEBDRIVER);

        // Bấm phím "End"
        actions.sendKeys(Keys.END).perform();

        sleep(3000);

        WebElement seatDropdown = Constant.WEBDRIVER.findElement(By.name("SeatType"));
        Select seatSelect = new Select(seatDropdown);
        seatSelect.selectByVisibleText(seatType);

        WebElement amountDropdown = Constant.WEBDRIVER.findElement(By.name("TicketAmount"));
        Select amountSelect = new Select(amountDropdown);
        amountSelect.selectByVisibleText(amount);

        Constant.WEBDRIVER.findElement(By.xpath("//select[@name='Date']")).click();
        String date = Constant.WEBDRIVER.findElement(By.xpath("//option[@value='5']")).getText();
        Constant.WEBDRIVER.findElement(By.xpath("//option[@value='5']")).click();

        WebElement departDropdown = Constant.WEBDRIVER.findElement(By.name("DepartStation"));
        Select departSelect = new Select(departDropdown);
        departSelect.selectByVisibleText(departStation);

        WebElement arriveDropdown = Constant.WEBDRIVER.findElement(By.name("ArriveStation"));
        Select arriveSelect = new Select(arriveDropdown);
        arriveSelect.selectByVisibleText(arriveStation);

        Constant.WEBDRIVER.findElement(By.xpath("//input[@type='submit']")).click();

        // Bấm phím "End"
        actions.sendKeys(Keys.END).perform();

        sleep(3000);

        this.departDate = date;
        this.departStation = departStation;
        this.arriveStation = arriveStation;
        this.seatType = seatType;
        this.amount = amount;
    }
}
