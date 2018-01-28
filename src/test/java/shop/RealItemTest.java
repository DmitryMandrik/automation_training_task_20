package shop;

import org.testng.Assert;
import org.testng.annotations.Test;

@Test(enabled = false)
class RealItemTest {
    private static final double WEIGHT = 10.5;
    private static final String EXPECTED_TO_STRING_RESULT = "Class: class shop.RealItem; Name: Ubuntu; Price: 20.3; Weight: 10.5";

    @Test
    void verifyThatToStringReturnsCorrectResultTest() {
        RealItem realItem = new RealItem();
        realItem.setName("Ubuntu");
        realItem.setWeight(WEIGHT);
        realItem.setPrice(20.3);

        String actualResult = realItem.toString();
        Assert.assertEquals(EXPECTED_TO_STRING_RESULT, actualResult);
    }
}
