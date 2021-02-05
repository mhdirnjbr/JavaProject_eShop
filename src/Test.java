import dao.*;
import model.*;

import java.util.List;

/**
 * Created by MRK on 11/24/2020.
 */
public class Test {

    private static UserDAO userDAO = new UserDAO();
    private static ProductDAO productDAO = new ProductDAO();
    private static DiscountDAO discountDAO = new DiscountDAO();

    private static CategoryDAO categoryDAO = new CategoryDAO();
    private static RoleDAO roleDAO = new RoleDAO();

    public static void main(String[] args) {

        Role role = new Role(0, "Customer");
        Role role2 = new Role(1, "Admin");

        roleDAO.createRole(role);
        roleDAO.createRole(role2);

        /*User mamad = userDAO.findUserByUsername("mamad");
        mamad.setRoleId(0); // admin
        userDAO.updateUser(mamad);*/

        //Category category = categoryDAO.createCategory(new Category(1, "Healthy", "Healthy Products"));

        /*Product shampoo = productDAO.findProductByName("Shampoo");
        System.out.println(shampoo);*/

        /*
        discountDAO.createDiscount(new Discount(1, "2F100C", 2000, false));
        discountDAO.createDiscount(new Discount(2, "23KSG0", 5000, false));
        discountDAO.createDiscount(new Discount(2, "567SSV", 8000, false));*/

        List<Category> categories = categoryDAO.getCategories();
        System.out.println(categories.size());
    }
}
