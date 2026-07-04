# Giải thích cấu hình CI Pipeline (`ci.yml`)

> **Dành cho người không chuyên DevOps:** Bạn có thể hình dung file `ci.yml` này giống như bản **"hướng dẫn công việc cho Robot bảo vệ cổng"** trên GitHub. Mỗi khi có ai đó nộp code mới lên dự án, con robot này sẽ tự động bật dậy, mang code đi xét nghiệm toàn diện. Nếu tất cả đều XANH (ổn), code mới được chấp nhận; nếu ĐỎ (có lỗi), nó sẽ báo động và chặn lại ngay.

---

## 1. Khi nào Robot được kích hoạt? (Phần `on:`)
```yaml
on:
  push:
    branches: [main]
  pull_request:
```
Robot sẽ tự động chạy trong 2 trường hợp:
1. **`push: branches: [main]`**: Khi có code được đẩy trực tiếp lên nhánh chính (`main`).
2. **`pull_request`**: Khi một lập trình viên tạo yêu cầu gộp code (Pull Request) từ nhánh tính năng của họ vào nhánh chính. Đây là bước kiểm duyệt trước khi code được sáp nhập.

---

## 2. Robot làm những nhiệm vụ gì? (Phần `jobs:`)
Trong cấu hình này, Robot được giao **2 nhiệm vụ (jobs)** chạy **đồng thời (song song)** trên 2 máy chủ ảo Linux (`ubuntu-latest`) hoàn toàn sạch sẽ do GitHub cung cấp miễn phí:

### 🛠️ Nhiệm vụ 1: `build` (Xây dựng & Kiểm tra chất lượng code)
Nhiệm vụ này đảm bảo code viết ra phải chạy được, đúng chuẩn, và không làm hỏng tính năng cũ. Nó trải qua 4 bước:

1. **Tải code về máy ảo (`actions/checkout@v4`):**
   Máy ảo của GitHub ban đầu trắng trơn, bước này copy toàn bộ code từ kho (repository) của bạn về máy ảo đó.

2. **Cài đặt môi trường Java (`actions/setup-java@v4`):**
   - Cài đặt Java JDK bản **21** (bản phân phối Temurin - chuẩn doanh nghiệp).
   - `cache: maven`: **Bật bộ nhớ đệm**. Mẹo nhỏ này giúp giữ lại các thư viện đã tải từ lần chạy trước, giúp các lần kiểm tra sau nhanh hơn rất nhiều (giảm từ 3-5 phút xuống còn hơn 1 phút).

3. **Kiểm tra và Thi hành kỷ luật sắt (`run: ./mvnw -B -ntp verify`):**
   Đây là bước "khám sức khỏe tổng thể" quan trọng nhất. Khi chạy lệnh `verify`, hệ thống sẽ tự động làm chuỗi 4 việc liên tiếp:
   - **Spotless (Kiểm tra hình thức):** Code có được căn lề, xuống dòng đẹp mắt đúng chuẩn không?
   - **Checkstyle (Kiểm tra tác phong):** Tên biến, tên hàm có đặt đúng quy tắc, dòng code có bị dài quá 120 ký tự không?
   - **Unit Test (Kiểm tra từng bộ phận):** Chạy các bài test kiểm tra hàm logic nhỏ (chạy cực nhanh, không cần database).
   - **Integration Test (Kiểm tra thực chiến với Testcontainers):** Tự động bật các container PostgreSQL (Cơ sở dữ liệu) và Redis thật trong bộ nhớ tạm, cắm app vào chạy thử các kịch bản thực tế xem có kết nối, lưu trữ dữ liệu thành công không, sau đó tự động dọn dẹp sạch sẽ.

4. **Lưu lại học bạ (`Upload JaCoCo report`):**
   - Nếu bước số 3 thành công (`if: success()`), hệ thống sẽ gom báo cáo chấm điểm **JaCoCo** (báo cáo cho biết test đã bao phủ được bao nhiêu % số dòng code của dự án).
   - Báo cáo này được đóng gói thành file đính kèm (`jacoco-report`) để bạn có thể tải về xem trên giao diện GitHub.

---

### 🛡️ Nhiệm vụ 2: `security-scan` (Quét virus & Lỗ hổng bảo mật)
Chạy song song với Nhiệm vụ 1, nhiệm vụ này như một bác sĩ chuyên khoa nhiễm trùng, rà soát xem các thư viện bên thứ 3 mà dự án đem về dùng có an toàn không.

1. **Tải code về (`actions/checkout@v4`):** Tương tự nhiệm vụ 1.
2. **Khám sức khỏe bảo mật bằng Trivy (`aquasecurity/trivy-action@v0.36.0`):**
   - **Trivy** là một công cụ bảo mật hàng đầu thế giới của hãng Aqua Security.
   - `scan-type: fs` và `scan-ref: .`: Quét toàn bộ file trong thư mục dự án (đặc biệt là file `pom.xml` chứa danh sách các thư viện).
   - `severity: CRITICAL,HIGH`: Chỉ nhắm vào các lỗ hổng bảo mật mức độ **CAO (High)** và **NGHIÊM TRỌNG (Critical)**.
   - `ignore-unfixed: true`: Bỏ qua các lỗ hổng mà thế giới chưa tìm ra thuốc chữa (chưa có bản vá), chỉ báo động những lỗi đã có bản update thư viện để mình sửa.
   - `exit-code: "1"`: **Luật bàn tay sắt!** Nếu phát hiện dù chỉ 1 lỗ hổng HIGH hoặc CRITICAL (mà đã có bản vá), ngay lập tức đánh trượt (FAIL) toàn bộ tiến trình, ngăn không cho gộp code bẩn vào dự án. *(Đây chính là lý do vừa rồi chúng ta phải nâng cấp Spring Boot và PostgreSQL để vá 3 lỗi HIGH mới pass được bước này).*

---

## 3. Tóm lại: Tại sao file này là "tài sản quý" của dự án?
Nhờ có file `.yml` nhỏ bé này, dự án ElectroStore đạt được chuẩn mực DevOps của các công ty công nghệ lớn:
* **Tự động hóa 100%:** Con người không cần phải ngồi chạy tay từng lệnh test hay tự đi đọc tin tức hacker xem thư viện mình dùng có bị hack không.
* **Bảo vệ nhánh `main` tuyệt đối:** Không ai có thể vô tình đẩy một đoạn code bị lỗi cú pháp, làm chết database, hoặc chứa lỗ hổng bảo mật nghiêm trọng vào hệ thống.
* **Tự tin mở rộng:** Khi hệ thống lớn lên với hàng trăm ngàn dòng code, lập trình viên vẫn dám sửa đổi hoặc thêm tính năng mới, vì nếu họ làm hỏng gì đó, "Robot CI" sẽ chỉ ra chính xác lỗi ở đâu ngay lập tức.
