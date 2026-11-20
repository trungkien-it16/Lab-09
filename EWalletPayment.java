/**
 * Thanh toán bằng ví điện tử: kiểm tra số điện thoại, phí 1%, không cần OTP (dùng hook mặc định).
 */
public class EWalletPayment extends PaymentProcessor {
    private final String phone;
    private long balance;

    public EWalletPayment(String phone, long balance) {
        this.phone = phone;
        this.balance = balance;
    }

    @Override
    protected String getMethodName() {
        return "Ví điện tử";
    }

    @Override
    protected boolean validatePaymentInfo(Order order) {
        boolean ok = phone != null && phone.matches("0\\d{9}");
        System.out.println("  - Kiểm tra số điện thoại ví (" + phone + "): " + (ok ? "hợp lệ" : "không hợp lệ"));
        return ok;
    }

    @Override
    protected long calculateFee(Order order) {
        return Math.round(order.getAmount() * 0.01); // 1%
    }

    @Override
    protected boolean charge(Order order, long total) {
        if (balance < total) {
            System.out.println("  - Số dư ví " + format(balance) + " không đủ.");
            return false;
        }
        balance -= total;
        System.out.println("  - Trừ tiền ví thành công, số dư còn lại: " + format(balance));
        return true;
    }
}
