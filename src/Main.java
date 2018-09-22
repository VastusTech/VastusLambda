enum Ay {
    LMAO
}
public class Main {
    public static void main(String[] args) {
        // This throws an error
        Ay ay = Ay.valueOf("LMAONOT");
        System.out.println("Hello World!");
    }
}
