package com.qlytro.entity.converter;

import com.qlytro.entity.enums.TrangThaiToaNha;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TrangThaiToaNhaConverter implements AttributeConverter<TrangThaiToaNha, Byte> {

    @Override
    public Byte convertToDatabaseColumn(TrangThaiToaNha attribute) {
        return attribute == null ? null : (byte) attribute.getValue();
    }

    @Override
    public TrangThaiToaNha convertToEntityAttribute(Byte dbData) {
        return dbData == null ? null : TrangThaiToaNha.fromValue(dbData.intValue());
    }
}
