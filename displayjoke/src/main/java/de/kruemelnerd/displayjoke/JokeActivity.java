package de.kruemelnerd.displayjoke;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class JokeActivity extends AppCompatActivity {

    public static final String EXTRA_JOKE = "extra_joke";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joke);


        Intent intent = getIntent();
        String joke = intent.getStringExtra(EXTRA_JOKE);

        TextView display_joke = (TextView) findViewById(R.id.textView_joke);
        display_joke.setText(joke);
    }
}
