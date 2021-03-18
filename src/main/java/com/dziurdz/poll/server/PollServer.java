package com.dziurdz.poll.server;

import org.web3j.crypto.Credentials;
import org.web3j.generated.contracts.SimplePoll;
import org.web3j.protocol.Web3j;
import org.web3j.tx.gas.ContractGasProvider;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static com.dziurdz.poll.common.SerializationUtils.toBytes;

public class PollServer {

    private final Web3j web3j;
    private final ContractGasProvider gasProvider;
    private final Credentials serverCredentials;

    public PollServer(Web3j web3j, ContractGasProvider gasProvider, Credentials serverCredentials) {
        this.web3j = web3j;
        this.gasProvider = gasProvider;
        this.serverCredentials = serverCredentials;
    }

    public String deployPoll(List<String> options, List<String> allowedVoters) throws Exception {
        List<byte[]> serializedOptions = serialize(options);
        SimplePoll contract = SimplePoll.deploy(web3j, serverCredentials, gasProvider,
                serializedOptions, allowedVoters)
                .send();
        return contract.getContractAddress();
    }

    private List<byte[]> serialize(Collection<String> options) {
        return options.stream()
                .map(str -> toBytes(32, str))
                .collect(Collectors.toList());
    }

}
