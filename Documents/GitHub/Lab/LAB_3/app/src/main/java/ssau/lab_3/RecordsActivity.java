package ssau.lab_3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import ssau.lab_3.Adapters.RecordAdapter;
import ssau.lab_3.DBModel.DBHelper;
import ssau.lab_3.DBModel.Photo;
import ssau.lab_3.DBModel.PhotosActivity;
import ssau.lab_3.DBModel.Record;

public class RecordsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    DBHelper dbHelper;
    ListView lvRecords;
    Record[] records;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);
        setTitle("Отметки времени");
        lvRecords = (ListView) findViewById(R.id.lvRecords);
        dbHelper = new DBHelper(this);
        records = Record.getRecords(dbHelper);
        lvRecords.setOnItemClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        RecordAdapter adapter = new RecordAdapter(this, records);
        lvRecords.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Record record = (Record) view.getTag();
        if (record != null) {
            Photo[] photos = Photo.getPhotos(dbHelper, record.getId());
            if (photos.length > 0) {
                Intent intent = new Intent(this, PhotosActivity.class);
                intent.putExtra("id", record.getId());
                startActivity(intent);
            } else {
                Toast.makeText(this, "Нет фотографий", Toast.LENGTH_LONG).show();
            }
        }
    }
}