package com.kanglee.doan_pokemonyeuthich_mrvinh_sqlite.Model;

import android.widget.Spinner;

import java.io.Serializable;

public class Type implements Serializable {
    private String typeId;
    private String typeTen;

    public Type() {
    }

    public Type(String typeId, String typeTen) {
        this.typeId = typeId;
        this.typeTen = typeTen;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getTypeTen() {
        return typeTen;
    }

    public void setTypeTen(String typeTen) {
        this.typeTen = typeTen;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Type type = (Type) o;

        if (typeId != null ? !typeId.equals(type.typeId) : type.typeId != null)
            return false;
        return typeTen != null ? typeTen.equals(type.typeTen) : type.typeTen == null;

    }

    @Override
    public int hashCode() {
        int result = typeId != null ? typeId.hashCode() : 0;
        result = 31 * result + (typeTen != null ? typeTen.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return typeId  +" - "+ typeTen;
    }
}
