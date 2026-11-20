
public class BankTransferPayment extends PaymentProcessor {
    private static final long FIXED_FEE = 3_300;

    private final String bankName;
    private final String accountNumber;
    private final long balance;

    public BankTransferPayment(String bankName, String accountNumber, long balance) {
        this.bankName = bankName;
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    @Override
    protected String getMethodName() {
        return "Chuyển khoản " + bankName;
    }

    @Override
    protected boolean validatePaymentInfo(Order order) {
        boolean ok = accountNumber != null && accountNumber.matches("\\d{9,14}");
        System.out.println("  - Kiểm tra số tài khoản (9-14 số): " + (ok ? "hợp lệ" : "không hợp lệ"));
        return ok;
    }

    @Override
    protected long calculateFee(Order order) {
        return FIXED_FEE; // phí cố định
    }

    @Override
    protected boolean requiresOtp() {
        return true; // dùng OTP mặc định (hook verifyOtp không bị ghi đè)
    }

    @Override
    protected boolean charge(Order order, long total) {
        boolean ok = balance >= total;
        System.out.println("  - Chuyển " + format(total) + " từ TK " + accountNumber + ": "
                + (ok ? "thành công" : "số dư không đủ"));
        return ok;
    }
}
