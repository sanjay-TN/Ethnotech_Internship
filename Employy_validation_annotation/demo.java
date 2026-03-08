package Employy_validation_annotation;

import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.util.regex.Pattern;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@interface Validate {
    String regex();    
    String message();  
}

class Emp {
    String name;

    
    @Validate(regex = "^[a-zA-Z]+\\d+$", message = "ID must start with alphabet followed by numbers")
    String id;

   
    @Validate(regex = "^\\d{10}$", message = "Phone must be exactly 10 digits!")
    String phone;

    Emp(String name, String id, String phone) {
        this.name = name;
        this.id = id;
        this.phone = phone;
    }
}

public class demo {
    public static void main(String[] args) {
        try {
           
            Emp e = new Emp("Sam", "saam2345", "1221301253");
            
            Class c = e.getClass();

            for (Field f : c.getDeclaredFields()) {
                if (f.isAnnotationPresent(Validate.class)) {
                    Validate v = f.getAnnotation(Validate.class);
                    f.setAccessible(true);
                    String value = (String) f.get(e); 

                   
                    if (!Pattern.matches(v.regex(), value)) {
                        System.out.println(" Error in " + f.getName() + ": " + v.message());
                    } else {
                        System.out.println(  f.getName() + " is valid!");
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}