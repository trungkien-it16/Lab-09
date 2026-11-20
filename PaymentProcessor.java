public abstract class PaymentProcessor {

    public final boolean processPayment(Order order) {
        System.out.println("==============================================");
        System.out.println("[" + getMethodName() + "] Bắt đầu xử lý đơn " + order.getOrderId());

        if (!validateOrder(order)) {
            System.out.println("=> THẤT BẠI: Đơn hàng không hợp lệ.\n");
            return false;
        }

        if (!validatePaymentInfo(order)) {
            System.out.println("=> THẤT BẠI: Thông tin thanh toán không hợp lệ.\n");
            return false;
        }

        long fee = calculateFee(order);
        long total = order.getAmount() + fee;
        System.out.println("  - Phí giao dịch: " + format(fee) + " | Tổng thanh toán: " + format(total));

        if (requiresOtp() && !verifyOtp(order)) {
            System.out.println("=> THẤT BẠI: Xác thực OTP không thành công.\n");
            return false;
        }

        if (!charge(order, total)) {
            System.out.println("=> THẤT BẠI: Không thể trừ tiền (không đủ số dư / giao dịch bị từ chối).\n");
            return false;
        }

        logTransaction(order, total);
        sendReceipt(order, total);
        System.out.println("=> THÀNH CÔNG\n");
        return true;
    }

    protected boolean validateOrder(Order order) {
        boolean ok = order != null
                && order.getOrderId() != null && !order.getOrderId().isEmpty()
                && order.getAmount() > 0;
        System.out.println("  - Kiểm tra đơn hàng: " + (ok ? "hợp lệ" : "không hợp lệ"));
        return ok;
    }

    protected void logTransaction(Order order, long total) {
        System.out.println("  - Ghi log: đơn " + order.getOrderId()
                + ", qua " + getMethodName() + ", tổng " + format(total));
    }

    protected void sendReceipt(Order order, long total) {
        System.out.println("  - Gửi biên lai cho khách hàng \"" + order.getCustomerName()
                + "\" (" + format(total) + ")");
    }

    protected abstract String getMethodName();

    protected abstract boolean validatePaymentInfo(Order order);

    protected abstract long calculateFee(Order order);

    protected abstract boolean charge(Order order, long total);

    protected boolean requiresOtp() {
        return false;
    }

    protected boolean verifyOtp(Order order) {
        System.out.println("  - Xác thực OTP: thành công");
        return true;
    }

    protected static String format(long money) {
        return String.format("%,d VND", money);
    }
}
