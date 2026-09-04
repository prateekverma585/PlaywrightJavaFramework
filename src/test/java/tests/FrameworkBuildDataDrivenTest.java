package tests;

import Utils.DataProviderUtil;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.*;

import java.io.IOException;
import java.util.HashMap;

public class FrameworkBuildDataDrivenTest extends BaseTest{

        @DataProvider(name="eventBookingData")
        public Object[][] eventBookingData() throws IOException {
            return DataProviderUtil.jsonData("/src/test/resources/eventBookingData.json");
        }
        //Ex-13
        @Test(groups ={"Regression","framework"}, dataProvider= "eventBookingData" , description = "Create Event - Book that event and verify that if its booked")
        public void demoTest(HashMap<String,String> data)
        {


            LoginPage newLoginPage = new LoginPage(page,base_url);
            DashboardPage dashboardPage =newLoginPage.loginToApplication();
            //DashboardPage dashboardPage = new DashboardPage(page); // See LoginPage return loginToApplication()
            dashboardPage.waitForEventToLoad();

            AdminEventPage adminEventPage = new AdminEventPage(page);
            adminEventPage.goTo();


            page.waitForURL("**/admin/events");

            adminEventPage.createEvent(
                    data.get("titlePrefix"),
                    data.get("description"),
                    data.get("city"),
                    data.get("venue"),
                    data.get("dateTime"),
                    data.get("price"),
                    data.get("totalSeats")
            );

            //Step 2 : Checking if the event is visible in the events page
            EventsPage eventsPage = new EventsPage(page);
            eventsPage.goTo();
            page.waitForTimeout(1000);
           Locator targetCard = eventsPage.findEventCard(data.get("titlePrefix"));
            int seatNumberBeforeBooking = eventsPage.getSeatsCount(targetCard);
            BookingFormPage bookingFormPage = eventsPage.proceedToBookingEvent(targetCard);

            //Step 3 : Booking the event
            int count = 1;
            for (int i = 1; i < 5; i++) {
                page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("+")).click();
                count = count + 1;
            }
            bookingFormPage.fillAndConfirm(data.get("fullName"),data.get("email"),data.get("phoneNumber"));

            String ConfirmationCode = page.locator(".booking-ref").innerText();
            System.out.println(ConfirmationCode);

            // Validating the booking reference ID
            bookingFormPage.bookingVerification(data.get("titlePrefix"));

            //Validating booking reference (Validating Seats available)
           eventsPage.seatsAvailable(targetCard,seatNumberBeforeBooking);




        }

        @AfterMethod
        public void tearDown()
        {

        }


}
