package com.example.smartpantrymanager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private RadioGroup radioGroupExpiryThreshold;
    private RadioButton radio3Days;
    private RadioButton radio7Days;
    private RadioButton radio14Days;

    private RadioGroup radioGroupUnits;
    private RadioButton radioMetric;
    private RadioButton radioImperial;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sharedPreferences = getSharedPreferences("SmartPantryPrefs", MODE_PRIVATE);

        radioGroupExpiryThreshold = findViewById(R.id.radioGroupExpiryThreshold);
        radio3Days = findViewById(R.id.radio3Days);
        radio7Days = findViewById(R.id.radio7Days);
        radio14Days = findViewById(R.id.radio14Days);

        radioGroupUnits = findViewById(R.id.radioGroupUnits);
        radioMetric = findViewById(R.id.radioMetric);
        radioImperial = findViewById(R.id.radioImperial);

        Button btnSave = findViewById(R.id.btnSaveSettings);
        Button btnBack = findViewById(R.id.btnBackSettings);

        int alertDays = sharedPreferences.getInt("expiry_alert_days", 7);
        if (alertDays == 3) {
            radio3Days.setChecked(true);
        } else if (alertDays == 14) {
            radio14Days.setChecked(true);
        } else {
            radio7Days.setChecked(true);
        }

        String unitSystem = sharedPreferences.getString("unit_system", "metric");
        if ("imperial".equalsIgnoreCase(unitSystem)) {
            radioImperial.setChecked(true);
        } else {
            radioMetric.setChecked(true);
        }

        btnSave.setOnClickListener(v -> saveSettings());
        btnBack.setOnClickListener(v -> finish());
    }

    private void saveSettings() {
        int selectedDays = 7;
        int checkedExpiryId = radioGroupExpiryThreshold.getCheckedRadioButtonId();
        if (checkedExpiryId == R.id.radio3Days) {
            selectedDays = 3;
        } else if (checkedExpiryId == R.id.radio14Days) {
            selectedDays = 14;
        }

        String unitSystem = "metric";
        int checkedUnitId = radioGroupUnits.getCheckedRadioButtonId();
        if (checkedUnitId == R.id.radioImperial) {
            unitSystem = "imperial";
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("expiry_alert_days", selectedDays);
        editor.putString("unit_system", unitSystem);
        editor.apply();

        Toast.makeText(this, "Settings saved successfully!", Toast.LENGTH_SHORT).show();
        finish();
    }
}
