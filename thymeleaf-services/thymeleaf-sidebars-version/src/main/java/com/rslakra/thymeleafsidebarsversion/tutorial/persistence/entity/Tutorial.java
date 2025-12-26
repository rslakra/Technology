package com.rslakra.thymeleafsidebarsversion.tutorial.persistence.entity;

import com.rslakra.thymeleafsidebarsversion.framework.persistence.entity.AbstractEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.StringJoiner;

@Entity
@Table(name = "tutorials")
public class Tutorial extends AbstractEntity {

    @Column(length = 128, nullable = false)
    private String title;

    @Column(length = 256)
    private String description;

    @Column(nullable = false)
    private int level;

    @Column
    private boolean published;

    public Tutorial() {
    }

    public Tutorial(String title, String description, int level, boolean published) {
        this.title = title;
        this.description = description;
        this.level = level;
        this.published = published;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    /**
     * @return
     */
    @Override
    public String toString() {
        return new StringJoiner(",")
            .add("Tutorial <id=" + getId())
            .add(", title=" + getTitle())
            .add(", description=" + getDescription())
            .add(", level=" + getLevel())
            .add(", published=" + isPublished())
            .add(">")
            .toString();
    }

}
