package com.qlytro.entity.converter;

import com.qlytro.entity.enums.TrangThaiChiSo;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TrangThaiChiSoConverter implements AttributeConverter<TrangThaiChiSo, Byte> {

    @Override
    public Byte convertToDatabaseColumn(TrangThaiChiSo attribute) {
        return attribute == null ? null : (byte) attribute.getValue();
    }

    @Override
    public TrangThaiChiSo convertToEntityAttribute(Byte dbData) {
        return dbData == null ? null : TrangThaiChiSo.fromValue(dbData.intValue());
    }
}
