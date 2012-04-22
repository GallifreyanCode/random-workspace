package be.gallifreyan.javaee.view.album;

import javax.validation.constraints.*;

public class EditAlbumRequest
{
	@NotNull(message = "{Album.name.notNull}")
	@Size(min = 1, max = 50, message = "{Album.name.size}")
	private String name;

	@Size(min = 0, max = 255, message = "{Album.description.size}")
	private String description;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

}
