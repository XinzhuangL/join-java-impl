package impl;

import lombok.extern.log4j.Log4j2;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
public class RightSemiHashJoin<A, B> implements HashJoin<A, B> {
    @Override
    public List<String> hashJoin(List<A> table1, List<B> table2, Class<A> aClass, Class<B> bClass, String aKey,
                                 String bKey) {
        List<String> result = new ArrayList<>();

        // build hash table
        Map<Object, List<B>> hashTable = new HashMap<>();
        for (B b : table2) {
            try {
                Field field = b.getClass().getDeclaredField(bKey);
                field.setAccessible(true);
                Object key = field.get(b);
                hashTable.computeIfAbsent(key, k -> new ArrayList<>()).add(b);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                log.error(e.getMessage());
                return Collections.emptyList();
            }
        }

        // probe
        for (A a : table1) {
            try {
                Field field = a.getClass().getDeclaredField(aKey);
                field.setAccessible(true);
                Object key = field.get(a);
                if (hashTable.containsKey(key)) {
                    List<B> bs = hashTable.get(key);
                    for (B b : bs) {
                        result.add(b.toString());
                    }
                    hashTable.remove(key);
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                log.error(e.getMessage());
                return Collections.emptyList();
            }
        }

        return result;
    }
}
