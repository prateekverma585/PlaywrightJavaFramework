package tests;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.testng.annotations.BeforeMethod;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class BaseTest {

    protected Page page;
    protected Browser browser;
    protected Playwright playwright;
    protected String base_url;

    @BeforeMethod(alwaysRun = true)
    public void setUp() throws IOException {
        //Ex-55
        Properties prop = new Properties();
        FileInputStream fis = new FileInputStream("src/test/Resources/config.properties");
        prop.load(fis);

        // mvn test -PRegression -Dbrowser=chrome
        String browserName = System.getProperty("browser")!=null ? System.getProperty("browser") : prop.getProperty("env","qa");
        //String browserName = prop.getProperty("browser");
        String envName = System.getProperty("qa")!=null ? System.getProperty("qa") : prop.getProperty("qa");
        playwright = Playwright.create();

        if("firefox".equals(browserName))
        {
            browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false));
        }else if("safari".equals(browserName))
        {
            browser = playwright.webkit().launch(new BrowserType.LaunchOptions().setHeadless(false));
        }else
        {
            browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        }

        page = browser.newPage();
        page.setDefaultTimeout(5000);
        base_url = prop.getProperty(envName+".baseurl");
    }
}
