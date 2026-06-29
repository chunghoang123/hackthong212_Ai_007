package fulfillment;

import java.util.List;

/**
 * Các lớp domain hỗ trợ (gộp trong 1 file cho gọn phần demo).
 * Trong dự án thật mỗi class nên nằm 1 file riêng.
 */

enum PaymentStatus { PAID, PENDING, FAILED }

class User {
    private final String email;
    public User(String email) { this.email = email; }
    public String getEmail() { return email; }
}

class OrderItem {
    private final String productId;
    private final String productType; // VD: DIGITAL_COURSE, PHYSICAL_STANDARD...
    public OrderItem(String productId, String productType) {
        this.productId = productId;
        this.productType = productType;
    }
    public String getProductId() { return productId; }
    public String getProductType() { return productType; }
}

class Order {
    private final String id;
    private final PaymentStatus paymentStatus;
    private final List<OrderItem> items;
    private String status = "PENDING";
    public Order(String id, PaymentStatus paymentStatus, List<OrderItem> items) {
        this.id = id;
        this.paymentStatus = paymentStatus;
        this.items = items;
    }
    public String getId() { return id; }
    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public List<OrderItem> getItems() { return items; }
    public void setStatus(String status) { this.status = status; }
    public String getStatus() { return status; }
}

class FulfillmentResult {
    private final String orderId;
    private final String status;
    public FulfillmentResult(String orderId, String status) {
        this.orderId = orderId;
        this.status = status;
    }
    public String getOrderId() { return orderId; }
    public String getStatus() { return status; }
    @Override public String toString() {
        return "FulfillmentResult{orderId='" + getOrderId() + "', status='" + getStatus() + "'}";
    }
}
