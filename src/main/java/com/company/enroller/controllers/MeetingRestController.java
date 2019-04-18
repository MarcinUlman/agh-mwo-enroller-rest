package com.company.enroller.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;
import com.company.enroller.persistence.MeetingService;

@RestController
@RequestMapping("/meetings")
public class MeetingRestController {

	@Autowired
	MeetingService meetingService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<?> getMeetings() {
		Collection<Meeting> meetings = meetingService.getAll();
		return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getMeeting(@PathVariable("id") long id) {
		Meeting meeting = meetingService.findById(id);
		if (meeting == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<?> createMeeting(@RequestBody Meeting meetring) {
		Meeting foundMeeting = meetingService.findById(meetring.getId());
		if (foundMeeting != null) {
			return new ResponseEntity<>("Unable to create. A meeting with Id " + meetring.getId() + " already exist.",
					HttpStatus.CONFLICT);
		}

		meetingService.add(meetring);
		return new ResponseEntity<Meeting>(meetring, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}/participants", method = RequestMethod.GET)
	public ResponseEntity<?> getParticipants(@PathVariable("id") long id) {
		Meeting meeting = meetingService.findById(id);
		if (meeting == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		Collection<Participant> participants = meetingService.getAllParticipants(id);
		return new ResponseEntity<Collection<Participant>>(participants, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}/participants", method = RequestMethod.POST)
	public ResponseEntity<?> addParticipantToMeeting(@PathVariable("id") long id,
			@RequestBody Participant participant) {
		Meeting meeting = meetingService.findById(id);
		if (meeting == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		if (meetingService.isParticipantEgsist(participant.getLogin())) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		if(meetingService.findByLoginInMeeting(id, participant.getLogin()) != null) {
			return new ResponseEntity(
					"Unable to add. A participant with login " + participant.getLogin() + " already added in to the Meeting.",
					HttpStatus.CONFLICT);
		}
		
		meeting.addParticipant(participant);
		meetingService.addParticipantToMeeting(meeting);

		return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteMeeting(@PathVariable("id") long id) {
		Meeting meeting = meetingService.findById(id);
		if (meeting == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		meetingService.deleteMeetind(meeting);

		return new ResponseEntity<Meeting>(meeting, HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updatenMeeting(@PathVariable("id") long id, @RequestBody Meeting incommingMeeting) {
		Meeting meeting = meetingService.findById(id);
		if (meeting == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		meeting.setTitle(incommingMeeting.getTitle());
		meeting.setDescription(incommingMeeting.getDescription());
		meeting.setDate(incommingMeeting.getDate());
				
		meetingService.update(meeting);
		
		return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}/participants/{participantId}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteParticipantFromMeeting(@PathVariable("participantId") String login,
			@PathVariable("id") long id) {
		Meeting meeting = meetingService.findById(id);
		if (meeting == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		Participant participant = meetingService.findByLoginInMeeting(id, login);
		if (participant == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		meeting.removeParticipant(participant);
		meetingService.deleteParticipantFromMeeting(meeting);
		return new ResponseEntity<Participant>(participant, HttpStatus.NO_CONTENT);
	}

}
