package ddwu.mobile.finalproject.ma01_20191012;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;


public class BookXmlParser {

    public enum TagType { NONE, TITLE, AUTHOR, IMAGE, PUBLISHER, DESCRIPTION };

    final static String TAG_ITEM = "item";
    final static String TAG_TITLE = "title";
    final static String TAG_AUTHOR = "author";
    final static String TAG_IMAGE = "image";
    final static String TAG_PUBLISHER = "publisher";
    final static String TAG_DESCRIPTION = "description";

    public BookXmlParser() {
    }

    public ArrayList<BookDto> parse(String xml) {

        ArrayList<BookDto> resultList = new ArrayList();
        BookDto dto = null;

        TagType tagType = TagType.NONE;

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(xml));

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals(TAG_ITEM)) {
                            dto = new BookDto();
                        } else if (parser.getName().equals(TAG_TITLE)) {
                            if (dto != null) tagType = TagType.TITLE;
                        } else if (parser.getName().equals(TAG_AUTHOR)) {
                            if (dto != null) tagType = TagType.AUTHOR;
                        } else if (parser.getName().equals(TAG_IMAGE)) {
                            if (dto != null) tagType = TagType.IMAGE;
                        } else if (parser.getName().equals(TAG_PUBLISHER)) {
                            if (dto != null) tagType = TagType.PUBLISHER;
                        } else if (parser.getName().equals(TAG_DESCRIPTION)) {
                            if (dto != null) tagType = TagType.DESCRIPTION;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals(TAG_ITEM)) {
                            resultList.add(dto);
                            dto = null;
                        }
                        break;
                    case XmlPullParser.TEXT:
                        switch(tagType) {
                            case TITLE:
                                dto.setBookName(parser.getText());
                                break;
                            case AUTHOR:
                                dto.setBookAuthor(parser.getText());
                                break;
                            case IMAGE:
                                dto.setImageLink(parser.getText());
                                break;
                            case PUBLISHER:
                                dto.setBookPublisher(parser.getText());
                                break;
                            case DESCRIPTION:
                                dto.setBookDescription(parser.getText());
                                break;
                        }
                        tagType = TagType.NONE;
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultList;
    }
}
