package by.skopinau.rescue.hr.dao.impl;

import by.skopinau.rescue.hr.config.Config;
import by.skopinau.rescue.hr.model.Employee;
import by.skopinau.rescue.hr.model.Position;
import by.skopinau.rescue.hr.model.Rank;
import by.skopinau.rescue.hr.model.Subdivision;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@ExtendWith(SpringExtension.class)
@Transactional
@ContextConfiguration(classes = Config.class)
public class EmployeeDaoTests {
    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private EmployeeDaoImpl employeeDao;

    @Autowired
    private RankDaoImpl rankDao;

    @Autowired
    private PositionDaoImpl positionDao;

    @Autowired
    private SubdivisionDaoImpl subdivisionDao;

    private List<Employee> expected;

    @BeforeEach
    void clearDB() {
        Session session = sessionFactory.getCurrentSession();
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
    }

    @BeforeEach
    void initTestComponent() {
        Employee employee1 = new Employee("Скопинов", "Дмитрий", "Николаевич",
                LocalDate.of(1993, 3, 17),
                new Rank("капитан"),
                new Position("инспектор"),
                new Subdivision("ИНиП"));
        Employee employee2 = new Employee("Иванов", "Иван", "Николаевич",
                LocalDate.of(1995, 3, 17),
                new Rank("рядовой"),
                new Position("пожарный"),
                new Subdivision("ПАСЧ-1"));
        Employee employee3 = new Employee("Петров", "Иван", "Васильевич",
                LocalDate.of(1993, 8, 11),
                new Rank("сержант"),
                new Position("водитель"),
                new Subdivision("ПАСЧ-3"));
        expected = List.of(employee1, employee2, employee3);
        employeeDao.save(employee1);
        employeeDao.save(employee2);
        employeeDao.save(employee3);
    }

    @Test
    void findBySurnameTest() {
        // GIVEN
        expected = expected.stream()
                .filter(employee -> employee.getSurname().equals("Скопинов"))
                .sorted(Comparator.comparing(Employee::getSurname)
                        .thenComparing(Employee::getName)
                        .thenComparing(Employee::getPatronymic))
                .collect(Collectors.toList());

        // WHEN
        List<Employee> actual = employeeDao.findBySurname("Скопинов");

        // THEN
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected.size(), actual.size());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void findByNameTest() {
        // GIVEN
        expected = expected.stream()
                .filter(employee -> employee.getName().equals("Иван"))
                .sorted(Comparator.comparing(Employee::getSurname)
                        .thenComparing(Employee::getName)
                        .thenComparing(Employee::getPatronymic))
                .collect(Collectors.toList());

        // WHEN
        List<Employee> actual = employeeDao.findByName("Иван");

        // THEN
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected.size(), actual.size());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void findByPatronymicTest() {
        // GIVEN
        expected = expected.stream()
                .filter(employee -> employee.getPatronymic().equals("Николаевич"))
                .sorted(Comparator.comparing(Employee::getSurname)
                        .thenComparing(Employee::getName)
                        .thenComparing(Employee::getPatronymic))
                .collect(Collectors.toList());

        // WHEN
        List<Employee> actual = employeeDao.findByPatronymic("Николаевич");

        // THEN
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected.size(), actual.size());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void findByBirthdayTest() {
        // GIVEN
        expected = expected.stream()
                .filter(employee -> employee.getBirthday().equals(LocalDate.of(1993, 3, 17)))
                .sorted(Comparator.comparing(Employee::getSurname)
                        .thenComparing(Employee::getName)
                        .thenComparing(Employee::getPatronymic))
                .collect(Collectors.toList());

        // WHEN
        List<Employee> actual = employeeDao.findByBirthday(LocalDate.of(1993, 3, 17));

        // THEN
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected.size(), actual.size());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void findByRankTest() {
        // GIVEN
        expected = expected.stream()
                .filter(employee -> employee.getRank().equals(rankDao.findByTitle("капитан")))
                .sorted(Comparator.comparing(Employee::getSurname)
                        .thenComparing(Employee::getName)
                        .thenComparing(Employee::getPatronymic))
                .collect(Collectors.toList());

        // WHEN
        List<Employee> actual = employeeDao.findByRank("капитан");

        // THEN
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected.size(), actual.size());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void findByPositionTest() {
        // GIVEN
        expected = expected.stream()
                .filter(employee -> employee.getPosition().equals(positionDao.findByTitle("пожарный")))
                .sorted(Comparator.comparing(Employee::getSurname)
                        .thenComparing(Employee::getName)
                        .thenComparing(Employee::getPatronymic))
                .collect(Collectors.toList());

        // WHEN
        List<Employee> actual = employeeDao.findByPosition("пожарный");

        // THEN
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected.size(), actual.size());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void findBySubdivisionTest() {
        // GIVEN
        expected = expected.stream()
                .filter(employee -> employee.getSubdivision().equals(subdivisionDao.findByTitle("ПАСЧ-3")))
                .sorted(Comparator.comparing(Employee::getSurname)
                        .thenComparing(Employee::getName)
                        .thenComparing(Employee::getPatronymic))
                .collect(Collectors.toList());

        // WHEN
        List<Employee> actual = employeeDao.findBySubdivision("ПАСЧ-3");

        // THEN
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected.size(), actual.size());
        Assertions.assertEquals(expected, actual);
    }
}