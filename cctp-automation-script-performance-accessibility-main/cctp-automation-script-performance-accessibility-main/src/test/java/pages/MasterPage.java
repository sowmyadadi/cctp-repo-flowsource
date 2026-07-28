package pages;

import com.cognizant.craft.ReusableLibrary;
import com.cognizant.craft.ScriptHelper;
import org.openqa.selenium.support.PageFactory;

public class MasterPage extends ReusableLibrary {

    protected MasterPage(ScriptHelper scriptHelper) {
        super(scriptHelper);
        PageFactory.initElements(driver.getWebDriver(), this);
    }
}
