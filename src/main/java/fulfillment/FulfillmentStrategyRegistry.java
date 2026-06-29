package fulfillment;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Registry ánh xạ productType -> FulfillmentStrategy.
 *
 * Trong môi trường Spring, ta KHÔNG cần new thủ công: chỉ cần khai báo
 * mỗi strategy là một @Component, rồi Spring inject sẵn List<FulfillmentStrategy>
 * vào constructor. Việc thêm 1 loại sản phẩm mới hoàn toàn "vô hình" với registry.
 *
 * Ví dụ phiên bản Spring:
 *
 *   @Component
 *   public class FulfillmentStrategyRegistry {
 *       private final Map<String, FulfillmentStrategy> registry;
 *       public FulfillmentStrategyRegistry(List<FulfillmentStrategy> strategies) {
 *           this.registry = strategies.stream()
 *               .collect(Collectors.toMap(FulfillmentStrategy::getProductType, s -> s));
 *       }
 *       ...
 *   }
 *
 * Ở đây để chạy được không cần Spring, ta nhận List qua constructor.
 */
public class FulfillmentStrategyRegistry {

    private final Map<String, FulfillmentStrategy> registry;

    public FulfillmentStrategyRegistry(List<FulfillmentStrategy> strategies) {
        // Nếu có 2 strategy trùng productType -> ném lỗi ngay khi khởi tạo (fail-fast)
        this.registry = strategies.stream()
                .collect(Collectors.toMap(
                        FulfillmentStrategy::getProductType,
                        Function.identity(),
                        (a, b) -> {
                            throw new IllegalStateException(
                                "Duplicate strategy for type: " + a.getProductType());
                        }));
    }

    public FulfillmentStrategy resolve(String productType) {
        FulfillmentStrategy strategy = registry.get(productType);
        if (strategy == null) {
            throw new IllegalArgumentException(
                "Product type not supported for fulfillment: " + productType);
        }
        return strategy;
    }
}
