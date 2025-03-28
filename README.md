```markdown
# Schedule Manage Mobile

## 📌 Giới thiệu
**Schedule Manage Mobile** là ứng dụng quản lý lịch trình cá nhân, giúp người dùng quản lý thời gian và công việc hiệu quả hơn.

## 🚀 Yêu cầu hệ thống
- **Hệ điều hành**: Windows, macOS hoặc Linux
- **Java Development Kit (JDK)**: Phiên bản 8 trở lên
- **Android Studio**: Phiên bản 4.0 trở lên
- **Kết nối Internet**: Để tải các phụ thuộc và plugin cần thiết

## 📥 Hướng dẫn cài đặt và cấu hình

### 1️⃣ Sao chép kho lưu trữ
Mở terminal hoặc command prompt và thực hiện lệnh sau:
```bash
git clone https://github.com/tandat0723/schedulemobileapplication.git
cd schedule-manage-mobile
```

### 2️⃣ Mở dự án bằng Android Studio
- Khởi động **Android Studio**.
- Chọn **"Open an existing project"**.
- Duyệt đến thư mục `schedule-manage-mobile` và nhấp **"OK"**.

### 3️⃣ Cài đặt các phụ thuộc
Android Studio sẽ tự động nhận diện và tải về các phụ thuộc cần thiết thông qua Gradle. Nếu không, bạn có thể đồng bộ hóa dự án bằng cách:
- Nhấp vào **"File"** > **"Sync Project with Gradle Files"**.

### 4️⃣ Cấu hình môi trường (nếu cần)
Nếu dự án yêu cầu các biến môi trường hoặc khóa API, bạn nên:
- Tạo tệp `local.properties` trong thư mục gốc của dự án.
- Thêm các cấu hình cần thiết, ví dụ:
  ```properties
  apiKey=YOUR_API_KEY
  ```

### 5️⃣ Chạy ứng dụng
- Kết nối thiết bị Android hoặc khởi động trình giả lập.
- Nhấp vào nút **"Run"** (hoặc nhấn **Shift + F10**) để biên dịch và chạy ứng dụng.
