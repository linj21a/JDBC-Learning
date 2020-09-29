package main.java.DAO_design.user;

/**
 * 建立数据类对应数据库中某张表的字段
 * 数据传递对象(DTO)
 */

public class User {
    private int id;
    private String name;
    private float money;

    public User() {
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", username=" + name + ", money=" + money + "]";
    }
}
