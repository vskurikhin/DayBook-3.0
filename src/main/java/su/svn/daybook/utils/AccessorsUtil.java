package su.svn.daybook.utils;

import org.jboss.logging.Logger;
import su.svn.daybook.converters.getters.GettersAnnotatedDomainFiled;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Function;

public class AccessorsUtil
{
    private static final Logger LOG = Logger.getLogger(GettersAnnotatedDomainFiled.class);
    public static final String GETTER_PREFIX_GET = "get";
    public static final String GETTER_PREFIX_IS = "is";
    public static final String GETTER_PREFIX_HAS = "has";
    public static final String[] BOOLEAN_GETTER_PREFIXES = new String[]{GETTER_PREFIX_IS, GETTER_PREFIX_HAS};

    public static boolean isBooleanField(Field field) {
        return field.getType() == boolean.class || field.getType().equals(Boolean.class);
    }

    public static boolean isCorrectFieldAndMethodName(String fieldName, String methodName) {
        return fieldName != null && fieldName.length() > 0
                && methodName != null && methodName.length() > 0;
    }

    public static boolean isMethodNameEndsWithFieldName(String methodName, String fieldName) {
        return methodName.toLowerCase().endsWith(fieldName.toLowerCase());
    }

    public static Function<Object, Object> getterFunction(@Nonnull Method getter) {
        return o -> {
            try {
                return getter.invoke(o);
            } catch (InvocationTargetException | IllegalAccessException e) {
                LOG.errorf("in getter function %s ", getter.getName(), e);
            }
            return null;
        };
    }

    /**
     * Checks whether the given executable is a valid JavaBean getter method, which
     * is the case if
     * <ul>
     * <li>its name starts with "get" and it has a return type but no parameter or</li>
     * <li>its name starts with "is", it has no parameter and is returning
     * {@code boolean} or</li>
     * <li>its name starts with "has", it has no parameter and is returning
     * {@code boolean} (HV-specific, not mandated by the JavaBeans spec).</li>
     * </ul>
     *
     * @param method The executable of interest.
     *
     * @return {@code true}, if the given executable is a JavaBean getter method,
     * {@code false} otherwise.
     */
    public static boolean isGetter(Method method) {
        if ( method.getParameterTypes().length != 0 ) {
            return false;
        }

        String methodName = method.getName();

        if (methodName.startsWith(GETTER_PREFIX_GET) && method.getReturnType() != void.class) {
            return true;
        } else if (methodName.startsWith(GETTER_PREFIX_IS)
                && (method.getReturnType() == boolean.class || Boolean.class.equals(method.getReturnType()))) {
            return true;
        } else return methodName.startsWith(GETTER_PREFIX_HAS)
                && (method.getReturnType() == boolean.class || Boolean.class.equals(method.getReturnType()));
    }
}
