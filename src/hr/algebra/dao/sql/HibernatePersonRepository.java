/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.dao.sql;

import hr.algebra.dao.Repository;
import hr.algebra.model.Person;
import java.util.List;
import javax.persistence.EntityManager;

public class HibernatePersonRepository implements Repository<Person> {

    @Override
    public int add(Person data) throws Exception {
        try (EntityManagerWrapper entityManager = HibernateFactory.getEntityManager()) {
            EntityManager em = entityManager.get();
            em.getTransaction().begin();
            Person person = new Person(data);
            em.persist(person);
            em.getTransaction().commit();
            return person.getIDPerson();
        }
    }

    @Override
    public void delete(Person person) throws Exception {
        try (EntityManagerWrapper entityManager = HibernateFactory.getEntityManager()) {
            EntityManager em = entityManager.get();
            em.getTransaction().begin();
            // person comes from earlier transaction, so we must merge it into this one
            em.remove(em.contains(person) ? person : em.merge(person));
            em.getTransaction().commit();
        }
    }

    @Override
    public List<Person> getAll() throws Exception {
        try (EntityManagerWrapper entityManager = HibernateFactory.getEntityManager()) {
            return entityManager.get().createNamedQuery(HibernateFactory.SELECT_PEOPLE).getResultList();
        }
    }

    @Override
    public Person get(int idPerson) throws Exception {
        try (EntityManagerWrapper entityManager = HibernateFactory.getEntityManager()) {
            EntityManager em = entityManager.get();
            return em.find(Person.class, idPerson);
        }
    }

    @Override
    public void update(Person person) throws Exception {
        try (EntityManagerWrapper entityManager = HibernateFactory.getEntityManager()) {
            EntityManager em = entityManager.get();
            em.getTransaction().begin();
            em.find(Person.class, person.getIDPerson()).updateDeatils(person);
            em.getTransaction().commit();
        }
    }

    @Override
    public void release() throws Exception {
        HibernateFactory.release();
    }

}
