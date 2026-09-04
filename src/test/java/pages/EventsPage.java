package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.LocatorAssertions;
import org.testng.Assert;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class EventsPage {
    Page page;


    public EventsPage(Page page){
        
        this.page= page;
    }

    public void goTo()
    {

        page.getByTestId("nav-events").click();
    }

    public Locator waitForEventsToLoad(){
        Locator eventCards = page.getByTestId("event-card");
        assertThat(eventCards.first()).isVisible();
        return eventCards;
    }

    public Locator findEventCard(String titleCard){

        Locator eventCards = waitForEventsToLoad();
        Locator targetCard =  eventCards.filter(new Locator.FilterOptions()
                .setHasText(titleCard)).first();
        assertThat(targetCard).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(5000));
        return targetCard;
    }

    public int getSeatsCount(Locator targetCard){

//        Locator targetCard = findEventCard(titleCard);
        String seatText = targetCard.getByText("seats").innerText();
        int seatsAvailableBeforeBooking = Integer.parseInt(seatText.split(" ")[0]);
        System.out.println(seatsAvailableBeforeBooking);

        return seatsAvailableBeforeBooking;
    }

    public BookingFormPage proceedToBookingEvent(Locator targetCard){

        targetCard.getByTestId("book-now-btn").click();
        return new BookingFormPage(page);
    }

    public void seatsAvailable(Locator targetCard,int seatsAvailableBeforeBooking){

        page.navigate("https://eventhub.rahulshettyacademy.com/");
        waitForEventsToLoad();
        String remainingSeats = targetCard.getByText("seats").innerText();
        int seatsAvailableAfterBooking = Integer.parseInt(remainingSeats.split(" ")[0]);
        System.out.println(seatsAvailableAfterBooking);
        Assert.assertTrue(seatsAvailableBeforeBooking > seatsAvailableAfterBooking);
    }
}
