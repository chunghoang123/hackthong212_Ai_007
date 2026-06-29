package fulfillment;

import java.util.List;

/**
 * Demo chạy thử (không cần Spring) để chứng minh code biên dịch & hoạt động.
 * Chạy: javac fulfillment/*.java && java fulfillment.Demo
 */
public class Demo {
    public static void main(String[] args) {

        // 1) Đăng ký toàn bộ strategy. Trong Spring, danh sách này do DI tự bơm vào.
        FulfillmentStrategyRegistry registry = new FulfillmentStrategyRegistry(List.of(
                new DigitalCourseFulfillment(),
                new PhysicalStandardFulfillment(),
                new PhysicalFragileFulfillment(),
                new SubscriptionFulfillment()   // <-- loại MỚI, cắm vào đây là xong
        ));

        OrderFulfillmentService service = new OrderFulfillmentService(registry);

        User customer = new User("student@edumentor.vn");
        Order order = new Order("ORD-1001", PaymentStatus.PAID, List.of(
                new OrderItem("COURSE-SPRING-101", "DIGITAL_COURSE"),
                new OrderItem("BOOK-CLEAN-CODE",   "PHYSICAL_STANDARD"),
                new OrderItem("VASE-CERAMIC",      "PHYSICAL_FRAGILE"),
                new OrderItem("PRO-MONTHLY",       "SUBSCRIPTION_SERVICE")
        ));

        FulfillmentResult result = service.processOrder(order, customer);

        System.out.println("--------------------------------------------------");
        System.out.println("Order status : " + order.getStatus());
        System.out.println("Result       : " + result);
    }
}
