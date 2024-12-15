package com.lab4.loginuit;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.lab4.loginuit.R.id.registerButton;
import static com.lab4.loginuit.R.layout.activity_register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.security.NoSuchAlgorithmException;

public class RegisterActivity extends AppCompatActivity{
    private SQLiteConnector db;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_register);
        EditText emailload=findViewById(R.id.emailregisterField);
        EditText mssvload=findViewById(R.id.mssvregisterField);
        EditText passload=findViewById(R.id.passregisterField);
        Button submit=findViewById(registerButton);
        submit.setOnClickListener(v->{
            String mssv = mssvload.getText().toString();
            String password = passload.getText().toString();
            try {
                password=SHA256.gen(password);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
            String email= emailload.getText().toString();
//            db=new SQLiteConnector(this);
//            TextView Errorregister=findViewById(R.id.registererror);
//            if(mssv.isEmpty() || password.isEmpty() || email.isEmpty()) {
//                Errorregister.setText("Please fill all fields !!!");
//            }
//            else {
//                if(db.checkUser(email)){
//                    Errorregister.setText("Email is exits !!!");
//                }
//                else{
//                    try {
//                        db.addUser(new User(mssv,email,password));
//                    } catch (NoSuchAlgorithmException e) {
//                        throw new RuntimeException(e);
//                    }
//                    Errorregister.setText("Register Successfully");
//                    Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
//                    startActivity(intent);
//                }
//            }
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Handler mainHandler = new Handler(Looper.getMainLooper());

            TextView Errorregister = findViewById(R.id.registererror);

            if (mssv.isEmpty() || password.isEmpty() || email.isEmpty()) {
                Errorregister.setText("Please fill all fields !!!");
            } else {
                String finalPassword = password;
                executor.execute(() -> {
                    try {
                        MySQLConnector connector = new MySQLConnector();

                        if (connector.checkUser(email)) {
                            // Cập nhật giao diện trên Main Thread
                            mainHandler.post(() -> Errorregister.setText("Email already exists !!!"));
                        } else {
                            connector.addUser(new User(mssv, email, finalPassword));
                            // Cập nhật giao diện trên Main Thread
                            mainHandler.post(() -> {
                                Errorregister.setText("Register Successfully");
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                            });
                        }
                    } catch (ClassNotFoundException | NoSuchAlgorithmException e) {
                        e.printStackTrace();
                        // Cập nhật lỗi trên Main Thread
                        mainHandler.post(() -> Errorregister.setText("An error occurred! Please try again."));
                    }
                });
            }
        });
    }
}
