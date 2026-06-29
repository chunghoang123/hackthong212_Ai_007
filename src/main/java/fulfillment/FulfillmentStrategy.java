package fulfillment;

/**
 * Hợp đồng (contract) chung cho mọi cách hoàn tất 1 loại sản phẩm.
 *
 * Đây là điểm mấu chốt của Open/Closed Principle:
 * - OPEN for extension: thêm 1 loại sản phẩm mới = tạo 1 class implements interface này.
 * - CLOSED for modification: KHÔNG cần sửa OrderFulfillmentService hay vòng lặp cốt lõi.
 */
public interface FulfillmentStrategy {

    /** Mã loại sản phẩm mà strategy này phụ trách, VD: "DIGITAL_COURSE". */
    String getProductType();

    /** Thực thi nghiệp vụ hoàn tất cho 1 item. */
    void fulfill(OrderItem item, User customer);
}
