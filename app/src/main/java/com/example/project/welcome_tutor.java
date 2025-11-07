package com.example.project;

import static java.lang.Boolean.valueOf;
import static java.security.AccessController.getContext;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class welcome_tutor extends AppCompatActivity {
    // button for logging out the tutor and returning to the main screen
    private Button btnLogout;
    public String slectedDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set the UI layout for this screen to activity_welcome_tutor.xml
        setContentView(R.layout.activity_welcome_tutor);
        // Link the button variable to the actual button in the layout

        btnLogout = findViewById(R.id.btnLogout);
        CalendarView calendar = findViewById(R.id.calendar);
        TextView dateView = findViewById(R.id.date_view);
        Button btnCreate = findViewById(R.id.btnCreate);
        Button btnCheck = findViewById(R.id.btnCheck);

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                String date = dayOfMonth + "-" + (month + 1) + "-" + year;
                dateView.setText(date);
                slectedDate=year+month+dayOfMonth +"";
            }
        });
        calendar.setMinDate(1762473600000L);


// when the button is clicked it does thew following instructions
        btnLogout.setOnClickListener(v -> {
            // create an Intent to navigate from the tutor welcome screen back to MainActivity
            Intent intent = new Intent(welcome_tutor.this, MainActivity.class);
            startActivity(intent);
        });

        btnCheck.setOnClickListener(v ->{
            Intent intent = new Intent(welcome_tutor.this, Calendar_Lists.class);
            startActivity(intent);
        });

        btnCreate.setOnClickListener(v ->{

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Create a session");
            builder.setMessage("Add a 30 minute time slot in millitary time ex: 1205 for 12:05pm");
            Context context = builder.getContext();
            LinearLayout layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.VERTICAL);

            final EditText start = new EditText(context);
            start.setInputType(InputType.TYPE_CLASS_NUMBER);
            layout.addView(start);
            //builder.setView(start);
            final EditText stop = new EditText(context);
            start.setInputType(InputType.TYPE_CLASS_NUMBER);
            layout.addView(stop);
            //builder.setView(stop);
            builder.setView(layout);

            builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    String startTime=start.getText().toString();
                    String endTime = stop.getText().toString();

                    if(startTime!=null||Integer.valueOf(startTime)> 2400 || Integer.valueOf(startTime)<0){
                        Toast.makeText(welcome_tutor.this, "Enter a valid start time", Toast.LENGTH_SHORT).show();
                    }
                    else if(endTime!=null||Integer.valueOf(endTime)> 2400 || Integer.valueOf(endTime)<0){
                        Toast.makeText(welcome_tutor.this, "Enter a valid end time", Toast.LENGTH_SHORT).show();
                    }
                    else if(Integer.valueOf(startTime)>Integer.valueOf(endTime)){
                        Toast.makeText(welcome_tutor.this, "The session must end after it starts", Toast.LENGTH_SHORT).show();

                    }
                    else{
                        int diff = 0;
                        diff+=Integer.valueOf(endTime.substring(0,2))*60+Integer.valueOf(endTime.substring(3,5));
                        diff-=Integer.valueOf(startTime.substring(0,2))*60+Integer.valueOf(startTime.substring(3,5));
                        if(diff==30){

                        }
                        else{
                            Toast.makeText(welcome_tutor.this, "The session must last 30 minutes", Toast.LENGTH_SHORT).show();

                        }
                    }


                    dialog.dismiss(); // Close the dialog
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });

    }
}