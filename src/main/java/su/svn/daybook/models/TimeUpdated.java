package su.svn.daybook.models;

import su.svn.daybook.models.Timed;

import java.time.LocalDateTime;

public interface TimeUpdated extends Timed {
    LocalDateTime getUpdateTime();
}
