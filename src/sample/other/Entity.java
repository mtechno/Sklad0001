package sample.other;

/**
 * Created by Note-001 on 07.12.2016.
 */
public abstract class Entity {

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    protected int id;

    abstract void create();
    abstract void update();
    abstract void delete();
}
