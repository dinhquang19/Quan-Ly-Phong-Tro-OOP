package com.qlytro.entity.converter;

import com.qlytro.entity.enums.VaiTroNguoiDung;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class VaiTroNguoiDungConverter implements AttributeConverter<VaiTroNguoiDung, Byte> {

    @Override
    public Byte convertToDatabaseColumn(VaiTroNguoiDung attribute) {
        return attribute == null ? null : (byte) attribute.getValue();
    }

    @Override
    public VaiTroNguoiDung convertToEntityAttribute(Byte dbData) {
        return dbData == null ? null : VaiTroNguoiDung.fromValue(dbData.intValue());
    }
}
