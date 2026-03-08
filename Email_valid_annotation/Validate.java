package Email_valid_annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@interface ValidEmailAnnotation{
    String email() default "email is invalid";
}

class abc{
    String name;
    @ValidEmailAnnotation(email = "email must in the form of xyz@gmail.com")
    String email;

    abc(String name,String email){
        this.name=name;
        this.email=email;
    }
}

public class Validate {
    public static void main(String[] args) {
        
        try {
            abc a=new abc("sam", "sam@gmail.com");
            Class c=a.getClass();
            Field f=c.getDeclaredField("email");
            ValidEmailAnnotation ve=f.getAnnotation(ValidEmailAnnotation.class);
            if(ve!=null){
                String validmail=(String)f.get(a);
                if(validmail.contains("@")&& validmail.contains(".")){
                    System.out.println("valid email....");
                }else{
                    System.out.println(ve.email());
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
       

    }
    
}
