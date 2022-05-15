package me.lauriichan.minecraft.itembound.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

public final class JavaInstance {

    private JavaInstance() {
        throw new UnsupportedOperationException("Utility class");
    }

    private static final ConcurrentHashMap<Class<?>, Object> INSTANCES = new ConcurrentHashMap<>();
    private static final ArrayList<Class<?>> TYPES = new ArrayList<>();

    public static <E> E get(Class<E> clazz) {
        Object object = INSTANCES.get(clazz);
        if (object != null) {
            return clazz.cast(object);
        }
        for (int index = 0; index < TYPES.size(); index++) {
            Class<?> type = TYPES.get(index);
            if (!type.isAssignableFrom(clazz)) {
                continue;
            }
            return clazz.cast(INSTANCES.get(type));
        }
        if (!Modifier.isAbstract(clazz.getModifiers())) {
            E value = initialize(clazz);
            if (value == null) {
                return null;
            }
            TYPES.add(clazz);
            INSTANCES.put(clazz, value);
            return value;
        }
        return null;
    }

    public static void remove(Class<?> type) {
        INSTANCES.remove(type);
        TYPES.remove(type);
    }

    public static void clear() {
        INSTANCES.clear();
        TYPES.clear();
    }

    @SuppressWarnings("unchecked")
    public static <E> boolean put(E object) {
        return put((Class<E>) object.getClass(), object);
    }

    public static <E> boolean put(Class<E> clazz, E object) {
        if (clazz == null || object == null || TYPES.contains(clazz)) {
            return false;
        }
        TYPES.add(clazz);
        INSTANCES.put(clazz, object);
        return true;
    }

    private static Constructor<?>[] getConstructors(final Class<?> clazz) {
        final Constructor<?>[] constructor0 = clazz.getConstructors();
        final Constructor<?>[] constructor1 = clazz.getDeclaredConstructors();
        final HashSet<Constructor<?>> constructors = new HashSet<>();
        Collections.addAll(constructors, constructor0);
        Collections.addAll(constructors, constructor1);
        return constructors.toArray(new Constructor[constructors.size()]);
    }

    public static <E> E initialize(Class<E> clazz) {
        Constructor<?>[] constructors = getConstructors(clazz);
        final Class<?>[] arguments = TYPES.toArray(new Class[TYPES.size()]);
        final int max = arguments.length;
        Constructor<?> builder = null;
        int args = 0;
        int[] argIdx = new int[max];
        for (final Constructor<?> constructor : constructors) {
            final int count = constructor.getParameterCount();
            if (count > max || count < args) {
                continue;
            }
            final int[] tmpIdx = new int[max];
            for (int idx = 0; idx < max; idx++) {
                tmpIdx[idx] = -1;
            }
            final Class<?>[] types = constructor.getParameterTypes();
            int tmpArgs = 0;
            for (int index = 0; index < count; index++) {
                for (int idx = 0; idx < max; idx++) {
                    if (!types[index].equals(arguments[idx])) {
                        continue;
                    }
                    tmpIdx[idx] = index;
                    tmpArgs++;
                }
            }
            if (tmpArgs != count) {
                continue;
            }
            argIdx = tmpIdx;
            args = tmpArgs;
            builder = constructor;
        }
        if (builder == null) {
            return null;
        }
        if (args == 0) {
            try {
                return clazz.cast(builder.newInstance());
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                return null;
            }
        }
        final Object[] parameters = new Object[args];
        for (int idx = 0; idx < max; idx++) {
            if (argIdx[idx] == -1) {
                continue;
            }
            parameters[argIdx[idx]] = get(arguments[idx]);
        }
        try {
            return clazz.cast(builder.newInstance(parameters));
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            return null;
        }
    }

}
