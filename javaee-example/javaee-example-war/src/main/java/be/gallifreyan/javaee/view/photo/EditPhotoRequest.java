package be.gallifreyan.javaee.view.photo;

import javax.validation.constraints.Size;

public class EditPhotoRequest
{

	@Size(min = 0, max = 255, message = "{Photo.title.size}")
	private String title;

	@Size(min = 0, max = 255, message = "{Photo.description.size}")
	private String description;

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
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
