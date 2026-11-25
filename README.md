# Quản lý phòng trọ

Ứng dụng quản lý phòng trọ xây dựng bằng Java 21, Spring Boot, Maven và MySQL.

## Yêu cầu

- JDK 21 (VS Code cần trỏ Java runtime đến JDK 21).
- VS Code với Extension Pack for Java và Spring Boot Extension Pack.
- MySQL Server 8.x đang chạy trên máy. Có thể cài MySQL Community Server và MySQL Workbench; không cần Docker.

Maven đã được đóng gói qua Maven Wrapper; không cần cài Maven riêng.

## Clone và cấu hình

1. Clone repository và mở thư mục dự án trong VS Code.
2. Tạo file cấu hình cá nhân từ mẫu:

   ```powershell
   Copy-Item .env.example .env
   ```

   Trên macOS/Linux dùng `cp .env.example .env`. Chỉnh mật khẩu và thông tin thanh toán trong `.env` nếu cần. File `.env` không được đưa lên Git.

3. Khởi tạo database và tài khoản ứng dụng trong MySQL Workbench:

   - Mở kết nối MySQL local bằng tài khoản quản trị (thường là `root`).
   - Mở và chạy toàn bộ file `setup-local-mysql.sql`. Script tạo database `quan_ly_phong_tro` và user `qlytro` với mật khẩu local mẫu khớp `.env`.
   - Mở và chạy toàn bộ file `qlytro.sql` để tạo các bảng.

   Nếu đặt mật khẩu khác trong `setup-local-mysql.sql`, cập nhật `DB_PASSWORD` trong `.env` cho trùng khớp.

   Có thể thay MySQL Workbench bằng MySQL CLI. Chạy script tài khoản trước, sau đó import schema:

   ```shell
   mysql -u root -p < setup-local-mysql.sql
   mysql -u root -p < qlytro.sql
   ```

   Các lệnh trên sẽ hỏi mật khẩu quản trị MySQL. MySQL phải đang chạy và lắng nghe cổng trong `DB_URL` (mặc định `3306`).

## Chạy trong VS Code

- Chọn **Run and Debug → QuanLyPhongTroApplication** để chạy/debug.
- Hoặc mở terminal tại thư mục dự án:

  ```powershell
  .\mvnw.cmd spring-boot:run
  ```

  Trên macOS/Linux:

  ```shell
  ./mvnw spring-boot:run
  ```

Mở <http://localhost:8080>. Tài khoản khởi tạo lần đầu là `admin`; mật khẩu lấy từ `APP_ADMIN_INITIAL_PASSWORD` trong `.env` (mặc định `admin@123`). Hãy đổi mật khẩu mặc định trước khi dùng môi trường có dữ liệu thật.

`docker-compose.yml` vẫn có thể dùng nếu máy đã cài Docker, nhưng đây là tùy chọn; quy trình mặc định ở trên dùng MySQL cài trực tiếp.

## Cấu hình

- `src/main/resources/application.properties` chứa cấu hình chung, không chứa mật khẩu cá nhân.
- `.env.example` là mẫu biến môi trường; tạo `.env` riêng cho từng máy.
- Ảnh tải lên được lưu trong `uploads/` và không được commit.
- Hibernate dùng `ddl-auto=update` để đồng bộ các cột mới trong môi trường phát triển.

Các biến chính: `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `DB_PORT`, `APP_ADMIN_INITIAL_PASSWORD`, `APP_CONTACT_PHONE`, `VIETQR_BANK_ID`, `VIETQR_ACCOUNT_NO`, `VIETQR_ACCOUNT_NAME`.
