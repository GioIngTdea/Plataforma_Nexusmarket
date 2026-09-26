package application.domain.ports.in;

import application.domain.valueobjects.UserStatus;

public interface ChangeUserStatusPort {
    void execute(String userId, UserStatus newStatus, String adminId);
}
