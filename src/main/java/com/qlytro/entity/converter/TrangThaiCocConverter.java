package com.qlytro.entity.converter;

import com.qlytro.entity.enums.TrangThaiCoc;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TrangThaiCocConverter implements AttributeConverter<TrangThaiCoc, Byte> {

    @Override
    public Byte convertToDatabaseColumn(TrangThaiCoc attribute) {
        return attribute == null ? null : (byte) attribute.getValue();
    }

    @Override
    public TrangThaiCoc convertToEntityAttribute(Byte dbData) {
        return dbData == null ? null : TrangThaiCoc.fromValue(dbData.intValue());
    }
}
