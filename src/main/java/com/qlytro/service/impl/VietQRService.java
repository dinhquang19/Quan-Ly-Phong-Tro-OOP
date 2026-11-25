package com.qlytro.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class VietQRService {

    @Value("${vietqr.bank-id:}")
    private String bankId;

    @Value("${vietqr.account-no:}")
    private String accountNo;

    @Value("${vietqr.account-name:}")
    private String accountName;

    // Tạo nội dung chuyển khoản
    public String taoNoiDungChuyenKhoan(Integer hoaDonId) {
        return "THANH TOAN HD" + hoaDonId;
    }

    // Kiểm tra cấu hình tài khoản ngân hàng
    public boolean isConfigured() {
        return bankId.matches("[a-zA-Z0-9]+")
                && accountNo.matches("[0-9]{6,19}")
                && !accountName.isBlank();
    }

    // Tạo đường dẫn ảnh VietQR
    public String taoMaQR(Integer hoaDonId, BigDecimal soTien) {

        if (!isConfigured()
                || hoaDonId == null
                || soTien == null
                || soTien.signum() <= 0) {
            return null;
        }

        BigDecimal amount = soTien.stripTrailingZeros();

        if (amount.scale() > 0
                || amount.toPlainString().length() > 13) {
            return null;
        }

        String noiDung = taoNoiDungChuyenKhoan(hoaDonId);

        return "https://img.vietqr.io/image/"
                + bankId + "-"
                + accountNo + "-compact2.svg"
                + "?amount=" + amount.toPlainString()
                + "&addInfo=" + encode(noiDung)
                + "&accountName=" + encode(accountName);
    }

    // Mã hóa dữ liệu đưa vào URL
    private String encode(String value) {
        return URLEncoder.encode(
                value, StandardCharsets.UTF_8
        ).replace("+", "%20");
    }
}
