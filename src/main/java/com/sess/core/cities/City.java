package com.sess.core.cities;

import javax.persistence.*;

@Entity
@Table(name = "cities")
public class City {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "cities_id_seq"
    )
    @SequenceGenerator(
            name = "cities_id_seq",
            sequenceName = "cities_id_seq",
            allocationSize = 1
    )
    private Long id;

    @Column(name = "address", length = 1024, unique = true, nullable = false)
    private String address;

    @Column(name = "postal_code", length = 100)
    private String postalCode;

    @Column(name = "federal_district", length = 100, nullable = false)
    private String federalDistrict;

    @Column(name = "region_type", length = 100, nullable = false)
    private String regionType;

    @Column(name = "region", length = 100, nullable = false)
    private String region;

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "kladr_id", length = 100, nullable = false, unique = true)
    private String kladrId;

    @Column(name = "fias_id", length = 100, nullable = false, unique = true)
    private String fiasId;

    @Column(name = "fias_level", nullable = false)
    private Integer fiasLevel;

    @Column(name = "okato", length = 100, nullable = false)
    private String okato;

    @Column(name = "oktmo", length = 100, nullable = false)
    private String oktmo;

    @Column(name = "tax_office", length = 100, nullable = false)
    private String taxOffice;

    @Column(name = "timezone", length = 50, nullable = false)
    private String timezone;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getFederalDistrict() {
        return federalDistrict;
    }

    public void setFederalDistrict(String federalDistrict) {
        this.federalDistrict = federalDistrict;
    }

    public String getRegionType() {
        return regionType;
    }

    public void setRegionType(String regionType) {
        this.regionType = regionType;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getKladrId() {
        return kladrId;
    }

    public void setKladrId(String kladrId) {
        this.kladrId = kladrId;
    }

    public String getFiasId() {
        return fiasId;
    }

    public void setFiasId(String fiasId) {
        this.fiasId = fiasId;
    }

    public Integer getFiasLevel() {
        return fiasLevel;
    }

    public void setFiasLevel(Integer fiasLevel) {
        this.fiasLevel = fiasLevel;
    }

    public String getOkato() {
        return okato;
    }

    public void setOkato(String okato) {
        this.okato = okato;
    }

    public String getOktmo() {
        return oktmo;
    }

    public void setOktmo(String oktmo) {
        this.oktmo = oktmo;
    }

    public String getTaxOffice() {
        return taxOffice;
    }

    public void setTaxOffice(String taxOffice) {
        this.taxOffice = taxOffice;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }
}
