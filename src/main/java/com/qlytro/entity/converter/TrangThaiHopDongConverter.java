package com.qlytro.entity.converter;

import com.qlytro.entity.enums.TrangThaiHopDong;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TrangThaiHopDongConverter implements AttributeConverter<TrangThaiHopDong, Byte> {

    @Override
    public Byte convertToDatabaseColumn(TrangThaiHopDong attribute) {
        return attribute == null ? null : (byte) attribute.getValue();
    }

    @Override
    public TrangThaiHopDong convertToEntityAttribute(Byte dbData) {
        return dbData == null ? null : TrangThaiHopDong.fromValue(dbData.intValue());
    }
}
