package su.svn.daybook.domain.model;

import java.time.LocalDateTime;

public interface TimeUpdated extends Timed {
    LocalDateTime getUpdateTime();
}
