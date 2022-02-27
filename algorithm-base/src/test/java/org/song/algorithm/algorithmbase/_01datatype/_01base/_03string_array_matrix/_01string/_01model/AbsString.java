package org.song.algorithm.algorithmbase._01datatype._01base._03string_array_matrix._01string._01model;

public abstract class AbsString {

    protected final char[] s;

    protected AbsString(boolean chars, char...s) {
        this.s = s;
    }

    protected AbsString(int capacity) {
        this.s = new char[capacity];
    }

    public abstract AbsString subString(int start, int end);

    public abstract boolean eq(AbsString str);

    public abstract AbsString append(AbsString str);

    public abstract AbsString insert(int index, AbsString str);

    public abstract AbsString remove(int start, int end);

    public abstract int charAt(char c);
    
    public abstract int length();

    public abstract char indexOf(int index);
    
}
