package hello.proxy.reflection;

public class Team {

    private String name;
    public int number;

    public Team(String name, int number) {

        this.name = name;
        this.number = number;
    }

    public int transferNumber(int value) {
        return number + value;
    }

    @Override
    public String toString() {
        return "Team{" +
                "name='" + name + '\'' +
                ", number=" + number +
                '}';
    }
}
