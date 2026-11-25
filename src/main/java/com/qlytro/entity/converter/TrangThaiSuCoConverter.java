package com.qlytro.entity.converter;

import com.qlytro.entity.enums.TrangThaiSuCo;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TrangThaiSuCoConverter implements AttributeConverter<TrangThaiSuCo, Byte> {

    @Override
    public Byte convertToDatabaseColumn(TrangThaiSuCo attribute) {
        return attribute == null ? null : (byte) attribute.getValue();
    }

    @Override
    public TrangThaiSuCo convertToEntityAttribute(Byte dbData) {
        return dbData == null ? null : TrangThaiSuCo.fromValue(dbData.intValue());
    }
}
