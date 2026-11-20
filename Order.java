public class Order {
    private final String orderId;
    private final String customerName;
    private final long amount; // số tiền (VND)

    public Order(String orderId, String customerName, long amount) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.amount = amount;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public long getAmount() {
        return amount;
    }
}
