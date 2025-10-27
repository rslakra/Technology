package com.rslakra.imageservice.domain;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "images")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "created_on")
    private Date createdOn;

    @Column(name = "type")
    private String type;

    @Lob
    private byte[] data;

    @Column(name = "size")
    private long size;

    /**
     *
     */
    public Image() {

    }

    /**
     * @param title
     * @param createdOn
     * @param type
     * @param data
     * @param size
     */
    public Image(String title, Date createdOn, String type, byte[] data, long size) {
        this.title = title;
        this.createdOn = createdOn;
        this.type = type;
        this.data = data;
        this.size = size;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}
