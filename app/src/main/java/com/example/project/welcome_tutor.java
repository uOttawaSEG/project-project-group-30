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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Objects;

public class welcome_tutor extends AppCompatActivity {
    // button for logging out the tutor and returning to the main screen
    private Button btnLogout;
    public String slectedDate="";

    FirebaseAuth mAuth;

    FirebaseUser user;
    public String userId;
    public String  dateID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set the UI layout for this screen to activity_welcome_tutor.xml
        setContentView(R.layout.activity_welcome_tutor);
        // Link the button variable to the actual button in the layout
        mAuth=FirebaseAuth.getInstance();
         user = mAuth.getCurrentUser();
          userId = user.getUid();

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
                slectedDate = year+"/"+(month+1)+"/"+dayOfMonth;
                 dateID=month+dayOfMonth+"";

            }
        });


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
         Date date =new Date();
        SimpleDateFormat dateFormatyd = new SimpleDateFormat("yyyy/MM/dd");
            try {
                if(slectedDate.isEmpty()) {
                    Toast.makeText(welcome_tutor.this, "You must select a date", Toast.LENGTH_SHORT).show();

                }
                else if(dateFormatyd.parse(dateFormatyd.format(date)).after(dateFormatyd.parse(slectedDate))){
                    Toast.makeText(welcome_tutor.this, "Enter a date that has not already passed", Toast.LENGTH_SHORT).show();
                }
                else{

                    AlertDialog.Builder builder = new AlertDialog.Builder(welcome_tutor.this);
                    builder.setTitle("Create a session");
                    builder.setMessage("Add a 30 minute time slot in millitary time ex: 1205 for 12:05pm");
                    Context context = builder.getContext();
                    LinearLayout layout = new LinearLayout(context);
                    layout.setOrientation(LinearLayout.VERTICAL);

                    final EditText start = new EditText(context);
                    start.setHint("Start time");
                    start.setInputType(InputType.TYPE_CLASS_NUMBER);
                    layout.addView(start);
                    //add end time box
                    final EditText stop = new EditText(context);
                    start.setInputType(InputType.TYPE_CLASS_NUMBER);
                    stop.setHint("End time");
                    layout.addView(stop);
                    //add a checkbox
                    final CheckBox checkBoxAuto = new CheckBox(context);
                    checkBoxAuto.setText("Auto accept students");
                    checkBoxAuto.setChecked(false);
                    layout.addView(checkBoxAuto);
                    builder.setView(layout);
                    builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            String startTime=start.getText().toString();
                            String endTime = stop.getText().toString();
                            SimpleDateFormat dateFormathm = new SimpleDateFormat("HH/mm");

                            boolean autoAccept = checkBoxAuto.isChecked();

                            if(startTime.isEmpty() ||Integer.valueOf(startTime)> 2400 || Integer.valueOf(startTime)<0 || startTime.length()!=4){
                                Toast.makeText(welcome_tutor.this, "Enter a valid start time", Toast.LENGTH_SHORT).show();
                            }
                            else if(endTime.isEmpty()||Integer.valueOf(endTime)> 2400 || Integer.valueOf(endTime)<0 || endTime.length()!=4){
                                Toast.makeText(welcome_tutor.this, "Enter a valid end time", Toast.LENGTH_SHORT).show();
                            }
                            else if(Integer.valueOf(startTime)>Integer.valueOf(endTime)){
                                Toast.makeText(welcome_tutor.this, "The session must end after it starts", Toast.LENGTH_SHORT).show();

                            }
                            else {
                                int curHour = date.getHours();
                                int curMin = date.getMinutes();
                                try {
                                    if(curHour*60+curMin > Integer.valueOf(startTime.substring(0,2))*60+Integer.valueOf(startTime.substring(2,4))&&dateFormatyd.parse(dateFormatyd.format(date)).equals(dateFormatyd.parse(slectedDate))){
                                        Toast.makeText(welcome_tutor.this, "This time has already happened", Toast.LENGTH_SHORT).show();

                                    }
                                    else{
                                        int diff = 0;
                                        diff+=Integer.valueOf(endTime.substring(0,2))*60+Integer.valueOf(endTime.substring(2,4));
                                        diff-=Integer.valueOf(startTime.substring(0,2))*60+Integer.valueOf(startTime.substring(2,4));
                                        if(diff==30){
                                            FirebaseDatabase.getInstance().getReference().child("Dates").get().addOnSuccessListener(dataSnapshot -> {
                                                boolean taken = false;
                                                HashMap<String, Object> dateMap = null;
                                                if (dataSnapshot.exists()) {
                                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                                        String date = ds.child("Date").getValue(String.class);
                                                        if (date == null) {
                                                            continue;
                                                        }
                                                        // String tutor = ds.child("Tutor").getValue(String.class); this is for later(Deliverable 4).
                                                        int btime = Integer.parseInt(Objects.requireNonNull(ds.child("Start").getValue(String.class)).substring(0, 2))*60+Integer.parseInt(Objects.requireNonNull(ds.child("Start").getValue(String.class).substring(2, 4)));
                                                        int etime = Integer.parseInt(Objects.requireNonNull(ds.child("End").getValue(String.class)).substring(0, 2))*60+Integer.parseInt(Objects.requireNonNull(ds.child("End").getValue(String.class).substring(2, 4)));
                                                        int pstart = Integer.parseInt(startTime.substring(0, 2))*60+Integer.parseInt(startTime.substring(2, 4));
                                                        int pend = Integer.parseInt(endTime.substring(0, 2))*60+Integer.parseInt(endTime.substring(2, 4));


                                                        if (date.equals(slectedDate)){
                                                            if (pstart >= btime && pstart < etime || pend >= btime && pend < etime) {
                                                                taken = true;
                                                                break;
                                                            }
                                                        }
                                                    }


                                                }
                                                if (!taken) {
                                                    Toast.makeText(welcome_tutor.this, "Date added", Toast.LENGTH_SHORT).show();
                                                    dateMap = new HashMap<>();
                                                    //String finalDate = slectedDate + "/" + startTime.substring(0, 2) + "/" + startTime.substring(2, 4);
                                                    dateMap.put("Date", slectedDate);
                                                    dateMap.put("Tutor", userId);
                                                    dateMap.put("Student", "Empty");
                                                    dateMap.put("IsTaken", "no");
                                                    dateMap.put("AutoAccept", autoAccept);
                                                    dateMap.put("Start", startTime);
                                                    dateMap.put("End", endTime);
                                                    FirebaseDatabase.getInstance().getReference().child("Dates").child(userId + dateID + startTime).setValue(dateMap);

                                                } else {
                                                    Toast.makeText(welcome_tutor.this, "This slot is already taken", Toast.LENGTH_SHORT).show();
                                                }
                                                dialog.dismiss();


                                            });
                                        }
                                        else{
                                            Toast.makeText(welcome_tutor.this, "The session must last 30 minutes", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                } catch (ParseException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
            }
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }


        });

        }

    }
