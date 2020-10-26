package lofod.reviewer;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class Card implements Parcelable {
    private String name;
    private double price;
    private String manufacturer;
    private String amount;
    private String review;
    private int mark;
    private String category;
    private int amountType;
    private String date;
    private String photo;


    protected Card(Parcel in) {
        name = in.readString();
        price = in.readDouble();
        manufacturer = in.readString();
        amount = in.readString();
        review = in.readString();
        mark = in.readInt();
        category = in.readString();
        amountType = in.readInt();
        date = in.readString();
        photo = in.readString();
    }

    public static final Creator<Card> CREATOR = new Creator<Card>() {
        @Override
        public Card createFromParcel(Parcel in) {
            return new Card(in);
        }

        @Override
        public Card[] newArray(int size) {
            return new Card[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeDouble(price);
        dest.writeString(manufacturer);
        dest.writeString(amount);
        dest.writeString(review);
        dest.writeInt(mark);
        dest.writeString(category);
        dest.writeInt(amountType);
        dest.writeString(date);
        dest.writeString(photo);
    }

    public Card() {
        name = "";
        price = 0;
        manufacturer = "";
        amount = "";
        review = "";
        mark = 0;
        category = "";
        amountType = 0;
        date = "";
        photo = "default";
    }

    public Card(String name, double price, String manufacturer, String amount, String review,
                int mark, String category, int amountType, String date, String photo) {
        this.name = name;
        this.price = price;
        this.manufacturer = manufacturer;
        this.amount = amount;
        this.review = review;
        this.mark = mark;
        this.category = category;
        this.amountType = amountType;
        this.date = date;
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getAmountType() {
        return amountType;
    }

    public void setAmountType(int amountType) {
        this.amountType = amountType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
