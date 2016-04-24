package tech.yzaguirre.avisos;

/**
 * Created by david on 22/04/16.
 */
public class Aviso {
    private int id;
    private String content;
    private int important;

    public Aviso(int id, String content, int important) {
        this.id = id;
        this.content = content;
        this.important = important;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getImportant() {
        return important;
    }

    public void setImportant(int important) {
        this.important = important;
    }
}
