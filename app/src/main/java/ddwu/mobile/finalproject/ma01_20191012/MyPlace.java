package ddwu.mobile.finalproject.ma01_20191012;
import com.google.android.libraries.places.api.model.Place;

import java.io.Serializable;
import java.util.List;

public class MyPlace implements Serializable {
    private int _id;
    private String placeId;
    private String name;
    private String number;
    private String address;
    private double latitude;
    private double longitude;
    private String memo;

    public MyPlace(String placeId, String name, String number, String address) {
        this.placeId = placeId;
        this.name = name;
        this.number = number;
        this.address = address;
    }

    public MyPlace(int _id, String placeId, String name, String number, String address) {
        this._id = _id;
        this.placeId = placeId;
        this.name = name;
        this.number = number;
        this.address = address;
    }

    public MyPlace(int _id, String placeId, String name, String number, String address, double latitude, double longitude) {
        this._id = _id;
        this.placeId = placeId;
        this.name = name;
        this.number = number;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public MyPlace(int _id, String placeId, String name, String number, String address, double latitude, double longitude, String memo) {
        this._id = _id;
        this.placeId = placeId;
        this.name = name;
        this.number = number;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.memo = memo;
    }


    public MyPlace() {
        super();
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
