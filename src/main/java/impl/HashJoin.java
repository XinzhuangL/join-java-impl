package impl;

import java.util.List;

public interface HashJoin<A, B> {

    List<String> hashJoin(List<A> table1, List<B> table2, Class<A> aClass, Class<B> bClass, String aKey, String bKey);
}
