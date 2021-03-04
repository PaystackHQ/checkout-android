package dev.michaelobi.javaexample;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.paystack.checkout.CheckoutResultListener;
import com.paystack.checkout.PaystackCheckout;
import com.paystack.checkout.model.Transaction;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button payBtn = findViewById(R.id.btnPay);
        payBtn.setOnClickListener(v -> {
            PaystackCheckout checkout = new PaystackCheckout.Builder(this, "michael@paystack.com", 10000, "NGN")
                    .reference("lezzGoLIvEagAIn41")
                    .build();
            checkout.charge(new CheckoutResultListener() {
                @Override
                public void onSuccess(@NonNull Transaction transaction) {
                    String message = String.format("Transaction complete - Ref: %s", transaction.getReference());
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
                    Log.i(TAG, "Transaction Success");
                }

                @Override
                public void onError(@NonNull Throwable exception) {
                    Toast.makeText(MainActivity.this, "Transaction failed", Toast.LENGTH_LONG)
                            .show();
                    Log.e(TAG, exception.getMessage(), exception);
                }

                @Override
                public void onCancelled() {
                    Toast.makeText(MainActivity.this, "Transaction cancelled by user", Toast.LENGTH_LONG)
                            .show();
                    Log.i(TAG, "Payment process cancelled!");
                }
            });
        });
    }
}