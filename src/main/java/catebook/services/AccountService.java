
package catebook.services;

import catebook.modules.Account;
import catebook.repositories.AccountRepository;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    
    @Autowired
    private AccountRepository accountRepository;
    
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }
    
    public String getCurrentlyLoggedUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
    
    public boolean isOwnerOfThePage(String username) {
        return SecurityContextHolder.getContext().getAuthentication().getName().equals(username);
    }
    
    public Account getCurrentlyLoggedAccount() {
        return accountRepository.findByUsername(getCurrentlyLoggedUsername());
    }
    
    public Account getAccountWithUsername(String username) {
        return accountRepository.findByUsername(username);
    }
    
    public Account getAccountWithId(Long id) {
        return accountRepository.getOne(id);
    }
    
    public long getAmountOfAccounts() {
        return accountRepository.count();
    }
    
    public boolean isUsernameUsed(String username) {
        return accountRepository.findByProfileName(username) != null;
    }
    
    public void saveAccount(Account accountToSave) {
        accountRepository.save(accountToSave);
    }
    
    public boolean isProfilePhotoSetted(Account acc) {
        return acc.getProfilePhotoId() != -1L;
    }
    
    public boolean deletedPhotoWasProfilephoto(Account acc, Long photoToDeleteId) {
        return acc.getProfilePhotoId().equals(photoToDeleteId);
    }
    
    public List<String> getUsernamesFromFriends(String logged) {
        return accountRepository.findByUsername(logged).getFriends()
                .stream()
                .map(h -> h.getUsername())
                .collect(Collectors 
                .toCollection(ArrayList::new));
    }
    
        
    public List<Account> getUsersToViewOnSearch(String searchString) {
        return accountRepository.findAll()
                .stream()
                .filter(h -> h.getProfileName().toLowerCase().contains(searchString))
                .collect(Collectors 
                .toCollection(ArrayList::new)); 
    }
}
