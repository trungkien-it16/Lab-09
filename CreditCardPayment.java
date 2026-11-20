
public class CreditCardPayment extends PaymentProcessor {
    private final String cardNumber;
    private final String cvv;
    private final long creditLimit;
    private final String otpInput;

    public CreditCardPayment(String cardNumber, String cvv, long creditLimit, String otpInput) {
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.creditLimit = creditLimit;
        this.otpInput = otpInput;
    }

    @Override
    protected String getMethodName() {
        return "Thẻ tín dụng";
    }

    @Override
    protected boolean validatePaymentInfo(Order order) {
        boolean ok = cardNumber != null && cardNumber.matches("\\d{16}")
                && cvv != null && cvv.matches("\\d{3}");
        System.out.println("  - Kiểm tra thẻ (16 số, CVV 3 số): " + (ok ? "hợp lệ" : "không hợp lệ"));
        return ok;
    }

    @Override
    protected long calculateFee(Order order) {
        return Math.round(order.getAmount() * 0.025); // 2,5%
    }

    @Override
    protected boolean requiresOtp() {
        return true; // ghi đè hook
    }

    @Override
    protected boolean verifyOtp(Order order) {
        boolean ok = "123456".equals(otpInput); // OTP giả lập
        System.out.println("  - Xác thực OTP: " + (ok ? "thành công" : "sai mã OTP"));
        return ok;
    }

    @Override
    protected boolean charge(Order order, long total) {
        boolean ok = total <= creditLimit;
        System.out.println("  - Trừ tiền từ thẻ ****" + cardNumber.substring(12)
                + " (hạn mức " + format(creditLimit) + "): " + (ok ? "thành công" : "vượt hạn mức"));
        return ok;
    }
}
