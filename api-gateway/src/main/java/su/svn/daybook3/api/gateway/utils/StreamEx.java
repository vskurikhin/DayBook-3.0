/*
 * This file was last modified at 2024-05-14 21:35 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * StreamEx.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.utils;

import java.util.function.BiFunction;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class StreamEx {

    /**
     * Returns a stream consisting of the results of applying the given function
     * to the every adjacent pair of elements of this stream.
     *
     * <p>
     * This is a <a href="package-summary.html#StreamOps">quasi-intermediate</a>
     * operation.
     *
     * <p>
     * The output stream will contain one element less than this stream. If this
     * stream contains zero or one element the output stream will be empty.
     *
     * @param <R> The element type of the new stream
     * @param mapper a non-interfering, stateless function to apply to each
     *        adjacent pair of this stream elements.
     * @return the new stream
     * @since 0.2.1
     */
    public static <T, R> Stream<R> pairMap(Stream<T> stream, BiFunction<T, T, R> mapper) {
        return StreamSupport.stream(new SimplePairSpliterator<>(mapper, stream.spliterator()), stream.isParallel())
                .onClose(stream::close);
    }
}
