package by.skopinau.rescue.hr.dao.impl;

import by.skopinau.rescue.hr.dao.BaseDao;
import by.skopinau.rescue.hr.model.Employee;
import by.skopinau.rescue.hr.model.Position;
import by.skopinau.rescue.hr.model.Rank;
import by.skopinau.rescue.hr.model.Subdivision;
import by.skopinau.rescue.hr.util.SessionUtil;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import org.hibernate.Session;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BaseDaoImplTests {
    private static BaseDao<Employee> baseDaoWithEmployee;
    private static BaseDao<Rank> baseDaoWithRank;

    @BeforeAll
    static void initTestComponent() {
        baseDaoWithEmployee = new EmployeeDaoImpl();
        baseDaoWithRank = new RankDaoImpl();
    }

    @BeforeEach
    void clearDB() {
        Session session = SessionUtil.openSession();
        session.getTransaction().begin();
        CriteriaBuilder cb = session.getCriteriaBuilder();

        CriteriaDelete<Employee> employeeCriteriaDelete = cb.createCriteriaDelete(Employee.class);
        CriteriaDelete<Rank> rankCriteriaDelete = cb.createCriteriaDelete(Rank.class);
        CriteriaDelete<Position> positionCriteriaDelete = cb.createCriteriaDelete(Position.class);
        CriteriaDelete<Subdivision> subdivisionCriteriaDelete = cb.createCriteriaDelete(Subdivision.class);

        employeeCriteriaDelete.from(Employee.class);
        rankCriteriaDelete.from(Rank.class);
        positionCriteriaDelete.from(Position.class);
        subdivisionCriteriaDelete.from(Subdivision.class);

        session.createQuery(employeeCriteriaDelete).executeUpdate();
        session.createQuery(rankCriteriaDelete).executeUpdate();
        session.createQuery(positionCriteriaDelete).executeUpdate();
        session.createQuery(subdivisionCriteriaDelete).executeUpdate();

        session.getTransaction().commit();
        session.close();
    }

    @Test
    void saveTest() {
        // GIVEN
        Employee expected = new Employee("Скопинов", "Дмитрий", "Николаевич",
                LocalDate.of(1993, 3, 17),
                new Rank("капитан"),
                new Position("инспектор"),
                new Subdivision("ИНиП"));

        // WHEN
        baseDaoWithEmployee.save(expected);

        // THEN
        Employee actual = baseDaoWithEmployee.findById(expected.getId());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void updateTest() {
        // GIVEN
        Employee expected = new Employee("Скопинов", "Дмитрий", "Николаевич",
                LocalDate.of(1993, 3, 17),
                new Rank("капитан"),
                new Position("инспектор"),
                new Subdivision("ИНиП"));
        baseDaoWithEmployee.save(expected);

        // WHEN
        expected.setSurname("Иванов");
        baseDaoWithEmployee.update(expected);

        // THEN
        Employee actual = baseDaoWithEmployee.findById(expected.getId());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void deleteTest() {
        // GIVEN
        Employee employee1 = new Employee("Скопинов", "Дмитрий", "Николаевич",
                LocalDate.of(1993, 3, 17),
                new Rank("капитан"),
                new Position("инспектор"),
                new Subdivision("ИНиП"));
        Employee employee2 = new Employee("Иванов", "Дмитрий", "Николаевич",
                LocalDate.of(1993, 3, 17),
                new Rank("рядовой"),
                new Position("пожарный"),
                new Subdivision("ПАСЧ-1"));
        baseDaoWithEmployee.save(employee1);
        baseDaoWithEmployee.save(employee2);

        // WHEN
        baseDaoWithEmployee.delete(employee1);

        // THEN
        List<Employee> actual = baseDaoWithEmployee.findAll();
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual.size(), 1);
    }

    @Test
    void findByIdTest() {
        // GIVEN
        Employee expected = new Employee("Скопинов", "Дмитрий", "Николаевич",
                LocalDate.of(1993, 3, 17),
                new Rank("капитан"),
                new Position("инспектор"),
                new Subdivision("ИНиП"));
        baseDaoWithEmployee.save(expected);

        // WHEN
        Employee actual = baseDaoWithEmployee.findById(expected.getId());

        // THEN
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected.getId(), actual.getId());
    }

    @Test
    void findAllEmployeesTest() {
        // GIVEN
        Employee employee1 = new Employee("Скопинов", "Алексей", "Алексеевич",
                LocalDate.of(1993, 3, 17),
                new Rank("капитан"),
                new Position("инспектор"),
                new Subdivision("ИНиП"));
        Employee employee2 = new Employee("Иванов", "Дмитрий", "Николаевич",
                LocalDate.of(1993, 3, 17),
                new Rank("рядовой"),
                new Position("пожарный"),
                new Subdivision("ПАСЧ-1"));
        List<Employee> expected = Stream.of(employee1, employee2)
                .sorted(Comparator.comparing(Employee::getSurname)
                        .thenComparing(Employee::getName)
                        .thenComparing(Employee::getPatronymic))
                .collect(Collectors.toList());

        baseDaoWithEmployee.save(employee1);
        baseDaoWithEmployee.save(employee2);

        // WHEN
        List<Employee> actual = baseDaoWithEmployee.findAll();

        // THEN
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected.size(), actual.size());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void findAllRanksTest() {
        // GIVEN
        Rank rank1 = new Rank("сержант");
        Rank rank2 = new Rank("рядовой");
        List<Rank> expected = List.of(rank1, rank2);
        baseDaoWithRank.save(rank1);
        baseDaoWithRank.save(rank2);

        // WHEN
        List<Rank> actual = baseDaoWithRank.findAll();

        // THEN
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected.size(), actual.size());
        Assertions.assertEquals(expected, actual);
    }
}