package spqrlol.exproj;

/**
 * Created by ilaforces1 on 4/29/15.
 */


/*
Class basically is just a layout of a single photo object.
Each iteration through the method "processResult" in "GetFlickrJsonData.java" will produce
another photo object with the given traits/variables.
*/

public class Photo {

    private String title;
    private String author;
    private String authorID;
    private String link;
    private String tags;
    private String image;

    public Photo(String title, String author, String authorID, String link, String tags, String image) {
        this.title = title;
        this.author = author;
        this.authorID = authorID;
        this.link = link;
        this.tags = tags;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getAuthorID() {
        return authorID;
    }

    public String getLink() {
        return link;
    }

    public String getTags() {
        return tags;
    }

    public String getImage() {
        return image;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", authorID='" + authorID + '\'' +
                ", link='" + link + '\'' +
                ", tags='" + tags + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
