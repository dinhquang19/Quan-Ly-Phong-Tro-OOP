package com.qlytro.service.impl;

import com.qlytro.dto.request.TimPhongRequest;
import com.qlytro.dto.response.PhongCongKhaiResponse;
import com.qlytro.entity.*;
import com.qlytro.entity.enums.TrangThaiHopDong;
import com.qlytro.entity.enums.TrangThaiPhong;
import com.qlytro.entity.enums.TrangThaiToaNha;
import com.qlytro.exception.BusinessException;
import com.qlytro.exception.ResourceNotFoundException;
import com.qlytro.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TimPhongService {

    private final PhongRepository phongRepository;
    private final HopDongRepository hopDongRepository;
    private final ToaNhaRepository toaNhaRepository;
    private final PhongThietBiRepository phongThietBiRepository;
    private final ThietBiRepository thietBiRepository;

    public TimPhongService(
            PhongRepository phongRepository,
            HopDongRepository hopDongRepository,
            ToaNhaRepository toaNhaRepository,
            PhongThietBiRepository phongThietBiRepository,
            ThietBiRepository thietBiRepository) {

        this.phongRepository = phongRepository;
        this.hopDongRepository = hopDongRepository;
        this.toaNhaRepository = toaNhaRepository;
        this.phongThietBiRepository = phongThietBiRepository;
        this.thietBiRepository = thietBiRepository;
    }

    // Tìm kiếm phòng công khai
    public Page<PhongCongKhaiResponse> timKiem(
            TimPhongRequest request,
            Pageable pageable) {

        LocalDate today = LocalDate.now();
        validateRequest(request, today);

        String viTri = request.getViTri();
        viTri = viTri == null || viTri.isBlank()
                ? null : viTri.trim();

        List<Integer> thietBiIds = request.getThietBiIds() == null
                ? List.of()
                : request.getThietBiIds().stream()
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        // Không truyền danh sách rỗng vào mệnh đề IN
        List<Integer> queryIds = thietBiIds.isEmpty()
                ? List.of(-1) : thietBiIds;

        Page<Phong> page = phongRepository.findPhongCongKhai(
                request.getToaNhaId(),
                viTri,
                request.getGiaMin(),
                request.getGiaMax(),
                request.getDienTichMin(),
                request.getDienTichMax(),
                request.getNgayChuyenVao(),
                today,
                queryIds,
                thietBiIds.size(),
                pageable
        );

        if (page.isEmpty()) {
            return new org.springframework.data.domain.PageImpl<>(
                    List.of(), pageable, page.getTotalElements());
        }

        List<Integer> phongIds = page.getContent().stream()
                .map(Phong::getId)
                .toList();

        // Lấy hợp đồng ACTIVE của các phòng
        Map<Integer, List<HopDong>> hopDongMap =
                hopDongRepository
                        .findActiveByPhongIds(
                                phongIds, TrangThaiHopDong.ACTIVE)
                        .stream()
                        .collect(Collectors.groupingBy(
                                hd -> hd.getPhong().getId()));

        // Lấy thiết bị của các phòng
        Map<Integer, List<PhongThietBi>> thietBiMap =
                phongThietBiRepository
                        .findByPhong_IdIn(phongIds)
                        .stream()
                        .collect(Collectors.groupingBy(
                                ptb -> ptb.getPhong().getId()));

        return page.map(p -> toResponse(
                p,
                hopDongMap.getOrDefault(p.getId(), List.of()),
                thietBiMap.getOrDefault(p.getId(), List.of()),
                today
        ));
    }

    // Xem chi tiết phòng công khai
    public PhongCongKhaiResponse findById(Integer id) {

        LocalDate today = LocalDate.now();

        Phong phong = phongRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Phòng", id));

        List<HopDong> active =
                hopDongRepository.findActiveByPhongIds(
                        List.of(id), TrangThaiHopDong.ACTIVE);

        boolean trong =
                phong.getTrangThai() == TrangThaiPhong.TRONG
                        && active.isEmpty();

        boolean duKien =
                phong.getTrangThai() == TrangThaiPhong.DANG_THUE
                        && active.size() == 1
                        && active.get(0).getNgayKetThuc() != null
                        && !active.get(0).getNgayKetThuc().isBefore(today);

        // Kiểm tra phòng có được phép công khai không
        if (phong.getToaNha().getTrangThai()
                != TrangThaiToaNha.HOAT_DONG
                || (!trong && !duKien)) {

            throw new ResourceNotFoundException("Phòng", id);
        }

        return toResponse(
                phong,
                active,
                phongThietBiRepository.findByPhong_Id(id),
                today
        );
    }

    // Lấy danh sách tòa nhà đang hoạt động
    public List<ToaNha> getDanhSachToaNha() {
        return toaNhaRepository.findByTrangThai(
                TrangThaiToaNha.HOAT_DONG);
    }

    // Lấy danh sách thiết bị phục vụ bộ lọc
    public List<ThietBi> getDanhSachThietBi() {
        return thietBiRepository.findAll();
    }

    // Kiểm tra dữ liệu tìm kiếm
    private void validateRequest(
            TimPhongRequest request,
            LocalDate today) {

        validateRange(
                request.getGiaMin(),
                request.getGiaMax(),
                "giá thuê");

        validateRange(
                request.getDienTichMin(),
                request.getDienTichMax(),
                "diện tích");

        if (request.getNgayChuyenVao() != null
                && request.getNgayChuyenVao().isBefore(today)) {

            throw new BusinessException(
                    "Ngày chuyển vào không được nằm trong quá khứ.");
        }
    }

    // Kiểm tra khoảng giá và diện tích
    private void validateRange(
            BigDecimal min,
            BigDecimal max,
            String ten) {

        if ((min != null && min.signum() < 0)
                || (max != null && max.signum() < 0)) {

            throw new BusinessException(
                    "Khoảng " + ten + " không được âm.");
        }

        if (min != null && max != null
                && min.compareTo(max) > 0) {

            throw new BusinessException(
                    "Giá trị tối thiểu của " + ten
                            + " không được lớn hơn giá trị tối đa.");
        }
    }

    // Chuyển Entity thành DTO công khai
    private PhongCongKhaiResponse toResponse(
            Phong phong,
            List<HopDong> active,
            List<PhongThietBi> thietBi,
            LocalDate today) {

        PhongCongKhaiResponse r =
                new PhongCongKhaiResponse();

        r.setId(phong.getId());
        r.setSoPhong(phong.getSoPhong());

        r.setTenToaNha(phong.getToaNha().getTenToaNha());
        r.setDiaChi(phong.getToaNha().getDiaChi());

        r.setDienTich(phong.getDienTich());
        r.setGiaThue(phong.getGiaThueTheoMacDinh());
        r.setMoTa(phong.getMoTa());

        r.setDanhSachThietBi(
                thietBi.stream()
                        .map(ptb -> ptb.getThietBi().getTenThietBi())
                        .distinct()
                        .toList()
        );

        boolean trong =
                phong.getTrangThai() == TrangThaiPhong.TRONG
                        && active.isEmpty();

        r.setCoTheThueNgay(trong);

        if (trong) {
            r.setNgayDuKienKhaDung(today);

        } else if (active.size() == 1
                && active.get(0).getNgayKetThuc() != null) {

            r.setNgayDuKienKhaDung(
                    active.get(0).getNgayKetThuc().plusDays(1));
        }

        return r;
    }
}
