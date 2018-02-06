package com.ibm.tfs.service.model.speech_to_text;

public interface MessageHandlerEX {
    public void handleMessage(String message, boolean isConnectionReset);
}
