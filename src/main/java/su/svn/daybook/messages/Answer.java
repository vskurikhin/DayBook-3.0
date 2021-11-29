package su.svn.daybook.messages;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Answer {

    private final String message;

    private final long error;

    private Object payload;

    private Class<?> payloadClass;

    public Answer(@Nonnull String message) {
        this.message = message;
        this.error = 0;
    }

    public Answer(@Nonnull String message, long error) {
        this.message = message;
        this.error = error;
    }

    private Answer(@Nonnull String message, long error, Object payload, Class<?> aClass) {
        this.message = message;
        this.error = error;
        this.payload = payload;
        this.payloadClass = aClass;
    }

    /** @noinspection unchecked*/
    public static <T> Answer from(@Nonnull String message, @Nonnull T payload) {
        return new Answer(message, 0, payload, payload.getClass());
    }

    public String getMessage() {
        return message;
    }

    public long getError() {
        return error;
    }

    @Nullable
    public Object getPayload() {
        return payload;
    }

    public <T> void setPayload(@Nonnull T payload) {
        this.payload = payload;
        //noinspection unchecked
        this.payloadClass = (Class<T>) payload.getClass();
    }

    @Nullable
    public Class<?> getPayloadClass() {
        return payloadClass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Answer)) return false;

        Answer answer = (Answer) o;

        if (error != answer.error) return false;
        if (!message.equals(answer.message)) return false;
        if (payload != null ? !payload.equals(answer.payload) : answer.payload != null) return false;
        return payloadClass != null ? payloadClass.equals(answer.payloadClass) : answer.payloadClass == null;
    }

    @Override
    public int hashCode() {
        int result = message.hashCode();
        result = 31 * result + (int) (error ^ (error >>> 32));
        result = 31 * result + (payload != null ? payload.hashCode() : 0);
        result = 31 * result + (payloadClass != null ? payloadClass.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "message='" + message + '\'' +
                ", error=" + error +
                ", payload=" + payload +
                ", payloadClass=" + (payloadClass != null ? payloadClass.getCanonicalName() : "null") +
                '}';
    }
}
