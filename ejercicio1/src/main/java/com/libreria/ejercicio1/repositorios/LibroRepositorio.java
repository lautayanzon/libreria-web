package com.libreria.ejercicio1.repositorios;

import com.libreria.ejercicio1.entidades.Libro;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author Lautaro Yanzon
 */
public interface LibroRepositorio extends JpaRepository<Libro, String> {

    //Metodo guardar,borrar,contar hereda de JpaRepository
    //long      count() Returns the number of entities available.
    //void      delete(T entity) Deletes a given entity.
    //void      deleteAll() Deletes all entities managed by the repository.
    //void      deleteAll(Iterable<? extends T> entities)Deletes the given entities.
    //void      deleteAllById(Iterable<? extends ID> ids) Deletes all instances of the type T with the given IDs.
    //void      deleteById(ID id) Deletes the entity with the given id.
    //boolean   existsById(ID id) Returns whether an entity with the given id exists.
    //Iterable<T>	findAll() Returns all instances of the type.
    //Iterable<T>	findAllById(Iterable<ID> ids) Returns all instances of the type T with the given IDs.
    //Optional<T>	findById(ID id) Retrieves an entity by its id.
    //<S extends T>	save(S entity) Saves a given entity.
    //Iterable<S>	saveAll(Iterable<S> entities)
    
    @Query("SELECT l FROM Libro l WHERE l.id = :id")
    public Libro buscarPorId(@Param("id") String id);
    
    @Query("SELECT l FROM Libro l WHERE l.titulo = :titulo ")
    public Libro buscarPorTitulo(@Param("titulo") String titulo);
    
    @Query("SELECT l FROM Libro l WHERE l.autor.nombre = :nombre")
    public Libro buscarPorAutor(@Param("nombre") String nombre);
    
    @Query("SELECT l FROM Libro l WHERE l.editorial.nombre = :nombre")
    public Libro buscarPorEditorial(@Param("nombre") String nombre);
    
    @Query("SELECT l FROM Libro l WHERE l.isbn = :isbn")
    public Libro buscarPorISBN(@Param("isbn") Long isbn);
    
    @Query("SELECT l FROM Libro l WHERE l.alta = 1 ORDER BY l.titulo")
    public List<Libro> buscarActivos();
    
    @Query("SELECT l FROM Libro l WHERE l.autor is null")
    public List<Libro> buscarAutoresActivos();
    
    @Query("SELECT l FROM Libro l WHERE l.editorial is null")
    public List<Libro> buscarEditorialesActivas();
    
    @Query("SELECT l FROM Libro l")
    public List<Libro> buscarTodos();
    
    @Query("SELECT l FROM Libro l WHERE l.autor.alta = 1")
    public List<Libro> buscarAutores();
    
    @Query("SELECT l FROM Libro l WHERE l.editorial.alta = 1")
    public List<Libro> buscarEditoriales();
    

}
