package com.qlytro.entity.converter;

import com.qlytro.entity.enums.TrangThaiHoaDon;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TrangThaiHoaDonConverter implements AttributeConverter<TrangThaiHoaDon, Byte> {

    @Override
    public Byte convertToDatabaseColumn(TrangThaiHoaDon attribute) {
        return attribute == null ? null : (byte) attribute.getValue();
    }

    @Override
    public TrangThaiHoaDon convertToEntityAttribute(Byte dbData) {
        return dbData == null ? null : TrangThaiHoaDon.fromValue(dbData.intValue());
    }
}
