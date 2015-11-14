package cabbage63.github.com.stripeanimation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import cabbage63.github.com.library.StripeTextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button showButton = (Button)findViewById(R.id.showButton);
        Button hideButton = (Button)findViewById(R.id.hideButton);
        final StripeTextView stripeTextView = (StripeTextView)findViewById(R.id.stripeTextView);

        showButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stripeTextView.show();
            }
        });

        hideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stripeTextView.hide();
            }
        });
    }



}
