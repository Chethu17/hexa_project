package Entity;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import Exception.InsufficientStockException;
import Exception.IncompleteOrderException;
import Util.DBConnUtil;

public class Inventory {
    private int inventoryID; private Product product;
    private int quantityInStock;
    private Date lastStockUpdate; private Connection connection;

    public Inventory(int inventoryID, Product product, int quantityInStock) {
        this.inventoryID = inventoryID;
        this.product = product;
        this.quantityInStock = quantityInStock;
        this.lastStockUpdate = new Date();
        int pid= product.getProductID(); String pname=product.getProductName();
        this.connection= DBConnUtil.getConnection();

        String query = "INSERT INTO inventory(inventory_id,product_id,quantity_in_stock,last_update)VALUES(?,?,?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, inventoryID);
            preparedStatement.setInt(2, pid);
            preparedStatement.setInt(3, quantityInStock);
            preparedStatement.setDate(4, null);
            // preparedStatement.setDate(4, lastStockUpdate);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println(pname + " addedd to inventory with ID: " + inventoryID);
            } else {
                System.out.println("Registration failed.");
            }
        }catch(SQLException e){e.getMessage();}
    }

    public void processOrder(OrderDetails orderDetails) throws InsufficientStockException, IncompleteOrderException {
        int orderedQuantity = orderDetails.getQuantity();
        if (!this.isProductAvailable(orderedQuantity)) {
            throw new InsufficientStockException("Insufficient stock for product: " + this.product.getProductName());
        } else if (orderDetails.getProduct() == null) {
            throw new IncompleteOrderException("Order detail is incomplete. Product reference is missing.");
        } else {
            System.out.println("Item Added successfully! " + orderDetails.getProduct().getProductName());
        }
    }

    public Product getProduct() {
        return this.product;
    }

    public int getQuantityInStock() {
        return this.quantityInStock;
    }

    public void addToInventory(int quantity) {
        if (quantity >= 0) {
            this.quantityInStock += quantity;
            this.lastStockUpdate = new Date();
            System.out.println("" + quantity + " " + this.product.getProductName() + " added to the inventory.");
        } else {
            System.out.println("Error: Invalid quantity");
        }

    }

    public void removeFromInventory(int quantity) {
        if (quantity <= this.quantityInStock && quantity >= 0) {
            this.quantityInStock -= quantity;
            this.lastStockUpdate = new Date();
            System.out.println("" + quantity + " " + this.product.getProductName() + " removed from the inventory.");
        } else {
            System.out.println("Insufficient quantity in stock. or invalid quantity");
        }

    }

    public void updateStockQuantity(int newQuantity) {
        if (newQuantity >= 0) {
            this.quantityInStock = newQuantity;
            this.lastStockUpdate = new Date();
            System.out.println("Stock quantity updated to: " + newQuantity);
        } else {
            System.out.println("Error: it must be non negative value");
        }

    }

    public boolean isProductAvailable(int quantityToCheck) {
        return this.quantityInStock >= quantityToCheck;
    }

    public double getInventoryValue() {
        return (double)this.quantityInStock * this.product.getPrice();
    }

    public void listLowStockProducts(int threshold) {
        if (this.quantityInStock < threshold) {
            PrintStream var10000 = System.out;
            String var10001 = this.product.getProductName();
            var10000.println(var10001 + " has low stock: " + this.quantityInStock + " units.");
        }

    }

    public void listOutOfStockProducts() {
        if (this.quantityInStock == 0) {
            System.out.println(this.product.getProductName() + " is out of stock.");
        }

    }

    public void listAllProducts() {
        PrintStream var10000 = System.out;
        String var10001 = this.product.getProductName();
        var10000.println("Product: " + var10001 + ", Quantity in Stock: " + this.quantityInStock);
    }

    public Date getLastStockUpdate() {
        return this.lastStockUpdate;
    }

    public int getInventoryID() {
        return this.inventoryID;
    }
}
