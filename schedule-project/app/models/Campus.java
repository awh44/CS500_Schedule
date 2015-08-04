package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Campuses")
public class Campus extends Model
{
	private String name_;

	public Campus(String name)
	{
		setName(name);
	}

	public void setName(String name)
	{
		name_ = name;
	}

	public String getName()
	{
		return name_;
	}
}
