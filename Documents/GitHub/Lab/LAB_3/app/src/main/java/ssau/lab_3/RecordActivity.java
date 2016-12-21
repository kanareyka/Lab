package ssau.lab_3;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.nfc.Tag;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.sql.Blob;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import ssau.lab_3.Adapters.PhotoAdapter;
import ssau.lab_3.DBModel.DBHelper;
import ssau.lab_3.DBModel.Photo;
import ssau.lab_3.DBModel.Record;

public class RecordActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tvStartTime, tvEndTime, tvTimeInterval;
    EditText etDescription;
    Button btnPhoto, btnSave;
    ListView lvPhotos;
    String TAG = "MY_TAG";
    int categoryID;
    Date startTime;
    Date endTime;
    Date interval;
    DBHelper dbHelper;
    ArrayList<Bitmap> photos = new ArrayList<>();
    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        categoryID = getIntent().getIntExtra("id", 0);
        setTitle(getIntent().getStringExtra("name"));
        tvStartTime = (TextView) findViewById(R.id.tvStartTime);
        tvStartTime.setOnClickListener(this);
        tvEndTime = (TextView) findViewById(R.id.tvEndTime);
        tvEndTime.setOnClickListener(this);
        tvTimeInterval = (TextView) findViewById(R.id.tvTimeInterval);
        etDescription = (EditText) findViewById(R.id.etDescription);
        btnPhoto = (Button) findViewById(R.id.btnPhoto);
        btnPhoto.setOnClickListener(this);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);
        lvPhotos = (ListView)findViewById(R.id.lvPhotos);
        initTimes();
        showTimes();
        dbHelper = new DBHelper(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        fillImagesList();
    }

    private void fillImagesList(){
        PhotoAdapter adapter = new PhotoAdapter(this, photos.toArray(new Bitmap[0]));
        lvPhotos.setAdapter(adapter);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            photos.add(imageBitmap);
        }
    }

    private void initTimes() {
        Calendar c = Calendar.getInstance();
        startTime = c.getTime();
        endTime =new Date(startTime.getTime() + 3600000);
    }
    private void showTimes(){
        DateFormat df = new SimpleDateFormat("HH:mm");
        if (startTime.getTime() >= endTime.getTime()){
            Toast.makeText(this, "Время окончания должно быть больше начала!", Toast.LENGTH_LONG).show();
            endTime.setTime(startTime.getTime() + 60000);
        }
        tvStartTime.setText(df.format(startTime));
        tvEndTime.setText(df.format(endTime));
        interval = new Date(endTime.getTime() - startTime.getTime());
        tvTimeInterval.setText(df.format(interval));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvStartTime:
                setStartTime();
                break;
            case R.id.tvEndTime:
                setEndTime();
                break;
            case R.id.btnPhoto:
                dispatchTakePictureIntent();
                break;
            case R.id.btnSave:
                if (etDescription.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Введите описание!", Toast.LENGTH_LONG).show();
                    return;
                }
                Record record = new Record(
                        categoryID,
                        startTime.getTime(),
                        endTime.getTime(),
                        interval.getTime(),
                        etDescription.getText().toString());
                Record.insert(dbHelper, record);
                if (record.getId() < 1){
                    Toast.makeText(getApplicationContext(), "Не удалось добавить запись!", Toast.LENGTH_LONG).show();
                }else{
                    savePhotos(record);
                }
                finish();
                break;
        }
    }

    private void savePhotos(Record record){
        for (Bitmap bitmap: photos){
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] img = stream.toByteArray();
            Photo photo = new Photo(record.getId(), img);
            Photo.insert(dbHelper, photo);
        }
    }

    private void  setStartTime(){
        TimePickerDialog dialog = new TimePickerDialog(
                this                ,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (hourOfDay == 23 && minute == 59){
                            minute = 58;
                            Toast.makeText(getApplicationContext(), "Время начала не может быть больше 23:58!", Toast.LENGTH_LONG).show();
                        }
                        startTime.setHours(hourOfDay);
                        startTime.setMinutes(minute);
                        showTimes();
                    }
                }, startTime.getHours(), startTime.getMinutes(), true);
        dialog.show();
    }

    private void setEndTime(){
        TimePickerDialog dialog = new TimePickerDialog(
                this                ,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        endTime.setHours(hourOfDay);
                        endTime.setMinutes(minute);
                        showTimes();
                    }
                }, endTime.getHours(), endTime.getMinutes(), true);
        dialog.show();
    }

    @Override
    protected void onStop() {
        dbHelper.close();
        super.onStop();
    }
}