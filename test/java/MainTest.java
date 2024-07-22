import com.google.common.collect.Lists;
import impl.FullHashJoin;
import impl.HashJoin;
import impl.InnerHashJoin;
import impl.LeftAntiHashJoin;
import impl.LeftHashJoin;
import impl.LeftSemiHashJoin;
import impl.RightAntiHashJoin;
import impl.RightHashJoin;
import impl.RightSemiHashJoin;
import lombok.extern.log4j.Log4j2;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import vo.Department;
import vo.Employee;

import java.util.List;

@Log4j2
public class MainTest {
    List<Employee> employees;
    List<Department> departments;

    @Before
    public void setUp() throws Exception {
        employees = Lists.newArrayList(
                new Employee(1, "Alice", 101),
                new Employee(2, "Bob", 102),
                new Employee(3, "Charlie", 103),
                new Employee(4, "David", 101),
                new Employee(4, "David Two", 102),
                new Employee(5, "Eve", 104),
                new Employee(6, "Frank", 105),
                new Employee(7, "Grace", null)
        );

        departments = Lists.newArrayList(
                new Department(101, "Human Resources"),
                new Department(102, "Marketing"),
                new Department(103, "Engineering"),
                new Department(104, "Customer Support"),
                new Department(106, "Research"),
                new Department(106, "Research Copy")
        );
    }

    @Test
    public void innerHashJoinTest() {
        List<String> expected = Lists.newArrayList(
                "1, Alice, 101, 101, Human Resources",
                "2, Bob, 102, 102, Marketing",
                "3, Charlie, 103, 103, Engineering",
                "4, David, 101, 101, Human Resources",
                "4, David Two, 102, 102, Marketing",
                "5, Eve, 104, 104, Customer Support"
        );

        HashJoin<Employee, Department> join = new InnerHashJoin<>();
        List<String> result =
                join.hashJoin(employees, departments, Employee.class, Department.class, "departmentId", "departmentId");
        Assert.assertEquals("The result list does not match the expected list", expected, result);
        for (String line : result) {
            log.info(line);
        }
    }

    @Test
    public void leftHashJoinTest() {
        List<String> expected = Lists.newArrayList(
                "1, Alice, 101, 101, Human Resources",
                "2, Bob, 102, 102, Marketing",
                "3, Charlie, 103, 103, Engineering",
                "4, David, 101, 101, Human Resources",
                "4, David Two, 102, 102, Marketing",
                "5, Eve, 104, 104, Customer Support",
                "6, Frank, 105, null, null",
                "7, Grace, null, null, null"
        );

        HashJoin<Employee, Department> join = new LeftHashJoin<>();
        List<String> result =
                join.hashJoin(employees, departments, Employee.class, Department.class, "departmentId", "departmentId");
        Assert.assertEquals("The result list does not match the expected list", expected, result);
        for (String line : result) {
            log.info(line);
        }
    }

    @Test
    public void rightHashJoinTest() {
        List<String> expected = Lists.newArrayList(
                "1, Alice, 101, 101, Human Resources",
                "4, David, 101, 101, Human Resources",
                "2, Bob, 102, 102, Marketing",
                "4, David Two, 102, 102, Marketing",
                "3, Charlie, 103, 103, Engineering",
                "5, Eve, 104, 104, Customer Support",
                "null, null, null, 106, Research",
                "null, null, null, 106, Research Copy"
        );
        HashJoin<Employee, Department> join = new RightHashJoin<>();
        List<String> result =
                join.hashJoin(employees, departments, Employee.class, Department.class, "departmentId", "departmentId");
        for (String line : result) {
            log.info(line);
        }
        Assert.assertEquals("The result list does not match the expected list", expected, result);
    }

    @Test
    public void leftSemiHashJoinTest() {
        List<String> expected = Lists.newArrayList(
                "1, Alice, 101",
                "2, Bob, 102",
                "3, Charlie, 103",
                "4, David, 101",
                "4, David Two, 102",
                "5, Eve, 104"

        );
        HashJoin<Employee, Department> join = new LeftSemiHashJoin<>();
        List<String> result =
                join.hashJoin(employees, departments, Employee.class, Department.class, "departmentId", "departmentId");
        for (String line : result) {
            log.info(line);
        }
        Assert.assertEquals("The result list does not match the expected list", expected, result);
    }

    @Test
    public void rightSemiHashJoinTest() {
        List<String> expected = Lists.newArrayList(
                "101, Human Resources",
                "102, Marketing",
                "103, Engineering",
                "104, Customer Support"
        );
        HashJoin<Employee, Department> join = new RightSemiHashJoin<>();
        List<String> result =
                join.hashJoin(employees, departments, Employee.class, Department.class, "departmentId", "departmentId");
        for (String line : result) {
            log.info(line);
        }
        Assert.assertEquals("The result list does not match the expected list", expected, result);
    }

    @Test
    public void rightAntiHashJoinTest() {
        List<String> expected = Lists.newArrayList(
                "106, Research",
                "106, Research Copy"
        );
        HashJoin<Employee, Department> join = new RightAntiHashJoin<>();
        List<String> result =
                join.hashJoin(employees, departments, Employee.class, Department.class, "departmentId", "departmentId");
        for (String line : result) {
            log.info(line);
        }
        Assert.assertEquals("The result list does not match the expected list", expected, result);
    }

    @Test
    public void fullHashJoinTest() {
        List<String> expected = Lists.newArrayList(
                "1, Alice, 101, 101, Human Resources",
                "2, Bob, 102, 102, Marketing",
                "3, Charlie, 103, 103, Engineering",
                "4, David, 101, 101, Human Resources",
                "4, David Two, 102, 102, Marketing",
                "5, Eve, 104, 104, Customer Support",
                "6, Frank, 105, null, null",
                "7, Grace, null, null, null",
                "null, null, null, 106, Research",
                "null, null, null, 106, Research Copy"
        );
        FullHashJoin<Employee, Department> hashJoin = new FullHashJoin<>();
        List<String> result =
                hashJoin.hashJoin(employees, departments, Employee.class, Department.class, "departmentId",
                        "departmentId");
        for (String line : result) {
            log.info(line);
        }
        Assert.assertEquals("The result list does not match the expected list", expected, result);
    }

    @Test
    public void leftAntiHashJoinTest() {
        List<String> expected = Lists.newArrayList(
"6, Frank, 105",
                "7, Grace, null"
        );
        LeftAntiHashJoin<Employee, Department> join = new LeftAntiHashJoin<>();
        List<String> result =
                join.hashJoin(employees, departments, Employee.class, Department.class, "departmentId", "departmentId");
        for (String line : result) {
            log.info(line);
        }
        Assert.assertEquals("The result list does not match the expected list", expected, result);
    }

}
