package com.hotel.service;

import com.hotel.model.Booking;
import com.hotel.model.Payment;
import com.hotel.repository.BookingRepository;
import com.hotel.repository.PaymentRepository;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Scanner;

/**
 * Service class for handling payment processing and invoice generation
 */
@Service
public class PaymentService {
    private Scanner scanner;
    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository, BookingRepository bookingRepository) {
        this.scanner = new Scanner(System.in);
        this.paymentRepository = paymentRepository;
        this.bookingRepository = bookingRepository;
    }
    
    /**
     * Get all payments
     * @return list of all payments
     */
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
    
    /**
     * Get payment by ID
     * @param id payment ID
     * @return payment object
     */
    public Payment getPaymentById(int id) {
        return paymentRepository.findById(id);
    }
    
    /**
     * Get payments by booking ID
     * @param bookingId booking ID
     * @return list of payments for the booking
     */
    public List<Payment> getPaymentsByBookingId(int bookingId) {
        return paymentRepository.findByBookingId(bookingId);
    }

    /**
     * Process payment for a booking
     * @param booking the booking to process payment for
     * @param paymentMethod the payment method (Credit Card, UPI, Cash)
     * @param paymentDetails the payment details (card number, UPI ID, etc.)
     * @return payment ID if successful, -1 otherwise
     */
    public int processPayment(Booking booking, String paymentMethod, String paymentDetails) {
        System.out.println("\n===== PAYMENT PROCESSING =====");
        System.out.println("Booking ID: " + booking.getBookingId());
        System.out.println("Customer: " + booking.getCustomerName());
        System.out.println("Amount to pay: ₹" + booking.getTotalPrice());
        System.out.println("Payment Method: " + paymentMethod);
        
        // Create payment object
        Payment payment = new Payment();
        payment.setBookingId(booking.getBookingId());
        payment.setAmount(booking.getTotalPrice());
        payment.setPaymentMethod(paymentMethod);
        payment.setTransactionDetails(paymentDetails);
        payment.setPaymentDate(LocalDateTime.now());
        
        // Validate payment information
        if (!payment.validatePaymentInfo()) {
            System.out.println("Invalid payment information. Payment failed.");
            return -1;
        }
        
        // Process payment (simulated)
        System.out.println("Processing payment...");
        try {
            Thread.sleep(1000); // Simulate processing time
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Update payment status
        payment.setPaymentStatus("Completed");
        
        // Save payment to database
        int paymentId = paymentRepository.save(payment);
        
        // Update booking with payment information
        booking.setPaymentMethod(paymentMethod);
        booking.setPaymentStatus("Completed");
        booking.setStatus("Confirmed");
        
        // Save updated booking
        bookingRepository.save(booking);
        
        System.out.println("Payment processed successfully!");
        System.out.println("Booking status updated to: " + booking.getStatus());
        
        return paymentId;
    }
    
    /**
     * Generate invoice for a booking
     * @param bookingId the booking ID to generate invoice for
     * @return true if invoice was generated successfully, false otherwise
     */
    public boolean generateInvoice(int bookingId) {
        System.out.println("\n===== GENERATING INVOICE =====");
        
        // Get booking from repository
        Booking booking = bookingRepository.findById(bookingId);
        if (booking == null) {
            System.out.println("Booking not found with ID: " + bookingId);
            return false;
        }
        
        if (!"Completed".equals(booking.getPaymentStatus())) {
            System.out.println("Cannot generate invoice. Payment is not completed.");
            return false;
        }
        
        String fileName = "Invoice_" + booking.getBookingId() + ".pdf";
        
        try {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();
            
            // Add title
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph title = new Paragraph("INVOICE", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(Chunk.NEWLINE);
            
            // Add invoice details
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
            Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            
            // Invoice generation details
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            document.add(new Paragraph("Invoice Date: " + now.format(formatter), normalFont));
            document.add(new Paragraph("Invoice Number: INV-" + booking.getBookingId() + "-" + now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")), normalFont));
            document.add(Chunk.NEWLINE);
            
            // Customer details
            document.add(new Paragraph("CUSTOMER DETAILS", boldFont));
            document.add(new Paragraph("Name: " + booking.getCustomerName(), normalFont));
            document.add(new Paragraph("Contact: " + booking.getContact(), normalFont));
            document.add(new Paragraph("Phone: " + booking.getPhoneNumber(), normalFont));
            document.add(Chunk.NEWLINE);
            
            // Booking details
            document.add(new Paragraph("BOOKING DETAILS", boldFont));
            document.add(new Paragraph("Booking ID: " + booking.getBookingId(), normalFont));
            document.add(new Paragraph("Room Type: " + booking.getRoom().getType(), normalFont));
            document.add(new Paragraph("Check-in Date: " + booking.getCheckIn(), normalFont));
            document.add(new Paragraph("Check-out Date: " + booking.getCheckOut(), normalFont));
            
            // Calculate stay duration
            long days = ChronoUnit.DAYS.between(booking.getCheckIn(), booking.getCheckOut());
            document.add(new Paragraph("Stay Duration: " + days + " days", normalFont));
            document.add(Chunk.NEWLINE);
            
            // Payment details
            document.add(new Paragraph("PAYMENT DETAILS", boldFont));
            document.add(new Paragraph("Payment Method: " + booking.getPaymentMethod(), normalFont));
            document.add(new Paragraph("Payment Status: " + booking.getPaymentStatus(), normalFont));
            document.add(new Paragraph("Total Amount: ₹" + booking.getTotalPrice(), normalFont));
            document.add(Chunk.NEWLINE);
            
            // Footer
            document.add(new Paragraph("Thank you for choosing our hotel!", boldFont));
            
            document.close();
            
            System.out.println("Invoice generated successfully: " + fileName);
            return true;
            
        } catch (DocumentException | IOException e) {
            System.out.println("Error generating invoice: " + e.getMessage());
            return false;
        }
    }

    /**
     * Return invoice PDF bytes for a booking. Generates the invoice if missing.
     * @param bookingId booking ID
     * @return PDF file bytes, or null if generation/read fails
     */
    public byte[] getInvoicePdf(int bookingId) {
        String fileName = "Invoice_" + bookingId + ".pdf";
        java.nio.file.Path path = java.nio.file.Paths.get(fileName);
        try {
            if (!java.nio.file.Files.exists(path)) {
                boolean ok = generateInvoice(bookingId);
                if (!ok) return null;
            }
            return java.nio.file.Files.readAllBytes(path);
        } catch (IOException e) {
            return null;
        }
    }
}