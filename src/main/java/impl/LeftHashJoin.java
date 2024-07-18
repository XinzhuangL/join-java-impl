package impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.log4j.Log4j2;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Log4j2
public class LeftHashJoin<A, B> implements HashJoin<A, B> {

    @Override
    public List<String> hashJoin(List<A> table1, List<B> table2, Class<A> aClass, Class<B> bClass, String aKey, String bKey) {
        Map<Object, List<B>> hashTable = Maps.newHashMap();
        List<String> result = Lists.newArrayList();
        // build
        for (B b : table2) {
            try {
                Field field = b.getClass().getDeclaredField(bKey);
                field.setAccessible(true);
                Object key = field.get(b);
                hashTable.computeIfAbsent(key, k -> Lists.newArrayList()).add(b);
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
                    for(B b: hashTable.get(key)) {
                        result.add(a + ", " + b);
                    }
                } else {
                    B empty = bClass.newInstance();
                    result.add(a + ", " + empty);
                }
            } catch (NoSuchFieldException | IllegalAccessException | InstantiationException e) {
                log.error(e.getMessage());
                return Collections.emptyList();
            }
        }
        return result;
    }
}
