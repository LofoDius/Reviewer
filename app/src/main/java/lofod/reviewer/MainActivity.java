package lofod.reviewer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private DBWorker dbWorker;
    private CardAdapter adapter;
    private Dialog chooseActionDialog;
    private Dialog itemInfoDialog;
    private ArrayList<Card> data;
    private Boolean choice = null;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbWorker = new DBWorker(this);

        chooseActionDialog = new Dialog(this);
        chooseActionDialog.setContentView(R.layout.dialog_choose_action);
        chooseActionDialog.setCanceledOnTouchOutside(true);
        Button editButton = chooseActionDialog.findViewById(R.id.editButton);
        Button deleteButton = chooseActionDialog.findViewById(R.id.deleteButton);
        editButton.setOnClickListener(v -> {
            choice = true;
        });
        deleteButton.setOnClickListener(v -> {
            choice = false;
        });

        data = new ArrayList<>();
        getData(null);

        adapter = new CardAdapter(data, getFilesDir(), this);
        RecyclerView recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }

    private void getData(String having) {
        SQLiteDatabase db = dbWorker.getReadableDatabase();
        Cursor cursor = db.query(DBWorker.reviewsTable, new String[]{"*"}, null,
                null, null, having, "mark DESC");

        try {
            int productName = cursor.getColumnIndex("product_name");
            int price = cursor.getColumnIndex("price");
            int manufacturer = cursor.getColumnIndex("manufacturer");
            int amount = cursor.getColumnIndex("amount");
            int review = cursor.getColumnIndex("review");
            int mark = cursor.getColumnIndex("mark");
            int category = cursor.getColumnIndex("category");
            int amountType = cursor.getColumnIndex("amount_type");
            int date = cursor.getColumnIndex("date");
            int photo = cursor.getColumnIndex("photo_uri");

            data.clear();
            while(cursor.moveToNext()) {
                Card card = new Card(cursor.getString(productName), cursor.getDouble(price),
                        cursor.getString(manufacturer), cursor.getString(amount),
                        cursor.getString(review), cursor.getInt(mark),
                        cursor.getString(category), cursor.getInt(amountType),
                        cursor.getString(date), cursor.getString(photo));
                data.add(card);
            }
        } finally {
            cursor.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        this.menu = menu;
        getMenuInflater().inflate(R.menu.categories_menu, menu);
        SQLiteDatabase db = dbWorker.getReadableDatabase();
        String[] fields = {"category_name"};
        Cursor cursor = db.query("categories", fields, null, null, null, null, null);
        try {
            int categoryName = cursor.getColumnIndex("category_name");
            int i = 0;
            while (cursor.moveToNext()) {
                menu.add(Menu.NONE, i, Menu.NONE, cursor.getString(categoryName));
                i++;
            }
            menu.add(Menu.NONE, i, Menu.NONE, "\t+");

        } finally {
            cursor.close();
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String itemName = item.getTitle().toString();
        String having = null;
        if (itemName.equals("\t+")) {
            menu.getItem(0).setChecked(true);
        } else if (!itemName.equals(getResources().getString(R.string.All_goods))) {
            having = "category = " + itemName;
        }
        getData(having);
        adapter.setData(data);
        return true;
    }

    public void onLongClick(int position) {
        chooseActionDialog.show();
        if(choice != null && choice) {
            editItem(position);
        } else {
            deleteItem(position);
        }
        choice = null;
    }

    public void editItem(int position) {
        Intent intent = new Intent(getApplicationContext(), EditItem.class);
        intent.putExtra("card", adapter.getCard(position));
        startActivityForResult(intent, EditItem.resultCode);
    }

    public void deleteItem(int position) {
        adapter.removeCard(position);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == EditItem.resultCode) {
            if(data == null) {
                return;
            }

            Card newCard = data.getParcelableExtra("newCard");
            adapter.addCard(newCard);
            SQLiteDatabase db = dbWorker.getWritableDatabase();
            //TODO db.insert()
        }
    }

    public void onClick(View view) {
        Intent intent = new Intent(getApplicationContext(), EditItem.class);
        startActivityForResult(intent, EditItem.resultCode);
    }
}