package com.iot.adder;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText et_1;
    EditText et_2;
    TextView et_result;
    EditText focusedEditText;

    Button btn_1, btn_2, btn_3, btn_4, btn_5, btn_6, btn_7, btn_8, btn_9, btn_0;
    Button btn_dot, btn_percent, btn_plus, btn_minus, btn_multiply, btn_slash, btn_reset;

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        et_1 = findViewById(R.id.et_1);
        et_2 = findViewById(R.id.et_2);
        et_result = findViewById(R.id.et_result);

        btn_1 = findViewById(R.id.btn_1);
        btn_2 = findViewById(R.id.btn_2);
        btn_3 = findViewById(R.id.btn_3);
        btn_4 = findViewById(R.id.btn_4);
        btn_5 = findViewById(R.id.btn_5);
        btn_6 = findViewById(R.id.btn_6);
        btn_7 = findViewById(R.id.btn_7);
        btn_8 = findViewById(R.id.btn_8);
        btn_9 = findViewById(R.id.btn_9);
        btn_0 = findViewById(R.id.btn_0);
        btn_dot = findViewById(R.id.btn_dot);
        btn_percent = findViewById(R.id.btn_percent);
        btn_plus = findViewById(R.id.btn_plus);
        btn_minus = findViewById(R.id.btn_minus);
        btn_multiply = findViewById(R.id.btn_multiply);
        btn_slash = findViewById(R.id.btn_slash);
        btn_reset = findViewById(R.id.btn_reset);

        // 포커스 리스너 수정
        et_1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    focusedEditText = et_1;
                }
            }
        });

        et_2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    focusedEditText = et_2;
                }
            }
        });

        // 초기 포커스 설정 (없으면 null일 수 있으니 기본값 설정)
        focusedEditText = et_1;

        // 숫자 및 특수문자 버튼 클릭 리스너 설정
        setButtonClickListener(btn_1);
        setButtonClickListener(btn_2);
        setButtonClickListener(btn_3);
        setButtonClickListener(btn_4);
        setButtonClickListener(btn_5);
        setButtonClickListener(btn_6);
        setButtonClickListener(btn_7);
        setButtonClickListener(btn_8);
        setButtonClickListener(btn_9);
        setButtonClickListener(btn_0);
        setButtonClickListener(btn_dot);

        // 더하기 버튼
        btn_plus.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                Float num1 = getInput(et_1);
                Float num2 = getInput(et_2);

                Float resultnum = num1 + num2;
                et_result.setText("  결과값 : " + resultnum);
            }
        });

        // 빼기 버튼
        btn_minus.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                Float num1 = getInput(et_1);
                Float num2 = getInput(et_2);

                Float resultnum = num1 - num2;
                et_result.setText("  결과값 : " + resultnum);
            }
        });

        // 곱하기 버튼
        btn_multiply.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                Float num1 = getInput(et_1);
                Float num2 = getInput(et_2);

                Float resultnum = num1 * num2;
                et_result.setText("  결과값 : " + resultnum);
            }
        });

        // 나누기 버튼
        btn_slash.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                Float num1 = getInput(et_1);
                Float num2 = getInput(et_2);

                if (num2 == 0f) {
                    et_result.setText("  오류: 0으로 나눌 수 없습니다.");
                    return;
                }

                Float resultnum = num1 / num2;
                et_result.setText("  결과값 : " + resultnum);
            }
        });

        // 나머지 버튼 (퍼센트)
        btn_percent.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                Float num1 = getInput(et_1);
                Float num2 = getInput(et_2);

                if (num2 == 0f) {
                    et_result.setText("  오류: 0으로 나눌 수 없습니다.");
                    return;
                }

                Float resultnum = num1 % num2;
                et_result.setText("  결과값 : " + resultnum);
            }
        });

        // 초기화 버튼
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (focusedEditText != null) {
                    focusedEditText.setText("");
                }
            }
        });
    }

    // 입력값을 Float으로 안전하게 변환하는 메서드
    private Float getInput(EditText editText) {
        String text = editText.getText().toString();
        if (text.isEmpty()) {
            return 0f;
        }
        try {
            return Float.parseFloat(text);
        } catch (NumberFormatException e) {
            return 0f;
        }
    }

    // 숫자, 점 버튼 클릭 시 입력 필드에 텍스트 추가
    private void setButtonClickListener(final Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num = button.getText().toString();
                if (focusedEditText != null) {
                    String currentText = focusedEditText.getText().toString();
                    focusedEditText.setText(currentText + num);
                    // 커서 위치를 텍스트 마지막으로 이동
                    focusedEditText.setSelection(focusedEditText.getText().length());
                }
            }
        });
    }
}
