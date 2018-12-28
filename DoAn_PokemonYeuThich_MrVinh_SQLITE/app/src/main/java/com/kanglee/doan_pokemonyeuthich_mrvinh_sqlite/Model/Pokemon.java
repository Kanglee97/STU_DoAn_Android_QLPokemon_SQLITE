package com.kanglee.doan_pokemonyeuthich_mrvinh_sqlite.Model;

import android.widget.Toast;

import java.io.Serializable;
import java.util.Arrays;

public class Pokemon implements Serializable {
    private String iD_Pokemon;
    private String ten_Pokemon;
    private Double can_Nang;
    private Double chieu_Cao;
    private byte[] anh_Pokemon;
    private Type iDType;

    public Pokemon() {
    }

    public Pokemon(String iD_Pokemon, String ten_Pokemon, Double can_Nang, Double chieu_Cao, byte[] anh_Pokemon, Type iDType) {
        this.iD_Pokemon = iD_Pokemon;
        this.ten_Pokemon = ten_Pokemon;
        this.can_Nang = can_Nang;
        this.chieu_Cao = chieu_Cao;
        this.anh_Pokemon = anh_Pokemon;
        this.iDType = iDType;
    }

    public String getiD_Pokemon() {
        return iD_Pokemon;
    }

    public void setiD_Pokemon(String iD_Pokemon) {
        this.iD_Pokemon = iD_Pokemon;
    }

    public String getTen_Pokemon() {
        return ten_Pokemon;
    }

    public void setTen_Pokemon(String ten_Pokemon) {
        this.ten_Pokemon = ten_Pokemon;
    }

    public Double getCan_Nang() {
        return can_Nang;
    }

    public void setCan_Nang(Double can_Nang) {
        this.can_Nang = can_Nang;
    }

    public Double getChieu_Cao() {
        return chieu_Cao;
    }

    public void setChieu_Cao(Double chieu_Cao) {
        this.chieu_Cao = chieu_Cao;
    }

    public byte[] getAnh_Pokemon() {
        return anh_Pokemon;
    }

    public void setAnh_Pokemon(byte[] anh_Pokemon) {
        this.anh_Pokemon = anh_Pokemon;
    }

    public Type getiDType() {
        return iDType;
    }

    public void setiDType(Type iDType) {
        this.iDType = iDType;
    }

    @Override
    public int hashCode() {
        int result = iD_Pokemon != null ? iD_Pokemon.hashCode() : 0;
        result = 31 * result + (ten_Pokemon != null ? ten_Pokemon.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(anh_Pokemon);
        result = 31 * result + (iDType != null ? iDType.hashCode() : 0);
        result = 31 * result + (can_Nang != null ? can_Nang.hashCode() : 0);
        result = 31 * result + (chieu_Cao != null ? chieu_Cao.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Pokemon{" +
                "iD_Pokemon='" + iD_Pokemon + '\'' +
                ", ten_Pokemon='" + ten_Pokemon + '\'' +
                ", can_Nang=" + can_Nang +
                ", chieu_Cao=" + chieu_Cao +
                ", anh_Pokemon=" + Arrays.toString(anh_Pokemon) +
                ", iDType=" + iDType +
                '}';
    }
}
