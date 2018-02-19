package parser;

import org.testng.Assert;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;
import shop.Cart;
import shop.RealItem;
import shop.VirtualItem;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Test
public class JsonParserTest {

    private static final double BOOK_PRICE = 25.2;
    private static final double SOFT_PRICE = 99.0;
    private static final String PATH_TO_CART_LIST = "src/test/java/datasets/carts.txt";
    private static final String PATH_TO_CART = "src/main/resources/john-cart.json";
    private Cart cart;
    private JsonParser parser;

    @DataProvider
    private Object[][] setCartFilesList() {
        BufferedReader in;
        List<String[]> cartFilesList = new ArrayList<>();

        try {
            in = new BufferedReader(new FileReader(PATH_TO_CART_LIST));
            String str;

            while ((str = in.readLine()) != null) {
                String[] str_arr = str.split(", ");
                cartFilesList.add(str_arr);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cartFilesList.toArray(new Object[0][]);
    }

    private Cart addItemsToCartTest() {
        RealItem rItem = new RealItem();
        rItem.setName("Book");
        rItem.setWeight(1.5);
        rItem.setPrice(BOOK_PRICE);

        VirtualItem vItem = new VirtualItem();
        vItem.setName("IntellIJ Idea");
        vItem.setSizeOnDisk(512.42);
        vItem.setPrice(SOFT_PRICE);

        Cart cart = new Cart("john-cart");
        cart.addRealItem(rItem);
        cart.addVirtualItem(vItem);
        return cart;
    }

    @BeforeClass
    void initialize() {
        this.cart = addItemsToCartTest();
        this.parser = new JsonParser();
        parser.writeToFile(cart);
    }

    @Test(expectedExceptions = NoSuchFileException.class, dataProvider = "setCartFilesList")
    void readFromFileTest(String file, String bool) {
        if (Boolean.valueOf(bool)){
            parser.readFromFile(new File(file));
        }
    }

    @Test
    void validateCorrectCartNameWrittenToFileTest() {
        Cart johnCart = parser.readFromFile(new File(PATH_TO_CART));
        String expectedResultName = cart.getCartName();
        String actualResultName = johnCart.getCartName();
        Assert.assertEquals(expectedResultName, actualResultName);
    }

    @Test
    void validateCorrectCartPriceWrittenToFileTest() {
        Cart johnCart = parser.readFromFile(new File(PATH_TO_CART));
        double expectedResultName = cart.getTotalPrice();
        double actualResultName = johnCart.getTotalPrice();
        Assert.assertEquals(expectedResultName, actualResultName);
    }

    @Test
    void validateCorrectRealItemWrittenToFileTest() {
        SoftAssert softAssert = new SoftAssert();
        Cart johnCart = parser.readFromFile(new File(PATH_TO_CART));
        List<RealItem> expectedResultName = cart.getRealItems();
        List<RealItem> actualResultName = johnCart.getRealItems();
        softAssert.assertEquals(expectedResultName.get(0).getWeight(), actualResultName.get(0).getWeight());
        softAssert.assertEquals(expectedResultName.get(0).getPrice(), actualResultName.get(0).getPrice());
        softAssert.assertEquals(expectedResultName.get(0).getName(), actualResultName.get(0).getName());
        softAssert.assertAll();
    }

    @AfterClass
    void cleanCart() {
        cart.clearCart();
    }
}
