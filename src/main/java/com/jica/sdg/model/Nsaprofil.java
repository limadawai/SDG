package com.jica.sdg.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "nsa_profil")
public class Nsaprofil implements Serializable {

    @Id
    private String id_nsa;
    @Column(name = "nm_nsa")
    private String nm_nsa;
    @Column(name = "pencapaian_nsa")
    private String pencapaian_nsa;
    @Column(name = "lokasi_nsa")
    private String lokasi_nsa;
    @Column(name = "penerima_manfaat")
    private String penerima_manfaat;
    @Column(name = "mitra_utama")
    private String mitra_utama;

    public Nsaprofil() {
    }

    public Nsaprofil(String id_nsa, String nm_nsa, String pencapaian_nsa, String lokasi_nsa, String penerima_manfaat, String mitra_utama) {
        this.id_nsa = id_nsa;
        this.nm_nsa = nm_nsa;
        this.pencapaian_nsa = pencapaian_nsa;
        this.lokasi_nsa = lokasi_nsa;
        this.penerima_manfaat = penerima_manfaat;
        this.mitra_utama = mitra_utama;
    }

    public String getId_nsa() {
        return id_nsa;
    }

    public void setId_nsa(String id_nsa) {
        this.id_nsa = id_nsa;
    }

    public String getNm_nsa() {
        return nm_nsa;
    }

    public void setNm_nsa(String nm_nsa) {
        this.nm_nsa = nm_nsa;
    }

    public String getPencapaian_nsa() {
        return pencapaian_nsa;
    }

    public void setPencapaian_nsa(String pencapaian_nsa) {
        this.pencapaian_nsa = pencapaian_nsa;
    }

    public String getLokasi_nsa() {
        return lokasi_nsa;
    }

    public void setLokasi_nsa(String lokasi_nsa) {
        this.lokasi_nsa = lokasi_nsa;
    }

    public String getPenerima_manfaat() {
        return penerima_manfaat;
    }

    public void setPenerima_manfaat(String penerima_manfaat) {
        this.penerima_manfaat = penerima_manfaat;
    }

    public String getMitra_utama() {
        return mitra_utama;
    }

    public void setMitra_utama(String mitra_utama) {
        this.mitra_utama = mitra_utama;
    }
}
