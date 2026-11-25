package com.qlytro.repository;

import com.qlytro.entity.Phong;
import com.qlytro.entity.enums.TrangThaiPhong;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;
import java.time.LocalDate;
@Repository
public interface PhongRepository extends JpaRepository<Phong, Integer> {

    List<Phong> findByToaNha_Id(Integer toaNhaId);

    List<Phong> findByToaNha_IdAndTrangThai(Integer toaNhaId, TrangThaiPhong trangThai);

    List<Phong> findByTrangThai(TrangThaiPhong trangThai);

    long countByTrangThai(TrangThaiPhong trangThai);

    boolean existsByToaNha_IdAndSoPhong(Integer toaNhaId, String soPhong);

    boolean existsByToaNha_IdAndSoPhongAndIdNot(Integer toaNhaId, String soPhong, Integer id);

    Optional<Phong> findByToaNha_IdAndSoPhong(Integer toaNhaId, String soPhong);

    @Query("SELECT COUNT(p) FROM Phong p WHERE p.toaNha.id = :toaNhaId")
    long countByToaNhaId(@Param("toaNhaId") Integer toaNhaId);

    @Query(
        value = """
            SELECT p FROM Phong p
            JOIN FETCH p.toaNha tn
            WHERE (:toaNhaId IS NULL OR tn.id = :toaNhaId)
              AND (:keyword IS NULL OR :keyword = '' OR LOWER(p.soPhong) LIKE LOWER(CONCAT('%', :keyword, '%')))
              AND (:trangThai IS NULL OR p.trangThai = :trangThai)
            ORDER BY LENGTH(p.soPhong) ASC, p.soPhong ASC, p.id ASC
            """,
        countQuery = """
            SELECT COUNT(p) FROM Phong p
            WHERE (:toaNhaId IS NULL OR p.toaNha.id = :toaNhaId)
              AND (:keyword IS NULL OR :keyword = '' OR LOWER(p.soPhong) LIKE LOWER(CONCAT('%', :keyword, '%')))
              AND (:trangThai IS NULL OR p.trangThai = :trangThai)
            """
    )
    Page<Phong> findPhongFilterAndPage(
            @Param("toaNhaId") Integer toaNhaId,
            @Param("keyword") String keyword,
            @Param("trangThai") TrangThaiPhong trangThai,
            Pageable pageable
    );

    @Query("""
        SELECT p.trangThai, COUNT(p) FROM Phong p
        WHERE (:toaNhaId IS NULL OR p.toaNha.id = :toaNhaId)
        GROUP BY p.trangThai
    """)
    List<Object[]> countPhongGroupByTrangThai(@Param("toaNhaId") Integer toaNhaId);

    @Query("""
    SELECT p FROM Phong p
    JOIN p.toaNha tn
    WHERE tn.trangThai =
        com.qlytro.entity.enums.TrangThaiToaNha.HOAT_DONG

    AND (:toaNhaId IS NULL OR tn.id = :toaNhaId)

    AND (:viTri IS NULL
        OR LOWER(tn.tenToaNha) LIKE LOWER(CONCAT('%', :viTri, '%'))
        OR LOWER(tn.diaChi) LIKE LOWER(CONCAT('%', :viTri, '%')))

    AND (:giaMin IS NULL OR p.giaThueTheoMacDinh >= :giaMin)
    AND (:giaMax IS NULL OR p.giaThueTheoMacDinh <= :giaMax)

    AND (:dienTichMin IS NULL OR p.dienTich >= :dienTichMin)
    AND (:dienTichMax IS NULL OR p.dienTich <= :dienTichMax)

    AND (
        (
            p.trangThai =
                com.qlytro.entity.enums.TrangThaiPhong.TRONG

            AND NOT EXISTS (
                SELECT hd.id FROM HopDong hd
                WHERE hd.phong = p
                AND hd.trangThai =
                    com.qlytro.entity.enums.TrangThaiHopDong.ACTIVE
            )
        )
        OR
        (
            :ngayChuyenVao IS NOT NULL

            AND p.trangThai =
                com.qlytro.entity.enums.TrangThaiPhong.DANG_THUE

            AND EXISTS (
                SELECT hd.id FROM HopDong hd
                WHERE hd.phong = p
                AND hd.trangThai =
                    com.qlytro.entity.enums.TrangThaiHopDong.ACTIVE
                AND hd.ngayKetThuc >= :today
                AND hd.ngayKetThuc < :ngayChuyenVao
            )

            AND (
                SELECT COUNT(hd2) FROM HopDong hd2
                WHERE hd2.phong = p
                AND hd2.trangThai =
                    com.qlytro.entity.enums.TrangThaiHopDong.ACTIVE
            ) = 1
        )
    )

    AND (
        :soThietBi = 0
        OR (
            SELECT COUNT(DISTINCT ptb.thietBi.id)
            FROM PhongThietBi ptb
            WHERE ptb.phong = p
            AND ptb.thietBi.id IN :thietBiIds
        ) = :soThietBi
    )

    ORDER BY
        CASE WHEN p.trangThai =
            com.qlytro.entity.enums.TrangThaiPhong.TRONG
        THEN 0 ELSE 1 END,
        p.giaThueTheoMacDinh ASC,
        p.id ASC
    """)
    Page<Phong> findPhongCongKhai(
            @Param("toaNhaId") Integer toaNhaId,
            @Param("viTri") String viTri,
            @Param("giaMin") BigDecimal giaMin,
            @Param("giaMax") BigDecimal giaMax,
            @Param("dienTichMin") BigDecimal dienTichMin,
            @Param("dienTichMax") BigDecimal dienTichMax,
            @Param("ngayChuyenVao") LocalDate ngayChuyenVao,
            @Param("today") LocalDate today,
            @Param("thietBiIds") List<Integer> thietBiIds,
            @Param("soThietBi") long soThietBi,
            Pageable pageable
    );
}
