import java.io.PrintStream;

public class Main {
        private static int passed = 0;
        private static int total = 0;

        private static void test(String title, PaymentProcessor processor, Order order, boolean expected) {
                System.out.println(">>> " + title);
                boolean actual = processor.processPayment(order);
                total++;
                boolean ok = actual == expected;
                if (ok)
                        passed++;
                System.out.println("    Kết quả mong đợi: " + (expected ? "THÀNH CÔNG" : "THẤT BẠI")
                                + " | Thực tế: " + (actual ? "THÀNH CÔNG" : "THẤT BẠI")
                                + " -> " + (ok ? "ĐẠT" : "KHÔNG ĐẠT") + "\n");
        }

        public static void main(String[] args) throws Exception {
                System.setOut(new PrintStream(System.out, true, "UTF-8"));

                test("TH1: Thẻ tín dụng hợp lệ, OTP đúng",
                                new CreditCardPayment("4111111111111111", "123", 10_000_000, "123456"),
                                new Order("DH001", "Nguyễn Văn An", 2_000_000), true);

                test("TH2: Thẻ tín dụng, nhập sai OTP",
                                new CreditCardPayment("4111111111111111", "123", 10_000_000, "000000"),
                                new Order("DH002", "Nguyễn Văn An", 2_000_000), false);

                test("TH3: Ví điện tử đủ số dư",
                                new EWalletPayment("0905123456", 1_000_000),
                                new Order("DH003", "Trần Thị Bình", 500_000), true);

                test("TH4: Ví điện tử không đủ số dư",
                                new EWalletPayment("0905123456", 100_000),
                                new Order("DH004", "Trần Thị Bình", 500_000), false);

                test("TH5: Chuyển khoản ngân hàng hợp lệ",
                                new BankTransferPayment("Vietcombank", "0123456789", 5_000_000),
                                new Order("DH005", "Lê Minh Cường", 1_200_000), true);

                test("TH6: Đơn hàng có số tiền <= 0 (bước kiểm tra chung)",
                                new EWalletPayment("0905123456", 1_000_000),
                                new Order("DH006", "Lê Minh Cường", 0), false);

                System.out.println("==============================================");
                System.out.println("TỔNG KẾT KIỂM THỬ: " + passed + "/" + total + " trường hợp đạt");
        }
}
