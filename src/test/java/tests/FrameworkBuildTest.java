package tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.LocatorAssertions;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import pages.*;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class FrameworkBuildTest extends BaseTest{


        //Ex-13
        @Test(groups={"framework"},description = "Create Event - Book that event and verify that if its booked")
        public void demoTest()
        {
            String eventTitle = "Playwright Framework Test";
            //EX-58
            LoginPage newLoginPage = new LoginPage(page,base_url);
            DashboardPage dashboardPage =newLoginPage.loginToApplication();
            //DashboardPage dashboardPage = new DashboardPage(page); // See LoginPage return loginToApplication()
            dashboardPage.waitForEventToLoad();

            AdminEventPage adminEventPage = new AdminEventPage(page);
            adminEventPage.goTo();


            page.waitForURL("**/admin/events");

            adminEventPage.createEvent(eventTitle,
                    "Playwright Learning",
                    "London",
                    "Royal Albert Hall",
                    "2026-08-25T05:20",
                    "199",
                    "5272");

            //Step 2 : Checking if the event is visible in the events page
            EventsPage eventsPage = new EventsPage(page);
            eventsPage.goTo();
            page.waitForTimeout(1000);
           Locator targetCard = eventsPage.findEventCard(eventTitle);
            int seatNumberBeforeBooking = eventsPage.getSeatsCount(targetCard);
            BookingFormPage bookingFormPage = eventsPage.proceedToBookingEvent(targetCard);

            //Step 3 : Booking the event
            int count = 1;
            for (int i = 1; i < 5; i++) {
                page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("+")).click();
                count = count + 1;
            }
            bookingFormPage.fillAndConfirm("Virat Kohli","vk@gmail.com","+91 3456789009");

            String ConfirmationCode = page.locator(".booking-ref").innerText();
            System.out.println(ConfirmationCode);

            // Validating the booking reference ID
            bookingFormPage.bookingVerification(eventTitle);

            //Validating booking reference (Validating Seats available)
           eventsPage.seatsAvailable(targetCard,seatNumberBeforeBooking);




        }

        @AfterMethod
        public void tearDown()
        {

        }


}
