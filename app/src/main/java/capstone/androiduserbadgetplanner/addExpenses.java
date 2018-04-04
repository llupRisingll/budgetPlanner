package capstone.androiduserbadgetplanner;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.support.v4.app.NavUtils;
import android.widget.Toast;

import java.util.Calendar;


public class addExpenses extends AppCompatActivity{

    DBController dbC;
    EditText expenses;
    EditText savings;
    Spinner category;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_expenses);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Find the Input Expenses and then save the value of it in this variable
        expenses = (EditText) findViewById(R.id.inputExpenses);
        savings = (EditText) findViewById(R.id.inputSavings);

        // Find the Spinner and then save the cursor to a variable
        category = (Spinner) findViewById(R.id.categories);

        // Create an instance of the database controller.
        dbC = new DBController(this, null);
    }

    public void addButtonClicked(View view){

        try{
            // Count the characters in the saving input
            final int savingsValLength = savings.getText().length();
            final int savingsVal = (savingsValLength < 1)? 0: Integer.parseInt(savings.getText().toString());


            // Get the value of Expenses, category:selected and then put in the variable
            final int expenseVal = Integer.parseInt(expenses.getText().toString());
            final String categoryVal = category.getSelectedItem().toString();

            // If the saving does not contain anything set it's default value to zero

            if (expenseVal > 0) {
                // Create a confirmation
                new AlertDialog.Builder(this)
                        .setTitle("Confirmation")
                        .setMessage("Are you sure you wanted to add this data?")
                        .setIcon(android.R.drawable.ic_input_add)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                // #1 inputted expenses, #2 selected category
                                Expenses expInstance = new Expenses(expenseVal, categoryVal, savingsVal);
                                // Add To database
                                dbC.addExpenseData(expInstance);

                                Calendar c = Calendar.getInstance();
                                int year = c.get(Calendar.YEAR);
                                int month = c.get(Calendar.MONTH)+1;

                                String monthName = "";
                                switch (month){
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
                                int incomeAmount = dbC.dbIncomeAmount(monthName, Integer.toString(year));

                                // Make sure that there is remaining amount
                                if (incomeAmount != 0){
                                    // Make the income 0 if nothing left
                                    if (incomeAmount > expenseVal){
                                        int total = incomeAmount - expenseVal;
                                        dbC.updateIncome(total);
                                    }else{
                                        Toast.makeText(addExpenses.this, "Sorry, you only have remaining PHP "+incomeAmount+".00 as allowance.", Toast.LENGTH_LONG).show();
                                        //dbC.updateIncome(0);
                                    }
                                }

                                printDatabase();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
            }

        }catch (Exception e){
            Toast.makeText(addExpenses.this, "Please Input a valid Integer", Toast.LENGTH_LONG).show();
        }
    }

    public void printDatabase(){
         // Show message that the Data was successfully added
         Toast.makeText(addExpenses.this, "Successfully Added!", Toast.LENGTH_SHORT).show();

        // Remove the values in the Form
        expenses.setText("");
        savings.setText("");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
