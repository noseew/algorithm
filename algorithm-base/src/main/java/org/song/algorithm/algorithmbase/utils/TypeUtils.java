package org.song.algorithm.algorithmbase.utils;

public class TypeUtils {

    /**
     * 自动转型工具, 方便向下转型
     *
     * @param obj
     * @param sClass
     * @param <P>
     * @param <S>
     * @return
     */
    @SuppressWarnings("all")
    public static <P, S extends P> S down(P obj, Class<S> sClass) {
        return (S) obj;
    }
}
