package JBiolette.AD340;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {
    public static final String MyPREFERENCES = "MyPrefs";
    Button Movie, Cam, County, Zip, loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Movie = (Button) findViewById(R.id.movie);
        Movie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(MainActivity.this, "Seattle", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, Movies.class);
                startActivity(intent);
            }
        });

        Cam = (Button) findViewById(R.id.cam);
        Cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(MainActivity.this, "Seattle", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, TrafficCamera.class);
                startActivity(intent);
            }
        });
        County = (Button) findViewById(R.id.location);
        County.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });
        Zip = (Button) findViewById(R.id.zip);
        Zip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "98109", Toast.LENGTH_SHORT).show();
            }
        });loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
                                           TextView textView = findViewById(R.id.userNameLayout);
                                           TextView emailView = findViewById(R.id.emailAddressLayout);
                                           TextView passwordView = findViewById(R.id.passwordLayout);

                                           @Override
                                           public void onClick(View view) {
                                               if ( validation() != true) {
                                                   validation();
                                               }else {
                                                   signIn();
                                               }
                                           }



                                           private void signIn() {
                                               String displayname = textView.getText().toString();
                                               String email = emailView.getText().toString();
                                               String password = passwordView.getText().toString();

                                               // 1 - validate display name, email, and password

                                               Log.d("FIREBASE", "signIn");

                                                   // 2 - save valid entries to shared preferences
                                                   SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                                                   SharedPreferences.Editor editor = sharedPreferences.edit();

                                                   editor.putString("Username", displayname);
                                                   editor.putString("Email", email);
                                                   editor.putString("Password", password);
                                                   editor.commit();

                                                   //@SuppressLint("WrongConstant") SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_APPEND);

                                                //3 - sign into Firebase
                                               FirebaseAuth mAuth = FirebaseAuth.getInstance();
                                               mAuth.signInWithEmailAndPassword(email, password)
                                                       .addOnCompleteListener(findViewById((Executor) this, new OnCompleteListener<AuthResult>() {
                                                           @Override
                                                           public void onComplete(@NonNull Task<AuthResult> task) {
                                                               Log.d("FIREBASE", "signIn:onComplete:" +
                                                                       task.isSuccessful());
                                                               if (task.isSuccessful()) {
                                                                   // update profile. displayname is the value entered in UI
                                                                   FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                                                   UserProfileChangeRequest profileUpdates = new
                                                                           UserProfileChangeRequest.Builder()
                                                                           .setDisplayName(displayname)
                                                                           .build();
                                                                   user.updateProfile(profileUpdates)
                                                                           .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                              @Override
                                                                                                              public void onComplete(@NonNull Task<Void> task) {
                                                                                                                  if (task.isSuccessful()) {
                                                                                                                      Log.d("FIREBASE", "User profile updated.");
                                                                                                                      // Go to FirebaseActivity
                                                                                                                      startActivity(new
                                                                                                                              Intent(MainActivity.this, FirebaseActivity.class));
                                                                                                                  }
                                                                                                              }
                                                                                                          });
                                                               } else {
                                                                   Log.d("FIREBASE", "sign-in failed");
                                                                   Toast.makeText(MainActivity.this, "Sign In Failed",
                                                                           Toast.LENGTH_SHORT).show();
                                                               }
                                                           }
                                                       });
                                           }


            private boolean validation() {
                boolean validEntries = false;
                if (textView.getText().toString().equals("")) {
                    Snackbar noName = Snackbar.make((findViewById(R.id.WordTable)), "Please enter valid user name", Snackbar.LENGTH_LONG);
                    noName.show();
                } else if (emailView.getText().toString().equals("")) {
                    Snackbar noEmail = Snackbar.make((findViewById(R.id.WordTable)), "Please enter valid email", Snackbar.LENGTH_LONG);
                    noEmail.show();
                } else if (passwordView.getText().toString().equals("")) {
                    Snackbar noPassword = Snackbar.make((findViewById(R.id.WordTable)), "Please enter valid password", Snackbar.LENGTH_LONG);
                    noPassword.show();
                }else{
                    validEntries = true;
                }
                return validEntries;
            }
        });

        }
    }
