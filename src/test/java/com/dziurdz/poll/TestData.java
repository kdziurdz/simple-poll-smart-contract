package com.dziurdz.poll;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.tx.Transfer;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import static com.dziurdz.poll.TestUtils.generateCredentials;

/**
 * Both Besu and Embedded EVM comes with prefunded account(s) with some amount of Ether
 * This class distributes Ether between test users and provides convenient handlers to the accounts
 * Additionally provides access to correct instance of web3j.
 * DefaultGasProvider is also here just to not repeat initialization in tests
 */
public abstract class TestData {
    protected static final int USERS_ETH = 10;

    private final Map<Person, Credentials> accounts;
    private final Web3j web3j;
    private final DefaultGasProvider gasProvider;

    protected TestData(Map<Person, Credentials> accounts, Web3j web3j) {
        this.accounts = accounts;
        this.web3j = web3j;
        this.gasProvider = new DefaultGasProvider();
    }

    public Web3j web3j() {
        return web3j;
    }

    public DefaultGasProvider gasProvider() {
        return gasProvider;
    }

    public Credentials alice() {
        return accounts.get(Person.ALICE);
    }

    public Credentials bob() {
        return accounts.get(Person.BOB);
    }

    public Credentials vitalik() {
        return accounts.get(Person.VITALIK);
    }

    public Credentials satoshi() {
        return accounts.get(Person.SATOSHI);
    }

    protected enum Person {
        ALICE,
        BOB,
        VITALIK,
        SATOSHI
    }

    protected static Map<Person, Credentials> prepareCredentials() {
        return Arrays.stream(Person.values())
                .collect(Collectors.toMap(person -> person, person -> generateCredentials()));
    }

    protected static void shareEther(Map<Person, Credentials> persons, TestData testData, Credentials startAccount) {
        persons.forEach((person, personCredentials) -> {
            try {
                sendEther(testData.web3j(), startAccount, personCredentials);
            } catch (Exception e) {
                throw new RuntimeException("Cannot transfer ether to user");
            }
        });
    }

    protected static void sendEther(Web3j web3j, Credentials startAccount, Credentials credentials) throws Exception {
        Transfer.sendFunds(
                web3j, startAccount, credentials.getAddress(),
                BigDecimal.valueOf(USERS_ETH), Convert.Unit.ETHER).send();
    }
}
