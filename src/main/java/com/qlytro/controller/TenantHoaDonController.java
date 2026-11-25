package com.qlytro.controller;

import com.qlytro.dto.response.HoaDonResponse;
import com.qlytro.entity.enums.TrangThaiHoaDon;
import com.qlytro.exception.BusinessException;
import com.qlytro.security.CustomUserDetails;
import com.qlytro.service.impl.FileStorageService;
import com.qlytro.service.impl.HoaDonService;
import com.qlytro.service.impl.VietQRService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/tenant/hoa-don")
public class TenantHoaDonController {

    private final HoaDonService hoaDonService;
    private final FileStorageService fileStorageService;
    private final VietQRService vietQRService;

    public TenantHoaDonController(
            HoaDonService hoaDonService,
            FileStorageService fileStorageService,
            VietQRService vietQRService) {

        this.hoaDonService = hoaDonService;
        this.fileStorageService = fileStorageService;
        this.vietQRService = vietQRService;
    }

    // Danh sách hóa đơn của người thuê
    @GetMapping
    public String list(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Model model) {

        model.addAttribute("danhSach",
                hoaDonService.findByNguoiDungId(userDetails.getId()));

        model.addAttribute("pageTitle", "Hóa đơn của tôi");
        model.addAttribute("pageContent", "tenant/hoa-don/list");

        return "layout/base";
    }

    // Chi tiết hóa đơn và mã VietQR
    @GetMapping("/{id}")
    public String detail(
            @PathVariable(name = "id") Integer id,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Model model) {

        HoaDonResponse hoaDon = hoaDonService.findByIdOfNguoiDung(
                id, userDetails.getId());

        model.addAttribute("hoaDon", hoaDon);

        // Chỉ tạo mã QR cho hóa đơn chưa thanh toán
        if (hoaDon.getTrangThai() == TrangThaiHoaDon.CHUA_THANH_TOAN) {

            String vietQRUrl = vietQRService.taoMaQR(
                    hoaDon.getId(),
                    hoaDon.getTongTien());

            String noiDungChuyenKhoan =
                    vietQRService.taoNoiDungChuyenKhoan(hoaDon.getId());

            model.addAttribute("vietQRUrl", vietQRUrl);
            model.addAttribute("noiDungChuyenKhoan", noiDungChuyenKhoan);
        }

        model.addAttribute("pageTitle", "Chi tiết Hóa đơn");
        model.addAttribute("pageContent", "tenant/hoa-don/detail");

        return "layout/base";
    }

    /**
     * Khách thuê nộp tiền kèm ảnh biên lai chuyển khoản,
     * chuyển trạng thái hóa đơn sang CHO_DUYET.
     * Chủ trọ sẽ xác nhận hoặc từ chối.
     */
    @PostMapping("/{id}/nop-tien")
    public String nopTien(
            @PathVariable(name = "id") Integer id,
            @RequestParam(value = "file", required = false)
            MultipartFile file,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            RedirectAttributes redirectAttributes) {

        if (file == null || file.isEmpty()) {

            redirectAttributes.addFlashAttribute(
                    "errorMsg",
                    "Vui lòng chọn ảnh biên lai chuyển khoản trước khi bấm nộp tiền.");

            return "redirect:/tenant/hoa-don/" + id;
        }

        try {

            // 1. Kiểm tra nghiệp vụ trước
            hoaDonService.validateKhachNopTien(
                    id, userDetails.getId());

            // 2. Validate và lưu file
            String anhChuyenKhoan =
                    fileStorageService.storeFile(file);

            // 3. Lưu vào CSDL, nếu lỗi thì xóa file đã ghi
            try {

                hoaDonService.khachNopTien(
                        id, anhChuyenKhoan, userDetails.getId());

            } catch (Exception e) {

                fileStorageService.deleteFile(anhChuyenKhoan);
                throw e;
            }

            redirectAttributes.addFlashAttribute(
                    "successMsg",
                    "Đã tải ảnh biên lai và gửi thông báo thanh toán thành công! Chủ trọ sẽ duyệt sớm nhất.");

        } catch (BusinessException e) {

            redirectAttributes.addFlashAttribute(
                    "errorMsg", e.getMessage());
        }

        return "redirect:/tenant/hoa-don/" + id;
    }
}
