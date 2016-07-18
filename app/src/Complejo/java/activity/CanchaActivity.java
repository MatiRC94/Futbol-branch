package activity;

import android.app.Activity;
import android.os.Bundle;

import com.example.matias.R;

/**
 * Created by Matias on 16/06/2016.
 */
public class CanchaActivity  extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Integer cant = extras.getInt("cantcomplex");
        }

    }

}
