package capstone.androiduserbadgetplanner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Calendar;

public class saving_summary extends AppCompatActivity {


    DBController dbC;
    // Get the Current Year and Month
    Calendar c = Calendar.getInstance();
    int year = c.get(Calendar.YEAR);
    int month = c.get(Calendar.MONTH);
    String monthName = getMonthName(month);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saving_summary);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbC = new DBController(this, null);

        // Initialize the instance of Spinners
        Spinner monthSpinner = (Spinner) findViewById(R.id.spinner3);
        Spinner yearSpinner = (Spinner) findViewById(R.id.spinner4);

        // Set the Current Month as the Default Month
        initDefaultMonth(monthSpinner, monthName);

        // Put the Years found in the database.
        initSpinnerYears(yearSpinner);

        // Set the Current Year as the Default Year
        initDefaultYear(yearSpinner, year);

        // Initialize the summary of saving with the current month and date
        initSavingSummary(monthName, year);

        // Initialize monthSpinner eventListener
        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                monthName = parentView.getItemAtPosition(position).toString();
                initSavingSummary(monthName, year);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}

        });

        // Initialize yearSpinner eventListener
        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                year = Integer.parseInt(parentView.getItemAtPosition(position).toString());
                initSavingSummary(monthName, year);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });
    }

    public void initSavingSummary(String month, int year){
        TextView savingsummary = (TextView) findViewById(R.id.savingValueContainer);
        int value = dbC.dbGetSavings(month, Integer.toString(year));
        savingsummary.setText("PHP: " + Integer.toString(value) + ".00");
    }

    public String getMonthName(int m){
        String monthName = "";
        switch (m+1){
            case 1: monthName = "January"; break;
            case 2: monthName = "February"; break;
            case 3: monthName = "March"; break;
            case 4: monthName = "April"; break;
            case 5: monthName = "May"; break;
            case 6: monthName = "June"; break;
            case 7: monthName = "July"; break;
            case 8: monthName = "August"; break;
            case 9: monthName = "September"; break;
            case 10: monthName = "October"; break;
            case 11: monthName = "November"; break;
            case 12: monthName = "December"; break;
        }
        return monthName;
    }
    public void initDefaultMonth(Spinner monthSpinner, String monthName){
        // Get selected default month.
        for(int i = 0; i < monthSpinner.getAdapter().getCount(); i++){
            if(monthSpinner.getItemAtPosition(i).toString().equals(monthName)){
                monthSpinner.setSelection(i);
                break;
            }
        }
    }

    public void initSpinnerYears(Spinner yearSpinner){
        // Put items on year Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, dbC.dbGetYears());
        yearSpinner.setAdapter(adapter);
    }

    public void initDefaultYear(Spinner yearSpinner, int year){
        // Get selected default year.
        for(int i = 0; i < yearSpinner.getAdapter().getCount(); i++){
            if(yearSpinner.getItemAtPosition(i).toString().equals(Integer.toString(year))){
                yearSpinner.setSelection(i);
                break;
            }
        }
    }
}
