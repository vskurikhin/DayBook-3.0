package su.svn.daybook.messages;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Command<T> {

    private final String command;

    private Object payload;

    private Class<T> payloadClass;

    public Command(@Nonnull String command) {
        this.command = command;
    }

    public Command(@Nonnull String command, @Nonnull T payload) {
        this.command = command;
        this.payload = payload;
        //noinspection unchecked
        this.payloadClass = (Class<T>) payload.getClass();
    }

    public String getCommand() {
        return command;
    }

    @Nullable
    public Object getPayload() {
        return payload;
    }

    public void setPayload(@Nonnull T payload) {
        this.payload = payload;
        //noinspection unchecked
        this.payloadClass = (Class<T>) payload.getClass();
    }

    @Nullable
    public Class<T> getPayloadClass() {
        return payloadClass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Command)) return false;

        Command<?> command1 = (Command<?>) o;

        if (!command.equals(command1.command)) return false;
        if (payload != null ? !payload.equals(command1.payload) : command1.payload != null) return false;
        return payloadClass != null ? payloadClass.equals(command1.payloadClass) : command1.payloadClass == null;
    }

    @Override
    public int hashCode() {
        int result = command.hashCode();
        result = 31 * result + (payload != null ? payload.hashCode() : 0);
        result = 31 * result + (payloadClass != null ? payloadClass.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Command{" +
                "command='" + command + '\'' +
                ", payload=" + payload +
                ", payloadClass=" + (payloadClass != null ? payloadClass.getCanonicalName() : "null") +
                '}';
    }
}
