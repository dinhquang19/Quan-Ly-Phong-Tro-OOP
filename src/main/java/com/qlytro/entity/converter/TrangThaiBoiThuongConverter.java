package com.qlytro.entity.converter;

import com.qlytro.entity.enums.TrangThaiBoiThuong;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TrangThaiBoiThuongConverter implements AttributeConverter<TrangThaiBoiThuong, Byte> {

    @Override
    public Byte convertToDatabaseColumn(TrangThaiBoiThuong attribute) {
        return attribute == null ? null : (byte) attribute.getValue();
    }

    @Override
    public TrangThaiBoiThuong convertToEntityAttribute(Byte dbData) {
        return dbData == null ? null : TrangThaiBoiThuong.fromValue(dbData.intValue());
    }
}
