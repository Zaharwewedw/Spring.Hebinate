package hiber.dao;

import hiber.model.User;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class UserDaoImp implements UserDao {

   private final SessionFactory sessionFactory;

   public UserDaoImp(SessionFactory sessionFactory) {
      this.sessionFactory = sessionFactory;
   }

   @Override
   public void add(User user) {
      sessionFactory.getCurrentSession().save(user);
   }

   @Override
   public List<User> listUsers() {
      TypedQuery<User> query = sessionFactory.getCurrentSession().createQuery("from User");
      return query.getResultList();
   }

   @Override
   public User findOwnerByCar(String car_model, int car_series) {
      EntityManager entityManager = sessionFactory.createEntityManager();
      try {
         TypedQuery<User> query = entityManager
                 .createQuery("SELECT u FROM User u JOIN u.car m WHERE m.model = :model AND m.series =: series", User.class)
                 .setParameter("model", car_model)
                 .setParameter("series", car_series);
         List<User> userList = query.getResultList();
         if (!userList.isEmpty()) {
            return userList.get(0);
         }
      } finally {
         entityManager.close();
      }
      return new User();
   }
}
