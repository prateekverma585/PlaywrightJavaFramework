package tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.LocatorAssertions;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class BasicTest {

    Page page;
    Browser browser;
    Playwright playwright;

    @BeforeMethod
    public void setUp()
    {
        playwright = Playwright.create();
        //Ex- 15 :: Headless / Head Mode , specify it in after launch

        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        page = browser.newPage();
        //EX-26
        page.setDefaultTimeout(5000);
        page.navigate("https://eventhub.rahulshettyacademy.com/login");
        //Ex-25
        PlaywrightAssertions.setDefaultAssertionTimeout(7000);
    }


    //Ex-13
    @Test(description = "Create Event - Book that event and verify that if its booked")
    public void demoTest()
    {   //Ex-14
        System.out.println(page.title());
        //Checking Assertions
        assertThat(page).hasTitle("EventHub — Discover & Book Events");
        //Ex-19
        page.getByLabel("Email").fill("veenarecordsinc@gmail.com");
        page.getByLabel("Password").fill("Narayan@123");
        page.getByRole(AriaRole.BUTTON,new Page.GetByRoleOptions().setName("Sign In")).click();
        assertThat(page.getByRole(AriaRole.LINK,
                new Page.GetByRoleOptions().setName("Browse Events →"))).isVisible();

        //Step 1 - Creating the event in the page from Admin option
        //Ex-20
        page.navigate("https://eventhub.rahulshettyacademy.com/admin/events");
        page.waitForURL("**/admin/events");
        //Ex-26
        page.getByTestId("event-title-input").fill("The Prateek Verma Experience",new Locator.FillOptions().setTimeout(10000));
        page.locator("textarea").first().fill("The Prateek Verma Experience musical show");
        //Ex-22
        page.getByLabel("Category").selectOption("Concert");
        page.getByLabel("City").fill("London");
        page.getByLabel("Venue").fill("Royal Albert Hall");
        page.getByLabel("Event Date & Time").fill("2026-07-25T05:20");
        //Ex-23
        page.getByLabel("Price ($)").fill("199");
        page.getByLabel("Total Seats").fill("5272");
        //Ex-26
        page.getByTestId("add-event-btn").click(new Locator.ClickOptions().setTimeout(12000)); // Event Created
        // Verify that event created text is appearing
        assertThat(page.getByText("Event Created!")).isVisible();

        //Step 2 : Checking if the event is visible in the events page
        page.getByTestId("nav-events").click();
        page.waitForTimeout(1000);
        Locator eventCards = page.getByTestId("event-card");
        System.out.println(eventCards.count());
        Locator targetCard = eventCards.filter(new Locator.FilterOptions()
                .setHasText("The Prateek Verma Experience")).first();
        //Ex-25
        assertThat(targetCard).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(5000));
        //Ex-27
        String seatText = targetCard.getByText("seats").innerText();
        int seatsAvailableBeforeBooking = Integer.parseInt(seatText.split(" ")[0]);
        System.out.println(seatsAvailableBeforeBooking);
        targetCard.getByTestId("book-now-btn").click();

        //Ex-28 // Confirm booking
        int count = 1;
        for(int i=1;i<5;i++) {
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("+")).click();
            count = count + 1;
        }
        page.getByLabel("Full Name").fill("Virat Kohli");
        page.getByLabel("Email").fill("vk@gmail.com");
        page.getByLabel("Phone Number").fill("+91 3456789009");
        page.getByText("Confirm Booking").click();

        String bookingConfirmation = page.locator("div .text-xl").textContent();
        Assert.assertEquals(bookingConfirmation,"Booking Confirmed! \uD83C\uDF89");

        String ConfirmationCode = page.locator(".booking-ref").innerText();
        System.out.println(ConfirmationCode);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("View My Bookings")).click();

        //Ex-29 // Verify in booking history
        Locator bookingID = page.locator("#booking-card");
        Locator myBooking= bookingID.filter(new Locator.FilterOptions().setHasText("The Prateek Verma Experience")).first();
        String actualBooking = myBooking.locator(".booking-ref").innerText();
        System.out.println(actualBooking);

        //Validating the Booking ID
        Assert.assertEquals(actualBooking,ConfirmationCode);

        //Validating booking reference (Validating Seats available)
        page.navigate("https://eventhub.rahulshettyacademy.com/");
        String remainingSeats = targetCard.getByText("seats").innerText();
        int seatsAvailableAfterBooking = Integer.parseInt(remainingSeats.split(" ")[0]);
        System.out.println(seatsAvailableAfterBooking);
        //Ex-30
        Assert.assertTrue(seatsAvailableBeforeBooking > seatsAvailableAfterBooking);




    }

    @AfterMethod
    public void tearDown()
    {

    }
}
