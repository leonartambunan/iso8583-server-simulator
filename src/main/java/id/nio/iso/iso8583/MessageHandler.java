/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.nio.iso.iso8583;

import java.io.IOException;
import java.util.logging.Logger;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISORequestListener;
import org.jpos.iso.ISOSource;

/**
 *
 * @author Esa Dewa
 */
public class MessageHandler implements ISORequestListener {
    private final static Logger logger = Logger.getLogger(MessageHandler.class.getName());
    @Override
    public boolean process(ISOSource isos, ISOMsg isomsg) {
        try {

            System.out.println("MTI:"+isomsg.getMTI());

            if (isomsg.getMTI().equalsIgnoreCase("0800")) {
                return handleNetman(isos, isomsg);
            } else if (isomsg.getMTI().equalsIgnoreCase("0200")) {
                return handle0200(isos, isomsg);
            } else if (isomsg.getMTI().equalsIgnoreCase("0400")) {
                return handle0420(isos, isomsg);
            } else {
                System.out.println("INVALID MTI");
                return false;
            }
        } catch (ISOException | InterruptedException | IOException ex) {
            logger.warning(ex.getMessage());
        }
        return true;
    }

    private boolean handleNetman(ISOSource iSOSource, ISOMsg iSOMsg) throws InterruptedException, IOException, ISOException {
        ISOMsg response = (ISOMsg) iSOMsg.clone();
        response.setMTI("0810");
        response.set(39, "00");
        send(iSOSource, response, false, 0);
        return true;
    }
    private boolean handle0200(ISOSource iSOSource, ISOMsg iSOMsg) throws InterruptedException, IOException, ISOException {
        ISOMsg response = (ISOMsg) iSOMsg.clone();
        response.setMTI("0210");
        response.set(39, "00");
        send(iSOSource, response, false, 0);
        return true;
    }

    private boolean handle0420(ISOSource iSOSource, ISOMsg iSOMsg) throws InterruptedException, IOException, ISOException {
        ISOMsg response = (ISOMsg) iSOMsg.clone();
        response.setMTI("0421");
        response.set(39, "00");
        send(iSOSource, response, false, 0);
        return true;
    }

    private void send(ISOSource iSOSource, ISOMsg iSOMsg, boolean isReversal, int delay) throws InterruptedException, IOException, ISOException {
        if (!isReversal) {
            Thread.sleep(delay * 1000);
            iSOSource.send(iSOMsg);
        }
    }

}