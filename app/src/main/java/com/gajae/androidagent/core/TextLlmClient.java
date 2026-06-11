package com.gajae.androidagent.core;

public interface TextLlmClient {
    String complete(String instruction, UiSnapshot snapshot);
}
