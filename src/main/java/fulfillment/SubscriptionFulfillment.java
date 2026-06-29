package fulfillment;

/**
 * BẰNG CHỨNG mở rộng theo Open/Closed Principle.
 *
 * Đây là loại sản phẩm MỚI (SUBSCRIPTION_SERVICE) được thêm vào hệ thống.
 * Lưu ý: để tích hợp nó, ta KHÔNG sửa 1 dòng nào trong:
 *   - OrderFulfillmentService
 *   - FulfillmentStrategyRegistry
 *   - Các strategy cũ
 * Chỉ cần tạo class này và đăng ký nó (xem Demo.java).
 */
class SubscriptionFulfillment implements FulfillmentStrategy {
    @Override public String getProductType() { return "SUBSCRIPTION_SERVICE"; }

    @Override public void fulfill(OrderItem item, User customer) {
        System.out.println("Activating subscription plan: " + item.getProductId());
        System.out.println("Scheduling recurring billing cycle for " + customer.getEmail());
        System.out.println("Sending welcome onboarding email...");
    }
}
