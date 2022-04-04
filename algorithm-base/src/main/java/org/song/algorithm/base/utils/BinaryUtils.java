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
        return pretty(sb.toString());
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
        return pretty(sb.toString());
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
        return pretty(sb.toString());
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
        return pretty(sb.toString());
    }

    private static String pretty(String s) {
        StringBuilder sb2 = new StringBuilder();
        String[] split = s.split("");
        for (int j = 0; j < split.length; j++) {
            sb2.append(split[j]);
            if ((j + 1) % 16 == 0) {
                sb2.append(" ");
            } else if ((j + 1) % 8 == 0) {
                sb2.append("_");
            }
        }
        return sb2.toString();
    }
}
