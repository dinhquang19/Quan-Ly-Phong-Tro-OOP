package com.qlytro.entity.converter;

import com.qlytro.entity.enums.TrangThaiNguoiDung;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TrangThaiNguoiDungConverter implements AttributeConverter<TrangThaiNguoiDung, Byte> {

    @Override
    public Byte convertToDatabaseColumn(TrangThaiNguoiDung attribute) {
        return attribute == null ? null : (byte) attribute.getValue();
    }

    @Override
    public TrangThaiNguoiDung convertToEntityAttribute(Byte dbData) {
        return dbData == null ? null : TrangThaiNguoiDung.fromValue(dbData.intValue());
    }
}
