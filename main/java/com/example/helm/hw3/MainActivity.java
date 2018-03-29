package com.example.helm.hw3;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.content.DialogInterface.BUTTON_NEGATIVE;

public class MainActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    Spinner spinnerFrom;
    Spinner spinnerTo;
    TextView tvSelection;
    TextView tvResult;
    Button btnSelection;
    ImageButton btnChange;
    Button btnGet;
    DatePickerDialog datePickerDialog;
    private String[] arrFrom = {"EUR","TRY", "USD","GBP"};
    private String[] arrTo = {"TRY", "USD", "EUR", "GBP"};
    Calendar c = Calendar.getInstance();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        spinnerFrom = (Spinner) findViewById(R.id.spinnerFrom);
        spinnerTo = (Spinner) findViewById(R.id.spinnerTo);
        tvResult = (TextView) findViewById(R.id.tvResult);
        tvSelection = (TextView) findViewById(R.id.tvSelection);
        btnSelection = (Button) findViewById(R.id.btnSelection);
        btnGet = (Button) findViewById(R.id.btnGet);
        btnChange = (ImageButton) findViewById(R.id.btnChange);

        final ArrayAdapter<String> adapterFrom = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arrFrom);
        adapterFrom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFrom.setAdapter(adapterFrom);
        final ArrayAdapter<String> adapterTo = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arrTo);
        adapterTo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTo.setAdapter(adapterTo);


        tvSelection.setText(DateUtil.getToday());

        btnSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date = tvSelection.getText().toString();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date convertedDate = new Date();
                try {
                    convertedDate = dateFormat.parse(date);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                c.setTime(convertedDate);
                datePickerDialog = new DatePickerDialog(MainActivity.this, R.style.DatePickerDialogStyle, new
                        DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int selectedYear, int
                                    selectedMonth, int selectedDay) {
                                //-- Set selected date
                                selectedMonth=++selectedMonth;
                                if(selectedMonth<10){
                                    if(selectedDay<10)
                                    tvSelection.setText(selectedYear + "-0" + selectedMonth + "-0" +selectedDay);
                                    else
                                        tvSelection.setText(selectedYear + "-0" + selectedMonth + "-" +selectedDay);
                                }
                                else {
                                    if (selectedDay<10)
                                    tvSelection.setText(selectedYear + "-" + selectedMonth + "-0" +
                                            selectedDay);
                                    else
                                        tvSelection.setText(selectedYear + "-" + selectedMonth + "-" +
                                                selectedDay);
                                }

                            }
                        }, c
                        .get(c.YEAR), c.get(c.MONTH),
                        c.get(c.DAY_OF_MONTH));

                datePickerDialog.show();
            }
        });
        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String from = spinnerFrom.getSelectedItem().toString();
                String date = tvSelection.getText().toString();
                if (date.equals(DateUtil.getToday()))
                    date = "latest";
                String url = "http://api.fixer.io/" + date + "?base=" + from;
                new BackgroundTask().execute(url);
            }
        });
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String from =  spinnerFrom.getSelectedItem().toString();
                String to =  spinnerTo.getSelectedItem().toString();
                int fromPos = adapterFrom.getPosition(to);
                int toPos = adapterTo.getPosition(from);
                spinnerFrom.setSelection(fromPos);
                spinnerTo.setSelection(toPos);
            }
        });

    }

    // Async Task
    class BackgroundTask extends AsyncTask<String, Integer, String> {

        String temp;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create progress dialog
            progressDialog = new ProgressDialog(MainActivity.this);
            // Sets the current progress
            progressDialog.setProgress(0);
            // Sets the maximum allowed progress value
            progressDialog.setMax(1);
            progressDialog.setCancelable(true);
            // Sets the style of this ProgressDialog, either STYLE_SPINNER or STYLE_HORIZONTAL
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setButton(BUTTON_NEGATIVE,
                    "Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // cancel async task
                            cancel(true);
                        }
                    });

            // Creates and shows a ProgressDialog
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... urls) {
            String url = urls[0];
            temp = WebServiceUtil.getRequest(url);
            return "";

        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            Integer currentProggress = values[0];
            // update progress bar
            progressDialog.setProgress(currentProggress);

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


            try {
                // parse response to json object
                String to= spinnerTo.getSelectedItem().toString();
                JSONObject jsonObject = new JSONObject(temp);
                JSONObject jsonResponse = jsonObject.getJSONObject("rates");
                if (jsonResponse.has(to))
                    result = jsonResponse.getString(to);
                else
                    result="0";
                } catch (JSONException e) {
                e.printStackTrace();
            }

            tvResult.setText(result);
            // close progress dialog
            progressDialog.dismiss();
        }

        @Override
        protected void onCancelled(String result) {
            super.onCancelled(result);
            // close progress dialog
            progressDialog.dismiss();
        }
    }
}



