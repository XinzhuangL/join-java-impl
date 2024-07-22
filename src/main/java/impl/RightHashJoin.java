package impl;

import lombok.extern.log4j.Log4j2;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
public class RightHashJoin<A, B> implements HashJoin<A,B>{
    @Override
    public List<String> hashJoin(List<A> table1, List<B> table2, Class<A> aClass, Class<B> bClass, String aKey,
                                 String bKey) {
        List<String> result = new ArrayList<>();

        Map<Object, List<B>> hashTable = new HashMap<>();
        // build
        for (B b : table2) {
            try {
                Field field = b.getClass().getDeclaredField(bKey);
                field.setAccessible(true);
                Object key = field.get(b);
                hashTable.computeIfAbsent(key, k -> new ArrayList<>()).add(b);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                log.error(e);
                return Collections.emptyList();
            }
        }

        // probe
        Map<Object, List<A>> leftHashTable = new HashMap<>();
        for (A a : table1) {
            try {
                Field field = a.getClass().getDeclaredField(aKey);
                field.setAccessible(true);
                Object key = field.get(a);
                if (hashTable.containsKey(key)) {
                    leftHashTable.computeIfAbsent(key, k -> new ArrayList<>()).add(a);
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                log.error(e);
                return Collections.emptyList();
            }
        }

        // one more
        for (Map.Entry<Object, List<A>> entry : leftHashTable.entrySet()) {
            Object key = entry.getKey();
            if(hashTable.containsKey(key)) {
                List<B> right = hashTable.get(key);
                List<A> left = entry.getValue();
                // cross
                for(B b : right) {
                    for (A a : left) {
                        result.add(a + ", " + b);
                    }
                }
            }
            hashTable.remove(key);
        }

        // add left b
        for (Map.Entry<Object, List<B>> entry : hashTable.entrySet()) {
            List<B> right = entry.getValue();
            for(B b : right) {
                try {
                    result.add(aClass.newInstance() + ", " + b);
                } catch (InstantiationException | IllegalAccessException e) {
                    log.error(e);
                    return Collections.emptyList();
                }
            }
        }

        return result;
    }
}
