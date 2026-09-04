package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;

public class BookingFormPage {

    Page page;
    private static final String FULL_NAME_LABEL = "Full Name";
    private static final String EMAIL_LABEL = "Email";
    private static final String PHONE_NUMBER_LABEL = "Phone Number";


    public BookingFormPage(Page page){
        this.page= page;
    }

    public void fillAndConfirm(String fullName, String email, String phone) {

        page.getByLabel(FULL_NAME_LABEL).fill(fullName);
        page.getByLabel(EMAIL_LABEL).fill(email);
        page.getByLabel(PHONE_NUMBER_LABEL).fill(phone);
        page.getByText("Confirm Booking").click();

        page.locator(".booking-ref").waitFor();

        String bookingConfirmation = page.locator("div .text-xl").textContent();
        Assert.assertEquals(bookingConfirmation, "Booking Confirmed! \uD83C\uDF89");

//        "Virat Kohli"
//        "vk@gmail.com"
//        "+91 3456789009"


        }
    // Booking Verification and matching the reference ID
    public void bookingVerification(String eventTitle){
        page.locator(".booking-ref").waitFor();
        String ConfirmationCode = page.locator(".booking-ref").first().innerText();
        System.out.println(ConfirmationCode);
        Locator viewBookingsButton= page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions()
                .setName("View My Bookings"));

        viewBookingsButton.waitFor();
        viewBookingsButton.click();


        page.waitForURL("**/bookings");


        Locator bookingID = page.locator("#booking-card");
        Locator myBooking= bookingID.filter(new Locator.FilterOptions().setHasText(eventTitle)).first();
        String actualBooking = myBooking.locator(".booking-ref").innerText();
        //System.out.println(actualBooking);

        //Validating the Booking ID
        Assert.assertEquals(actualBooking,ConfirmationCode);

    }
}
