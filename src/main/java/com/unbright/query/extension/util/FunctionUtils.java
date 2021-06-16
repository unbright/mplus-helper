package com.unbright.query.extension.util;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.ClassUtils;
import com.baomidou.mybatisplus.core.toolkit.ExceptionUtils;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.unbright.query.extension.support.JFunction;

import java.lang.invoke.SerializedLambda;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IDEA
 * ProjectName: mplus-helper
 * Date: 2020/4/21
 * Time: 12:01
 *
 * @author WZP
 * @version v1.0
 */
public class FunctionUtils {

    /**
     * SerializedLambda 反序列化缓存
     */
    private static final Map<Class<?>, WeakReference<SerializedLambda>> FUNC_CACHE = new ConcurrentHashMap<>();

    /**
     * 解析 lambda 表达式, 该方法只是调用了  中的方法，在此基础上加了缓存。
     * 该缓存可能会在任意不定的时间被清除
     *
     * @param func 需要解析的 lambda 对象
     * @return 返回解析后的结果
     */
    public static SerializedLambda resolve(JFunction func) {
        Class<?> clazz = func.getClass();
        return Optional.ofNullable(FUNC_CACHE.get(clazz))
                .map(WeakReference::get)
                .orElseGet(() -> {
                    SerializedLambda lambda = readLambda(func);
                    FUNC_CACHE.put(clazz, new WeakReference<>(lambda));
                    return lambda;
                });
    }

    /**
     * 通过反序列化转换 lambda 表达式，该方法只能序列化 lambda 表达式，不能序列化接口实现或者正常非 lambda 写法的对象
     *
     * @param lambda lambda对象
     * @return 返回解析后的 SerializedLambda
     */
    public static SerializedLambda readLambda(JFunction lambda) {
        if (!lambda.getClass().isSynthetic()) {
            throw ExceptionUtils.mpe("该方法仅能传入 lambda 表达式产生的合成类");
        }
        try {
            Method method = lambda.getClass().getDeclaredMethod("writeReplace");
            return (SerializedLambda) ReflectionKit.setAccessible(method).invoke(lambda);
        } catch (NoSuchMethodException e) {
            String message = "Cannot find method writeReplace, please make sure that the lambda composite class is currently passed in";
            throw new MybatisPlusException(message);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new MybatisPlusException(e);
        }
    }

    public static Class<?> getInstantiatedClass(JFunction func) {
        SerializedLambda lambda = resolve(func);
        String instantiatedType = lambda.getImplClass().replace('/', '.');
        return ClassUtils.toClassConfident(instantiatedType);
    }
}
