/*
 * This file was last modified at 2021.12.06 19:31 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * Codifier.java
 * $Id$
 */

package su.svn.daybook.domain.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Codifier implements Serializable {

    private static final long serialVersionUID = 1265480523704797546L;

    private Long id;

    private String code;

    private String value;

    private String userName;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Boolean enabled;

    private Boolean visible;

    private Integer flags;

    public Codifier() {}

    public Codifier(
            Long id,
            String code,
            String value,
            String userName,
            LocalDateTime createTime,
            LocalDateTime updateTime,
            Boolean enabled,
            Boolean visible,
            Integer flags) {
        this.id = id;
        this.code = code;
        this.value = value;
        this.userName = userName;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.enabled = enabled;
        this.visible = visible;
        this.flags = flags;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public Integer getFlags() {
        return flags;
    }

    public void setFlags(Integer flags) {
        this.flags = flags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Codifier)) return false;

        Codifier codifier = (Codifier) o;

        if (id != null ? !id.equals(codifier.id) : codifier.id != null) return false;
        if (code != null ? !code.equals(codifier.code) : codifier.code != null) return false;
        if (value != null ? !value.equals(codifier.value) : codifier.value != null) return false;
        if (userName != null ? !userName.equals(codifier.userName) : codifier.userName != null) return false;
        if (createTime != null ? !createTime.equals(codifier.createTime) : codifier.createTime != null) return false;
        if (updateTime != null ? !updateTime.equals(codifier.updateTime) : codifier.updateTime != null) return false;
        if (enabled != null ? !enabled.equals(codifier.enabled) : codifier.enabled != null) return false;
        if (visible != null ? !visible.equals(codifier.visible) : codifier.visible != null) return false;
        return flags != null ? flags.equals(codifier.flags) : codifier.flags == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (updateTime != null ? updateTime.hashCode() : 0);
        result = 31 * result + (enabled != null ? enabled.hashCode() : 0);
        result = 31 * result + (visible != null ? visible.hashCode() : 0);
        result = 31 * result + (flags != null ? flags.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Codifier{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", value='" + value + '\'' +
                ", userName='" + userName + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", enabled=" + enabled +
                ", visible=" + visible +
                ", flags=" + flags +
                '}';
    }
}
