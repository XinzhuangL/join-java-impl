package vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
CREATE TABLE employees (
    employee_id INT NOT NULL COMMENT "ID of the employee",
    name VARCHAR(255) NOT NULL COMMENT "Name of the employee",
    department_id INT COMMENT "ID of the department the employee belongs to"
) ENGINE = OLAP DUPLICATE KEY(employee_id)
 COMMENT "OLAP table for employees"
 DISTRIBUTED BY HASH(employee_id)
 BUCKETS 10 PROPERTIES("replication_num" = "1");
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    private Integer employeeId;
    private String employeeName;
    private Integer departmentId;

    @Override
    public String toString() {
        return employeeId + ", " + employeeName + ", " + departmentId;
    }
}
