package Entity;

import Util.DBConnUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OrderDetails {
    private int orderDetailID;
    private Orders order;
    private Product product;
    private int quantity; private Connection connection;

    public OrderDetails(int orderDetailID, Orders order, Product product, int quantity) {
        this.orderDetailID = orderDetailID;
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        int pid=product.getProductID();
        int oid=order.getOrderID();
        this.connection= DBConnUtil.getConnection();

        String query = "INSERT INTO order_details(orderDetail_id,order_id,product_id,quantity)VALUES(?,?,?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, orderDetailID);
            preparedStatement.setInt(2, oid);
            preparedStatement.setInt(3, pid);
            preparedStatement.setInt(4, quantity);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Order Details addedd with ID: " + orderDetailID);
            } else {
                System.out.println("Registration failed.");
            }
        }catch(SQLException e){e.getMessage();}
    }

    public double calculateSubtotal() {
        return (double)this.quantity * this.product.getPrice();
    }

    public int getQuantity() {
        return this.quantity;
    }

    public Product getProduct() {
        return this.product;
    }

    public void getOrderDetailInfo() {
        System.out.println("Order Detail ID: " + this.orderDetailID);
        System.out.println("Product: " + this.product.getProductName());
        System.out.println("Quantity: " + this.quantity);
        System.out.println("Subtotal: $" + this.calculateSubtotal());
    }

    public void updateQuantity(int newQuantity) {
        this.quantity = newQuantity;
        System.out.println("Quantity updated to: " + this.quantity);
    }

    public void addDiscount(double discountPercentage) {
        double discountAmount = this.calculateSubtotal() * (discountPercentage / 100.0);
        double discountedSubtotal = this.calculateSubtotal() - discountAmount;
        System.out.println("Discount applied. New subtotal after discount: $" + discountedSubtotal);
    }
}
