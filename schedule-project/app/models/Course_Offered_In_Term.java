package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Course_Offered_In_Term")
public class Course_Offered_In_Term extends Model
{
	private String subject_;
	private String num_;
	private String season_;
	private String term_type_;
	private int year_;

	public Course_Offered_In_Term(String subject, String num, String season, String term_type, int year)
	{
		setSubject(subject);
		setNum(num);
		setSeason(season);
		setType(term_type);
		setYear(year);
	}

	public void setSubject(String subject)
	{
		subject_ = subject;
	}

	public String getSubject()
	{
		return subject_;
	}

	public void setNum(String num)
	{
		num_ = num;
	}

	public String getNum()
	{
		return num_;
	}

	public void setSeason(String season)
	{
		season_ = season;
	}

	public String getSeason()
	{
		return season_;
	}

	public void setType(String term_type)
	{
		term_type_ = term_type;
	}

	public String getType()
	{
		return term_type_;
	}

	public void setYear(int year)
	{
		year_ = year;
	}
	
	public int getYear()
	{
		return year_;
	}
/*
	public int compareTo(Course_Offered_In_Term other)
	{
		if (year_ < other.year_)
			return -1;

		if (year_ > other.year_)
			return 1;

		if (season_.equals(other.season_))
		{
			if (term_type_.equals(other.term_type_))
			{
				return 0;
			}
			else
			{
				//quarters will come before semesters
				return term_type_.equals("Quarter") ? -1 : 1;
			}
					
		}

		//same year and seasons aren't the same - fall always comes last,
		//so know that the this object must come after the current
		if (season_.equals("Fall"))
		{
			switch (other.season_)
			{
				case "Winter":
				case "Spring":
				case "Summer":
					return 1;
					break;
				default:
					break;
			}
		}

		if (season_.equals("Winter"))
		{
			switch (other.season_)
			{
				case "Spring":
				case "Summer":
				case "Fall":
					return -1;
				default:
					break;
			}
		}

		if (season_.equals("Spring"))
		{
			switch (other.season_)
			{
				case "Winter":
					return 1;
				case "Summer":
				case "Fall":
					return -1;
				default:
					break;
			}
		}

		//season must equal summer at this point,
		//unless given bad data
		//if (season_.equals("Summer"))
		{
			switch (other.season_)
			{
				case "Winter":
				case "Spring":
					return 1;
				case "Fall":
					return -1;
				default:
					break;
			}
		}

	}
*/

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + subject_.hashCode();
		result = prime * result + num_.hashCode();
		result = prime * result + season_.hashCode();
		result = prime * result + term_type_.hashCode();
		result = prime * result + year_;

		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Course_Offered_In_Term other = (Course_Offered_In_Term) obj;
		return subject_ == other.subject_ &&
			num_ == other.num_ &&
			season_ == other.season_ &&
			term_type_ == other.term_type_ &&
			year_ == other.year_;
	}
}
