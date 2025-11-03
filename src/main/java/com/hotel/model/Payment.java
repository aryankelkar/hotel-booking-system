package com.hotel.model;

import java.time.LocalDateTime;

/**
 * Payment class to manage payment mode and status for bookings
 */
public class Payment {
    private int paymentId;
    private int bookingId;
    private String paymentMethod; // Credit Card, UPI, Cash
    private String paymentStatus; // Pending, Completed
    private double amount;
    private String transactionDetails;
    private LocalDateTime paymentDate;

    public Payment() {
        this.paymentStatus = "Pending";
    }

    public Payment(int paymentId, int bookingId, String paymentMethod, String paymentStatus, double amount) {
        this.paymentId = paymentId;
        this.bookingId = bookingId;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.amount = amount;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getTransactionDetails() {
        return transactionDetails;
    }

    public void setTransactionDetails(String transactionDetails) {
        this.transactionDetails = transactionDetails;
    }
    
    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    /**
     * Validates payment information based on payment method
     * @return true if payment information is valid, false otherwise
     */
    public boolean validatePaymentInfo() {
        if (this.transactionDetails == null || this.transactionDetails.isEmpty()) {
            return false;
        }

        switch (this.paymentMethod.toLowerCase()) {
            case "credit card":
                return validateCreditCard(this.transactionDetails);
            case "upi":
                return validateUPI(this.transactionDetails);
            case "cash":
                return true; // Cash payments are always valid
            default:
                return false;
        }
    }

    /**
     * Validates credit card number format (simple validation)
     * @param cardNumber the credit card number to validate
     * @return true if the card number format is valid
     */
    private boolean validateCreditCard(String cardNumber) {
        // Simple validation: 16 digits, no spaces
        return cardNumber.replaceAll("\\s", "").matches("\\d{16}");
    }

    /**
     * Validates UPI ID format
     * @param upiId the UPI ID to validate
     * @return true if the UPI ID format is valid
     */
    private boolean validateUPI(String upiId) {
        // Simple validation: username@provider format
        return upiId.matches("[a-zA-Z0-9.]+@[a-zA-Z0-9]+");
    }

    @Override
    public String toString() {
        return "Payment{" +
                "paymentId=" + paymentId +
                ", bookingId=" + bookingId +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", paymentStatus='" + paymentStatus + '\'' +
                ", amount=" + amount +
                '}';
    }
}