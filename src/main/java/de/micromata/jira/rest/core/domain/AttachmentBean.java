package de.micromata.jira.rest.core.domain;

import com.google.gson.annotations.Expose;

public class AttachmentBean extends BaseBean {
    @Expose
    private UserBean author;
    @Expose
    private String   content;
    @Expose
    private String   created;
    @Expose
    private String   filename;
    @Expose
    private String   mimeType;
    @Expose
    private String   size;
    @Expose
    private String   thumbnail;

    public UserBean getAuthor() {
        return author;
    }

    public void setAuthor(UserBean author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
