package iuh.fit.se;


//Dưới đây là 3 lý do chọn State Pattern được tóm tắt ngắn gọn:

//Hành vi linh hoạt theo trạng thái: Đáp ứng đúng yêu cầu thay đổi hành vi
//của đơn hàng tùy thuộc vào giai đoạn hiện tại.

//Kiểm soát luồng chặt chẽ: Tách riêng từng trạng thái thành các lớp (class)
////độc lập để dễ dàng quản lý và chặn các thao tác sai logic.

//Dễ bảo trì và mở rộng: Dễ dàng thêm giai đoạn mới mà không cần sửa code cũ,
//loại bỏ triệt để các khối if-else phức tạp.


//@startuml
//skinparam classAttributeIconSize 0
//
//        ' Lớp Context (Đơn hàng)
//class Order {
//    - state: OrderState
//    + Order()
//    + setState(state: OrderState): void
//    + confirmOrder(): void
//    + prepareOrder(): void
//    + deliverOrder(): void
//    + completeOrder(): void
//    + cancelOrder(): void
//}
//
//' Interface định nghĩa hành vi chung cho các trạng thái
//interface OrderState {
//    + confirmOrder(order: Order): void
//    + prepareOrder(order: Order): void
//    + deliverOrder(order: Order): void
//    + completeOrder(order: Order): void
//    + cancelOrder(order: Order): void
//}
//
//' Các lớp trạng thái cụ thể (Concrete States)
//class CreatedState {
//    + confirmOrder(order: Order): void
//    + prepareOrder(order: Order): void
//    + deliverOrder(order: Order): void
//    + completeOrder(order: Order): void
//    + cancelOrder(order: Order): void
//}
//
//class ConfirmedState {
//    + confirmOrder(order: Order): void
//    + prepareOrder(order: Order): void
//    + deliverOrder(order: Order): void
//    + completeOrder(order: Order): void
//    + cancelOrder(order: Order): void
//}
//
//class PreparingState {
//    + confirmOrder(order: Order): void
//    + prepareOrder(order: Order): void
//    + deliverOrder(order: Order): void
//    + completeOrder(order: Order): void
//    + cancelOrder(order: Order): void
//}
//
//class DeliveringState {
//    + confirmOrder(order: Order): void
//    + prepareOrder(order: Order): void
//    + deliverOrder(order: Order): void
//    + completeOrder(order: Order): void
//    + cancelOrder(order: Order): void
//}
//
//class CompletedState {
//    + confirmOrder(order: Order): void
//    + prepareOrder(order: Order): void
//    + deliverOrder(order: Order): void
//    + completeOrder(order: Order): void
//    + cancelOrder(order: Order): void
//}
//
//class CancelledState {
//    + confirmOrder(order: Order): void
//    + prepareOrder(order: Order): void
//    + deliverOrder(order: Order): void
//    + completeOrder(order: Order): void
//    + cancelOrder(order: Order): void
//}
//
//' Quan hệ giữa các thành phần
//Order o-> OrderState : -state
//OrderState <|.. CreatedState
//OrderState <|.. ConfirmedState
//OrderState <|.. PreparingState
//OrderState <|.. DeliveringState
//OrderState <|.. CompletedState
//OrderState <|.. CancelledState
//
//note right of Order
//Context giữ state hiện tại
//và ủy quyền (delegate)
//các hành vi cho State
//end note
//@enduml

// 1. State Interface: Định nghĩa các hành vi có thể có của đơn hàng
interface OrderState {
    void confirmOrder(Order order);
    void prepareOrder(Order order);
    void deliverOrder(Order order);
    void completeOrder(Order order);
    void cancelOrder(Order order);
}

// -------------------------------------------------------------
// 2. Concrete States: Các trạng thái cụ thể của đơn hàng

class CreatedState implements OrderState {
    @Override
    public void confirmOrder(Order order) {
        System.out.println("Nhà hàng đã xác nhận đơn hàng.");
        order.setState(new ConfirmedState()); // Chuyển sang trạng thái Confirmed
    }
    @Override
    public void prepareOrder(Order order) { System.out.println("Lỗi: Phải xác nhận đơn trước khi chuẩn bị!"); }
    @Override
    public void deliverOrder(Order order) { System.out.println("Lỗi: Đơn chưa xong, không thể giao!"); }
    @Override
    public void completeOrder(Order order) { System.out.println("Lỗi: Đơn chưa giao, không thể hoàn thành!"); }
    @Override
    public void cancelOrder(Order order) {
        System.out.println("Đơn hàng đã bị người dùng hủy.");
        order.setState(new CancelledState());
    }
}

class ConfirmedState implements OrderState {
    @Override
    public void confirmOrder(Order order) { System.out.println("Đơn hàng đã được xác nhận rồi."); }
    @Override
    public void prepareOrder(Order order) {
        System.out.println("Nhà hàng đang chuẩn bị món ăn...");
        order.setState(new PreparingState()); // Chuyển sang trạng thái Preparing
    }
    @Override
    public void deliverOrder(Order order) { System.out.println("Lỗi: Món ăn chưa làm xong!"); }
    @Override
    public void completeOrder(Order order) { System.out.println("Lỗi: Chưa giao hàng!"); }
    @Override
    public void cancelOrder(Order order) {
        System.out.println("Đơn hàng đã bị hủy bởi nhà hàng/khách hàng.");
        order.setState(new CancelledState());
    }
}

