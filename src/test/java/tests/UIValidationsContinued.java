package tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.file.Paths;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class UIValidationsContinued {

    Playwright playwright;
    Browser browser;
    Page page;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        page = browser.newPage();
        page.navigate("https://rahulshettyacademy.com/AutomationPractice/");


    }

    @Test(groups={"Regression"})
    public void popupValidations() {
        //Ex-36
        assertThat(page.getByPlaceholder("Hide/Show Example")).isVisible();
        page.locator("#hide-textbox").click();
        assertThat(page.getByPlaceholder("Hide/Show Example")).isHidden();
        //For the dialogue boxes
        page.onDialog(dialog -> dialog.accept());
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Alert")).click();
        //Ex-37
        //Mouse Hover
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Mouse Hover")).hover();
        //page.waitForTimeout(2000);
        //page.getByRole(AriaRole.LINK,new Page.GetByRoleOptions().setName("Top")).click();

        // Ex-38 // handeling Frames
        FrameLocator framePage = page.frameLocator("#courses-iframe");
        framePage.getByRole(AriaRole.LINK,new FrameLocator.GetByRoleOptions().setName("Learning paths")).click();
        String pageHeading = framePage.locator(".inner-box h1").textContent();

        System.out.println(pageHeading);

    }
    // Ex-39-40
    @Test
    public void screenshotTest(){
        //Page Level
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("pagescreenshot.png")));

        // Component/Locator Level
        Locator displayEditBox = page.getByPlaceholder("Hide/Show Example");
        displayEditBox.screenshot(new Locator.ScreenshotOptions().setPath(Paths.get("editboxscreenshot.png")));
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("pagePostScreenshot.png")));

    }
}
