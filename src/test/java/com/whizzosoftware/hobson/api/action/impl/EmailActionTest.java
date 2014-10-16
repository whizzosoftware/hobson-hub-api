package com.whizzosoftware.hobson.api.action.impl;

import com.whizzosoftware.hobson.api.action.meta.ActionMeta;
import com.whizzosoftware.hobson.bootstrap.api.HobsonRuntimeException;
import org.junit.Test;

import javax.mail.Message;
import javax.mail.MessagingException;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import static org.junit.Assert.*;

public class EmailActionTest {
    @Test
    public void testConstructor() {
        EmailAction a = new EmailAction("pluginId");
        assertEquals(4, a.getMeta().size());
        for (ActionMeta meta : a.getMeta()) {
            assertTrue(
                EmailAction.SENDER_ADDRESS.equals(meta.getId()) ||
                "recipientAddress".equals(meta.getId()) ||
                "subject".equals(meta.getId()) ||
                "message".equals(meta.getId())
            );
        }
    }

    @Test
    public void testCreateAndSendMessageWithSMTP() throws Exception {
        EmailAction a = new EmailAction("pluginId");

        Dictionary c = new Hashtable();
        Map<String,Object> p = new HashMap<String,Object>();
        p.put(EmailAction.SENDER_ADDRESS, "foo@bar.com");
        p.put(EmailAction.RECIPIENT_ADDRESS, "bar@foo.com");
        p.put(EmailAction.SUBJECT, "Subject");
        p.put(EmailAction.MESSAGE, "Message");

        try {
            a.createAndSendMessage(c, p);
        } catch (HobsonRuntimeException ignored) {}

        c.put(EmailAction.PROP_SMTP_SERVER, "fafifwefnfdddsd");

        try {
            a.createAndSendMessage(c, p);
        } catch (HobsonRuntimeException ignored) {}

        c.put(EmailAction.PROP_USE_SMTPS, false);

        try {
            a.createAndSendMessage(c, p);
        } catch (MessagingException ignored) {}
    }

    @Test
    public void testCreateAndSendMessageWithSMTPS() throws Exception {
        EmailAction a = new EmailAction("pluginId");

        Dictionary c = new Hashtable();
        Map<String,Object> p = new HashMap<String,Object>();
        p.put(EmailAction.SENDER_ADDRESS, "foo@bar.com");
        p.put(EmailAction.RECIPIENT_ADDRESS, "bar@foo.com");
        p.put(EmailAction.SUBJECT, "Subject");
        p.put(EmailAction.MESSAGE, "Message");

        try {
            a.createAndSendMessage(c, p);
        } catch (HobsonRuntimeException ignored) {}

        c.put(EmailAction.PROP_SMTP_SERVER, "fafifwefnfdddsd");

        try {
            a.createAndSendMessage(c, p);
        } catch (HobsonRuntimeException ignored) {}

        c.put(EmailAction.PROP_USE_SMTPS, true);

        try {
            a.createAndSendMessage(c, p);
        } catch (HobsonRuntimeException ignored) {}

        c.put(EmailAction.PROP_USER, "user");

        try {
            a.createAndSendMessage(c, p);
        } catch (HobsonRuntimeException ignored) {}

        c.put(EmailAction.PROP_PASSWORD, "password");

        try {
            a.createAndSendMessage(c, p);
        } catch (MessagingException ignored) {}
    }

    @Test
    public void testCreateMessage() throws Exception {
        EmailAction a = new EmailAction("pluginId");

        Map<String,Object> p = new HashMap<String,Object>();

        try {
            a.createMessage(null, p);
            fail("Should have failed with exception");
        } catch (HobsonRuntimeException ignored) {}

        p.put(EmailAction.SENDER_ADDRESS, "foo@bar.com");

        try {
            a.createMessage(null, p);
            fail("Should have failed with exception");
        } catch (HobsonRuntimeException ignored) {}

        p.put(EmailAction.RECIPIENT_ADDRESS, "bar@foo.com");

        try {
            a.createMessage(null, p);
            fail("Should have failed with exception");
        } catch (HobsonRuntimeException ignored) {}

        p.put(EmailAction.SUBJECT, "Subject");

        try {
            a.createMessage(null, p);
            fail("Should have failed with exception");
        } catch (HobsonRuntimeException ignored) {}

        p.put(EmailAction.MESSAGE, "Message");

        Message m = a.createMessage(null, p);
        assertEquals(1, m.getFrom().length);
        assertEquals("foo@bar.com", m.getFrom()[0].toString());
        assertEquals(1, m.getAllRecipients().length);
        assertEquals("bar@foo.com", m.getAllRecipients()[0].toString());
        assertEquals("Subject", m.getSubject());
        assertEquals("Message", m.getContent());
    }
}
