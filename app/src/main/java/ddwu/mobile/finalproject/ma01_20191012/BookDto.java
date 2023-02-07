package ddwu.mobile.finalproject.ma01_20191012;

import android.text.Html;
import android.text.Spanned;

import java.io.Serializable;

public class BookDto implements Serializable {

    private int _id;
    private String bookName;
    private String bookAuthor;
    private String bookPublisher;
    private String link;
    private String imageLink;
    private String imageFileName = null;       // 외부저장소에 저장했을 때의 파일명
    private String bookMemo;
    private int bookCate = -1;
    private String bookDescription;
    private String plusimageFileName = null;
    private int plusType = -1;

    public BookDto() {
        super();
    }

    public BookDto(int _id, String bookName, String bookAuthor, String bookPublisher, String imageFileName, String bookMemo, int bookCate) {
        this._id = _id;
        this.bookName = bookName;
        this.bookAuthor = bookAuthor;
        this.bookPublisher = bookPublisher;
        this.imageFileName = imageFileName;
        this.bookMemo = bookMemo;
        this.bookCate = bookCate;
    }
    public BookDto(int _id, String bookName, String bookAuthor, String bookPublisher, String imageFileName, String bookMemo, int bookCate, String bookDescription) {
        this._id = _id;
        this.bookName = bookName;
        this.bookAuthor = bookAuthor;
        this.bookPublisher = bookPublisher;
        this.imageFileName = imageFileName;
        this.bookMemo = bookMemo;
        this.bookCate = bookCate;
        this.bookDescription = bookDescription;
    }

    public BookDto(int _id, String bookName, String bookAuthor, String bookPublisher, String imageFileName, String bookMemo, int bookCate, String bookDescription, String plusimageFileName) {
        this._id = _id;
        this.bookName = bookName;
        this.bookAuthor = bookAuthor;
        this.bookPublisher = bookPublisher;
        this.imageFileName = imageFileName;
        this.bookMemo = bookMemo;
        this.bookCate = bookCate;
        this.bookDescription = bookDescription;
        this.plusimageFileName = plusimageFileName;
    }

    public BookDto(int _id, String bookName, String bookAuthor, String bookPublisher, String imageFileName, String bookMemo, int bookCate, String bookDescription, String plusimageFileName, int plusType) {
        this._id = _id;
        this.bookName = bookName;
        this.bookAuthor = bookAuthor;
        this.bookPublisher = bookPublisher;
        this.imageFileName = imageFileName;
        this.bookMemo = bookMemo;
        this.bookCate = bookCate;
        this.bookDescription = bookDescription;
        this.plusimageFileName = plusimageFileName;
        this.plusType = plusType;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    public String getBookName() {
        Spanned spanned = Html.fromHtml(bookName);
        return spanned.toString();
//        return title;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookPublisher() {
        return bookPublisher;
    }

    public void setBookPublisher(String bookPublisher) {
        this.bookPublisher = bookPublisher;
    }

    public String getBookMemo() {
        return bookMemo;
    }

    public void setBookMemo(String bookMemo) {
        this.bookMemo = bookMemo;
    }

    public int getBookCate() {
        return bookCate;
    }

    public void setBookCate(int bookCate) {
        this.bookCate = bookCate;
    }

    public String getBookDescription() {
        return bookDescription;
    }

    public void setBookDescription(String bookDescription) {
        this.bookDescription = bookDescription;
    }

    public String getPlusimageFileName() {
        return plusimageFileName;
    }

    public void setPlusimageFileName(String plusimageFileName) {
        this.plusimageFileName = plusimageFileName;
    }

    public int getPlusType() {
        return plusType;
    }

    public void setPlusType(int plusType) {
        this.plusType = plusType;
    }

    @Override
    public String toString() {
        return  _id + ": " + bookName + " (" + bookAuthor + ')';
    }
}
