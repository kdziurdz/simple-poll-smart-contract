package com.dziurdz.poll;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;

import java.util.Map;

public class BesuTestData extends TestData {

    // from https://besu.hyperledger.org/en/stable/Reference/Accounts-for-Testing/
    private static final String START_ACCOUNT_SK = "0x8f2a55949038a9610f50fb23b5883af3b4ecb3c3bb792cbcefbd1542c692be63";

    static public BesuTestData initialize(Web3j web3j) {
        Map<Person, Credentials> persons = prepareCredentials();

        Credentials startAccount = Credentials.create(START_ACCOUNT_SK);

        BesuTestData testData = new BesuTestData(persons, web3j);

        shareEther(persons, testData, startAccount);

        return testData;
    }

    private BesuTestData(Map<Person, Credentials> accounts, Web3j web3j) {
        super(accounts, web3j);
    }
}
