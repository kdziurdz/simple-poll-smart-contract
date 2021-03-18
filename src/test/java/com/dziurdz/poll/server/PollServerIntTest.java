package com.dziurdz.poll.server;

import com.dziurdz.poll.EmbeddedTestData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.web3j.EVMTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@EVMTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PollServerIntTest {

    private EmbeddedTestData testData;

    private PollServer server;

    @BeforeAll
    void initializeTestData() {
        testData = EmbeddedTestData.initialize();
    }

    @BeforeEach
    void beforeEach() {
        server = new PollServer(testData.web3j(), testData.gasProvider(), testData.alice());
    }

    @Test
    void shouldDeployPoll() throws Exception {
        //given
        List<String> allowedVoters = Collections.emptyList();
        List<String> options = Arrays.asList("a", "b");

        //when
        String result = server.deployPoll(options, allowedVoters);

        //then
        assertThat(result).isNotBlank();
    }
}