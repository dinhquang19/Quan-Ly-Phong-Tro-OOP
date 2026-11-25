package com.qlytro.entity.converter;

import com.qlytro.entity.enums.NguyenNhanSuCo;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/** Cot nguyen_nhan la NULLABLE — xu ly null ca 2 chieu. */
@Converter(autoApply = true)
public class NguyenNhanSuCoConverter implements AttributeConverter<NguyenNhanSuCo, Byte> {

    @Override
    public Byte convertToDatabaseColumn(NguyenNhanSuCo attribute) {
        return attribute == null ? null : (byte) attribute.getValue();
    }

    @Override
    public NguyenNhanSuCo convertToEntityAttribute(Byte dbData) {
        return dbData == null ? null : NguyenNhanSuCo.fromValue(dbData.intValue());
    }
}
