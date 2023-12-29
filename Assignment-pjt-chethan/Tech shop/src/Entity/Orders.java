package Entity;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import Exception.PaymentFailedException;
import Exception.ConcurrencyException;
import Util.DBConnUtil;

public class Orders {
    private int orderID;
    private Customer customer;
    private Date orderDate;
    private double totalAmount;
    private String orderStatus;
    public List<Product> productList;
    public List<OrderDetails> odList;
    private Lock lock; private Connection connection;

    public Orders(int orderID, Customer customer, Date orderDate, String orderStatus) {
        this.orderID = orderID;
        this.customer = customer;
        this.orderDate = orderDate;
        this.totalAmount = 0.0;
        this.productList = new ArrayList();
        this.odList = new ArrayList();
        this.orderStatus = orderStatus;
        this.lock = new ReentrantLock();
        int cid=customer.getCustomerID();
        this.connection= DBConnUtil.getConnection();

        String query = "INSERT INTO Orders(order_id,customer_id,order_date,order_status)VALUES(?,?,?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, orderID);
            preparedStatement.setInt(2, cid);
            preparedStatement.setDate(3, null);
            preparedStatement.setString(4, orderStatus);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println( "Order addedd with ID: " + orderID);
            } else {
                System.out.println("Registration failed.");
            }
        }catch(SQLException e){e.getMessage();}
    }

    public void calculateTotalAmount() {
        this.totalAmount = 0.0;

        OrderDetails orderDetails;
        for(Iterator var1 = this.odList.iterator(); var1.hasNext(); this.totalAmount += orderDetails.calculateSubtotal()) {
            orderDetails = (OrderDetails)var1.next();
        }

    }

    private void handlePaymentFailure(PaymentFailedException e) {
        System.out.println("Error: " + e.getMessage());
    }

    public void getOrderDetails() {
        this.calculateTotalAmount();
        System.out.println("Order ID: " + this.orderID);
        System.out.println("Order Date: " + String.valueOf(this.orderDate));
        PrintStream var10000 = System.out;
        String var10001 = this.customer.getFirstName();
        var10000.println("Customer: " + var10001 + " " + this.customer.getLastName());
        System.out.println("Order Status: " + this.orderStatus);
        System.out.println("Product List:");
        Iterator var1 = this.productList.iterator();

        while(var1.hasNext()) {
            Product product = (Product)var1.next();
            var10000 = System.out;
            var10001 = product.getProductName();
            var10000.println("- " + var10001 + ": " + product.getPrice());
        }

        System.out.println("Total Amount: $" + this.totalAmount);
    }

    public void initiatePayment(double totalAmount) throws PaymentFailedException {
        try {
            boolean paymentSuccessful = true;
            if (!paymentSuccessful) {
                throw new PaymentFailedException("Payment failed. The transaction was declined.");
            }
            System.out.println("Payment processed successfully!");
            System.out.println("Order processed successfully!");
            int oid=this.orderID;
            String query = "UPDATE Orders SET total_amount = ? where order_id=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setDouble(1, totalAmount);
                preparedStatement.setInt(2, oid);
                preparedStatement.executeUpdate();
            }catch(SQLException e){e.getMessage();}

        } catch (PaymentFailedException var4) {
            this.handlePaymentFailure(var4);
        }

    }

    public void updateOrderStatus(String newStatus) throws ConcurrencyException {
        this.lock.lock();

        try {
            if (this.orderStatus.equals(newStatus)) {
                throw new ConcurrencyException("Concurrent update detected. Please retry.");
            }
            this.orderStatus = newStatus;
            System.out.println("Order status updated to: " + this.orderStatus);
            int oid=this.orderID;
            String query = "UPDATE Orders SET order_staus = ? where order_id=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, this.orderStatus);
                preparedStatement.setInt(2, oid);
                preparedStatement.executeUpdate();
            }catch(SQLException e){e.getMessage();}
        } finally {
            this.lock.unlock();
        }

    }

    public void cancelOrder() {
        System.out.println("Order canceled. Stock levels adjusted.");
    }

    public double getTotalAmount() {
        this.calculateTotalAmount();
        return this.totalAmount;
    }
    public int getOrderID(){return this.orderID;}
}
