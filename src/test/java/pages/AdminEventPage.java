package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class AdminEventPage {
    Page page;

    private static final String EVENT_TITLE_INPUT = "event-title-input";
    private static final String DESCRIPTION_TEXTAREA = "textarea";
    private static final String CATEGORY = "Category";
    private static final String CITY_LABEL  = "City";
    private static final String VENUE_LABEL  = "Venue";
    private static final String DATE_TIME_LABEL = "Event Date & Time";
    private static final String PRICE_LABEL = "Price ($)";
    private static final String SEAT_LABEL = "Total Seats";
    private static final String ADD_EVENT_BTN = "add-event-btn";

    public AdminEventPage(Page page) {
        this.page=page;
    }

    public void goTo(){
        page.navigate("https://eventhub.rahulshettyacademy.com/admin/events");
    }

    public void createEvent(String title, String description, String city, String venue,
                            String dateTime, String price, String seats) {
        page.getByTestId(EVENT_TITLE_INPUT).fill(title,new Locator.FillOptions().setTimeout(10000));
        page.locator(DESCRIPTION_TEXTAREA).first().fill(description);
        page.getByLabel(CATEGORY).selectOption("Concert");
        page.getByLabel(CITY_LABEL).fill(city);
        page.getByLabel(VENUE_LABEL).fill(venue);
        page.getByLabel(DATE_TIME_LABEL).fill(dateTime);
        page.getByLabel(PRICE_LABEL).fill(price);
        page.getByLabel(SEAT_LABEL).fill(seats);
        page.getByTestId(ADD_EVENT_BTN).click(new Locator.ClickOptions().setTimeout(12000)); // Event Created
        // Verify that event created text is appearing
       // assertThat(page.getByText("Event Created!")).isVisible();
        page.getByText("Event Created", new Page.GetByTextOptions().setExact(false)).waitFor();
    }
}
