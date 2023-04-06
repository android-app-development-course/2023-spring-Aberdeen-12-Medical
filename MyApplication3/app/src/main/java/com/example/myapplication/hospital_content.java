package com.example.myapplication;

import static com.example.myapplication.R.drawable.edit_background;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.appcompat.app.AppCompatActivity;

public class hospital_content extends AppCompatActivity {


    /**
     * 创建一个textView，参数为文本框内容
     */
    @SuppressLint({"ResourceAsColor", "SetTextI18n"})
    private void createTableRow(TableLayout parent) {
        TableRow tableRow = new TableRow(this);

        tableRow.setGravity(Gravity.CENTER_HORIZONTAL);
        tableRow.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT
        ));

        Button button1 = new Button(this); // 创建一个新的 TextView
        button1.setText("Hello"); // 设置 TextView 的文本内容
        button1.setBackgroundColor(R.color.black);
        button1.setBackgroundDrawable(getResources().getDrawable(edit_background));
        button1.setWidth(320); // 设置 Button 的宽度
        button1.setHeight(160);
        tableRow.addView(button1); // 将 TextView 添加到 TableRow 中

        View separatorView = new View(this);
        separatorView.setLayoutParams(new TableRow.LayoutParams(100, 160));
        tableRow.addView(separatorView);

        Button button2 = new Button(this); // 创建一个新的 TextView
        button2.setText("World"); // 设置 TextView 的文本内容
        button2.setBackgroundColor(R.color.black);
        button2.setBackgroundDrawable(getResources().getDrawable(edit_background));
        button2.setWidth(320); // 设置 Button 的宽度
        button2.setHeight(160);
        tableRow.addView(button2); // 将 TextView 添加到 TableRow 中

        parent.addView(tableRow);
        System.out.println("创建成功");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_user);


        Intent intent = getIntent();
        int data = intent.getIntExtra("imageNameID",0);
        ImageView image = findViewById(data);

        setContentView(R.layout.hospital_content);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        TableLayout tableLayout = findViewById(R.id.hospital_content_tablelayout);
        createTableRow(tableLayout);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        ImageView imageView = findViewById(R.id.hospital_content_imageView);
        System.out.println(data);


        Drawable drawable = image.getDrawable();
        imageView.setImageDrawable(drawable);

    }
}
