package org.song.algorithm.base.utils;

public class BinaryUtils {

    public static String binaryPretty(int i) {
        boolean positive = i >= 0;
        String binaryString = Integer.toBinaryString(i);
        int append = Integer.SIZE - binaryString.length();
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < append; j++) {
            sb.append(positive ? 0 : 1);
        }
        sb.append(binaryString);
        return pretty(sb.toString(), 4);
    }

    public static String binaryPretty(int i, int u) {
        boolean positive = i >= 0;
        String binaryString = Integer.toBinaryString(i);
        int append = Integer.SIZE - binaryString.length();
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < append; j++) {
            sb.append(positive ? 0 : 1);
        }
        sb.append(binaryString);
        return pretty(sb.toString(), u);
    }

    public static String binaryPretty(long i) {
        boolean positive = i >= 0;
        String binaryString = Long.toBinaryString(i);
        int append = Long.SIZE - binaryString.length();
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < append; j++) {
            sb.append(positive ? 0 : 1);
        }
        sb.append(binaryString);
        return pretty(sb.toString(), 8);
    }

    public static String binaryPretty(long i, int u) {
        boolean positive = i >= 0;
        String binaryString = Long.toBinaryString(i);
        int append = Long.SIZE - binaryString.length();
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < append; j++) {
            sb.append(positive ? 0 : 1);
        }
        sb.append(binaryString);
        return pretty(sb.toString(), u);
    }

    public static String hexPretty(int i) {
        boolean positive = i >= 0;
        String binaryString = Integer.toHexString(i);
        int append = Integer.SIZE / 4 - binaryString.length();
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < append; j++) {
            sb.append(positive ? 0 : 1);
        }
        sb.append(binaryString);
        return pretty(sb.toString(), 4);
    }

    public static String hexPretty(long i) {
        boolean positive = i >= 0;
        String binaryString = Long.toHexString(i);
        int append = Long.SIZE / 4 - binaryString.length();
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < append; j++) {
            sb.append(positive ? 0 : 1);
        }
        sb.append(binaryString);
        return pretty(sb.toString(), 8);
    }

    private static String pretty(String s, int u) {
        StringBuilder sb2 = new StringBuilder();
        char[] chars = s.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            sb2.append(chars[chars.length - i - 1]);
            if ((i + 1) % (u * 2) == 0) {
                sb2.append(" ");
            } else if ((i + 1) % (u) == 0) {
                sb2.append("_");
            }
        }
        return sb2.reverse().toString();
    }
}
