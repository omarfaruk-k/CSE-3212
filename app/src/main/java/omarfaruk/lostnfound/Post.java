package omarfaruk.lostnfound;

public class Post {
    private int id;
    private String type;
    private String itemName;
    private String category;
    private String date;
    private byte[] image;
    private String description;
    private String location;
    private String mobile;

    public Post(int id, String type, String itemName, String category, String date, byte[] image,
                String description, String location, String mobile) {
        this.id = id;
        this.type = type;
        this.itemName = itemName;
        this.category = category;
        this.date = date;
        this.image = image;
        this.description = description;
        this.location = location;
        this.mobile = mobile;
    }

    public int getId() { return id; }
    public String getType() { return type; }
    public String getItemName() { return itemName; }
    public String getCategory() { return category; }
    public String getDate() { return date; }
    public byte[] getImage() { return image; }
    public String getDescription() { return description; }
    public String getLocation() { return location; }
    public String getMobile() { return mobile; }
}