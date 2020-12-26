package com.example.FASIM;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;


public class MainActivity extends AppCompatActivity {

    Button scan;
    public static String username;
    public static String emailid ;
    EditText name,email;
    String EmailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_main);

        name=(EditText)findViewById(R.id.name);
        email=(EditText)findViewById(R.id.email);

        scan=(Button)findViewById(R.id.button);


        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (name.getText().toString().isEmpty() || email.getText().toString().isEmpty()) {

                    Toast.makeText(getApplicationContext(),"Please Enter Username And Email Address",Toast.LENGTH_SHORT).show();

                }
                else {


                    if(name.getText().toString().trim().length()>=8) {
                        if (email.getText().toString().trim().matches(EmailPattern)) {



                            MainActivity.username = name.getText().toString().trim();
                            MainActivity.emailid = email.getText().toString().trim();

                            Toast.makeText(getApplicationContext(), MainActivity.username+"\n"+MainActivity.emailid, Toast.LENGTH_LONG).show();


                            openNewActivity();

                            Dexter.withContext(getApplicationContext()).withPermission(Manifest.permission.CAMERA)
                                    .withListener(new PermissionListener() {
                                        @Override
                                        public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                            openNewActivity();

                                        }

                                        @Override
                                        public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                                            Toast.makeText(getApplicationContext(), "Permission is compulsory for Scanning", Toast.LENGTH_LONG).show();


                                        }

                                        @Override
                                        public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                                            permissionToken.continuePermissionRequest();
                                        }
                                    }).check();

                        } else {

                            Toast.makeText(getApplicationContext(), "Please Enter valid Email Address", Toast.LENGTH_SHORT).show();

                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Enter proper username at least 8 characters",Toast.LENGTH_SHORT).show();

                    }
                }

            }
        });


    }

    public void openNewActivity(){
        Intent intent = new Intent(this, Scanner.class);
        startActivity(intent);
    }
}