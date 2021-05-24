/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.tss.pw.secondi.posts;

import it.tss.pw.secondi.documents.DocumentStore;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author 588se
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class PostStore {

    @PersistenceContext(name = "pw")
    private EntityManager em;

    @Inject
    DocumentStore documentStore;

    public Post find(Long id) {
        System.out.println("----------------------------------------------uso find  PostStore----------------------------------------------");
        Map hints = new HashMap<>();
        hints.put("javax.persistence.fetchgraph", em.createEntityGraph(Post.GRAPH_WITH_DOCUMENTS));
        return em.find(Post.class, id, hints);
    }

    public Post create(Post p) {
        System.out.println("----------------------------------------------uso create  PostStore----------------------------------------------");
        return em.merge(p);
    }

    public Post update(Post p) {
        System.out.println("----------------------------------------------uso update  PostStore----------------------------------------------");
        return em.merge(p);
    }

    public void delete(Long id) {
        System.out.println("----------------------------------------------uso delete  PostStore----------------------------------------------");
        Post found = find(id);
        found.getDocuments().forEach(documentStore::remove);
        em.remove(em.find(Post.class, id));
    }

    public List<Post> findByUsr(Long userId) {
        System.out.println("----------------------------------------------uso findByUsr  PostStore----------------------------------------------");
        EntityGraph entityGraph = em.getEntityGraph(Post.GRAPH_WITH_DOCUMENTS);
        return em.createNamedQuery(Post.FIND_BY_USR, Post.class)
                .setParameter("user_id", userId)
                .setHint("javax.persistence.fetchgraph", entityGraph)
                .getResultList();
    }

    public Optional<Post> findByIdAndUsr(Long id, Long userId) {
        System.out.println("----------------------------------------------uso findByIdAndUsr  PostStore----------------------------------------------");
        try {
            EntityGraph entityGraph = em.getEntityGraph(Post.GRAPH_WITH_DOCUMENTS);
            Post result = em.createNamedQuery(Post.FIND_BY_ID_AND_USR, Post.class)
                    .setParameter("id", id)
                    .setParameter("user_id", userId)
                    .setHint("javax.persistence.fetchgraph", entityGraph)
                    .getSingleResult();
            return Optional.of(result);
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }

    public List<Post> search(Long id, String search) {
        System.out.println("----------------------------------------------uso search  PostStore----------------------------------------------");
        EntityGraph entityGraph = em.getEntityGraph(Post.GRAPH_WITH_DOCUMENTS);
        return em.createNamedQuery(Post.SEARCH)
                .setParameter("user_id", id)
                .setParameter("search", "%" + search + "%")
                .setHint("javax.persistence.fetchgraph", entityGraph)
                .getResultList();
    }
}
