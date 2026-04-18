package iuh.fit.se;

// 1. Interface chung mà hệ thống của bạn sẽ giao tiếp
interface PaymentStrategy {
    boolean processPayment(double amount);
}

// ---------------------------------------------------------
// 2. Các lớp API bên ngoài (Mô phỏng sự khác biệt về API)
class ExternalMomoAPI {
    public void callMomoEndpoint(String jsonData) {
        System.out.println("Giao tiếp với API Momo bằng JSON: " + jsonData);
    }
}

class ExternalVisaAPI {
    public void sendSoapRequest(String xmlData) {
        System.out.println("Giao tiếp với cổng Visa bằng SOAP XML: " + xmlData);
    }
}

// ---------------------------------------------------------
// 3. Các Concrete Strategy (Đóng vai trò như các Adapter)

// Cổng thanh toán Momo
class MomoPaymentStrategy implements PaymentStrategy {
    private ExternalMomoAPI momoApi;

    public MomoPaymentStrategy() {
        this.momoApi = new ExternalMomoAPI();
    }

    @Override
    public boolean processPayment(double amount) {
        System.out.println("\n--- Xử lý thanh toán qua Momo ---");
        // Chuyển đổi dữ liệu chuẩn của hệ thống thành định dạng Momo cần
        String jsonPayload = "{ \"amount\": " + amount + ", \"currency\": \"VND\" }";
        momoApi.callMomoEndpoint(jsonPayload);
        return true; // Giả lập thanh toán thành công
    }
}

// Cổng thanh toán Thẻ Tín Dụng (Visa)
class VisaPaymentStrategy implements PaymentStrategy {
    private ExternalVisaAPI visaApi;

    public VisaPaymentStrategy() {
        this.visaApi = new ExternalVisaAPI();
    }

    @Override
    public boolean processPayment(double amount) {
        System.out.println("\n--- Xử lý thanh toán qua Visa ---");
        // Chuyển đổi dữ liệu chuẩn của hệ thống thành định dạng Visa cần
        String xmlPayload = "<Payment><Amount>" + amount + "</Amount><Type>Credit</Type></Payment>";
        visaApi.sendSoapRequest(xmlPayload);
        return true;
    }
}

// Cổng thanh toán Ví nội bộ (Hệ thống tự xây dựng)
class InternalWalletStrategy implements PaymentStrategy {
    @Override
    public boolean processPayment(double amount) {
        System.out.println("\n--- Xử lý thanh toán qua Ví nội bộ ---");
        System.out.println("Trừ " + amount + " VND trực tiếp từ số dư tài khoản hệ thống.");
        return true;
    }
}

// ---------------------------------------------------------
// 4. Lớp Context: Thành phần chính của hệ thống sử dụng thanh toán
class PaymentProcessor {
    private PaymentStrategy paymentStrategy;

    // Cho phép thay đổi chiến lược thanh toán linh hoạt lúc runtime
    public void setPaymentStrategy(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }

    // Phương thức thực thi logic chính
    public void executePayment(double amount) {
        if (paymentStrategy == null) {
            System.out.println("Vui lòng chọn phương thức thanh toán!");
            return;
        }
        
        boolean isSuccess = paymentStrategy.processPayment(amount);
        if (isSuccess) {
            System.out.println("Thanh toán đơn hàng thành công!");
        } else {
            System.out.println("Thanh toán thất bại, vui lòng thử lại.");
        }
    }
}

// ---------------------------------------------------------
// 5. Chương trình chạy thử (Client) - File Main
public class Main {
    public static void main(String[] args) {
        PaymentProcessor processor = new PaymentProcessor();
        double orderAmount = 500000.0;

        // Người dùng chọn thanh toán bằng Momo
        processor.setPaymentStrategy(new MomoPaymentStrategy());
        processor.executePayment(orderAmount);

        // Người dùng đổi ý, chọn thanh toán bằng Visa
        processor.setPaymentStrategy(new VisaPaymentStrategy());
        processor.executePayment(orderAmount);

        // Người dùng chọn thanh toán bằng Ví nội bộ
        processor.setPaymentStrategy(new InternalWalletStrategy());
        processor.executePayment(orderAmount);
    }
}