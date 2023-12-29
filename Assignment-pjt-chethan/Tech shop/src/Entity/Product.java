package Entity;

import java.util.Scanner;
import java.sql.*;
import Util.DBConnUtil;
import java.sql.SQLException;
public class Product {
    private int productID; private String productName;
    private String description; private double price;
    private boolean inStock;
    private Connection connection;

    public Product(int productID, String productName, String description, double price, boolean inStock) {
        this.productID = productID;
        this.productName = productName;this.description = description;
        this.price = price;this.inStock = inStock;
        this.connection=DBConnUtil.getConnection();

        String query = "INSERT INTO product(product_id,product_name,description,price)VALUES(?,?,?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, productID);
            preparedStatement.setString(2, productName);
            preparedStatement.setString(3, description);
            preparedStatement.setDouble(4, price);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println(productName + " addedd with ID: " + productID);
            } else {
                System.out.println("Registration failed.");
            }
        }catch(SQLException e){e.getMessage();}
    }

    public String getProductName() {
        return this.productName;
    }

    public double getPrice() {
        return this.price;
    }

    public void getProductDetails() {
        int var10001 = this.productID;
        System.out.println("Product ID: " + var10001);
        String var1 = this.productName;
        System.out.println("Product Name: " + var1);
        var1 = this.description;
        System.out.println("Description: " + var1);
        double var2 = this.price;
        System.out.println("Price: $" + var2);
        System.out.println("In Stock: " + (this.inStock ? "Yes" : "No"));
    }

    public void updateProductInfo() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Select information to update:");
        System.out.println("1. Price");
        System.out.println("2. Description");
        System.out.println("3. In Stock");
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                System.out.println("Enter new price: ");
                this.price = scanner.nextDouble();
                break;
            case 2:
                System.out.println("Enter new description: ");
                this.description = scanner.next();
                break;
            case 3:
                System.out.println("Is the product in stock? (true/false): ");
                this.inStock = scanner.nextBoolean();
                break;
            default:
                System.out.println("Invalid choice");
        }

        System.out.println("Product information updated successfully!");
    }
    public int getProductID(){return this.productID;}

    public boolean isProductInStock() {
        return this.inStock;
    }
}
