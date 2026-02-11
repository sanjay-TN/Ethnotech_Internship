public class Class_obj {
    int id;
    String name;
    String city;
    int age;

    // creating constructor [constructors are used to initialisze variables or
    // properties]
    Class_obj(int id, String name, String city, int age) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.age = age;
    }

    public static void main(String[] args) {
        // creating and initializing object of class with new keyword
        Class_obj co = new Class_obj(1, "anil", "bnglr", 25);
        System.out.println(co.age);
        System.out.println(co.name + " " + co.city);

    }
}
