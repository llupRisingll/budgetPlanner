package capstone.androiduserbadgetplanner;

import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;


public class tips extends AppCompatActivity {

    TextView savingsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        savingsText = (TextView) findViewById(R.id.textView3);

        savingsText.setText(getSavingTips());
    }

    public String getSavingTips (){
        String tips[] = {
                "The first step to saving money is to figure out how much you spend. Keep track of all your expenses—that means every coffee, newspaper and snack you buy. Ideally, you can account for every penny. Once you have your data, organize the numbers by categories, such as gas, groceries and mortgage, and total each amount. Consider using your credit card or bank statements to help you with this. If you bank online, you may be able to filter your statements to easily break down your spending.",
                "Once you have an idea of what you spend in a month, you can begin to organize your recorded expenses into a workable budget. Your budget should outline how your expenses measure up to your income—so you can plan your spending and limit overspending. In addition to your monthly expenses, be sure to factor in expenses that occur regularly but not every month, such as car maintenance. Find more information about creating a budget.",
                "Now that you’ve made a budget, create a savings category within it. Try to put away 10–15 percent of your income as savings. If your expenses are so high that you can’t save that much, it might be time to cut back. To do so, identify non-essentials that you can spend less on, such as entertainment and dining out. We’ve put together ideas for saving money every day as well as cutting back on your fixed monthly expenses.",
                "After your expenses and income, your goals are likely to have the biggest impact on how you save money. Be sure to remember long-term goals—it’s important that planning for retirement doesn’t take a back seat to shorter-term needs. Prioritizing goals can give you a clear idea of where to start saving. For example, if you know you’re going to need to replace your car in the near future, you could start putting money away for one.",
                "Almost all banks offer automated transfers between your checking and savings accounts. You can choose when, how much and where to transfer money to, or even split your direct deposit between your checking and savings accounts. Automated transfers are a great way to save money since you don’t have to think about it and it generally reduces the temptation to spend the money instead.",
                "Check your progress every month. Not only will this help you stick to your personal savings plan but it also helps you identify and fix problems quickly. These simple ways to save money may even inspire you to save more and hit your goals faster."
        };

        int rnd = new Random().nextInt(tips.length);
        return tips[rnd];

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
