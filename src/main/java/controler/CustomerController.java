package controler;

import ModelPackage.Product.Product;
import ModelPackage.System.exeption.cart.NoSuchAProductInCart;
import ModelPackage.System.exeption.product.NoSuchAProductException;
import ModelPackage.Users.Cart;
import ModelPackage.Users.Customer;
import ModelPackage.Users.SubCart;
import View.PrintModels.CartPM;
import View.PrintModels.InCartPM;
import View.PrintModels.MiniProductPM;

import java.util.ArrayList;
import java.util.List;

public class CustomerController extends Controller {

    public CartPM viewCart(String username){
        Customer customer = (Customer)accountManager.getUserByUsername(username);
        Cart cart = customer.getCart();
        ArrayList<InCartPM> inCartPMS = new ArrayList<>();

        for (SubCart subCart : cart.getSubCarts()) {
            inCartPMS.add(createInCartPM(subCart));
        }

        return new CartPM(
                cart.getTotalPrice(),
                inCartPMS
        );
    }

    private InCartPM createInCartPM(SubCart subCart){
        Product product = subCart.getProduct();
        MiniProductPM miniProductPM = new MiniProductPM(
                product.getName(),
                product.getId(),
                product.getPrices(),
                product.getCompany(),
                product.getTotalScore(),
                product.getDescription()
        );

        return new InCartPM(
                miniProductPM,
                subCart.getSeller().getUsername(),
                subCart.getAmount()
        );
    }

    public List<Product> showProducts(String username){
        Customer customer = (Customer)accountManager.getUserByUsername(username);
        Cart cart = customer.getCart();
        List<Product> products = new ArrayList<>();

        for (SubCart subCart : cart.getSubCarts()) {
            products.add(subCart.getProduct());
        }

        return products;
    }

    public Product viewProduct(int id) throws NoSuchAProductException {
        return productManager.findProductById(id);
    }

    public void changeAmount(String username, int id, int change) throws Exception {
        Customer customer = (Customer)accountManager.getUserByUsername(username);
        Cart cart = customer.getCart();
        String sellerId = cartManager.getSubCartByProductId(cart, id).getSeller().getUsername();
        cartManager.changeProductAmountInCart(cart, id, sellerId, change);
    }



}
