package com.iot.sqlite;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    SQLiteHelper helper;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 뷰 연결
        textView = findViewById(R.id.textView);

        // DB 헬퍼 생성 및 DB 열기
        helper = new SQLiteHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();

        // 테스트 데이터 넣기
        db.execSQL("INSERT INTO " + SQLiteHelper.TABLE_NAME + " (" +
                        SQLiteHelper.TIME + ", " + SQLiteHelper.TITLE + ") VALUES (?, ?);",
                new Object[]{System.currentTimeMillis(), "테스트 제목"});

        // 데이터 조회
        Cursor cursor = db.rawQuery("SELECT * FROM " + SQLiteHelper.TABLE_NAME, null);
        StringBuilder sb = new StringBuilder();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(SQLiteHelper.ID));
            long time = cursor.getLong(cursor.getColumnIndexOrThrow(SQLiteHelper.TIME));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(SQLiteHelper.TITLE));

            sb.append("ID: ").append(id)
                    .append(", Time: ").append(time)
                    .append(", Title: ").append(title)
                    .append("\n");
        }
        cursor.close();

        // TextView에 출력
        textView.setText(sb.toString());

        db.close();
    }
}
