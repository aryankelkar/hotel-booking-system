package com.hotel;

import com.hotel.model.Booking;
import com.hotel.model.Room;
import com.hotel.repository.BookingRepository;
import com.hotel.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.time.LocalDate;
import java.util.Scanner;

/**
 * Console-based application to demonstrate payment integration and invoice generation
 * Run with: mvn spring-boot:run -Dspring-boot.run.profiles=payment-demo
 */
@SpringBootApplication
@Profile("payment-demo")
public class ConsolePaymentDemo {

    public static void main(String[] args) {
        SpringApplication.run(ConsolePaymentDemo.class, args);
    }
    
    @Bean
    public CommandLineRunner demoRunner(PaymentService paymentService, BookingRepository bookingRepository) {
        return args -> {
            Scanner scanner = new Scanner(System.in);
            System.out.println("===== Hotel Booking System - Payment & Invoice Demo =====");
            
            // Create a sample room
            Room room = new Room();
            room.setRoomId(1);
            room.setType("Deluxe");
            room.setPrice(1500.0);
            room.setStatus("Available");
            
            // Create a sample booking
            Booking booking = createSampleBooking(room);
            
            // Save booking to repository
            int bookingId = bookingRepository.save(booking);
            booking.setBookingId(bookingId);
            
            // Display booking details
            displayBookingDetails(booking);
            
            // Process payment
            System.out.println("\nWould you like to proceed with payment? (y/n): ");
            String choice = scanner.nextLine().trim().toLowerCase();
            
            if (choice.equals("y")) {
                // Get payment method
                System.out.println("\nSelect payment method:");
                System.out.println("1. Credit Card");
                System.out.println("2. UPI");
                System.out.println("3. Cash");
                System.out.print("Enter your choice (1-3): ");
                
                int paymentChoice = 0;
                try {
                    paymentChoice = Integer.parseInt(scanner.nextLine().trim());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid choice. Defaulting to Cash payment.");
                    paymentChoice = 3;
                }
                
                String paymentMethod;
                switch (paymentChoice) {
                    case 1:
                        paymentMethod = "Credit Card";
                        break;
                    case 2:
                        paymentMethod = "UPI";
                        break;
                    case 3:
                        paymentMethod = "Cash";
                        break;
                    default:
                        paymentMethod = "Cash";
                        break;
                }
                
                // Get payment details
                System.out.print("Enter " + paymentMethod + " details: ");
                String paymentDetails = scanner.nextLine().trim();
                
                // Process payment
                int paymentId = paymentService.processPayment(booking, paymentMethod, paymentDetails);
                
                if (paymentId > 0) {
                    System.out.println("\nBooking confirmed successfully!");
                    System.out.println("Payment ID: " + paymentId);
                    System.out.println("Payment Status: " + booking.getPaymentStatus());
                    System.out.println("Booking Status: " + booking.getStatus());
                    
                    // Simulate check-out
                    simulateCheckout(scanner, paymentService, booking);
                } else {
                    System.out.println("\nPayment failed. Booking status remains: " + booking.getStatus());
                }
            } else {
                System.out.println("\nPayment cancelled. Booking status remains: " + booking.getStatus());
            }
            
            System.out.println("\nDemo completed. Exiting...");
            scanner.close();
        };
    }
    
    private static Booking createSampleBooking(Room room) {
        Booking booking = new Booking();
        booking.setCustomerName("John Doe");
        booking.setContact("john.doe@example.com");
        booking.setPhoneNumber("9876543210");
        booking.setCheckIn(LocalDate.now());
        booking.setCheckOut(LocalDate.now().plusDays(3));
        booking.setRoomId(room.getRoomId());
        booking.setNegotiatedPrice(room.getPrice() * 3); // 3 days stay
        booking.setRoom(room);
        booking.setStatus("Pending");
        booking.setPaymentStatus("Awaiting Payment");
        return booking;
    }
    
    private static void displayBookingDetails(Booking booking) {
        System.out.println("\n===== Booking Details =====");
        System.out.println("Booking ID: " + booking.getBookingId());
        System.out.println("Customer: " + booking.getCustomerName());
        System.out.println("Contact: " + booking.getContact());
        System.out.println("Phone: " + booking.getPhoneNumber());
        System.out.println("Room: " + booking.getRoomId() + " (" + booking.getRoom().getType() + ")");
        System.out.println("Check-in: " + booking.getCheckIn());
        System.out.println("Check-out: " + booking.getCheckOut());
        System.out.println("Total Amount: $" + booking.getNegotiatedPrice());
        System.out.println("Status: " + booking.getStatus());
    }
    
    private static void simulateCheckout(Scanner scanner, PaymentService paymentService, Booking booking) {
        System.out.println("\n===== Simulating Check-out =====");
        System.out.println("Press Enter to simulate customer check-out...");
        scanner.nextLine();
        
        booking.setStatus("Checked Out");
        System.out.println("Customer has checked out.");
        
        // Generate invoice
        System.out.println("\nGenerating invoice...");
        boolean invoiceGenerated = paymentService.generateInvoice(booking.getBookingId());
        
        if (invoiceGenerated) {
            System.out.println("Invoice generated successfully!");
        } else {
            System.out.println("Failed to generate invoice.");
        }
    }
}