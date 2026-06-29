package fulfillment;

/**
 * Service đã được tái cấu trúc.
 *
 * SO SÁNH VỚI BẢN CŨ:
 * - Vòng lặp cốt lõi processOrder() KHÔNG còn chuỗi if/else theo productType.
 * - Logic riêng của từng loại sản phẩm được "ủy quyền" (delegate) cho strategy.
 * - Thêm loại "SUBSCRIPTION_SERVICE": chỉ cần tạo class SubscriptionFulfillment
 *   implements FulfillmentStrategy và đăng ký vào registry. KHÔNG đụng file này.
 *   => Tuân thủ Open/Closed Principle.
 */
public class OrderFulfillmentService {

    private final FulfillmentStrategyRegistry strategyRegistry;

    public OrderFulfillmentService(FulfillmentStrategyRegistry strategyRegistry) {
        this.strategyRegistry = strategyRegistry;
    }

    public FulfillmentResult processOrder(Order order, User customer) {
        if (order.getPaymentStatus() != PaymentStatus.PAID) {
            throw new IllegalStateException("Order is not paid yet");
        }

        // Vòng lặp cốt lõi — ỔN ĐỊNH, không thay đổi khi có loại sản phẩm mới.
        for (OrderItem item : order.getItems()) {
            FulfillmentStrategy strategy = strategyRegistry.resolve(item.getProductType());
            strategy.fulfill(item, customer);
        }

        order.setStatus("FULFILLED");
        return new FulfillmentResult(order.getId(), "SUCCESS");
    }
}
