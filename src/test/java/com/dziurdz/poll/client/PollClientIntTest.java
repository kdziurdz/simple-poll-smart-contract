package com.dziurdz.poll.client;

import com.dziurdz.poll.EmbeddedTestData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.web3j.EVMTest;
import org.web3j.crypto.Credentials;
import org.web3j.generated.contracts.SimplePoll;

import java.math.BigInteger;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.dziurdz.poll.common.SerializationUtils.toBytesAll;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@EVMTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PollClientIntTest {

    private EmbeddedTestData testData;
    private final List<byte[]> options = toBytesAll(32, "a", "b");

    private PollClient client;

    @BeforeAll
    void initializeTestData() {
        testData = EmbeddedTestData.initialize();
    }

    @BeforeEach
    void beforeEach() throws Exception {
        Credentials clientCredentials = testData.bob();
        SimplePoll pollContract = SimplePoll.deploy(testData.web3j(), testData.alice(), testData.gasProvider(),
                options, Collections.singletonList(clientCredentials.getAddress())).send();

        client = new PollClient(testData.web3j(), testData.gasProvider(), testData.bob(),
                pollContract.getContractAddress());
    }

    @Test
    void shouldGetOptions() throws Exception {
        //given
        Map<BigInteger, String> expectedResult = new HashMap<>();
        expectedResult.put(BigInteger.ZERO, "a");
        expectedResult.put(BigInteger.ONE, "b");

        //when
        Map<BigInteger, String> result = client.getOptions();

        //then
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void shouldGetWinningOption() throws Exception {
        //given
        String expectedResult = "a";

        //when
        String result = client.getWinningOption();

        //then
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void shouldGetWinningOptionWhenVoted() throws Exception {
        //given
        String expectedResult = "b";

        //when
        client.vote(BigInteger.ONE);
        String result = client.getWinningOption();

        //then
        assertThat(result).isEqualTo(expectedResult);
    }
}