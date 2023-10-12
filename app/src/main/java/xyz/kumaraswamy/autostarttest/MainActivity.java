package xyz.kumaraswamy.autostarttest;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import xyz.kumaraswamy.autostart.Autostart;
import xyz.kumaraswamy.autostart.Utils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkAutoStart();
    }

    private void checkAutoStart() {
        String stateText;
        if (Utils.INSTANCE.isOnMiui()) {
            Toast.makeText(this, String.valueOf(Autostart.INSTANCE.isAutoStartEnabled(this, true)),
                    Toast.LENGTH_LONG).show();
        }
        try {
            stateText = switch (Autostart.INSTANCE.getAutoStartState(this)) {
                case ENABLED -> "Enabled";
                case DISABLED -> "Disabled";
                case NO_INFO -> "No Info";
                case UNEXPECTED_RESULT -> "Unexpected Result";
            };
        } catch (Exception e) {
            stateText = "Not a Xiaomi device";
        }
        TextView textView = findViewById(R.id.textView);
        textView.setText(stateText);
    }
}
