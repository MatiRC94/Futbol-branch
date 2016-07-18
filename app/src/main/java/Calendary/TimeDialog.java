package Calendary;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by Matias on 24/05/2016.
 */
@SuppressLint("ValidFragment")

public class TimeDialog extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    EditText etHora;
    public TimeDialog(View view){
        etHora=(EditText)view;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {


        // Get Current Time
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);
        // Create a new instance of DatePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, mHour, mMinute, true);


    }
    public void onTimeSet(TimePicker view,int mHour,int mMinute) {
        //show to the selected date in the text box
        if( mMinute < 10 ) {
            String time = (mHour + ":0" + mMinute);
            etHora.setText(time);
        } else {
            String time = (mHour + ":" + mMinute);
            etHora.setText(time);
        }
    }



}
