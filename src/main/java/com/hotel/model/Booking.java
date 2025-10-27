package com.hotel.model;

import java.time.LocalDate;


public class Booking {

    private int bookingId;
    private String customerName;
    private String contact;
    private String phoneNumber;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private Integer roomId;
    private Double negotiatedPrice;
    private String status;
    private Room room;

    public Booking() {
        this.status = "Pending";
    }

    public Booking(int bookingId, String customerName, String contact, String phoneNumber,
                   LocalDate checkIn, LocalDate checkOut, Integer roomId, Double negotiatedPrice) {
        this.bookingId = bookingId;
        this.customerName = customerName;
        this.contact = contact;
        this.phoneNumber = phoneNumber;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.roomId = roomId;
        this.negotiatedPrice = negotiatedPrice;
        this.status = "Pending";
    }
    
    public Booking(int bookingId, String customerName, String contact, String phoneNumber,
                   LocalDate checkIn, LocalDate checkOut, Integer roomId, Double negotiatedPrice, String status) {
        this.bookingId = bookingId;
        this.customerName = customerName;
        this.contact = contact;
        this.phoneNumber = phoneNumber;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.roomId = roomId;
        this.negotiatedPrice = negotiatedPrice;
        this.status = status;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(LocalDate checkIn) {
        this.checkIn = checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(LocalDate checkOut) {
        this.checkOut = checkOut;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Double getNegotiatedPrice() {
        return negotiatedPrice;
    }

    public void setNegotiatedPrice(Double negotiatedPrice) {
        this.negotiatedPrice = negotiatedPrice;
    }
    
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "bookingId=" + bookingId +
                ", customerName='" + customerName + '\'' +
                ", contact='" + contact + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", checkIn=" + checkIn +
                ", checkOut=" + checkOut +
                ", roomId=" + roomId +
                ", negotiatedPrice=" + negotiatedPrice +
                ", status='" + status + '\'' +
                '}';
    }

    public long getStayDays() {
        if (checkIn == null || checkOut == null) return 0;
        long days = java.time.temporal.ChronoUnit.DAYS.between(checkIn, checkOut);
        return days > 0 ? days : 0;
    }

    public double getTotalPrice() {
        if (room == null) return 0;
        
        
        if (negotiatedPrice != null) {
            return negotiatedPrice;
        }
        
        long days = getStayDays();
        if (days == 0) days = 1;
        return room.getPrice() * days;
    }
    
    public double getStandardTotalPrice() {
        if (room == null) return 0;
        long days = getStayDays();
        if (days == 0) days = 1;
        return room.getPrice() * days;
    }
}