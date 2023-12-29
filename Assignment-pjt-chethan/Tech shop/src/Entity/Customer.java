package Entity;

import Util.DBConnUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class Customer {
    private int customerID;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private int totalOrders;
    private Connection connection;

    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[a-zA-Z0-9_+&-]+(?:\\.[a-zA-Z0-9_+&-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
    }

    public Customer(int customerID, String firstName, String lastName, String email, String phone, String address) {
        if (!this.isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email address. Please provide a valid email.");
        } else {
            this.customerID = customerID;
            this.firstName = firstName;this.lastName = lastName;
            this.email = email;this.phone = phone;
            this.address = address;this.totalOrders = 0;
            this.connection= DBConnUtil.getConnection();
            this.calculateTotalOrders();
            System.out.println("Customer Registration successful!");

            String query = "INSERT INTO customer(customer_id,first_name,last_name,email,phone,address,total_orders)VALUES(?,?,?,?,?,?,?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, customerID);
                preparedStatement.setString(2, firstName);
                preparedStatement.setString(3, lastName);
                preparedStatement.setString(4, email);
                preparedStatement.setString(5, phone);
                preparedStatement.setString(6, address);
                preparedStatement.setInt(7, totalOrders);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println(firstName + " addedd with ID: " + customerID);
                } else {
                    System.out.println("Registration failed.");
                }
            }catch(SQLException e){e.getMessage();}
        }
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }
    public int getCustomerID(){return this.customerID;}

    public void calculateTotalOrders() {
        ++this.totalOrders;
    }

    public void getCustomerDetails() {

        System.out.println("Customer ID: " + this.customerID);
        System.out.println("Name: " + this.firstName + " " + this.lastName);
        System.out.println("Email: " + this.email);
        System.out.println("Phone: " + this.phone);
        System.out.println("Address: " + this.address);
        System.out.println("Total Orders: " + this.totalOrders);
    }

    public void updateCustomerInfo() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Select information to update:");
        System.out.println("1. Email");
        System.out.println("2. Phone");
        System.out.println("3. Address");
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                System.out.println("Enter new email: ");
                this.email = scanner.next();
                break;
            case 2:
                System.out.println("Enter new phone number: ");
                this.phone = scanner.next();
                break;
            case 3:
                System.out.println("Enter new address: ");
                this.address = scanner.next();
                break;
            default:
                System.out.println("Invalid choice");
        }

        System.out.println("Customer information updated successfully!");
    }
}
