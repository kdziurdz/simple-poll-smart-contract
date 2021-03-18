package com.dziurdz.poll.client;

import org.web3j.crypto.Credentials;
import org.web3j.generated.contracts.SimplePoll;
import org.web3j.protocol.Web3j;
import org.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.dziurdz.poll.common.SerializationUtils.fromBytes;

public class PollClient {
    private final SimplePoll poll;

    public PollClient(Web3j web3j, ContractGasProvider gasProvider, Credentials clientCredentials,
                      String pollAddress) {
        this.poll = SimplePoll.load(pollAddress, web3j, clientCredentials, gasProvider);
    }

    public Map<BigInteger, String> getOptions() throws Exception {
        BigInteger optionsCount = poll.getOptionsCount().send();

        return Stream.iterate(BigInteger.ZERO,
                i -> i.compareTo(optionsCount) < 0,
                i -> i.add(BigInteger.ONE))
                .collect(Collectors.toMap(i -> i, this::getOptionValue));
    }

    private String getOptionValue(BigInteger i) {
        try {
            return fromBytes(poll.options(i).send().component1());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void vote(BigInteger optionNumber) throws Exception {
        poll.vote(optionNumber).send();
    }

    public String getWinningOption() throws Exception {
        return fromBytes(poll.winnerName().send());
    }
}
