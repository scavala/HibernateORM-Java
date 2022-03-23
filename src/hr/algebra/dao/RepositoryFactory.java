/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.dao;

import hr.algebra.dao.sql.HibernateKlubRepository;
import hr.algebra.dao.sql.HibernatePersonRepository;
import hr.algebra.model.Klub;
import hr.algebra.model.Person;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author zakesekresa
 */
public class RepositoryFactory {

    private RepositoryFactory() {
    }

    private static final Map<String, Repository> map = new HashMap<>();

    static {
        map.put(Person.class.getSimpleName(), new HibernatePersonRepository());
        map.put(Klub.class.getSimpleName(), new HibernateKlubRepository());
    }

    public static Repository getRepository(Class type) throws Exception {
        return map.get(type.getSimpleName());
    }

}
