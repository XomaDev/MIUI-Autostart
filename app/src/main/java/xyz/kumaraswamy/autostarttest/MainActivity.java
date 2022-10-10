package xyz.kumaraswamy.autostarttest;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import xyz.kumaraswamy.autostart.Autostart;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkAutoStart();
    }

    private void checkAutoStart() {
        String stateText = "Nothing";
        try {
            Autostart autostart = new Autostart(getApplicationContext());
            Autostart.State state = autostart.getAutoStartState();
            switch (state) {
                case ENABLED:
                    stateText = "Enabled";
                    break;
                case DISABLED:
                    stateText = "Disabled";
                    break;
                case NO_INFO:
                    stateText = "No Info";
                    break;
                case UNEXPECTED_RESULT:
                    stateText = "Unexpected Result";
                    break;
            }
        } catch (Exception e) {
            stateText = "Not a Xiaomi device";
        }
        TextView textView = findViewById(R.id.textView);
        textView.setText(stateText);
    }
}
