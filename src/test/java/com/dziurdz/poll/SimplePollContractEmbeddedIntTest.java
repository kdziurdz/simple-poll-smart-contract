package com.dziurdz.poll;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.web3j.EVMTest;
import org.web3j.generated.contracts.SimplePoll;
import org.web3j.protocol.exceptions.TransactionException;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

import static com.dziurdz.poll.common.SerializationUtils.toBytesAll;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@EVMTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SimplePollContractEmbeddedIntTest {

    private EmbeddedTestData testData;

    @BeforeAll
    void initializeTestData() {
        testData = EmbeddedTestData.initialize();
    }

    @Test
    public void shouldGetWinnerNameWhenVoted() throws Exception {
        //given
        List<String> allowedVoters = Collections.singletonList(testData.bob().getAddress());
        List<byte[]> optionNames = toBytesAll(32, "first", "second");

        //and
        SimplePoll serverPoll = SimplePoll.deploy(testData.web3j(), testData.alice(),
                testData.gasProvider(), optionNames, allowedVoters).send();
        SimplePoll clientPoll = SimplePoll.load(serverPoll.getContractAddress(),
                testData.web3j(), testData.bob(), testData.gasProvider());

        //when
        clientPoll.vote(BigInteger.ONE).send();
        byte[] winnerName = clientPoll.winnerName().send();

        //then
        assertThat(winnerName).isEqualTo(optionNames.get(1));
    }

    @Test
    public void shouldGetWinnerNumberWhenVoted() throws Exception {
        //given
        List<String> allowedVoters = Collections.singletonList(testData.bob().getAddress());
        List<byte[]> optionNames = toBytesAll(32, "first", "second");

        //and
        SimplePoll serverPoll = SimplePoll.deploy(testData.web3j(), testData.alice(),
                testData.gasProvider(), optionNames, allowedVoters).send();
        SimplePoll clientPoll = SimplePoll.load(serverPoll.getContractAddress(),
                testData.web3j(), testData.bob(), testData.gasProvider());

        //when
        clientPoll.vote(BigInteger.ONE).send();
        BigInteger winnerName = clientPoll.winningOption().send();

        //then
        assertThat(winnerName).isEqualTo(1);
    }

    @Test
    public void shouldReturnFirstOptionWhenNoVotes() throws Exception {
        //given
        List<String> allowedVoters = Collections.singletonList(testData.bob().getAddress());
        List<byte[]> optionNames = toBytesAll(32, "first", "second");

        //and
        SimplePoll serverPoll = SimplePoll.deploy(testData.web3j(), testData.alice(),
                testData.gasProvider(), optionNames, allowedVoters).send();
        SimplePoll clientPoll = SimplePoll.load(serverPoll.getContractAddress(),
                testData.web3j(), testData.bob(), testData.gasProvider());

        //when
        BigInteger defaultWinningOption = clientPoll.winningOption().send();

        //then
        assertThat(defaultWinningOption).isEqualTo(0);
    }

    @Test
    public void shouldGetOptionsCount() throws Exception {
        //given
        List<String> allowedVoters = Collections.singletonList(testData.bob().getAddress());
        List<byte[]> optionNames = toBytesAll(32, "first", "second");

        //and
        SimplePoll serverPoll = SimplePoll.deploy(testData.web3j(), testData.alice(),
                testData.gasProvider(), optionNames, allowedVoters).send();
        SimplePoll clientPoll = SimplePoll.load(serverPoll.getContractAddress(),
                testData.web3j(), testData.bob(), testData.gasProvider());

        //when
        BigInteger optionsCount = clientPoll.getOptionsCount().send();

        //then
        assertThat(optionsCount).isEqualTo(2);
    }

    @Test
    public void shouldThrowWhenNotAuthorizedVoteCommited() throws Exception {
        //given
        List<String> allowedVoters = Collections.singletonList(testData.bob().getAddress());
        List<byte[]> optionNames = toBytesAll(32, "first", "second");

        //and
        SimplePoll serverPoll = SimplePoll.deploy(testData.web3j(), testData.alice(),
                testData.gasProvider(), optionNames, allowedVoters).send();
        SimplePoll clientPoll = SimplePoll.load(serverPoll.getContractAddress(),
                testData.web3j(), testData.vitalik(), testData.gasProvider());

        //when
        //then
        assertThrows(TransactionException.class, () -> clientPoll.vote(BigInteger.ONE).send());
    }
}
