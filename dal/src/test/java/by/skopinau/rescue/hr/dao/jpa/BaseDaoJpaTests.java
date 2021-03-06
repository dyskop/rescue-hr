package by.skopinau.rescue.hr.dao.jpa;

import by.skopinau.rescue.hr.config.OrmConfig;
import by.skopinau.rescue.hr.dao.BaseDao;
import by.skopinau.rescue.hr.entity.Employee;
import by.skopinau.rescue.hr.entity.Position;
import by.skopinau.rescue.hr.entity.Rank;
import by.skopinau.rescue.hr.entity.Subdivision;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ExtendWith(SpringExtension.class)
@Transactional
@ContextConfiguration(classes = OrmConfig.class)
public class BaseDaoJpaTests {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private BaseDao<Employee> baseDaoWithEmployee;

    @Autowired
    private BaseDao<Rank> baseDaoWithRank;

    @BeforeEach
    void clearDB() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaDelete<Employee> employeeCriteriaDelete = cb.createCriteriaDelete(Employee.class);
        CriteriaDelete<Rank> rankCriteriaDelete = cb.createCriteriaDelete(Rank.class);
        CriteriaDelete<Position> positionCriteriaDelete = cb.createCriteriaDelete(Position.class);
        CriteriaDelete<Subdivision> subdivisionCriteriaDelete = cb.createCriteriaDelete(Subdivision.class);

        employeeCriteriaDelete.from(Employee.class);
        rankCriteriaDelete.from(Rank.class);
        positionCriteriaDelete.from(Position.class);
        subdivisionCriteriaDelete.from(Subdivision.class);

        entityManager.createQuery(employeeCriteriaDelete).executeUpdate();
        entityManager.createQuery(rankCriteriaDelete).executeUpdate();
        entityManager.createQuery(positionCriteriaDelete).executeUpdate();
        entityManager.createQuery(subdivisionCriteriaDelete).executeUpdate();
    }

    @Test
    void saveTest() {
        // GIVEN
        Employee expected = new Employee("????????????????", "??????????????", "????????????????????",
                LocalDate.of(1993, 3, 17),
                new Rank("??????????????"),
                new Position("??????????????????"),
                new Subdivision("????????"));

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
        Employee expected = new Employee("????????????????", "??????????????", "????????????????????",
                LocalDate.of(1993, 3, 17),
                new Rank("??????????????"),
                new Position("??????????????????"),
                new Subdivision("????????"));
        baseDaoWithEmployee.save(expected);

        // WHEN
        expected.setSurname("????????????");
        baseDaoWithEmployee.update(expected);

        // THEN
        Employee actual = baseDaoWithEmployee.findById(expected.getId());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void deleteTest() {
        // GIVEN
        Employee employee1 = new Employee("????????????????", "??????????????", "????????????????????",
                LocalDate.of(1993, 3, 17),
                new Rank("??????????????"),
                new Position("??????????????????"),
                new Subdivision("????????"));
        Employee employee2 = new Employee("????????????", "??????????????", "????????????????????",
                LocalDate.of(1993, 3, 17),
                new Rank("??????????????"),
                new Position("????????????????"),
                new Subdivision("????????-1"));
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
        Employee expected = new Employee("????????????????", "??????????????", "????????????????????",
                LocalDate.of(1993, 3, 17),
                new Rank("??????????????"),
                new Position("??????????????????"),
                new Subdivision("????????"));
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
        Employee employee1 = new Employee("????????????????", "??????????????", "????????????????????",
                LocalDate.of(1993, 3, 17),
                new Rank("??????????????"),
                new Position("??????????????????"),
                new Subdivision("????????"));
        Employee employee2 = new Employee("????????????", "??????????????", "????????????????????",
                LocalDate.of(1993, 3, 17),
                new Rank("??????????????"),
                new Position("????????????????"),
                new Subdivision("????????-1"));
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
        Rank rank1 = new Rank("??????????????");
        Rank rank2 = new Rank("??????????????");
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