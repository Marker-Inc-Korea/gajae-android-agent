package com.gajae.androidagent;

import android.app.Activity;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.gajae.androidagent.core.AgentResult;
import com.gajae.androidagent.core.GmailScenario;
import com.gajae.androidagent.core.NoopVlmClient;
import com.gajae.androidagent.core.UniversalAppAgent;
import com.gajae.androidagent.core.SpokenResponseLimiter;

public final class MainActivity extends Activity implements VoiceCallback {
    private TextView status;
    private GmailLauncher launcher;
    private EditText instruction;
    private EditText endpoint;
    private EditText apiKey;
    private EditText model;
    private ConfigStore configStore;
    private SpeechInputController speechInput;
    private SpeechOutputController speechOutput;
    private VoiceState voiceState = VoiceState.IDLE;
    private final SpokenResponseLimiter spokenLimiter = new SpokenResponseLimiter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        launcher = new GmailLauncher(this);
        configStore = new ConfigStore(this);
        speechOutput = new SpeechOutputController(this);
        speechInput = new SpeechInputController(this, this);
        requestAudioPermission();
        setContentView(buildUi());
    }

    private ScrollView buildUi() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        int pad = 32;
        layout.setPadding(pad, pad, pad, pad);

        TextView title = new TextView(this);
        title.setText("Gajae Android Agent\n모든 앱을 접근성 텍스트로 우선 조작하는 프로토타입");
        title.setTextSize(20f);
        layout.addView(title);

        Button settings = new Button(this);
        settings.setText("접근성 설정 열기");
        settings.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { launcher.openAccessibilitySettings(); }
        });
        layout.addView(settings);

        instruction = new EditText(this);
        instruction.setMinLines(3);
        instruction.setHint("예: Gmail 앱에서 새로 온 이메일을 읽고 설명해줘\n예: 카카오톡에서 최근 메시지를 요약해줘");
        layout.addView(instruction);

        endpoint = new EditText(this);
        endpoint.setHint("VLM/LLM API endpoint URL");
        apiKey = new EditText(this);
        apiKey.setHint("API key");
        model = new EditText(this);
        model.setHint("Model name, e.g. gpt-4.1-mini or custom-vlm");
        layout.addView(endpoint);
        layout.addView(apiKey);
        layout.addView(model);

        Button save = new Button(this);
        save.setText("API 설정 저장");
        save.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { saveConfig(); }
        });
        layout.addView(save);

        Button generic = new Button(this);
        generic.setText("자연어 지시 실행");
        generic.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { runUniversalInstruction(); }
        });
        layout.addView(generic);

        Button listen = new Button(this);
        listen.setText("말로 지시하기 / 끼어들기");
        listen.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { startVoiceTurn(); }
        });
        layout.addView(listen);

        Button stopVoice = new Button(this);
        stopVoice.setText("말 끊기");
        stopVoice.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { stopVoiceNow(); }
        });
        layout.addView(stopVoice);

        Button run = new Button(this);
        run.setText("Gmail 새 메일 요약 실행");
        run.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { runGmailScenario(); }
        });
        layout.addView(run);

        status = new TextView(this);
        status.setText("대기 중입니다. 먼저 접근성 서비스를 활성화하세요.");
        status.setTextSize(16f);
        layout.addView(status);

        ScrollView scroll = new ScrollView(this);
        scroll.addView(layout);
        return scroll;
    }

    private void runGmailScenario() {
        status.setText("Gmail 실행 및 화면 읽기 중...");
        GmailScenario scenario = new GmailScenario(
                new GmailScenario.SnapshotProvider() {
                    @Override public com.gajae.androidagent.core.UiSnapshot latestSnapshot() { return SnapshotStore.get(); }
                },
                new GmailScenario.AppActions() {
                    @Override public boolean launch(String packageName) { return launcher.launch(packageName); }
                    @Override public void waitMillis(long millis) {
                        try { Thread.sleep(millis); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                    }
                },
                new NoopVlmClient());
        new Thread(new Runnable() {
            @Override public void run() {
                final AgentResult result = scenario.run();
                runOnUiThread(new Runnable() {
                    @Override public void run() { publishResult(result.message); }
                });
            }
        }).start();
    }

    private void runUniversalInstruction() {
        final String text = instruction.getText().toString();
        status.setText("앱 탐색 및 접근성 화면 읽기 중...");
        final UniversalAppAgent agent = new UniversalAppAgent(
                new GmailScenario.SnapshotProvider() {
                    @Override public com.gajae.androidagent.core.UiSnapshot latestSnapshot() { return SnapshotStore.get(); }
                },
                new GmailScenario.AppActions() {
                    @Override public boolean launch(String packageName) { return launcher.launch(packageName); }
                    @Override public void waitMillis(long millis) {
                        try { Thread.sleep(millis); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                    }
                },
                new AppResolver(this),
                new AndroidActionPerformer());
        new Thread(new Runnable() {
            @Override public void run() {
                final AgentResult result = agent.run(text);
                runOnUiThread(new Runnable() {
                    @Override public void run() { publishResult(result.message); }
                });
            }
        }).start();
    }

    private void saveConfig() {
        configStore.save(endpoint.getText().toString(), apiKey.getText().toString(), model.getText().toString());
        status.setText("API 설정을 저장했습니다. 접근성 서비스를 켠 뒤 자연어 지시를 실행하세요.");
    }

    private void startVoiceTurn() {
        speechOutput.stop();
        speechInput.cancel();
        voiceState = VoiceState.LISTENING;
        status.setText("듣고 있습니다. 말씀하세요.");
        speechInput.start();
    }

    private void stopVoiceNow() {
        speechInput.cancel();
        speechOutput.stop();
        voiceState = VoiceState.IDLE;
        status.setText("음성을 중단했습니다.");
    }

    private void publishResult(String message) {
        String shortText = spokenLimiter.shortAnswer(message);
        status.setText(shortText);
        voiceState = VoiceState.SPEAKING;
        speechOutput.speak(shortText);
    }

    private void requestAudioPermission() {
        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] { Manifest.permission.RECORD_AUDIO }, 42);
        }
    }

    @Override public void onPartialText(String text) {
        instruction.setText(text);
        instruction.setSelection(instruction.length());
    }

    @Override public void onFinalText(String text) {
        instruction.setText(text);
        instruction.setSelection(instruction.length());
        voiceState = VoiceState.THINKING;
        runUniversalInstruction();
    }

    @Override public void onVoiceError(String message) {
        voiceState = VoiceState.IDLE;
        status.setText(message);
    }

    @Override protected void onDestroy() {
        speechInput.destroy();
        speechOutput.destroy();
        super.onDestroy();
    }
}
