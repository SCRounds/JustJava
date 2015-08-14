package com.example.android.justjava;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.text.NumberFormat;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    int quantity = 1;
    double price = 5.00;
    String orderName = "Sam Rounds";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /** increase number of cups being ordered by 1 */
    public void increment(View view) {
        quantity = quantity + 1;
        displayQuantity(quantity);
        // submitOrder(view);
    }
    /** decrease number of cups being ordered by 1 */
    public void decrement(View view) {
        quantity = quantity - 1;
        displayQuantity(quantity);
        // submitOrder(view);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        String orderSummary = createOrderSummary();
        displayMessage(orderSummary);
    }

    private String createOrderSummary() {
        return "Name: " + orderName + "\nQuantity: " + quantity
                + "\nTotal: " + calculateOrderTotal() + "\nThank you!";
    }

    /**
     * Calculates the price of the order.
     */
    private double calculateOrderTotal() {
        return quantity * price;
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int numberOfCups) {
        TextView quantityTextView = (TextView) findViewById(
                R.id.quantity_text_view);
        quantityTextView.setText("" + numberOfCups);
    }

    /**
     * This method displays the given text on the screen.
     */
    private void displayMessage(String message) {
        TextView orderSummaryTextView = (TextView) findViewById(R.id.order_summary_text_view);
        orderSummaryTextView.setText(message);
    }

    /**
     * This method displays the given price on the screen.
    private void displayPrice(int number) {
        TextView priceTextView = (TextView) findViewById(R.id.price_text_view);
        priceTextView.setText(NumberFormat.getCurrencyInstance().format(number));
    }*/
}

