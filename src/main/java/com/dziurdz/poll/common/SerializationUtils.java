package com.dziurdz.poll.common;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SerializationUtils {

    private SerializationUtils() {
    }

    public static byte[] toBytes(int byteLength, String string) {
        byte[] byteValue = string.getBytes();
        byte[] byteValueLen32 = new byte[byteLength];
        System.arraycopy(byteValue, 0, byteValueLen32, 0, byteValue.length);
        return byteValueLen32;
    }

    public static List<byte[]> toBytesAll(int byteLength, String... strings) {
        return Arrays.stream(strings).map(str -> toBytes(byteLength, str))
                .collect(Collectors.toList());
    }

    public static String fromBytes(byte[] bytes) {
        return new String(trimTrailingZeros(bytes)).intern();
    }

    private static byte[] trimTrailingZeros(byte[] arr) {
        byte[] output = null;
        System.out.println(arr.length);
        for (int i = arr.length - 1; i >= 0; i--) {
            if (arr[i] != 0) {
                if (output == null) {
                    output = new byte[i + 1];
                }
                output[i] = arr[i];
            }
        }

        if (output == null) {
            output = new byte[0];
        }

        return output;
    }
}
