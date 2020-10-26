package lofod.reviewer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.UUID;

public class EditItem extends AppCompatActivity {
    private Card newCard;
    public static final int imagePickCode = 322;
    public static final int resultCode = 3228;

    private ImageView photo;
    private EditText productName;
    private EditText manufacturerName;
    private EditText categoryName;
    private EditText amount;
    private EditText price;
    private Spinner amountType;
    private RatingBar ratingBar;
    private EditText date;
    private EditText review;
    private Bitmap itemImageBitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        photo = findViewById(R.id.uploadPhoto);
        productName = findViewById(R.id.editItemName);
        manufacturerName = findViewById(R.id.editManufacturer);
        categoryName = findViewById(R.id.editCategory);
        amount = findViewById(R.id.amount);
        price = findViewById(R.id.editPrice);
        amountType = findViewById(R.id.amountType);
        ratingBar = findViewById(R.id.itemRating);
        date = findViewById(R.id.addDate);
        review = findViewById(R.id.review);

        Intent intent = getIntent();
        if (intent != null) {
            Card card = intent.getParcelableExtra("card");

            try {
                String photoUri = getFilesDir().toString() + "/" + card.getPhoto() + ".png";
                Uri uri = Uri.parse(photoUri);
                assert uri != null;
                InputStream inputStream = getContentResolver().openInputStream(uri);
                itemImageBitmap = BitmapFactory.decodeStream(inputStream);
                photo.setImageBitmap(itemImageBitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            productName.setText(card.getName());
            manufacturerName.setText(card.getManufacturer());
            categoryName.setText(card.getCategory());
            amount.setText(card.getAmount());
            price.setText(String.valueOf(card.getPrice()));
            amountType.setSelection(card.getAmountType());
            ratingBar.setRating(card.getMark());
            date.setText(card.getDate());
            review.setText(card.getReview());
        }
    }

    public void onClick(View view) {
        if (findViewById(R.id.uploadPhoto).equals(view)) {
            boolean isImageUploaded = true;
            newCard.setPhoto(UUID.randomUUID().toString());

            Intent imagePick = new Intent(Intent.ACTION_PICK);
            imagePick.setType("image/*");
            startActivityForResult(imagePick, imagePickCode);
            if (itemImageBitmap == null) {
                isImageUploaded = false;
                newCard.setPhoto("default");
            } else {
                String filename = getFilesDir().toString() + "/" + newCard.getPhoto() + ".png";
                try (FileOutputStream out = new FileOutputStream(filename)) {
                    itemImageBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Toast toast;
            if (productName.getText().toString().equals("")) {
                toast = Toast.makeText(this, "А как ваш товар называется?",
                        Toast.LENGTH_LONG);
                toast.show();
                return;
            } else if (amount.getText().toString().equals("")) {
                toast = Toast.makeText(this, "А скока в одной упаковке товара?",
                        Toast.LENGTH_LONG);
                toast.show();
                return;
            } else if (price.getText().toString().equals("")) {
                toast = Toast.makeText(this, "А скока стоит ваш товар?",
                        Toast.LENGTH_LONG);
                toast.show();
                return;
            }

            newCard.setAmount(amount.getText().toString());
            newCard.setAmountType(amountType.getSelectedItemPosition());
            newCard.setCategory(categoryName.getText().toString());
            newCard.setDate(date.getText().toString());
            newCard.setManufacturer(manufacturerName.getText().toString());
            newCard.setMark(ratingBar.getNumStars());
            newCard.setName(productName.getText().toString());
            newCard.setPrice(Float.parseFloat(price.getText().toString()));
            newCard.setReview(review.getText().toString());

            Intent result = new Intent();
            result.putExtra("newCard", newCard);
            setResult(resultCode, result);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == imagePickCode) {
            try {
                assert data != null;
                Uri uri = data.getData();
                assert uri != null;
                InputStream inputStream = getContentResolver().openInputStream(uri);
                itemImageBitmap = BitmapFactory.decodeStream(inputStream);
                photo.setImageBitmap(itemImageBitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
