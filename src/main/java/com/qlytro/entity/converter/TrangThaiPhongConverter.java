package com.qlytro.entity.converter;

import com.qlytro.entity.enums.TrangThaiPhong;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TrangThaiPhongConverter implements AttributeConverter<TrangThaiPhong, Byte> {

    @Override
    public Byte convertToDatabaseColumn(TrangThaiPhong attribute) {
        return attribute == null ? null : (byte) attribute.getValue();
    }

    @Override
    public TrangThaiPhong convertToEntityAttribute(Byte dbData) {
        return dbData == null ? null : TrangThaiPhong.fromValue(dbData.intValue());
    }
}
