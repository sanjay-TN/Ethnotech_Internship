package Annotations;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.classfile.TypeAnnotation.TargetType;
import java.lang.reflect.Field;



@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@interface SmartPhone{
    String os() default "abc";
    int version() default 1;
}

@SmartPhone(os="android",version=2)
class Nokia{
    String color;
    int id;
    Nokia(String color,int id){
        this.color=color;
        this.id=id;
    }
}

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@interface EmailAnnotation{
    String email() default "invalid email";
}

@EmailAnnotation(email = "sam@gmail.com")
class Email{
    String name;
    int id;
    Email(String name,int id){
        this.name=name;
        this.id=id;
    }

}

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@interface EmailFieldAnnotation{
    String email() default "invalid email";
}

class user{
    String name;

    @EmailFieldAnnotation(email="email is in valid")
    String email;

    user(String name,String email){
        this.name=name;
        this.email=email;
    }
}

public class Demo {
    public static void main(String[] args) {
        // Nokia n=new Nokia("abc", 1);
        // Class c=n.getClass();
        // Annotation annotation= (Annotation) c.getAnnotation(SmartPhone.class);
        // SmartPhone s=(SmartPhone)annotation;
        // System.out.println(s.os()+" "+s.version());

        // Email e=new Email("sam",1);
        // Class c=e.getClass();
        // Annotation a=c.getAnnotation(EmailAnnotation.class);
        // EmailAnnotation ea=(EmailAnnotation)a;
        // if(!ea.equals(null)){
        //     System.out.println(ea.email());
        // }
       
        try {
            user u=new user("sami", "sami@gmail.com");
            Class c=u.getClass();
            
            Field f=c.getDeclaredField("email");
            EmailFieldAnnotation e=f.getAnnotation(EmailFieldAnnotation.class);
            if(e!=null){
                System.out.println(e.email());

                System.out.println(u.email);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
        
    }
    
}
