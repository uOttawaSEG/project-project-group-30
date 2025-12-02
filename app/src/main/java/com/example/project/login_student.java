package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class login_student extends AppCompatActivity {

    private Button btnContinueStudent;
    private Button sbackbutton;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_student);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // assign the characteristics of the actual button to the varible declared in this code, same as the username and password text fields
        btnContinueStudent = findViewById(R.id.btnContinueStudent);
        sbackbutton = findViewById(R.id.sbackbutton);
        EditText txtUsername = (EditText) findViewById(R.id.txtUserStudent);
        EditText txtPassword =(EditText) findViewById(R.id.txtPassStudent);
        // when the button is clicked it makes sure that the text in the user is the same as the student login and the password, it calles a function to get the student password and student user and then verifies
        btnContinueStudent.setOnClickListener(v -> {
            String email = txtUsername.getText().toString().trim();
            String password = txtPassword.getText().toString().trim();
            /*for(MainActivity.student s :MainActivity.students){
                // if it matches up then it proceeds to student welcome page
                if(email.equals(s.getEmail()) && password.equals(s.getPassword())) {
                    Intent intent = new Intent(login_student.this, welcome_student.class);
                    startActivity(intent);
                    return;
                    //
                }
                // if its different then it sends a message that its either the wrong email or password
                else{
                    Toast.makeText(this, "Invalid email or password.", Toast.LENGTH_SHORT).show();

                }
            }*/
            // This connects the app to Firebase’s Authentication service
            mAuth = FirebaseAuth.getInstance();
            // attempts to sign in, so it checks these credentials against the Authentication data stored in the database, and runs a listener when its done
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // If the sign-in was successful, get the currently logged-in Firebase user.
                                FirebaseUser User = mAuth.getCurrentUser();
                                // Get the unique ID (UID) that Firebase assigns to every user account.
                                String userId = User.getUid();
                                // Go to the Realtime Database and retrieve this user's "Status" field
                                // located under: Accounts/{userId}/Status
                                FirebaseDatabase.getInstance().getReference("Accounts").child(userId).child("Status").get().addOnCompleteListener(statusTask->{
                                    // This inner listener runs when the database has returned the user's Status value.
                                    if(statusTask.isSuccessful()){
                                        // Get the Status value as a Long (number).
                                        // 0 = pending, 1 = rejected, 2 = approved
                                        String status = String.valueOf(statusTask.getResult().getValue());
                                        //If Status == 1, Account rejected by admin
                                        if("1".equals(status)){
                                            Toast.makeText(login_student.this, "Your Account has been Rejected by the Admin. If you think this is incorrect please contact 123-456-7890", Toast.LENGTH_LONG).show();
                                        }
                                        //If Status == 2, Account approved by admin
                                        else if("2".equals(status)){
                                            Toast.makeText(login_student.this, "Your Account has been Approved. Welcome!",Toast.LENGTH_LONG).show();
                                            // Send the student to the welcome screen
                                            Intent intent = new Intent(login_student.this, welcome_student.class);
                                            startActivity(intent);
                                        }
                                        //If Status == 0, Account still pending approval
                                        else if("0".equals(status)){
                                            Toast.makeText(login_student.this, "Your account is pending approval",Toast.LENGTH_LONG ).show();
                                        }
                                    }

                                });

                            }
                            else{
                                // If authentication failed (wrong email/password or user doesn’t exist),
                                // show an error message.
                                Toast.makeText(login_student.this,"Authentication failed", Toast.LENGTH_LONG).show();
                            }

                        }

                    });
        });
        sbackbutton.setOnClickListener(v -> {
            Intent intent = new Intent(login_student.this, login.class);
            startActivity(intent);
        });


    }

}