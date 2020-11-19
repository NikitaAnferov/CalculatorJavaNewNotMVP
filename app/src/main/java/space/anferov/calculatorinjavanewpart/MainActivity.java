package space.anferov.calculatorinjavanewpart;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView result_text; // поле для вывода результата расчетов
    TextView math_operation; // поле для ввода математических действий

    int [] btn_id = new int[] {R.id.btn_0, R.id.btn_1, R.id.btn_2, R.id.btn_3, R.id.btn_4,
            R.id.btn_5, R.id.btn_6, R.id.btn_7, R.id.btn_8, R.id.btn_9, R.id.btn_point, R.id.btn_minus, R.id.btn_plus,
            R.id.btn_slash, R.id.btn_multiplication, R.id.btn_left_parenthesis, R.id.btn_right_parenthesis,
            R.id.btn_ac, R.id.btn_back, R.id.btn_equal}; // массив с id кнопок, для удобного присвоения их к соответствующим textView

    TextView [] textView_array = new TextView[btn_id.length]; // массив textView которые будут являться кнопками

    String [] signs = new String [] {"+","-","*","/"}; // массив знаков, для проверки, если этот знак последний в строке
                                                                // то другой поставить нельзя







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for(int i = 0 ; i < textView_array.length; i++) { // цикл для создания рабочих кнопок
            textView_array[i] = findViewById(btn_id[i]); // присваиваем полю id кнопки
            textView_array[i].setOnClickListener(this); // устанавливаем слушателя нажатия на каждую кнопку
        }



        result_text = findViewById(R.id.result_text); // присваиваем полю id результирующего поля
        math_operation = findViewById(R.id.math_operation); // присваиваем полю id поля для вывода результата вычисления

    }


    public void onClick(View v) { // метод - обработчик нажатий
        try{ // блок обрабатывающий ошибки
            String buttonText = ((TextView) v).getText().toString(); // записываем в переменную текс хранящийся в нажатой кнопке
            switch (buttonText) { // ветвление по значению текста кнопки
                case ".":
                    setTextFiled(buttonText); // вызов метода выводящего результат нажатия в поле математических действий
                    deletePoint(); // метод удаляющий "." если вдруг по каким-то причинам ввод ее в данный момент будет некорректным
                    break;

                case "-":
                case "+":
                case "/":
                case "*":
                    repeatSign(); // метод добавляющий "0" если строка была пуста и удаляющий последний введенный знак. чтобы не было повторений знаков
                    setTextFiled(buttonText); // вызов метода выводящего результат нажатия в поле математических действий
                    break;

                case "(":
                case ")":
                    setTextFiled(buttonText); // вызов метода выводящего результат нажатия в поле математических действий
                    break;

                case "AC":
                    math_operation.setText(""); // очистка поля для ввода математических действий
                    result_text.setText(""); // очиска результирующего поля
                    break;

                case "Back":
                    String str = math_operation.getText().toString(); // записываем в переменную текст поля для математических действий
                    if (!str.equals("")) { // если поле не равно пустоте
                        math_operation.setText(str.substring(0, str.length() - 1)); // то удаляем последний символ
                    }
                    result_text.setText(""); // очистка результирующего поля
                    break;

                case "=":
                    try { // обработчик ошибок
                        parenthesisClose(); // метод закрывающий все открытые скобки
                        String line  = math_operation.getText().toString(); // записываем в переменную значение поля для математических вычислений
                        if (!line.substring(line.length()-1).matches("[0-9]|[.]")) // если последний символ в строке не точка и не число
                            line = line.substring(0,line.length()-1); // удаляем последний символ (он является математическим знаком, вызовет ошибку при вычислении если его не удалить)
                        Expression ex = new Expression(new ExpressionBuilder(line).build()); // создаем экземпляр для вычислени переданного выражения
                        double result = ex.evaluate(); // получаем результат вычисления выражения


                        long longRes = (long) result; // конвертируем результат в целочисленное значение
                        if (result == (double) longRes) // если дробное значение равно целочисленному, значит точка и нули в конце числа не нужны
                            result_text.setText(String.valueOf(longRes)); // выводим в результирующее поле целое число
                        else // иначе
                            result_text.setText(String.valueOf(result)); // выводим в результат дробное число
                    } catch (Exception e) { // юлок при возникновении ошибок
                        result_text.setText(R.string.error); // выводим в результирующее поле сообщение об ошибке
                    }
                    break;
                default: // ветка обрабатывающая все кнопоки с цифрами
                    setTextFiled(buttonText); // вызов метода выводящего результат нажатия в поле математических действий
                    break;
            }
        } catch (Exception ignored) { // игнорируем возникшие ошибки

        }

    }



    private void setTextFiled(String str) { // метод для ввода значений в поле для математических вычислений
        if(result_text.getText() != "") { // если результирующее поле не пустое
            math_operation.setText(""); // очищаем поле для ввода математических выражений
            Resources myResources = getResources(); // получаем экземпляр ресурсов приложения, для обращения к ним
            if (result_text.getText() != myResources.getText(R.string.error)) { // если текст результирующего поля не "ошибка"
                if (!str.matches("[0-9]|[().]")) { // проверяем если в поле для математических действий передается значение указанное в условие
                   // math_operation.setText(""); // очищаем поле для математических действий
              //  } else { // иначе
                    math_operation.append(result_text.getText()); // записываем в поле для математических действий значение из результирующего поля
                }                                            // если текст результирующего поля "ошибка"
            } else if (!str.matches("[0-9]|[().]")) { // проверяем если в поле для математических действий передается значение указанное в условие
                    math_operation.append("0"); // записываем в поле для математических действий 0 перед вводимым знаком, которого нет в условии if
                }

            result_text.setText(""); // очищаем поле с результатом вычисления
        }
        math_operation.append(str); // запичываем в поле для математическиз дейсвтий переданное значение
    }

    private void repeatSign() { // метод для проверки повторения знаков (нелья ввести "++", "+-", " *")
        String math_operation_line = math_operation.getText().toString(); // записываем значение из поля для ввода математических вычислений в переменную
        if (math_operation_line.equals("")){ // если строка была пустой
            math_operation.append("0"); // записываем в math_operation 0
            return; // выход из метода
        }
        String math_operation_symbol = math_operation_line.substring(math_operation_line.length() - 1); // записываем в переменную последний введенный символ из поля математических операций
        for (String sign : signs) { // цикл для проверки "последний элемент математичесий знак?"
            if (math_operation_symbol.equals(sign)) { // если последний элемент является математическим знаком
                math_operation.setText(math_operation_line.substring(0,math_operation_line.length() - 1)); // удаляем его (в другой части кода на удаленное место впишется введный математичский знак)
                return; // выход из цикла
            }
        }
    }


    private void parenthesisClose() { // метод закрывающий все открытые скобки (используется перед вычислением результата)
        String line = math_operation.getText().toString(); // записываем в переменную поле для ввода математических вычислений
        int countParenthesis = 0; // объявляем переменную, хранящую разницу "открытые скобки - закрытые скобки"

        for(int i = 0; i < line.length(); i++) { // цикл считающий разницу "открытые скобки - закрытые скобки"
            if(line.charAt(i) == '(') { // если в строке встретилась открывающая скобка
                countParenthesis++; // увеличиваем переменную разности скобок на 1
            }
            if(line.charAt(i) == ')') { // если в строке встретилась закрывающая скобка
                countParenthesis--; // уменьшаем переменную разности скобок на 1
            }
        }

        for(int i = 0; i < countParenthesis; i++) { // цикл для звкрытия всех незакрытых скобок
            math_operation.append(")"); // записываем в поле для математических вычислений закрывающую скобку
         }

    }



    private void deletePoint(){ // метод для удаления введенной "." запрет таких записей, как: "+.", "..", "3.6.7"
        String line = math_operation.getText().toString(); // записываем в переменную значение поля для матиматических вычислений
        int numbers = 0; // переменная хранящая колчество чисел попавшихся при парсинге строки
        int points = 0; // переменная хранящая колчество точек попавшихся при парсинге строки
        for(int i = line.length() - 1; i > -1 ; i--) { // цикл пробегающий по всейм зачениям строки для математических вычислений
            try { // обработчик ошабок
                Integer.parseInt(String.valueOf(line.charAt(i))); // пытаемся конвертировать в int попавшийся в строке символ
                numbers++; // если конвертировалось удачно, значит это было число
            } catch (NumberFormatException e) { // обработчик ошибок (блок при возникновении ошибки)
                if (line.charAt(i) == '.') {  // если символ который неудалось конвертировать является "."
                    points++; // счетчик точек +1
                } else { // иначе
                    break; // выходим  из цикла
                }
            }
        }

        if(points == 1 && numbers == 0) { // если в поле для математических вычислений была введена "." без чисел
            math_operation.setText(line.substring(0, line.length() - 1)); // удаление введенной точки
            math_operation.append("0."); // добавляем в поле для математических вычислений "0."
        } else if(points == 2 && numbers > 0) { // если чисел было более одного и 2 точки
            math_operation.setText(line.substring(0,line.length() - 1)); // удаляем почледнюю введенную точку
        }

    }

}