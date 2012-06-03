package be.gallifreyan.api;

/** <p>This is the <b>parent interface</b> for services,
 * all global characteristics and behavior are defined here.</p>
 * @author	GallifreyanCode
 */
public interface EntityService<T extends Entity> {
	T save(final T entity);
	T findByName(String name);
}
