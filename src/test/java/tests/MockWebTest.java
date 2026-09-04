package tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.file.Paths;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class MockWebTest {
        Page page;
        Browser browser;
        Playwright playwright;

        @BeforeMethod
        public void setUp()
        {
            playwright = Playwright.create();
            browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
            page = browser.newPage();
            page.setDefaultTimeout(5000);
            page.navigate("https://eventhub.rahulshettyacademy.com/login");
            PlaywrightAssertions.setDefaultAssertionTimeout(7000);
        }


        //Ex-47
        @Test(description = "Sandbox message is shown more than 5 events are returned")
        public void demoTest() {
            page.getByLabel("Email").fill("veenarecordsinc@gmail.com");
            page.getByLabel("Password").fill("Narayan@123");
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign In")).click();
            assertThat(page.getByRole(AriaRole.LINK,
                    new Page.GetByRoleOptions().setName("Browse Events →"))).isVisible();


            page.navigate("https://eventhub.rahulshettyacademy.com/events");
            page.waitForURL("**/admin/events");

            page.route("**/api/events**",route->route.fulfill(new Route.FulfillOptions()
                    .setPath(Paths.get("src/test/Resources/route_6.json"))
            ));

            page.navigate("https://eventhub.rahulshettyacademy.com/events");

            Locator eventsCard = page.getByTestId("event-card");
            assertThat(eventsCard.first()).isVisible();

            Assert.assertEquals(eventsCard.count(),6);
            assertThat(page.locator(".mx-1").first()).isVisible();


            //Ex-48
            page.navigate("https://eventhub.rahulshettyacademy.com/events");
            page.route("**/api/events**",route-> route.fulfill(
                    new Route.FulfillOptions().setPath(Paths.get("/src/teset/Resources/route_4.json"))
            ));
            Locator eventsCard1 = page.getByTestId("event-card");
            assertThat(eventsCard1.first()).isVisible();

            Assert.assertEquals(eventsCard1.count(),4);
            assertThat(page.locator(".mx-1").first()).isHidden();


        }
}
