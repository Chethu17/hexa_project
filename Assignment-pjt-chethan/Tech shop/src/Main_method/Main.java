package Main_method;

import java.util.Date;
import Entity.*;
import Exception.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello and welcome to TECH-SHOP!");
        try{
            securityService secureService1 = new securityService();
            secureService1.authenticateUser("user123", "password123");
            secureService1.authorizeAccess("user123");
            try {
                Product p1 = new Product(101, "Laptop", "High-performance laptop", 999.99, true);
                Product p2 = new Product(102, "Wireless Mouse", "Ergonomic wireless mouse", 29.99, true);
                Inventory invItem1 = new Inventory(501, p1, 10);
                Inventory invItem2 = new Inventory(502, p2, 10);

                Customer c1 = new Customer(1, "Chethan", "Kumar", "chethan@gmail.com", "123-456-7890", "123 Main St");
                // c1.getCustomerDetails();
                Orders order1 = new Orders(201, c1, new Date(), "Processing");

                OrderDetails od1 = new OrderDetails(301, order1, p1, 2);
                invItem1.processOrder(od1);
                order1.productList.add(p1);
                OrderDetails od2 = new OrderDetails(302, order1, p2, 2);
                invItem2.processOrder(od2);
                order1.productList.add(p2);

                order1.odList.add(od1);
                order1.odList.add(od2);

                order1.getOrderDetails();
                order1.initiatePayment(order1.getTotalAmount());
/*
                Runnable updateOrderTask = () -> {
                    try {order1.updateOrderStatus("Shipped");}
                    catch (ConcurrencyException var2) {System.out.println("Error: " + var2.getMessage());}
                };
                Thread thread1 = new Thread(updateOrderTask);
                Thread thread2 = new Thread(updateOrderTask);
                thread1.start();
                thread2.start(); */

            } catch (IllegalArgumentException var13) {System.out.println("Error: " + var13.getMessage());}
            catch (InsufficientStockException var14) {System.out.println("Error: " + var14.getMessage());}
            catch (IncompleteOrderException var15) {System.out.println("Error: " + var15.getMessage());}
            catch (PaymentFailedException var16) {System.out.println("Error: " + var16.getMessage());}

        }catch (AuthorizationException | AuthenticationException var17) {
            System.out.println("Error: " + var17.getMessage());
        }

    }
}