package com.dziurdz.poll.common;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class SerializationUtilsTest {

    @Test
    void shouldEncodeIntoBytes() {
        //given
        int length = 32;
        String str = "str";

        //and
        byte[] expectedResult = new byte[]{115, 116, 114, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        //when
        byte[] result = SerializationUtils.toBytes(length, str);

        //then
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void shouldEncodeAllIntoBytes() {
        //given
        int length = 32;
        String str = "str";

        //and
        byte[] singleExpectedResult = new byte[]{115, 116, 114, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};


        //when
        List<byte[]> result = SerializationUtils.toBytesAll(length, str, str);

        //then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0)).isEqualTo(singleExpectedResult);
        assertThat(result.get(1)).isEqualTo(singleExpectedResult);
    }

    @Test
    void shouldDecodeBytesIntoStringWithTrimmedZeroes() {
        //given
        String expectedResultStr = "str";
        byte[] expectedResultInBytes = new byte[]{115, 116, 114};

        //and
        byte[] byteArray = new byte[]{115, 116, 114, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};


        //when
        String result = SerializationUtils.fromBytes(byteArray);

        //then
        assertThat(result).isEqualTo(expectedResultStr);
        assertThat(result.getBytes()).isEqualTo(expectedResultInBytes);
    }
}