package capstone.androiduserbadgetplanner;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.content.Intent;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TabHost;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

// API Libraries
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.orangegangsters.lollipin.lib.PinCompatActivity;
import com.github.orangegangsters.lollipin.lib.managers.AppLock;
import com.github.orangegangsters.lollipin.lib.managers.LockManager;
import android.widget.Spinner;
import android.widget.Toast;


public class mainInterface extends PinCompatActivity{
    // Initialize Global Variables
    PieChart pieChart, pieChart_income;
    ArrayList<Entry> entries, entries_income ;
    ArrayList<String> PieEntryLabels, PieEntryLabels_income ;
    PieDataSet pieDataSet, pieDataSet_income ;
    PieData pieData, pieData_income ;
    DBController dbC;

    // Get the Current Year and Month
    Calendar c = Calendar.getInstance();
    int year = c.get(Calendar.YEAR);
    int month = c.get(Calendar.MONTH);
    String monthName = getMonthName(month);


    private static final int REQUEST_CODE_ENABLE = 11;

    private boolean locked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // pag di pa naka set saka lang pwede mag enable
        LockManager lockManager = LockManager.getInstance();
        try{
            // If the Pin Code hasn't set yet, make the user set it
            if(!lockManager.getAppLock().isPasscodeSet()) {
                // Prompt for pin code
                initLock();
            }
        }catch (Exception RuntimeException){
            // Prompt for pin Code
            initLock();
        }finally {
            // Initialize View
            initView();
        }
    }

    public void initBugetIncome(){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(mainInterface.this);
        // Alert Dialog Title and SubTtitle

        alertDialog.setTitle("Monthly Budget Plan");
        alertDialog.setMessage("You are required to enter a budget since you have nothing.");

        // Create Template
        final EditText input = new EditText(mainInterface.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setLayoutParams(lp);

        // Put the Template on the dialog
        alertDialog.setView(input); // uncomment this line
        alertDialog.setCancelable(false);

        // Set an alertDialog Button
        alertDialog.setPositiveButton("Finalize", null);


        final AlertDialog mAlertDialog = alertDialog.create();
        mAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(final DialogInterface dialog) {

                Button b = mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        // TODO Do something
                        String startingBudget = input.getText().toString();
                        int startingBudgetVal;
                        if (!startingBudget.equals("") && Integer.parseInt(startingBudget) > 0){
                            startingBudgetVal = Integer.parseInt(startingBudget);
                            // Income Instance to save the data
                            Income incInstance = new Income(startingBudgetVal);
                            // Add To database
                            dbC.addIncomeData(incInstance);

                            dialog.dismiss();

                        }else{
                            Toast.makeText(mainInterface.this, "Please Enter a valid value", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        // Show the alert Dialog
        mAlertDialog.show();

    }

    public void initTab(){
        final TabHost tabs = (TabHost)findViewById(android.R.id.tabhost);
        tabs.setup();

        // Calculator Tab
        TabHost.TabSpec tab1 = tabs.newTabSpec("expenses");
        tab1.setContent(R.id.expenses);
        tab1.setIndicator("expenses");
        tabs.addTab(tab1);

        // Income Tab
        TabHost.TabSpec tab2 = tabs.newTabSpec("income");
        tab2.setContent(R.id.income);
        tab2.setIndicator("income");
        tabs.addTab(tab2);

        // Set an event Listener to make the tab selection dynamic
        tabs.setOnTabChangedListener(new TabHost.OnTabChangeListener() {

            @Override
            public void onTabChanged(String tabId) {

                int i = tabs.getCurrentTab();

                switch (i){
                    case 0:
                        // Initialize the view on Income Tab
                        initPieChart(monthName, Integer.toString(year));
                        break;
                    case 1:
                        initIncomePieChart(monthName, Integer.toString(year));
                        break;
                }
            }
        });


    }

    public void initLock(){
        if (!locked) {
            Intent intent = new Intent(mainInterface.this, CustomPinActivity.class);
            intent.putExtra(AppLock.EXTRA_TYPE, AppLock.ENABLE_PINLOCK);
            startActivityForResult(intent, REQUEST_CODE_ENABLE);
            locked = true;
        }
    }

    public void initView(){

        // This is what activity to be loaded
        setContentView(R.layout.activity_main_interface);

        // Create an instance of the database controller.
        dbC = new DBController(this, null);

        // Initialize the instance of Spinners
        Spinner monthSpinner = (Spinner) findViewById(R.id.spinner);
        Spinner yearSpinner = (Spinner) findViewById(R.id.spinner2);
        Spinner incomeMonth = (Spinner) findViewById(R.id.income_month);

        // If there is no budget
        if (!getBudgetStatus(monthName, Integer.toString(year))){
            // Initialize the monthly budget Income of the user
            initBugetIncome();
        }
        // Initialize the view on Income Tab
        Spinner incomeYear = (Spinner) findViewById(R.id.income_year);
        initSpinnerIncomeYears(incomeYear);
        initDefaultYear(incomeYear, year);


        // Set the Current Month as the Default Month
        initDefaultMonth(monthSpinner, monthName);
        initDefaultMonth(incomeMonth, monthName);

        // Put the Years found in the database.
        initSpinnerYears(yearSpinner);

        // Set the Current Year as the Default Year
        initDefaultYear(yearSpinner, year);


        // Initialize the Action Bar
        initActionBar();

        // Initialize the Pie Chart
        initPieChart(monthName, Integer.toString(year));

        // Initialize the Multiple Tabs
        initTab();


        // Initialize monthSpinner eventListener
        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                monthName = parentView.getItemAtPosition(position).toString();
                initPieChart(monthName, Integer.toString(year));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}

        });

        // Initialize yearSpinner eventListener
        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                year = Integer.parseInt(parentView.getItemAtPosition(position).toString());
                initPieChart(monthName, Integer.toString(year));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });
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

    public void initSpinnerIncomeYears(Spinner yearSpinner){
        // Put items on year Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, dbC.dbGetIncomeYears());
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

    public void initActionBar(){
        // This setups the Action Bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void initPieChart(String month, String years){
        // This is the cursor of PieChart Tag
        pieChart = (PieChart) findViewById(R.id.chart1);

        // This is where the number of expenses stored
        entries = new ArrayList<>();
        PieEntryLabels = new ArrayList<>();

        // SetValues to the PieChart
        AddValuesToPIEENTRY(month, years);

        pieDataSet = new PieDataSet(entries, "");
        pieData = new PieData(PieEntryLabels, pieDataSet);

        // This is the list of colors available
        int[] COLOR_LIST = colorCollections();

        // Give it Color
        pieDataSet.setColors(COLOR_LIST);

        // Plot the data
        pieChart.setData(pieData);

        // Animate the Pie Chart
        pieChart.animateY(3000);
    }

    public void initIncomePieChart(String month, String years){
        // This is the cursor of PieChart Tag
        pieChart_income = (PieChart) findViewById(R.id.income_chart);

        // This is where the number of income stored
        entries_income = new ArrayList<>();
        PieEntryLabels_income = new ArrayList<>();

        // SetValues to the PieChart
        AddValuesToPIEENTRY_Income(month, years);

        pieDataSet_income = new PieDataSet(entries_income, "");
        pieData_income = new PieData(PieEntryLabels_income, pieDataSet_income);

        // This is the list of colors available
        int[] COLOR_LIST = colorCollections();

        // Give it Color
        pieDataSet_income.setColors(COLOR_LIST);

        // Plot the data
        pieChart_income.setData(pieData_income);

        // Animate the Pie Chart
        pieChart_income.animateY(3000);
    }

    public void AddValuesToPIEENTRY(String month, String years){
        List <Integer> amountArray;
        List <String> categoryArray;
        amountArray = dbC.dbGetAmounts(month, years);
        categoryArray = dbC.dbGetLabels(month, years);

        for(int i=0; i < amountArray.size(); i++){
            entries.add(new BarEntry(amountArray.get(i), i));
            PieEntryLabels.add(""+ categoryArray.get(i));
        }
    }

    public void AddValuesToPIEENTRY_Income(String month, String years){
        int amountSumIncome, amountSumExpenses;
        amountSumIncome = dbC.dbIncomeAmount(month, years);
        amountSumExpenses = dbC.dbExpenseAmount(month, years);

        // Add the amount of Income
        entries_income.add(new BarEntry(amountSumIncome, 0));
        PieEntryLabels_income.add("Remaining Allowance");
        // Add the amount of Expenses
        entries_income.add(new BarEntry(amountSumExpenses, 1));
        PieEntryLabels_income.add("Total Expenses");

    }

    public boolean getBudgetStatus(String month, String years){
        int amountSum = dbC.dbIncomeAmount(month, years);
        return amountSum > 0;
    }

    public int[] colorCollections(){
        int[] colors = {
                Color.rgb(193, 37, 82),
                Color.rgb(255, 102, 0),
                Color.rgb(245, 199, 0),
                Color.rgb(106, 150, 31),
                Color.rgb(179, 100, 53),
                Color.rgb(207, 248, 246),
                Color.rgb(148, 212, 212),
                Color.rgb(136, 180, 187),
                Color.rgb(118, 174, 175),
                Color.rgb(42, 109, 130),
                Color.rgb(217, 80, 138),
                Color.rgb(254, 149, 7),
                Color.rgb(254, 247, 120),
                Color.rgb(106, 167, 134),
                Color.rgb(53, 194, 209)
        };
        return  colors;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_interface, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // noinspection SimplifiableIfStatement
        switch (id){
            case R.id.action_exit:
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            case R.id.daily_tip:
                startActivity(new Intent(this, tips.class));
                return true;
            case R.id.saving_summary:
                startActivity(new Intent(this, saving_summary.class));
                return true;
            case R.id.expenses_summary:
                startActivity(new Intent(this, expenses_summary.class));
                return true;
            case R.id.change_pin:
                changePin();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void addExpenses(View view) {
        Intent getAct = new Intent(mainInterface.this, addExpenses.class);
        startActivity(getAct);
    }

    public void changePin() {
        Intent intent = new Intent(mainInterface.this, CustomPinActivity.class);
        intent.putExtra(AppLock.EXTRA_TYPE, AppLock.CHANGE_PIN);
        startActivity(intent);
    }


}
