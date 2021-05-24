/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.tss.pw.secondi.users;

import it.tss.pw.secondi.security.Credential;
import it.tss.pw.secondi.security.SecurityEncoding;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author 588se
 */
@Stateless //stateless permette di creare una sola istanza x tutti 
@TransactionAttribute(TransactionAttributeType.REQUIRED) //permette di aprire e chiudere la richiesta in automatico quando ne ho bisogno senza dover ricordarsi di aprire e chiudere manualmente
public class UserStore {

    @PersistenceContext(name = "pw")
    EntityManager em;
    
    

    //metodo che viene "attivato" dopo la costruzione di userStore
    @PostConstruct
    public void init() {

    }

    public List<User> all() {
        System.out.println("---------------------------------------------uso all UserStore---------------------------------------------");
        return em.createNamedQuery(User.FIND_ALL)
                .getResultList();
    }

    public  Optional<User>  find(Long id) {
        System.out.println("---------------------------------------------uso find UserStore---------------------------------------------");
        User found = em.find(User.class, id);
        return found == null ? Optional.empty() : Optional.of(found);
    }

    public User create(User u) {
        System.out.println("---------------------- CREATE : " + u + " ----------------------------------");
        if (findByUsr(u.getUsr()).isPresent()) {
            throw new UserAlreadyExistException(u.getUsr());
        }
        //cripto la passowrd in sha-256 e la sostituisco a quella in chiaro
        u.setPwd(SecurityEncoding.shaHash(u.getPwd()));
        return em.merge(u);
    }

    public User update(User u) {
        System.out.println("---------------------- UPDATE : " + u + " ----------------------------------");
        return em.merge(u);
    }

    public void delete(Long id) {
        User u = em.find(User.class, id);
        System.out.println("---------------------- DELETE : " + u + " ----------------------------------");
        em.remove(u);
    }

    public Optional<User> findByUsr(String usr) {
        System.out.println("---------------------------------------------uso findByUsr UserStore---------------------------------------------");
        return em.createNamedQuery(User.FIND_BY_USR, User.class)
                .setParameter("usr", usr)
                .getResultStream()
                .findFirst();
    }

    public List<User> search(String search) {
        System.out.println("---------------------------------------------uso search 1 UserStore---------------------------------------------");
        return em.createNamedQuery(User.SEARCH)
                .setParameter("fname", "%" + search + "%")
                .setParameter("lname", "%" + search + "%")
                .setParameter("usr", "%" + search + "%")
                .getResultList();
    }

    public Optional<User> search(Credential credential) {
        System.out.println("---------------------------------------------uso search 2 UserStore---------------------------------------------");
        credential.setPwd(SecurityEncoding.shaHash(credential.getPwd()));
        try {
            User found = em.createNamedQuery(User.FIND_BY_USR_PWD, User.class)
                    .setParameter("usr", credential.getUsr())
                    .setParameter("pwd", credential.getPwd())
                    .getSingleResult();
            return Optional.of(found);
        } catch (Exception ex) {
            Logger.getLogger(UserStore.class.getName()).log(Level.SEVERE, null, ex);
            return Optional.empty();
        }
    }

}



























