package com.example.project;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.database.FirebaseDatabase;

public class login_tutor extends AppCompatActivity {

    private Button btnContinueTutor;
    private Button backbutton;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_tutor);
        // assign the characteristics of the actual button to the varible declared in this code, same as the username and password text fields
        btnContinueTutor = findViewById(R.id.btnContinueTutor);
        backbutton = findViewById(R.id.backbutton);
        EditText txtUsername = (EditText) findViewById(R.id.txtUserTutor);
        EditText txtPassword =(EditText) findViewById(R.id.txtPassTutor);
        // when the button is clicked it makes sure that the text in the user is the same as the tutor login and the password, first it goes through all the tutors in the tutor list and it calls a function to get the tutor password and tutor user and then verifies, if both of them dont match it goes onto the next in the list
        btnContinueTutor.setOnClickListener(v -> {
            String email = txtUsername.getText().toString().trim();
            String password = txtPassword.getText().toString().trim();
            //verifies email with the verify email class
            EmaiVerify verify= new EmaiVerify();
            if (!verify.isValidEmail(email)) {
                Toast.makeText(login_tutor.this, "Please enter a valid email address.", Toast.LENGTH_SHORT).show();
                return;
            }
            PasswordVerify verify2 = new PasswordVerify();
            if (!verify2.isValidPassword(password)) {
                Toast.makeText(login_tutor.this, "Please enter a valid password.", Toast.LENGTH_SHORT).show();
                return;
            }
            PhoneNumberVerify verify3 = new PhoneNumberVerify();
            if (!verify3.isValidPhoneNumber(password)) {
                Toast.makeText(login_tutor.this, "Please enter a valid phone number.", Toast.LENGTH_SHORT).show();
                return;
            }
            /* for(MainActivity.tutor t :MainActivity.tutors){
//if both the user and password are correct it takes us to the the tutor login page
                if(email.equals(t.getEmail()) && password.equals(t.getPassword())) {
                 Intent intent = new Intent(login_tutor.this, welcome_tutor.class);
                 startActivity(intent);
                 return;
            }
            }
            //if it goes through the for loop and doest take them to the next page then that means that theres no tutors in the tutor list that match these login details therefore it prints an message saying its the wrong password or user
            Toast.makeText(this, "Invalid email or password.", Toast.LENGTH_SHORT).show();

                */
            // This connects the app to Firebase’s Authentication service
            mAuth = FirebaseAuth.getInstance();
            // attempts to sign in, so it checks these credentials against the Authentication data stored in the database, and runs a listener when its done
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // This method runs after Firebase Authentication finishes trying to sign in the user.
                            // 'task' contains the result of the login attempt (success or failure).
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
                                            Toast.makeText(login_tutor.this, "Your Account has been Rejected by the Admin. If you think this is incorrect please contact 123-456-7890", Toast.LENGTH_LONG).show();
                                        }
                                        // if Status == 2, Account approved by admin
                                        else if("2".equals(status)){
                                            Toast.makeText(login_tutor.this, "Your Account has been Approved. Welcome!",Toast.LENGTH_LONG).show();
                                            // Send the tutor to the welcome screen
                                            Intent intent = new Intent(login_tutor.this, welcome_tutor.class);
                                            startActivity(intent);
                                        }
                                        //If Status == 0, Account still pending approval
                                        else if("0".equals(status)){
                                            Toast.makeText(login_tutor.this, "Your account is pending approval",Toast.LENGTH_LONG ).show();
                                        }
                                    }

                                });

                            }
                            else{
                                // If authentication failed (wrong email/password or user doesn’t exist),
                                // show an error message.
                                Toast.makeText(login_tutor.this,"Authentication failed", Toast.LENGTH_LONG).show();
                            }

                        }

                    });
        });
        backbutton.setOnClickListener(v -> {
            Intent intent = new Intent(login_tutor.this, login.class);
            startActivity(intent);
        });
        }

    }

