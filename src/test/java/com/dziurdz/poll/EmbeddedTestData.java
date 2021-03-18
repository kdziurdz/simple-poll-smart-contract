package com.dziurdz.poll;

import org.hyperledger.besu.ethereum.vm.OperationTracer;
import org.web3j.abi.datatypes.Address;
import org.web3j.crypto.Credentials;
import org.web3j.evm.Configuration;
import org.web3j.evm.ConsoleDebugTracer;
import org.web3j.evm.EmbeddedWeb3jService;
import org.web3j.protocol.Web3j;

import java.util.Map;

import static com.dziurdz.poll.TestUtils.generateCredentials;

public class EmbeddedTestData extends TestData {

    public static final int START_ETH = USERS_ETH * (Person.values().length + 1);

    static public EmbeddedTestData initialize() {
        Map<Person, Credentials> persons = prepareCredentials();

        Credentials startAccount = generateCredentials();
        Web3j web3j = prepareWeb3j(startAccount);
        EmbeddedTestData testData = new EmbeddedTestData(persons, web3j);

        shareEther(persons, testData, startAccount);

        return testData;
    }


    private static Web3j prepareWeb3j(Credentials startAccount) {
        Configuration configuration = new Configuration(new Address(startAccount.getAddress()), START_ETH);
        OperationTracer operationTracer = new ConsoleDebugTracer();
        return Web3j.build(new EmbeddedWeb3jService(configuration, operationTracer));
    }

    private EmbeddedTestData(Map<Person, Credentials> accounts, Web3j web3j) {
        super(accounts, web3j);
    }
}
