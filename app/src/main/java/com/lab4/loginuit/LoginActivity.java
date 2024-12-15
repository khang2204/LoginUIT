package com.lab4.loginuit;
import static com.lab4.loginuit.R.id.loginButton;
import static com.lab4.loginuit.R.id.passloginField;
import static com.lab4.loginuit.R.layout.activity_login;

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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity{
    private SQLiteConnector db;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_login);
        EditText mssvload=findViewById(R.id.mssvloginField);
        EditText passload=findViewById(passloginField);
        Button submit=findViewById(loginButton);
        submit.setOnClickListener(v->{
            String mssv = mssvload.getText().toString();
            String password = passload.getText().toString();
            try {
                password=SHA256.gen(password);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
//            db=new SQLiteConnector(this);
//            TextView Errorlogin=findViewById(R.id.loginerror);
//            if(db.checkUser(mssv,password)){
//                Intent intent=new Intent(LoginActivity.this, DisplayActivity.class);
//                intent.putExtra("username", mssv);
//                Errorlogin.setText("Login Successfully");
//                startActivity(intent);
//            }
//            else{
//                if(mssv.isEmpty() || password.isEmpty()) {
//                    Errorlogin.setText("Please fill all fields !!!");
//                }
//                else {
//                    Errorlogin.setText("Wrong username or password !!!");
//                }
//            }
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Handler mainHandler = new Handler(Looper.getMainLooper());

            TextView Errorlogin = findViewById(R.id.loginerror);

            String finalPassword = password;
            executor.execute(() -> {
                MySQLConnector connector = null;
                try {
                    connector = new MySQLConnector();
                    if (mssv.isEmpty() || finalPassword.isEmpty()) {
                        // Cập nhật lỗi trên Main Thread
                        mainHandler.post(() -> Errorlogin.setText("Please fill all fields !!!"));
                    } else {
                        // Kiểm tra người dùng trong cơ sở dữ liệu trên luồng nền
                        boolean isValidUser = connector.checkUser(mssv, finalPassword);
                        mainHandler.post(() -> {
                            if (isValidUser) {
                                // Đăng nhập thành công
                                Intent intent = new Intent(LoginActivity.this, DisplayActivity.class);
                                intent.putExtra("username", mssv);
                                Errorlogin.setText("Login Successfully");
                                startActivity(intent);
                            } else {
                                // Đăng nhập thất bại
                                Errorlogin.setText("Wrong username or password !!!");
                            }
                        });
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    mainHandler.post(() -> Errorlogin.setText("An error occurred! Please try again."));
                }
            });
        });
        Button register=findViewById(R.id.gotoregisterButton);
        register.setOnClickListener(v->{
            Intent intent=new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}
