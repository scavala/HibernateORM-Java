/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.dao.sql;

import hr.algebra.dao.Repository;
import hr.algebra.model.Klub;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author zakesekresa
 */
public class HibernateKlubRepository implements Repository<Klub> {

    @Override
    public int add(Klub data) throws Exception {
        try (EntityManagerWrapper entityManager = HibernateFactory.getEntityManager()) {
            EntityManager em = entityManager.get();
            em.getTransaction().begin();
            Klub klub = new Klub(data);
            em.persist(klub);
            em.getTransaction().commit();
            return klub.getIDKlub();
        }
    }

    @Override
    public void delete(Klub klub) throws Exception {
        try (EntityManagerWrapper entityManager = HibernateFactory.getEntityManager()) {
            EntityManager em = entityManager.get();
            em.getTransaction().begin();
            em.remove(em.contains(klub) ? klub : em.merge(klub));
            em.getTransaction().commit();
        }
    }

    @Override
    public List<Klub> getAll() throws Exception {
        try (EntityManagerWrapper entityManager = HibernateFactory.getEntityManager()) {
            return entityManager.get().createNamedQuery(HibernateFactory.SELECT_KLUBS).getResultList();
        }
    }

    @Override
    public Klub get(int idItem) throws Exception {
        try (EntityManagerWrapper entityManager = HibernateFactory.getEntityManager()) {
            EntityManager em = entityManager.get();
            return em.find(Klub.class, idItem);
        }
    }

    @Override
    public void update(Klub item) throws Exception {
        try (EntityManagerWrapper entityManager = HibernateFactory.getEntityManager()) {
            EntityManager em = entityManager.get();
            em.getTransaction().begin();
            em.find(Klub.class, item.getIDKlub()).updateDeatils(item);
            em.getTransaction().commit();
        }
    }

    @Override
    public void release() throws Exception {
        HibernateFactory.release();
    }

}
