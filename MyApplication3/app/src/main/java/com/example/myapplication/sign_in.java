package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class sign_in extends AppCompatActivity {

    private Boolean checkAccount = false;
    private Boolean checkPassword = false;
    private Boolean checkPasswordAgain = false;

    private InitDatabase initDatabase = new InitDatabase(this);

    private Boolean checkAccount(String account){
        Pattern pattern=Pattern.compile("^[a-zA-Z0-9_]+$");
        //testStr被检测的文本
        Matcher matcher = pattern.matcher(account);

        ArrayList<String> userData = new ArrayList<>();
        SQLiteDatabase database = initDatabase.getReadableDatabase();  // 这是一个对数据库操作的对象
        Cursor cursor = database.query("user",null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            if (cursor.getColumnIndex("userAccount")>=0 && cursor.getColumnIndex("userPassword")>=0){
                @SuppressLint("Range") String userAccount = cursor.getString(cursor.getColumnIndex("userAccount"));
                userData.add(userAccount);
            }
        }

        cursor.close();
        database.close();


        return matcher.matches() && account.length()<=16 && account.length()>= 8 && !userData.contains(account);
    }

    private Boolean checkPassword(String password){
        return password.length() >= 8 && password.length() <= 16;
    }

    private Boolean checkPasswordAgain(String password, String passwordAgain){
        return Objects.equals(password, passwordAgain) && passwordAgain.length()>=8 && passwordAgain.length() <= 16;
    }



// 我这里写了一个加密 加密用户的密码
    public static String getSha256(String str) {
        MessageDigest messageDigest;
        String encodeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes(StandardCharsets.UTF_8));
            encodeStr = byte2Hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return encodeStr;
    }


    private static String byte2Hex(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        String temp;
        for (byte aByte : bytes) {
            temp = Integer.toHexString(aByte & 0xFF);
            if (temp.length() == 1) {
                //1得到一位的进行补0操作
                stringBuilder.append("0");
            }
            stringBuilder.append(temp);
        }
        return stringBuilder.toString();
    }

// 加密到此结束


// 提示弹窗开始
private void showAlertDialog(String title, String content, Boolean success) {
    AlertDialog dialog = new AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(content)
            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (success){
                        startActivity(new Intent(sign_in.this, log_in.class));
                    }
                }
            })
            .create();
//    展示后再更改 就不会导致空指针异常了
    dialog.show();
    dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.font_color));


}


// 提示弹窗结束




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        findViewById(R.id.account_sign_in).setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // 此处为失去焦点时的处理内容
                    EditText account = findViewById(R.id.account_sign_in);
                    if (checkAccount(String.valueOf(account.getText()))){
                        TextView textView = findViewById(R.id.account_sign_in_Alert);
                        textView.setTextColor(Color.rgb(26,84,152));
                        textView.setText("accepted");
                        checkAccount = true;
                    }else {
                        TextView textView = findViewById(R.id.account_sign_in_Alert);
                        textView.setTextColor(android.graphics.Color.RED);
                        textView.setText("账号格式有误");
                        checkAccount = false;
                    }
                }
            }
        });

        findViewById(R.id.password_sign_in).setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // 此处为失去焦点时的处理内容
                    EditText password = findViewById(R.id.password_sign_in);
                    if (checkPassword(String.valueOf(password.getText()))){
                        TextView textView = findViewById(R.id.password_sign_in_Alert_1);
                        textView.setTextColor(Color.rgb(26,84,152));
                        textView.setText("accepted");
                        checkPassword = true;
                    }else {
                        TextView textView = findViewById(R.id.password_sign_in_Alert_1);
                        textView.setTextColor(android.graphics.Color.RED);
                        textView.setText("密码格式有误");
                        checkPassword = false;
                    }

                }
            }
        });

        findViewById(R.id.password_sign_in_Again).setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // 此处为失去焦点时的处理内容
                    EditText password = findViewById(R.id.password_sign_in);
                    EditText passwordAgain = findViewById(R.id.password_sign_in_Again);
                    if (checkPasswordAgain(String.valueOf(password.getText()), String.valueOf(passwordAgain.getText()))){
                        TextView textView = findViewById(R.id.password_sign_in_Alert_2);
                        textView.setTextColor(Color.rgb(26,84,152));
                        textView.setText("accepted");
                        checkPasswordAgain = true;
                    }else {
                        TextView textView = findViewById(R.id.password_sign_in_Alert_2);
                        textView.setTextColor(android.graphics.Color.RED);
                        textView.setText("两次密码必须相同");
                        checkPasswordAgain = false;
                    }

                }
            }
        });

        findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                先失去焦点
                findViewById(R.id.account_sign_in).clearFocus();
                findViewById(R.id.password_sign_in).clearFocus();
                findViewById(R.id.password_sign_in_Again).clearFocus();

                EditText account = findViewById(R.id.account_sign_in);
                EditText password = findViewById(R.id.password_sign_in);

                if (checkAccount && checkPassword && checkPasswordAgain){
                    System.out.println("-----------123---------------------ok---------------");
                    SQLiteDatabase database = initDatabase.getWritableDatabase();  // 这是一个对数据库操作的对象
                    database.execSQL("insert into user (userAccount, userPassword) values ('"+String.valueOf(account.getText())+"','"+getSha256(String.valueOf(password.getText()))+"')");
                    showAlertDialog("成功", "注册账号成功", true);
                }else showAlertDialog("失败", "注册账号失败请检查输入的账号和密码",false);


            }
        });





    }
}