class PreparingState implements OrderState {
    @Override
    public void confirmOrder(Order order) { System.out.println("Lỗi: Đơn đang chuẩn bị rồi."); }
    @Override
    public void prepareOrder(Order order) { System.out.println("Đang chuẩn bị rồi."); }
    @Override
    public void deliverOrder(Order order) {
        System.out.println("Đã nấu xong. Tài xế đang đi giao...");
        order.setState(new DeliveringState()); // Chuyển sang Delivering
    }
    @Override
    public void completeOrder(Order order) { System.out.println("Lỗi: Chưa giao hàng!"); }
    @Override
    public void cancelOrder(Order order) {
        System.out.println("Đơn hàng bị hủy trong lúc chuẩn bị (có thể bị tính phí).");
        order.setState(new CancelledState());
    }
}

class DeliveringState implements OrderState {
    @Override
    public void confirmOrder(Order order) { System.out.println("Lỗi: Đang giao hàng."); }
    @Override
    public void prepareOrder(Order order) { System.out.println("Lỗi: Đang giao hàng."); }
    @Override
    public void deliverOrder(Order order) { System.out.println("Đang trên đường giao..."); }
    @Override
    public void completeOrder(Order order) {
        System.out.println("Đơn hàng đã được giao thành công cho khách!");
        order.setState(new CompletedState()); // Chuyển sang Completed
    }
    @Override
    public void cancelOrder(Order order) {
        System.out.println("Giao hàng thất bại. Đơn hàng bị hủy.");
        order.setState(new CancelledState());
    }
}

class CompletedState implements OrderState {
    @Override
    public void confirmOrder(Order order) { System.out.println("Đơn đã hoàn thành, không thể thao tác."); }
    @Override
    public void prepareOrder(Order order) { System.out.println("Đơn đã hoàn thành, không thể thao tác."); }
    @Override
    public void deliverOrder(Order order) { System.out.println("Đơn đã hoàn thành, không thể thao tác."); }
    @Override
    public void completeOrder(Order order) { System.out.println("Đơn đã hoàn thành."); }
    @Override
    public void cancelOrder(Order order) { System.out.println("Lỗi: Không thể hủy đơn đã giao thành công."); }
}

class CancelledState implements OrderState {
    @Override
    public void confirmOrder(Order order) { System.out.println("Đơn đã hủy, không thể thao tác."); }
    @Override
    public void prepareOrder(Order order) { System.out.println("Đơn đã hủy, không thể thao tác."); }
    @Override
    public void deliverOrder(Order order) { System.out.println("Đơn đã hủy, không thể thao tác."); }
    @Override
    public void completeOrder(Order order) { System.out.println("Đơn đã hủy, không thể thao tác."); }
    @Override
    public void cancelOrder(Order order) { System.out.println("Đơn đã bị hủy rồi."); }
}

// -------------------------------------------------------------
// 3. Context: Lớp Đơn hàng chứa trạng thái hiện tại

class Order {
    private OrderState state;

    public Order() {
        // Trạng thái ban đầu khi vừa tạo đơn
        this.state = new CreatedState();
        System.out.println("--- Đơn hàng mới đã được tạo ---");
    }

    public void setState(OrderState state) {
        this.state = state;
    }

    // Các hàm ủy quyền (delegate) xử lý cho State hiện tại
    public void confirmOrder() { state.confirmOrder(this); }
    public void prepareOrder() { state.prepareOrder(this); }
    public void deliverOrder() { state.deliverOrder(this); }
    public void completeOrder() { state.completeOrder(this); }
    public void cancelOrder()  { state.cancelOrder(this); }
}

// -------------------------------------------------------------
// 4. Main Class: Chạy thử nghiệp vụ

public class Main {
    public static void main(String[] args) {
        // Kịch bản 1: Luồng đi chuẩn (Happy Path)
        System.out.println("KỊCH BẢN 1: GIAO THÀNH CÔNG");
        Order order1 = new Order();
        order1.confirmOrder();   // Chuyển sang Confirmed
        order1.prepareOrder();   // Chuyển sang Preparing
        order1.deliverOrder();   // Chuyển sang Delivering
        order1.completeOrder();  // Chuyển sang Completed
        order1.cancelOrder();    // Cố tình hủy khi đã xong -> Lỗi

        System.out.println("\n-------------------------------\n");

        // Kịch bản 2: Luồng lỗi / Hủy đơn
        System.out.println("KỊCH BẢN 2: HỦY ĐƠN VÀ THAO TÁC SAI");
        Order order2 = new Order();
        order2.prepareOrder();   // Cố tình yêu cầu chuẩn bị khi chưa confirm -> Lỗi
        order2.confirmOrder();   // Chuyển sang Confirmed
        order2.cancelOrder();    // Khách hủy đơn -> Chuyển sang Cancelled
        order2.deliverOrder();   // Cố tình giao khi đã hủy -> Lỗi
    }
}