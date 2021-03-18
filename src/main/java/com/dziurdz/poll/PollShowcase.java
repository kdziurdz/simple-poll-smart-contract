package com.dziurdz.poll;

import com.dziurdz.poll.client.PollClient;
import com.dziurdz.poll.server.PollServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.tx.gas.StaticGasProvider;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PollShowcase {
    private static final Logger log = LoggerFactory.getLogger(PollShowcase.class);

    static final String nodeUrl = "http://127.0.0.1:7545";
    static final String privateKey1 = "64aee4685b7156f8d29a555404117635434f1994f2a1b50f315c041e4243665d";
    static final String privateKey2 = "565fde79f07049c726d465c02dc25ca90c3df29fb61e0baabfbcef1621ac62cc";
    static final String privateKey3 = "d807b8711e9cd4ad0c92bee0195d0562bbd9dbe018e073017234926c37bd204a";

    public static void main(String[] args) throws Exception {
        Web3j web3j = Web3j.build(new HttpService(nodeUrl));
        log.info("Prepared Web3j at [{}]", nodeUrl);

        StaticGasProvider serverGasProvider = new StaticGasProvider(BigInteger.valueOf(200000000L),
                BigInteger.valueOf(6721975));

        Credentials generatedCreds = generateCredentials();
        Credentials serverCreds = Credentials.create(privateKey1);
        Credentials clientCreds2 = Credentials.create(privateKey2);
        Credentials clientCreds3 = Credentials.create(privateKey3);

        PollServer pollServer = new PollServer(web3j, serverGasProvider, serverCreds);

        List<String> allowedVotersAddresses = Stream.of(generatedCreds, clientCreds2, clientCreds3)
                .map(Credentials::getAddress)
                .collect(Collectors.toList());

        String pollAddress = pollServer.deployPoll(Arrays.asList("bitcoin", "ethereum"), allowedVotersAddresses);
        log.info("Created poll contract at [{}]", pollAddress);


        PollClient pollClient1 = new PollClient(web3j, serverGasProvider, clientCreds2, pollAddress);
        PollClient pollClient2 = new PollClient(web3j, serverGasProvider, clientCreds3, pollAddress);
        PollClient pollClient3 = new PollClient(web3j, serverGasProvider, generatedCreds, pollAddress);

        Map<BigInteger, String> options = pollClient1.getOptions();
        log.info("Poll options [{}]", options);

        // client 1 and 2 comes from Ganache pre created accounts, and the have ether
        pollClient1.vote(BigInteger.ZERO);
        pollClient2.vote(BigInteger.ONE);

        // client with generated credentials needs some ether, to pay the gas
        sendEther(web3j, clientCreds2, generatedCreds);

        pollClient3.vote(BigInteger.ONE);
        log.info("Votes committed");

        String winningOption = pollClient3.getWinningOption();
        log.info("Winning option [{}]", winningOption);
    }

    private static void sendEther(Web3j web3j, Credentials from, Credentials to) throws Exception {
        Transfer.sendFunds(
                web3j, from, to.getAddress(),
                BigDecimal.valueOf(5.0), Convert.Unit.ETHER).send();
    }

    private static Credentials generateCredentials() throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
        ECKeyPair ecKeyPair = Keys.createEcKeyPair();
        BigInteger privateKeyInDec = ecKeyPair.getPrivateKey();

        String privateKeyHex = privateKeyInDec.toString(16);
        return Credentials.create(privateKeyHex);
    }
}
