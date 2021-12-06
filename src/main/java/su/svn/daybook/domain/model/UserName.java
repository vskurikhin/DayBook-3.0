/*
 * This file was last modified at 2021.12.06 19:31 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * UserName.java
 * $Id$
 */

package su.svn.daybook.domain.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public class UserName implements Serializable {

    private static final long serialVersionUID = 3526532892030791269L;

    private UUID id;

    private String userName;

    private String password;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Boolean enabled;

    private Boolean visible;

    private Integer flags;
}
