public class Text
{

    private int id;
    private String text;

    /**
     * Default constructor with no arguments.
     */
    Text()
    {
        id = 0;
        text="";
    }


    /**
     * Constructor with two (2) arguments.
     * @param id The unique number of the text.
     * @param text The text we want to save into an object @Text.
     */
    Text(int id, String text)
    {
        this.id = id;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Text{" +
                "id=" + id +
                ", text='" + text + '\'' +
                '}';
    }
}
