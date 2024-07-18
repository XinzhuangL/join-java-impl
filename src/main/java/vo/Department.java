package vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

/*
CREATE TABLE departments (
    department_id INT NOT NULL COMMENT "ID of the department",
    department_name VARCHAR(255) NOT NULL COMMENT "Name of the department"
) ENGINE = OLAP
 DUPLICATE KEY(department_id) COMMENT "OLAP table for departments"
 DISTRIBUTED BY HASH(department_id)
 BUCKETS 10
 properties("replication_num" = "1");
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Department {
    Integer departmentId;
    String departmentName;

    @Override
    public String toString() {
        return departmentId + ", " + departmentName;
    }
}
