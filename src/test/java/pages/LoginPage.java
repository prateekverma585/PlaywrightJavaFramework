package pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class LoginPage {

    Page page;
    String base_url;
    //Ex-58
    private static final String email_label = "Email";
    private static final String pass_label = "Password";

    public LoginPage(Page page, String baseUrl) {
        this.page = page;
        this.base_url = baseUrl;
    }

    public DashboardPage loginToApplication(){
        page.navigate(base_url);

        assertThat(page).hasTitle("EventHub — Discover & Book Events");
        page.getByLabel(email_label).fill("veenarecordsinc@gmail.com");
        page.getByLabel(pass_label).fill("Narayan@123");
        //page.locator("button:has-text('Sign In')").click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(java.util.regex.Pattern.
                compile("sign in", java.util.regex.Pattern.CASE_INSENSITIVE))).click();
        DashboardPage dashboardPage = new DashboardPage(page);
        return dashboardPage;

    }
}
