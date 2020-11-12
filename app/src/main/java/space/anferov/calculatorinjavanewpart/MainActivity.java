package space.anferov.calculatorinjavanewpart;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView result_text;
    TextView math_operation;

    int [] btn_id = new int[] {R.id.btn_0, R.id.btn_1, R.id.btn_2, R.id.btn_3, R.id.btn_4,
            R.id.btn_5, R.id.btn_6, R.id.btn_7, R.id.btn_8, R.id.btn_9, R.id.btn_point, R.id.btn_minus, R.id.btn_plus,
            R.id.btn_slash, R.id.btn_multiplication, R.id.btn_left_parenthesis, R.id.btn_right_parenthesis,
            R.id.btn_ac, R.id.btn_back, R.id.btn_equal};

    TextView [] textView_array = new TextView[btn_id.length];

    String [] signs = new String [] {"+","-","*","/"}; // массив знаков, для проверки, если этот знак последний в строке
                                                                // то другой поставить нельзя







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for(int i = 0 ; i < textView_array.length; i++) {
            textView_array[i] = findViewById(btn_id[i]);
            textView_array[i].setOnClickListener(this);
        }



        result_text = findViewById(R.id.result_text);
        math_operation = findViewById(R.id.math_operation);

    }

    public void onClick(View v) {
        try{
            String buttonText = ((TextView) v).getText().toString();
            switch (buttonText) {
                case ".":
                    setTextFiled(buttonText);
                    deletePoint();
                    break;

                case "-":
                case "+":
                case "/":
                case "*":
                    repeatSign();
                    setTextFiled(buttonText);
                    break;

                case "(":
                case ")":
                    setTextFiled(buttonText);
                    break;

                case "AC":
                    math_operation.setText("");
                    result_text.setText("");
                    break;

                case "Back":
                    String str = math_operation.getText().toString();
                    if (!str.equals("")) {
                        math_operation.setText(str.substring(0, str.length() - 1));
                    }
                    result_text.setText("");
                    break;

                case "=":
                    try {
                        parenthesisClose();
                        String line  = math_operation.getText().toString();
                        if (!line.substring(line.length()-1).matches("[0-9]|[.]"))
                            line = line.substring(0,line.length()-1);
                        Expression ex = new Expression(new ExpressionBuilder(line).build());
                        double result = ex.evaluate();


                        long longRes = (long) result;
                        if (result == (double) longRes)
                            result_text.setText(String.valueOf(longRes));
                        else
                            result_text.setText(String.valueOf(result));
                    } catch (Exception e) {
                        result_text.setText(R.string.error);
                    }

                    break;
                default:
                    setTextFiled(buttonText);
                    break;
            }
        } catch (Exception ignored) {

        }

    }



    private void setTextFiled(String str) {
        if(result_text.getText() != "") {
            math_operation.setText("");
            Resources myResources = getResources();
            if (result_text.getText() != myResources.getText(R.string.error)) {
                if (str.matches("[0-9]|[().]")) {
                    math_operation.setText("");
                } else {
                    math_operation.append(result_text.getText());
                }
            } else if (!str.matches("[0-9]|[().]")) {
                    math_operation.append("0");
                }

            result_text.setText("");
        }
        math_operation.append(str);
    }

    private void repeatSign() {
        String math_operation_line = math_operation.getText().toString();
        if (math_operation_line.equals("")){
            math_operation.append("0"); // записываем в math_operation 0
            return;
        }
        String math_operation_symbol = math_operation_line.substring(math_operation_line.length() - 1);
        for (String sign : signs) {
            if (math_operation_symbol.equals(sign)) {
                math_operation.setText(math_operation_line.substring(0,math_operation_line.length() - 1));
                return;
            }
        }
    }


    private void parenthesisClose() {

        String line = math_operation.getText().toString();
        int countParenthesis = 0;

        for(int i = 0; i < line.length(); i++) {
            if(line.charAt(i) == '(') {
                countParenthesis++;
            }
            if(line.charAt(i) == ')') {
                countParenthesis--;
            }
        }

        for(int i = 0; i < countParenthesis; i++) {
            math_operation.append(")");
        }

    }



    private void deletePoint(){
        String line = math_operation.getText().toString();
        int numbers = 0;
        int points = 0;
        for(int i = line.length() - 1; i > -1 ; i--) {
            try {
                Integer.parseInt(String.valueOf(line.charAt(i)));
                numbers++;
            } catch (NumberFormatException e) {
                if (line.charAt(i) == '.') {
                    points++;
                } else {
                    break;
                }
            }
        }

        if(points == 1 && numbers == 0) {
            math_operation.setText(line.substring(0, line.length() - 1));
            math_operation.append("0.");
        } else if(points == 2 && numbers > 0) {
            math_operation.setText(line.substring(0,line.length() - 1));
        }

    }

}