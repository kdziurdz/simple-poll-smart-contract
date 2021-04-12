package com.dziurdz.poll;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.web3j.crypto.*;

import java.io.File;
import java.io.IOException;
import java.security.*;

public class CreateAccountShowcase {

    private final static ObjectMapper objectMapper = new ObjectMapper();
    public static final String DEST = "wallets";

    public static void main(String[] args) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException, CipherException, IOException {
        String password = "pass1";
        ECKeyPair ecKeyPair = Keys.createEcKeyPair();

        WalletFile wallet = Wallet.createStandard(password, ecKeyPair);

        File walletsDestination = new File(DEST);
        walletsDestination.mkdir();

        objectMapper.writeValue(new File(DEST + "/walletObjectMapper.json"), wallet);

        WalletUtils.generateWalletFile(password, ecKeyPair, walletsDestination, true);

        WalletUtils.generateWalletFile(password, ecKeyPair, walletsDestination, false);
    }
}
