package fulfillment;

/**
 * Các strategy cụ thể cho từng loại sản phẩm hiện có.
 * Mỗi class chỉ chịu trách nhiệm cho ĐÚNG 1 loại (Single Responsibility).
 */

class DigitalCourseFulfillment implements FulfillmentStrategy {
    @Override public String getProductType() { return "DIGITAL_COURSE"; }

    @Override public void fulfill(OrderItem item, User customer) {
        System.out.println("Granting access to course ID: " + item.getProductId());
        System.out.println("Sending email with login credentials to " + customer.getEmail());
    }
}

class PhysicalStandardFulfillment implements FulfillmentStrategy {
    @Override public String getProductType() { return "PHYSICAL_STANDARD"; }

    @Override public void fulfill(OrderItem item, User customer) {
        System.out.println("Generating shipping label for standard delivery...");
        System.out.println("Deducting inventory in warehouse for item: " + item.getProductId());
    }
}

class PhysicalFragileFulfillment implements FulfillmentStrategy {
    @Override public String getProductType() { return "PHYSICAL_FRAGILE"; }

    @Override public void fulfill(OrderItem item, User customer) {
        System.out.println("Applying special bubble wrap packaging...");
        System.out.println("Booking specialized fragile courier for item: " + item.getProductId());
        System.out.println("Adding extra shipping insurance...");
    }
}
