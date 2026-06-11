package com.gajae.androidagent;

import android.app.Activity;
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

public final class MainActivity extends Activity {
    private TextView status;
    private GmailLauncher launcher;
    private EditText instruction;
    private EditText endpoint;
    private EditText apiKey;
    private EditText model;
    private ConfigStore configStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        launcher = new GmailLauncher(this);
        configStore = new ConfigStore(this);
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
                    @Override public void run() { status.setText(result.message); }
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
                    @Override public void run() { status.setText(result.message); }
                });
            }
        }).start();
    }

    private void saveConfig() {
        configStore.save(endpoint.getText().toString(), apiKey.getText().toString(), model.getText().toString());
        status.setText("API 설정을 저장했습니다. 접근성 서비스를 켠 뒤 자연어 지시를 실행하세요.");
    }
}
