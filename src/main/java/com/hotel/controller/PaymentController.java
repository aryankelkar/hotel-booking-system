package com.hotel.controller;

import com.hotel.model.Booking;
import com.hotel.service.BookingService;
import com.hotel.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/bookings")
public class PaymentController {

    private final PaymentService paymentService;
    private final BookingService bookingService;

    @Autowired
    public PaymentController(PaymentService paymentService, BookingService bookingService) {
        this.paymentService = paymentService;
        this.bookingService = bookingService;
    }

    @GetMapping("/pay/{id}")
    public String showPaymentForm(@PathVariable int id, Model model) {
        Booking booking = bookingService.getBookingById(id);
        if (booking == null) {
            return "redirect:/bookings";
        }
        model.addAttribute("booking", booking);
        return "payment";
    }

    @PostMapping("/pay/{id}")
    public String processPayment(@PathVariable int id,
                                 @RequestParam("paymentMethod") String paymentMethod,
                                 @RequestParam(value = "paymentDetails", required = false) String paymentDetails) {
        Booking booking = bookingService.getBookingById(id);
        if (booking == null) {
            return "redirect:/bookings";
        }
        int paymentId = paymentService.processPayment(booking, paymentMethod, paymentDetails != null ? paymentDetails : "");
        if (paymentId > 0) {
            return "redirect:/bookings";
        } else {
            return "redirect:/bookings/pay/" + id + "?error=payment_failed";
        }
    }

    @GetMapping("/invoice/{id}")
    public ResponseEntity<byte[]> downloadInvoice(@PathVariable int id) {
        byte[] data = paymentService.getInvoicePdf(id);
        if (data == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        String fileName = "Invoice_" + id + ".pdf";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", fileName);
        return new ResponseEntity<>(data, headers, HttpStatus.OK);
    }
}