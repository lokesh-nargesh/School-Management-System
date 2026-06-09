package com.school.management.repository;

import com.school.management.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findBySenderIdAndReceiverIdOrSenderIdAndReceiverIdOrderByTimestampAsc(
        Long senderId1, Long receiverId1, Long senderId2, Long receiverId2
    );
}
