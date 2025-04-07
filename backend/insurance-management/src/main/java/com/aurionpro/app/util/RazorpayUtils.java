package com.aurionpro.app.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;


public class RazorpayUtils {
    public static boolean verifySignature(String orderId, String paymentId, String signature, String secret) {
        try {
            String data = orderId + "|" + paymentId;
            SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(keySpec);
            byte[] digest = mac.doFinal(data.getBytes());
            String generatedSignature = Hex.encodeHexString(digest);
            return generatedSignature.equals(signature);
        } catch (Exception e) {
            return false;
        }
    }
}

