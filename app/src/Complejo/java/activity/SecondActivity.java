package activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.matias.R;

/**
 * Created by Matias on 24/05/2016.
 */
public class SecondActivity extends Activity {

    private Spinner spinner;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        spinner = (Spinner) findViewById(R.id.spinner);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.complex_items, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        String cantc = spinner.getSelectedItem().toString();
        int cant = Integer.parseInt(cantc);
        /*
        int spinner_pos = spinner.getSelectedItemPosition();
        int[] size_values = getResources().getIntArray(R.array.complex_items);
        int cant = Integer.valueOf(size_values[spinner_pos]);
        */
        Toast.makeText(getApplicationContext(),
                "HAY :"+cant+"complejos", Toast.LENGTH_LONG)
                .show();
    }



    private void complexnext(int cant){
        Intent intent = new Intent(this, CanchaActivity.class);
        intent.putExtra("cantcomplex",cant);
        startActivity(intent);
        finish();
    }

}
