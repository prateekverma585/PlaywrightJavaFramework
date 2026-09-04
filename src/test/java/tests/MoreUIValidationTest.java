package tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.file.Paths;

public class MoreUIValidationTest {

    Playwright playwright;
    Browser browser;
    Page page;
    BrowserContext context;
    BrowserContext contextB;
    Page page2;

    @BeforeMethod(alwaysRun=true)
    public void setup(){

        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        //Ex-31
        // Opening two incognito windows
        context = browser.newContext();
        //Ex-34
        context.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSnapshots(true));
        page = context.newPage();

        page.navigate("https://rahulshettyacademy.com/loginpagePractise/");

        //opening another browser parallely duting execution
        //contextB = browser.newContext(); // Using this context will open the link in new tab in a new browser


        page2 =  context.newPage(); // use same sontext to open in a new tab in the same browser
        page2.navigate("https://www.google.com");

        }

        @AfterMethod
        public void tearDown(){
            //Ex-34
            context.tracing().stop(new Tracing.StopOptions()
                    .setPath(Paths.get("trace.zip")));
        }

        //Ex-32
        @Test(groups={"Smoke"})
        public void childWindowsHandle(){
        Locator blinkingTexts = page.locator(".blinkingText");
        Page newPage = context.waitForPage(()-> blinkingTexts.first().click());
        newPage.waitForLoadState();
        String childWindowText = newPage.locator(".im-para.red").textContent();
        //Ex-33
        String emailId = childWindowText.split("at")[1].trim().split("with")[0].trim();
       // System.out.println(emailId);

        page.locator("#username").fill(emailId);
        System.out.println(page.getByLabel("Username").inputValue());

        String password = page.getByText("Learning@830$3mK2").textContent();
        page.getByLabel("Password:").fill(password);

        }

        @Test
        public void UIControls(){
            //Ex-35
            Locator userRadoButton = page.getByRole(AriaRole.RADIO, new Page.GetByRoleOptions().setName("User"));
            userRadoButton.click();

            page.getByRole(AriaRole.BUTTON,new Page.GetByRoleOptions().setName("Okay")).click();
            Assert.assertTrue(userRadoButton.isChecked());

            Locator checkBox = page.getByRole(AriaRole.CHECKBOX,
                    new Page.GetByRoleOptions().setName("I Agree to the terms and conditions"));
            checkBox.check();
            Assert.assertTrue(checkBox.isChecked());

            page.getByRole(AriaRole.COMBOBOX).selectOption("Consultant");
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign In")).click();

        }
}
