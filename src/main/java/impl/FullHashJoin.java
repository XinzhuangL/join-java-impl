package impl;

import lombok.extern.log4j.Log4j2;

import java.lang.reflect.Field;
import java.security.Key;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Log4j2
public class FullHashJoin<A, B> implements HashJoin<A, B> {
    @Override
    public List<String> hashJoin(List<A> table1, List<B> table2, Class<A> aClass, Class<B> bClass, String aKey,
                                 String bKey) {
        List<String> result = new ArrayList<>();
        // build
        Map<Object, List<B>> hashTable = new HashMap<>();
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

        Set<Object> isMatched = new HashSet<>();

        // probe
        for (A a : table1) {
            try {
                Field field = a.getClass().getDeclaredField(aKey);
                field.setAccessible(true);
                Object key = field.get(a);
                if (hashTable.containsKey(key)) {
                    isMatched.add(key);
                    List<B> list = hashTable.get(key);
                    for (B b : list) {
                        result.add(a + ", " + b.toString());
                    }
                } else {
                    result.add(a + ", " + bClass.newInstance());
                }
            } catch (NoSuchFieldException | IllegalAccessException | InstantiationException e) {
                log.error(e);
                return Collections.emptyList();
            }
        }

        // add unmatched in b
        for (Map.Entry<Object, List<B>> entry : hashTable.entrySet()) {
            if (!isMatched.contains(entry.getKey())) {
                List<B> value = entry.getValue();
                for (B b : value) {
                    try {
                        result.add(aClass.newInstance() + ", " + b.toString());
                    } catch (InstantiationException | IllegalAccessException e) {
                        log.error(e);
                        return Collections.emptyList();
                    }
                }
            }
        }
        return result;
    }
}
