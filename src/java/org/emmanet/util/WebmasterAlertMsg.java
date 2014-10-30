/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.emmanet.util;

import java.io.UnsupportedEncodingException;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author phil
 */
public class WebmasterAlertMsg {
    public boolean sendAlert(String subject, String content) throws UnsupportedEncodingException {
        System.out.println("at send alert method!");
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        try {
            Message msg;
            msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("info@infrafrontier.eu", "Infrafrontier"));
            msg.addRecipient(Message.RecipientType.TO,
                    new InternetAddress("philw@ebi.ac.uk", "Webmaster"));
            msg.setSubject(subject);
            msg.setText(content);
            msg.setHeader("X-Priority", "1"); 
            //System.out.println("Message number = " + msg.getMessageNumber());
            Transport.send(msg);

        } catch (AddressException e) {
            System.out.println(e);
        } catch (MessagingException e) {
            System.out.println(e);
        } finally {
            
    }
        return true;
    }
}
