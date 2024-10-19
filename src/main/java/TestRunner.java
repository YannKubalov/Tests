import annotation.AfterSuit;
import annotation.AfterTest;
import annotation.BeforeSuit;
import annotation.BeforeTest;
import annotation.CsvSource;
import annotation.Test;
import lombok.SneakyThrows;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Comparator;

public class TestRunner {

    @SneakyThrows
    public static void runTests(Class<?> cls){
        var methods = cls.getDeclaredMethods();
        var instance = cls.getDeclaredConstructor().newInstance();
        checkForAnnotation(methods, AfterSuit.class);
        checkForAnnotation(methods, BeforeSuit.class);

        var testSuit = sortListByPriority(methods);

        invokeStaticMethod(methods, BeforeSuit.class);
        invokeSuit(methods, testSuit, instance);
        invokeStaticMethod(methods, AfterSuit.class);
    }

    @SneakyThrows
    private static void invokeSuit(Method[] methods, ArrayList<Method> suit, Object instance){
        for (Method m : suit){
            invokeNonStaticMethod(methods, BeforeTest.class, instance);
            if (m.isAnnotationPresent(CsvSource.class)){
                var params = m.getAnnotation(CsvSource.class).params().split(",\\s*");
                m.invoke(instance, Integer.parseInt(params[0]), params[1], Integer.parseInt(params[2]),
                        Boolean.parseBoolean(params[3]) );
            } else m.invoke(instance);
            invokeNonStaticMethod(methods, AfterTest.class, instance);
        }
    }

    @SneakyThrows
    private static void invokeNonStaticMethod(Method[] methods, Class<? extends Annotation> annotation, Object instance){
        for(Method m : methods){
            if(m.isAnnotationPresent(annotation)){
                m.invoke(instance);
            }
        }
    }

    @SneakyThrows
    private static void invokeStaticMethod(Method[] methods, Class<? extends Annotation> annotation){
        for(Method m : methods){
            if(m.isAnnotationPresent(annotation)){
                m.invoke(null);
            }
        }
    }


    private static void checkForAnnotation(Method[] methods, Class<? extends Annotation> annotation){
        int count = 0;
        for(Method m : methods){
            if(m.isAnnotationPresent(annotation)) {
                if(!Modifier.isStatic(m.getModifiers())){
                    throw new IllegalStateException("Метод" + m.getName() + "должен быть статическим");
                }
                count++;
            }
        }
        if(count > 1){
            throw new IllegalStateException("Класс содержит несколько методов с аннотацией" + annotation.getName());
        }
    }

    private static ArrayList<Method> sortListByPriority(Method[] methods){
        var testMethods = new ArrayList<Method>();
        for(Method m : methods){
            if(m.isAnnotationPresent(Test.class)){
                testMethods.add(m);
            }
        }
        testMethods.sort(Comparator.comparingInt((Method m) -> m.getAnnotation(Test.class).priority()).reversed());
        return testMethods;
    }
}
