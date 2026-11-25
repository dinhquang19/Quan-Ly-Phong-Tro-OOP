package com.qlytro.dto.request;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class TimPhongRequest {

    private Integer toaNhaId;
    private String viTri;

    private BigDecimal giaMin;
    private BigDecimal giaMax;

    private BigDecimal dienTichMin;
    private BigDecimal dienTichMax;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate ngayChuyenVao;

    private List<Integer> thietBiIds = new ArrayList<>();
}