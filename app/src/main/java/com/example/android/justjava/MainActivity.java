package com.example.android.justjava;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.protocol.HTTP;

import java.text.NumberFormat;
import java.util.Set;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    int quantity = 1;
    double pricePerCup = 5.00;
    double whippedCreamPricePerCup = 1;
    double chocolatePricePerCup = 2;
    //String orderName = "Sam Rounds"; take user input instead
    //boolean addWhippedCream = false; Don't need this global variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /** increase number of cups being ordered by 1 */
    public void increment(View view) {
        if (quantity >= 10) {
            //use return if you want to break out of the METHOD call completely!
            //use break if you want to exit a loop or switch statement
            Context context = getApplicationContext(); //ALL THESE LINES OF CODE CAN BE COMPOUNDED TO 1 LINE (SEE BELOW)
            CharSequence toastMessage = getString(R.string.exceed_max_order_quantity);
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, toastMessage, duration);
            toast.show(); //We can chain the makeText() and show() methods to avoid storing a Toast object.
        } else { //Notice that, if we use return above, then we don't need the else statement
                 // and could execute the remaining lines outside the if/else statement (see below)
            quantity = quantity + 1;
            displayQuantity(quantity);
        }
    }
    /** decrease number of cups being ordered by 1 */
    public void decrement(View view) {
        if (quantity <= 1) {
            Toast.makeText(this, getString(R.string.less_than_min_order_quantity), Toast.LENGTH_SHORT).show();
            return;
        }
        quantity = quantity - 1;
        displayQuantity(quantity);
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int numberOfCups) {
        TextView quantityTextView = (TextView) findViewById(
                R.id.quantity_text_view);
        quantityTextView.setText("" + numberOfCups);
    }

    /** This method is called when the order button is clicked.
     * It gets the customer's name and email from EditText fields in activity_main.xml
     * Then it collects boolean info on if customer wants toppings
     * Then creates an email intent and send the order summary in the body of the message*/
    public void submitOrder(View view) {
        //Get user input name from the EditText object
        EditText userInput = (EditText) findViewById(R.id.order_name_edit_text);
        String orderName = userInput.getText().toString();

        EditText userEmail = (EditText) findViewById(R.id.customer_email_edit_text);
        String customerEmail = userEmail.getText().toString();
        String[] customerEmailArray = new String[] {customerEmail};

        //Note, we do not need an onClick attribute for the whipped_cream_checkbox;
        // we simply take the existing information here when we want to print it out
        CheckBox box1 = (CheckBox) findViewById(R.id.whipped_cream_checkbox);
        boolean hasWhippedCream = box1.isChecked();

        //Like above, assign the view to a CheckBox object and extract a boolean attribute
        CheckBox box2 = (CheckBox) findViewById(R.id.chocolate_checkbox);
        boolean hasChocolate = box2.isChecked();

        /* String orderSummary = createOrderSummary(orderName, hasWhippedCream, hasChocolate);
        displayMessage(orderSummary); */

        Double priceOfCompleteOrder = calculateOrderTotal(hasWhippedCream, hasChocolate);

        /* Create an intent that can be started to create an email with our orderSummary in the body
         * sent from the user's preferred email app (implicit intent).
         */
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:")); // only email apps should handle this
        // Note that Uri.parse("mailto:") can also be used as the second argument in Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_EMAIL, customerEmailArray); // can use a string array, ie new String[], for multiple recipients (Note just one recipient still requires a String[], not a String
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject));
        emailIntent.putExtra(Intent.EXTRA_TEXT, createOrderSummary(orderName, hasWhippedCream, hasChocolate, priceOfCompleteOrder));
        if (emailIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(emailIntent);
        }
    }

    /** This method is called by the submitOrder method to construct the message to be displayed. */
    private String createOrderSummary(String name, boolean hasWhippedCream, boolean hasChocolate, double price) {
        String summaryString = "" + getString(R.string.email_name) + " " + name;
        summaryString += "\n" + getString(R.string.email_add_whipped_cream) + " " + hasWhippedCream; //this boolean variable is automatically converted to a String
        summaryString += "\n" + getString(R.string.email_add_chocolate) + " " + hasChocolate; //this boolean variable is automatically converted to a String
        summaryString += "\n" + getString(R.string.email_quantity) + " " + quantity;
        summaryString += "\n" + getString(R.string.email_price, NumberFormat.getCurrencyInstance().format(price)); //add an input to calculateOrderTotal, which means we don't need a global variable for hasWhippedCream
        // Note that the getString() method can take in multiple arguments and concatenate them, even booleans and doubles.
        summaryString += "\n" + getString(R.string.email_thank_you);
        return summaryString;
    }

    /**
     * Calculates the total price of the order.
     * @param ihasWhippedCream tells us yes/no if customer wants Whipped Cream on their drink(s)
     * @param ihasChocolate tells us yes/no if customer wants Chocolate topping on their drink(s)
     * @return gives back the price of the entire order in a double, accounting for extras w/checkbox input
     */
    private double calculateOrderTotal(boolean ihasWhippedCream, boolean ihasChocolate) {
        double orderTotal = quantity * pricePerCup;
        if (ihasWhippedCream) { orderTotal += whippedCreamPricePerCup*quantity; }
        if (ihasChocolate) { orderTotal += chocolatePricePerCup*quantity; }
        return orderTotal;
    }

    /**
     * This method displays the given text on the screen.
    private void displayMessage(String message) {
        TextView orderSummaryTextView = (TextView) findViewById(R.id.order_summary_text_view);
        orderSummaryTextView.setText(message);
    }*/

    /**
     * This method displays the given price on the screen.
    private void displayPrice(int number) {
        TextView priceTextView = (TextView) findViewById(R.id.price_text_view);
        priceTextView.setText(NumberFormat.getCurrencyInstance().format(number));
    }*/
}

