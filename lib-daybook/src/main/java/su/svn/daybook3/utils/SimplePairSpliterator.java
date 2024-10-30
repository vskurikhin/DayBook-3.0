/*
 * This file was last modified at 2024-10-29 23:22 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * SimplePairSpliterator.java
 * $Id$
 */

package su.svn.daybook3.utils;

import java.util.Spliterator;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class SimplePairSpliterator<T, R> implements Spliterator<R> {
    Spliterator<T> source;
    boolean hasLast, hasPrev;
    private T cur;
    private final T last;
    private final BiFunction<T, T, R> mapper;

    public SimplePairSpliterator(BiFunction<T, T, R> mapper, Spliterator<T> source) {
        this(mapper, source, null, false, null, false);
    }

    public SimplePairSpliterator(
            BiFunction<T, T, R> mapper, Spliterator<T> source, T prev, boolean hasPrev, T last,
            boolean hasLast) {
        this.source = source; // исходный сплитератор
        this.hasLast = hasLast; // есть ли дополнительный элемент в конце (первый из следующего куска)
        this.hasPrev = hasPrev; // известен ли предыдущий элемент
        this.cur = prev; // предыдущий элемент
        this.last = last; // дополнительный элемент в конце
        this.mapper = mapper;
    }

    void setCur(T t) {
        cur = t;
    }

    @Override
    public boolean tryAdvance(Consumer<? super R> action) {
        if (!hasPrev) { // мы в самом начале: считаем один элемент из источника
            if (!source.tryAdvance(this::setCur)) {
                return false; // источник вообще пустой — выходим
            }
            hasPrev = true;
        }
        T prev = cur; // запоминаем предыдущий элемент
        if (!source.tryAdvance(this::setCur)) { // вычитываем следующий из источника
            if (!hasLast)
                return false; // совсем всё закончилось — выходим
            hasLast = false; // обрабатываем пару на стыке двух кусков
            cur = last;
        }
        action.accept(mapper.apply(prev, cur)); // передаём в action результат mapper'а
        return true;
    }

    public Spliterator<R> trySplit() {
        Spliterator<T> prefixSource = source.trySplit(); // пытаемся разделить источник
        if (prefixSource == null)
            return null; // не вышло — тогда мы сами тоже не делимся
        T prev = cur; // это последний считанный до сих пор элемент, если он вообще был
        if (!source.tryAdvance(this::setCur)) { // вычитываем первый элемент второй половины
            source = prefixSource; // вторая половина источника оказалась пустой — смысла делиться нет
            return null;
        }
        boolean oldHasPrev = hasPrev;
        hasPrev = true; // теперь текущий сплитератор обходит вторую половину, а для первой создаём новый
        return new SimplePairSpliterator<>(mapper, prefixSource, prev, oldHasPrev, cur, true);
    }

    @Override
    public long estimateSize() {
        long size = source.estimateSize();
        if (size == Long.MAX_VALUE) // источник не смог оценить свой размер — мы тоже не можем
            return size;
        if (hasLast) // этот сплитератор будет обрабатывать дополнительную пару на стыке кусков
            size++;
        if (!hasPrev && size > 0) // этот сплитератор ещё не вычитал первый элемент
            size--;
        return size;
    }

    @Override
    public int characteristics() {
        return source.characteristics() & (SIZED | SUBSIZED | CONCURRENT | IMMUTABLE | ORDERED);
    }
}
