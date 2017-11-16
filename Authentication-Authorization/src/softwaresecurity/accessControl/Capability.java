/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package softwaresecurity.accessControl;

import java.util.Base64;
import softwaresecurity.encryptionAlgorithm.AESencrp;

/**
 *
 * @author miguel
 */
public class Capability {

    private static final String SEPARATOR = "|";

    private final String owner;
    private final String Grantee;
    private final int Nonce;
    private final String Resource;
    private final String Operation;
    private final long timeout;
    private String key;

    public Capability(String owner, String Grantee, int Nonce, String Resource, String Operation, long timeout, String key) {
        this.owner = owner;
        this.Grantee = Grantee;
        this.Nonce = Nonce;
        this.Resource = Resource;
        this.Operation = Operation;
        this.timeout = timeout;
        this.key = key;
    }

    public Capability(String encodedCapability) {
        String decodedCap = new String(Base64.getDecoder().decode(encodedCapability));
        String[] payload = decodedCap.split("\\|");

        this.owner = payload[0];
        this.Grantee = payload[1];
        this.Nonce = Integer.parseInt(payload[2]);
        this.Resource = payload[3];
        this.Operation = payload[4];
        this.timeout = Integer.parseInt(payload[5]);
        this.key = null;
    }

    public boolean equals(String resource, String operation) {
        return this.Operation.equals(operation) && this.Resource.equals(resource);
    }

    public String getOwner() {
        return owner;
    }

    public String getGrantee() {
        return Grantee;
    }

    public int getNonce() {
        return Nonce;
    }

    public String getResource() {
        return Resource;
    }

    public String getOperation() {
        return Operation;
    }

    public long getTimeout() {
        return timeout;
    }

    public String getCapability() throws Exception {
        String payload = owner + SEPARATOR + Grantee + SEPARATOR + Nonce + SEPARATOR + Resource
                + SEPARATOR + Operation + SEPARATOR + timeout;
        String enc_payload = encode(payload);

        String signed = encode(sign(enc_payload));

        return enc_payload + "." + signed;
    }

    public String encode(String string) {
        return Base64.getEncoder().encodeToString(string.getBytes());
    }

    public String decode(String string) {
        byte[] decoded = Base64.getDecoder().decode(string);

        return new String(decoded);
    }

    public String sign(String string) throws Exception {
        return AESencrp.encrypt(string, key);
    }

    public String unSigned(String string) throws Exception {
        return AESencrp.decrypt(string, key);
    }
}
