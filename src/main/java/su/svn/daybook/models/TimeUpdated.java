package su.svn.daybook.models;

import java.time.LocalDateTime;

public interface TimeUpdated extends Timed {
    LocalDateTime updateTime();
}
