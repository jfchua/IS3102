package application.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import application.entity.Event;
import application.entity.Feedback;

public interface FeedbackRepository  extends JpaRepository<Feedback, Long> {

}
