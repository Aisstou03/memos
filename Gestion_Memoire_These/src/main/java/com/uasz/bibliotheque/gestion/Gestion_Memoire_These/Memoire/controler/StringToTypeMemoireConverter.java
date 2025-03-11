package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.controler;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.TypeMemoire;

@Component
public class StringToTypeMemoireConverter implements Converter<String, TypeMemoire> {
    @Override
    public TypeMemoire convert(String source) {
        try {
            return TypeMemoire.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
