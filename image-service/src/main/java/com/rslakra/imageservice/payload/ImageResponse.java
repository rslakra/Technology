package com.rslakra.imageservice.payload;


import java.util.Date;

/**
 * The File Upload Response.
 */
public class ImageResponse {
    private String title;
    private Date createdOn;
    private String type;
    private long size;
    private String downloadUrl;

    /**
     * @param title
     * @param createdOn
     * @param type
     * @param size
     * @param downloadUrl
     */
    public ImageResponse(String title, Date createdOn, String type, long size, String downloadUrl) {
        this.title = title;
        this.createdOn = createdOn;
        this.type = type;
        this.size = size;
        this.downloadUrl = downloadUrl;
    }

    /**
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return
     */
    public Date getCreatedOn() {
        return createdOn;
    }

    /**
     * @param createdOn
     */
    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    /**
     * @return
     */
    public String getDownloadUrl() {
        return downloadUrl;
    }

    /**
     * @param downloadUrl
     */
    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    /**
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return
     */
    public long getSize() {
        return size;
    }

    /**
     * @param size
     */
    public void setSize(long size) {
        this.size = size;
    }
}
