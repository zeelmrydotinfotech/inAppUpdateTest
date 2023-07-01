package com.tridhyaintuit.mynotes.Activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rydotinfotech.mynotes.R;
import com.tridhyaintuit.mynotes.Boadcasts.NotificationReceiver;

import java.util.Calendar;

public class AddNote extends AppCompatActivity {

    private EditText title, description;
    private Button save, cancel;
    int noteId;
    private LinearLayout linearLayout;
    TextView tvHour, tvMinute;

    int selectedHour;
    int selectedMin;
    boolean done = false;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference();
    DatabaseReference reference = databaseReference.child("Reminder Time");
    DatabaseReference getReference = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Add Note");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_add);

        title = findViewById(R.id.et_name);
        description = findViewById(R.id.et_description);
        save = findViewById(R.id.btn_save);
        cancel = findViewById(R.id.btn_cancel);
        linearLayout = findViewById(R.id.ln_time);
        tvHour = findViewById(R.id.tv_hour);
        tvMinute = findViewById(R.id.tv_minute);

        if (noteId != -1) {
            updateSavedNote();
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newNoteSave();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AddNote.this, "Canceled", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void newNoteSave() {
        String noteTitle = title.getText().toString();
        String noteDescription = description.getText().toString();

        Intent i = new Intent(this, NoteList.class);
        i.putExtra("noteData", "data");
        if (noteId != -1) {
            i.putExtra("noteId", noteId);
        }
        i.putExtra("noteTitle", noteTitle);
        i.putExtra("noteDescription", noteDescription);
        initReminder(selectedHour, selectedMin);
        addDataInFirebase(noteTitle);
        startActivity(i);
        Toast.makeText(AddNote.this, "Note Saved", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void addDataInFirebase(String noteTitle) {
        reference.child("Note").child("Hour").setValue(selectedHour);
        reference.child("Note").child("Minute").setValue(selectedMin);
    }


    public void updateSavedNote() {
        Intent updatedData = getIntent();
        noteId = updatedData.getIntExtra("id", -1);
        String noteTitle = updatedData.getStringExtra("name");
        String noteDescription = updatedData.getStringExtra("description");

        title.setText(noteTitle);
        description.setText(noteDescription);
    }

    public void initReminder(int selectedHour, int selectedMin) {
        Calendar calender = Calendar.getInstance();
        calender.set(Calendar.HOUR_OF_DAY, selectedHour);
        calender.set(Calendar.MINUTE, selectedMin);
        calender.set(Calendar.SECOND, 0);

        Intent intent = new Intent(getApplicationContext(), NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 100, intent
                , PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calender.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    public void alertAddTime() {
        Calendar calendar = Calendar.getInstance();
        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);

        int mHour = calendar.get(Calendar.HOUR);
        int mMinute = calendar.get(Calendar.MINUTE);

//        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
//                new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
//                        String selectedDate = (day + "-" + (month + 1) + "-" + year);
//                        Log.e("year", selectedDate);
//                        done = true;
//                    }
//                }, mYear, mMonth, mDay);
//        datePickerDialog.show();

//        if(done){
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        selectedHour = hour;
                        selectedMin = minute;
                        linearLayout.setVisibility(View.VISIBLE);
                        tvHour.setText(String.valueOf(selectedHour));
                        tvMinute.setText(String.valueOf(selectedMin));

                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.reminder_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.reminder_icon:
                alertAddTime();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}