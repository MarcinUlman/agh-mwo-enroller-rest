package com.company.enroller.persistence;

import java.util.Collection;

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

//	public Collection<Meeting> getAll() {
//		String hql = "FROM Meeting";
//		Query query = connector.getSession().createQuery(hql);
//		return query.list();
//	}
	
	public Collection<Meeting> getAll() {
		return connector.getSession().createCriteria(Meeting.class).list();
	}

	public Meeting findByLogin(long id) {
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

	public Meeting addParticipantToMeeting(Meeting meeting, Participant participant) {
		Transaction transaction  = connector.getSession().beginTransaction();
		meeting.addParticipant(participant);
		connector.getSession().save(meeting);
		transaction.commit();
		return null;
	}

}
//    Wersja GOLD (dodatkowo do BASIC)
//        Usuwanie spotkań
//        Aktualizację spotkań
//        Usuwanie uczestnika ze spotkania
//    Wersja PREMIUM (dodatkowo do GOLD)
//        Sortowanie listy spotkań po tytule spotkania
//        Przeszukiwanie listy spotkań po tytule i opisie (na zasadzie substring)
//        Przeszukiwanie listy spotkań po zapisanym uczestniku spotkania
