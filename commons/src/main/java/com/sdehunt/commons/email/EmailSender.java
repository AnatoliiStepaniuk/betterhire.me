package com.sdehunt.commons.email;

import java.util.Map;

public interface EmailSender {

    void send(String from, String to, String templateId, Map<String, Object> data);
}
