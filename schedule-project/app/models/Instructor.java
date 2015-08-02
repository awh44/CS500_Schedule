package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Instructors")
public class Instructor extends Model
{
	private String name_;

	public Instructor(String name)
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
