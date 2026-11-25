package com.qlytro.entity.converter;

import com.qlytro.entity.enums.NguonPhatHienBoiThuong;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class NguonPhatHienBoiThuongConverter implements AttributeConverter<NguonPhatHienBoiThuong, Byte> {

    @Override
    public Byte convertToDatabaseColumn(NguonPhatHienBoiThuong attribute) {
        return attribute == null ? null : (byte) attribute.getValue();
    }

    @Override
    public NguonPhatHienBoiThuong convertToEntityAttribute(Byte dbData) {
        return dbData == null ? null : NguonPhatHienBoiThuong.fromValue(dbData.intValue());
    }
}
