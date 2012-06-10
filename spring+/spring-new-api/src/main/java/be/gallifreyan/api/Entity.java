package be.gallifreyan.api;


/** <p>This is the <b>parent interface</b> for entities,
 * all global characteristics and behavior are defined here.</p>
 * Each entity should have
 * <ul> 
 * <li> Id
 * <li> Version
 * <li> Name
 * </ul>
 * @author	GallifreyanCode
 */
public interface Entity {
	Long getId();

	void setId(final Long id);

	Integer getVersion();

	void setVersion(Integer version);

	String getName();

	void setName(String name);
}
