package com.zl.gaozhiliang152jianyi.decorate;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @version 1.0
 * @desc:
 * @date 2021/7/26 2:35 下午
 * @auth ALLEN
 */
public class ClassUtils {

    public static <T> Class<T> getGenericClassType(Class clz) {
        Type type = clz.getGenericSuperclass();

        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type[] types = parameterizedType.getActualTypeArguments();
            if (types != null && types.length > 0 && types[0] instanceof Class) {
                return (Class<T>) types[0];
            }
        }

        return (Class<T>) Object.class;
    }

}

abstract class BaseDao<T> {
    private Class<T> clz = ClassUtils.getGenericClassType(getClass());

    public void printClassName(){
        System.out.println(clz.getTypeName());
        System.out.println(clz.getSimpleName());
        System.out.println(clz.getCanonicalName());
        System.out.println(clz.getName());
    }
}

class UserDao extends BaseDao<Animal> {
    public static void main(String[] args) {
        UserDao dao = new UserDao();
        dao.printClassName();
    }
}
