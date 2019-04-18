package com.company.enroller.persistence;

import java.util.Collection;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;

@Component("meetingService")
public class MeetingService {

	DatabaseConnector connector;

	public MeetingService() {
		connector = DatabaseConnector.getInstance();
	}

	public Collection<Meeting> getAll() {
		return connector.getSession().createCriteria(Meeting.class).list();
	}

	public Meeting findById(long id) {
		return (Meeting) connector.getSession().get(Meeting.class, id);
	}

	public Meeting add(Meeting meeting) {
		Transaction transaction  = connector.getSession().beginTransaction();
		connector.getSession().save(meeting);
		transaction.commit();
		return meeting;
	}

	public Collection<Participant> getAllParticipants(long id) {
		return ((Meeting) connector.getSession().get(Meeting.class, id)).getParticipants();
	}
	
	public boolean isParticipantEgsist(String login) {
		return !(connector.getSession().get(Participant.class, login) != null);
	}

	public void addParticipantToMeeting(Meeting meeting) {
		Transaction transaction  = connector.getSession().beginTransaction();
		connector.getSession().save(meeting);
		transaction.commit();
	}

	public void deleteMeetind(Meeting meeting) {
		Transaction transaction  = connector.getSession().beginTransaction();
		connector.getSession().delete(meeting);
		transaction.commit();
	}

	public Participant findByLoginInMeeting(long id, String login) {
		Collection<Participant> participants = ((Meeting) connector.getSession().get(Meeting.class, id)).getParticipants();
		for(Participant participant : participants) {
			if(participant.getLogin().equals(login)) {
				return participant;
			}
		}
		return null;
	}

	public void deleteParticipantFromMeeting(Meeting meeting) {
		Transaction transaction  = connector.getSession().beginTransaction();
		connector.getSession().save(meeting);
		transaction.commit();
	}

	public void update(Meeting meeting) {
		Transaction transaction  = connector.getSession().beginTransaction();
		connector.getSession().merge(meeting);
		transaction.commit();
	}

}
//        Sortowanie listy spotkań po tytule spotkania
//        Przeszukiwanie listy spotkań po tytule i opisie (na zasadzie substring)
//        Przeszukiwanie listy spotkań po zapisanym uczestniku spotkania
