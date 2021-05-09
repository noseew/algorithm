package org.song.algorithm.algorithmbase.utils;

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

        StringBuilder sb2 = new StringBuilder();
        String[] split = sb.toString().split("");
        for (int j = 0; j < split.length; j++) {
            sb2.append(split[j]);
            if ((j + 1) % 8 == 0) {
                sb2.append(" ");
            } else if ((j + 1) % 4 == 0) {
                sb2.append("_");
            }
        }
        return sb2.toString();
    }
}
