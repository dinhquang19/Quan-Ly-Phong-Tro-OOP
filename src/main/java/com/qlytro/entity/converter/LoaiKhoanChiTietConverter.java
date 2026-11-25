package com.qlytro.entity.converter;

import com.qlytro.entity.enums.LoaiKhoanChiTiet;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class LoaiKhoanChiTietConverter implements AttributeConverter<LoaiKhoanChiTiet, Byte> {

    @Override
    public Byte convertToDatabaseColumn(LoaiKhoanChiTiet attribute) {
        return attribute == null ? null : (byte) attribute.getValue();
    }

    @Override
    public LoaiKhoanChiTiet convertToEntityAttribute(Byte dbData) {
        return dbData == null ? null : LoaiKhoanChiTiet.fromValue(dbData.intValue());
    }
}
