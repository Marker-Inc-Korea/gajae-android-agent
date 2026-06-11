package com.gajae.androidagent.core;

public final class RuleBasedTextLlmClient implements TextLlmClient {
    @Override
    public String complete(String instruction, UiSnapshot snapshot) {
        int count = snapshot.textNodeCount();
        if (count == 0) return "화면에서 읽을 수 있는 텍스트가 없습니다.";
        return "접근성 트리에서 " + count + "개의 텍스트 노드를 읽었습니다.";
    }
}
