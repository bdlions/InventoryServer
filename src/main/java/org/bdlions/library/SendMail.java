package org.bdlions.library;

import org.bdlions.email.MailSender;
import org.bdlions.email.MailUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author nazmul hasan
 */
public class SendMail {
    private final Logger logger = LoggerFactory.getLogger(SendMail.class);
    public boolean sendSignUpMail(String mailTo)
    {
        boolean status = false;
        // outgoing message information
        //mailTo = "nazhasan15@yopmail.com";
        String subject = "Room Auction Email Verification";

        //generate a random code and store that into the database, deactivate account
        Map<String, String> input = new HashMap<>();
        input.put("v_link", "http://roomauction.co.uk/Verifycode?vCode=" + UUID.randomUUID().toString());
        input.put("c_link", "http://roomauction.co.uk/Verifycode?cCode=" + UUID.randomUUID().toString());
        input.put("email", mailTo);

        //HTML mail content
        String htmlText = MailUtil.readEmailFromHtml("mail-templates/mail-verification.html", input);

        MailSender mailer = new MailSender();

        try {
            mailer.sendHtmlEmail(mailTo, subject, htmlText);
            status = true;
            //System.out.println("Email sent.");
        } catch (Exception ex) {
            status = false;
            //System.out.println("Failed to sent email.");
            logger.error(ex.toString());
        }
        return status;
    }
}
