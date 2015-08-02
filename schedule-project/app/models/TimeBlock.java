package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import java.sql.Time;

@Entity
@Table(name = "TimeBlocks")
public class TimeBlock extends Model
{
	private String day_;
	private Time start_time_;
	private Time end_time_;

	public TimeBlock(String day, Time start_time, Time end_time)
	{
		setDay(day);
		setStartTime(start_time);
		setEndTime(end_time);
	}

	public void setDay(String day)
	{
		day_ = day;
	}

	public String getDay()
	{
		return day_;
	}

	public void setStartTime(Time start_time)
	{
		start_time_ = start_time;
	}

	public Time getStartTime()
	{
		return start_time_;
	}

	public void setEndTime(Time end_time)
	{
		end_time_ = end_time;
	}

	public Time getEndTime()
	{
		return end_time_;
	}
}
