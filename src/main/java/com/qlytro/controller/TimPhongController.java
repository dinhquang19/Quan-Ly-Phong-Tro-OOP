package com.qlytro.controller;

import com.qlytro.dto.request.TimPhongRequest;
import com.qlytro.dto.response.PhongCongKhaiResponse;
import com.qlytro.exception.BusinessException;
import com.qlytro.service.impl.TimPhongService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/tim-phong")
public class TimPhongController {

    private final TimPhongService timPhongService;

    @Value("${app.contact.phone:}")
    private String soDienThoaiLienHe;

    public TimPhongController(TimPhongService timPhongService) {
        this.timPhongService = timPhongService;
    }

    // Danh sách và tìm kiếm phòng công khai
    @GetMapping
    public String list(
            @ModelAttribute("request") TimPhongRequest request,
            BindingResult bindingResult,
            @RequestParam(defaultValue = "0") int page,
            Model model) {

        model.addAttribute("danhSachToaNha",
                timPhongService.getDanhSachToaNha());

        model.addAttribute("danhSachThietBi",
                timPhongService.getDanhSachThietBi());

        Page<PhongCongKhaiResponse> pageResult = Page.empty();

        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMsg",
                    "Thông tin tìm kiếm không hợp lệ.");
        } else {
            try {
                pageResult = timPhongService.timKiem(
                        request,
                        PageRequest.of(Math.max(0, page), 10)
                );

            } catch (BusinessException e) {
                model.addAttribute("errorMsg", e.getMessage());
            }
        }

        model.addAttribute("pageResult", pageResult);
        model.addAttribute("danhSach", pageResult.getContent());
        model.addAttribute("pageTitle", "Tìm phòng trọ");

        return "public/phong/list";
    }

    // Xem chi tiết phòng công khai
    @GetMapping("/{id}")
    public String detail(
            @PathVariable("id") Integer id,
            Model model) {

        model.addAttribute("phong",
                timPhongService.findById(id));

        model.addAttribute("soDienThoaiLienHe",
                soDienThoaiLienHe);

        model.addAttribute("pageTitle", "Chi tiết phòng trọ");

        return "public/phong/detail";
    }
}
