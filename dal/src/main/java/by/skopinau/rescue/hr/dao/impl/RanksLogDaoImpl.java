package by.skopinau.rescue.hr.dao.impl;

import by.skopinau.rescue.hr.model.Employee;
import by.skopinau.rescue.hr.model.RanksLog;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class RanksLogDaoImpl extends BaseDaoImpl<RanksLog> {
    public RanksLogDaoImpl() {
        super(RanksLog.class);
    }

    public List<RanksLog> findByEmployee(Employee employee) {
        Session session = getSessionFactory().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<RanksLog> criteria = cb.createQuery(RanksLog.class);
        Root<RanksLog> ranksLog = criteria.from(RanksLog.class);
        criteria.select(ranksLog)
                .where(cb.equal(ranksLog.get("employee").get("id"), employee.getId()))
                .orderBy(cb.desc(ranksLog.get("rankGettingDate")));
        if (session.createQuery(criteria).getResultList().isEmpty()) {
            throw new NullPointerException("Объекты не существуют");
        } else return session.createQuery(criteria).getResultList();
    }

    @Override
    public List<RanksLog> findAll() {
        Session session = getSessionFactory().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<RanksLog> criteria = cb.createQuery(RanksLog.class);
        Root<RanksLog> ranksLog = criteria.from(RanksLog.class);
        criteria.select(ranksLog)
                .orderBy(
                        cb.desc(ranksLog.get("rankGettingDate")),
                        cb.asc(ranksLog.get("employee").get("surname")),
                        cb.asc(ranksLog.get("employee").get("name")),
                        cb.asc(ranksLog.get("employee").get("patronymic"))
                );
        if (session.createQuery(criteria).getResultList().isEmpty()) {
            throw new NullPointerException("Объекты не существуют");
        } else return session.createQuery(criteria).getResultList();
    }
}