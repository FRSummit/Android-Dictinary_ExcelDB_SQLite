package com.frsummit.sl_dictionary_2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

public class MainActivity extends Activity {
    private EditText textField;
    private TextView textView;
    private String entTxt;
    private ArrayList<String> arrayList;
    private AutoCompleteTextView autoCompleteTextView;
    DbHelp dbHelp;
    ArrayList<String> newList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textField = findViewById(R.id.enterText);
        textView = findViewById(R.id.text);

        arrayList = new ArrayList<>();
        getAllWordsToArray();

        dbHelp = new DbHelp(this, "sample.db", 1);
        try {
            dbHelp.checkDb();
            dbHelp.openDatabase();
        }catch (Exception e) {
            e.printStackTrace();
        }

        newList = new ArrayList<>();
        autoCompleteTextView = findViewById(R.id.autoTextField);
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0) {
                    System.out.println(s + "    " + start + "    " + before + "    " + count);
//                    newList.add("Apple");
//                    newList.add("Abul");
//                    newList.add("Asad");
                    newList.addAll(arrayList);
                    autoCompleteTextView.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, newList));
//                    newList.addAll(dbHelp.getEngWord(s.toString()));
//                    autoCompleteTextView.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, newList));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String word = newList.get(position);
                Toast.makeText(MainActivity.this, word, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void searchBtnClick(View view) {
        textView.setText("");
        entTxt = textField.getText().toString().toLowerCase();
        for (String string : arrayList) {
            if(string.split("=")[0].equals(entTxt)){
                textView.setText((string.split("=")[0].substring(0, 1).toUpperCase()) + (string.split("=")[0].substring(1)) + " : " + string.split("=")[1]);
            }
        }
    }

    public void getAllWordsToArray() {
        try {
            AssetManager assetManager = getAssets();
            InputStream inputStream = assetManager.open("disc.xls");
            Workbook workbook = Workbook.getWorkbook(inputStream);
            Sheet sheet = workbook.getSheet(0);
            int rows = sheet.getRows();
            int columns = sheet.getColumns();
            String text = "";
            for(int r=0; r< rows; r++) {
                for(int col=-0; col< columns; col++) {
                    Cell cell = sheet.getCell(col, r);
                    text = text + cell.getContents();
                    text = text + " ";
                    arrayList.add(text.split("\\|")[1] + "=" + text.split("\\|")[2]);
                    System.out.println(text.split("\\|")[1]);
                    text = "";
                }
                text = text + "\n";
            }
        } catch(Exception e) {
            Toast.makeText(this, "Exception : " + e, Toast.LENGTH_LONG).show();
        }
    }
}
