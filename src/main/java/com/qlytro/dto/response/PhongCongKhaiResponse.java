package com.qlytro.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class PhongCongKhaiResponse {

    private Integer id;
    private String soPhong;

    private String tenToaNha;
    private String diaChi;

    private BigDecimal dienTich;
    private BigDecimal giaThue;

    private String moTa;

    private String anhThumbnail;
    private String anhPhongKhach;
    private String anhPhongNgu;
    private String anhNhaVeSinh;

    private List<String> danhSachThietBi = new ArrayList<>();

    private boolean coTheThueNgay;
    private LocalDate ngayDuKienKhaDung;
}
