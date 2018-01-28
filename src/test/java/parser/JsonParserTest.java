package parser;

import org.testng.Assert;
import org.testng.annotations.*;
import shop.Cart;
import shop.RealItem;
import shop.VirtualItem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Test
class JsonParserTest {

    private static final double BOOK_PRICE = 25.2;
    private static final double SOFT_PRICE = 99.0;
    private static final String PATH_TO_CART_LIST = "/Users/dmitrymandrik/Documents/training/testng-maven/src/test/java/datasets/carts.txt";
    private static final String PATH_TO_CART = "/Users/dmitrymandrik/Documents/training/testng-maven/src/main/resources/john-cart.json";
    private Cart cart;
    private JsonParser parser;

    @DataProvider
    private Object[][] setCartFilesList() {
        BufferedReader in;
        List<String> cartFilesList = new ArrayList<>();

        try {
            in = new BufferedReader(new FileReader(PATH_TO_CART_LIST));
            String str;

            while ((str = in.readLine()) != null) {
                cartFilesList.add(str);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Object[][]{{cartFilesList.get(0)}, {cartFilesList.get(1)}, {cartFilesList.get(2)},
                {cartFilesList.get(3)}, {cartFilesList.get(4)}};
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
    void readFromFileTest(String file) {
        parser.readFromFile(new File(file));
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
        Cart johnCart = parser.readFromFile(new File(PATH_TO_CART));
        List<RealItem> expectedResultName = cart.getRealItems();
        List<RealItem> actualResultName = johnCart.getRealItems();
        Assert.assertEquals(expectedResultName.get(0).getWeight(), actualResultName.get(0).getWeight());
        Assert.assertEquals(expectedResultName.get(0).getPrice(), actualResultName.get(0).getPrice());
        Assert.assertEquals(expectedResultName.get(0).getName(), actualResultName.get(0).getName());
    }

    @AfterClass
    void cleanCart() {
        cart.clearCart();
    }
}
