package nl.tudelft.sem.template.domain.user;

import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final transient UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public AppUser getUserById(int userID){
        if(userRepository.findById(String.valueOf(userID)).isPresent()){
            return userRepository.findById(String.valueOf(userID)).get();
        }
        return null;
    }

    public AppUser getUserByEmail(Email email){
        if(userRepository.findByEmail(email).isPresent()){
            return userRepository.findByEmail(email).get();
        }
        return null;
    }

    public boolean userExistsByEmail(Email email){
        if(getUserByEmail(email) == null) return false;
        return true;
    }

    public void deleteUserById(int userID){
        if(getUserById(userID) == null) return;
        userRepository.delete(getUserById(userID));
    }
}
