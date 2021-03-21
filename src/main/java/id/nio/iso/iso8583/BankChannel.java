/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.nio.iso.iso8583;

import java.io.IOException;
import java.net.ServerSocket;
import org.jpos.iso.BaseChannel;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOPackager;
import org.jpos.iso.ISOUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BankChannel extends BaseChannel {

    public final String DEFAULT_DATE_FORMAT = "yyyyMMddHHmmss";
    private final Logger LOGGER = LoggerFactory.getLogger(BankChannel.class);

    @Override
    protected void sendMessageLength(int len) throws IOException {
        int b0 = (len / 256);
        int b1 = len % 256;

        byte[] b = new byte[2];
        b[0] = (byte) b0;
        b[1] = (byte) b1;
        serverOut.write(b);
    }

    @Override
    protected int getMessageLength() throws IOException, ISOException {
        int l = 0;
        int msglength = 0;
        byte[] b = new byte[2];
        while (l == 0) {
            serverIn.readFully(b, 0, 2);
            msglength = (Integer.parseInt(ISOUtil.hexString(b, 0, 1), 16) * 256)
                    + (Integer.parseInt(ISOUtil.hexString(b, 1, 1), 16));
            try {
                if ((l = msglength) == 0) {
                    serverOut.write(b);
                    serverOut.flush();
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                throw new ISOException("Invalid message length " + new String(b) + "length=" + l);
            }
        }
        return l;
    }

    @Override
    public void connect() throws IOException {
        LOGGER.error("======== CONNECTING TO BANK SERVER ===============");
        super.connect();
    }
    
    @Override
    public void disconnect() throws IOException {
//        sendSignoff();
        super.disconnect();
    }

    public BankChannel(ISOPackager p, ServerSocket serverSocket) throws IOException {
        super(p, serverSocket);
    }

    public BankChannel(ISOPackager p) throws IOException {
        super(p);
    }

    public BankChannel(String host, int port, ISOPackager p) {
        super(host, port, p);
    }
    
    public BankChannel() {
    }

}