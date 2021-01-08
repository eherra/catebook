
package catebook.services;

import catebook.modules.Account;
import catebook.repositories.AccountRepository;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@EnableCaching
public class AccountService {
    
    @Autowired
    private AccountRepository accountRepository;
    
    @Cacheable("accounts")
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }
   
    public Account getCurrentlyLoggedAccount() {
        return accountRepository.findByUsername(getCurrentlyLoggedUsername());
    }
    
    @Cacheable("accounts")
    public Account getAccountWithUsername(String username) {
        return accountRepository.findByUsername(username);
    }
    
    @Cacheable("accounts")
    public Account getAccountWithId(Long id) {
        return accountRepository.getOne(id);
    }
    
    @Cacheable("accounts")
    public long getAmountOfAccounts() {
        return accountRepository.count();
    }
    
    @CacheEvict(value = "accounts", allEntries = true)
    public void setProfilePhoto(Long photoId) {
        getCurrentlyLoggedAccount()
                .setProfilePhotoId(photoId);
    }
    
    @CacheEvict(value = "accounts", allEntries = true)
    public void acceptAndUpdateFriendLists(Long id) {
        Account currentlyLoggedAccount = getCurrentlyLoggedAccount();
        Account whoSentRequest = getAccountWithId(id);
        
        currentlyLoggedAccount.getFriendRequests().remove(whoSentRequest);
        currentlyLoggedAccount.getFriends().add(whoSentRequest);
        whoSentRequest.getFriends().add(currentlyLoggedAccount);
    }
    
    @CacheEvict(value = "accounts", allEntries = true)
    public void declineFriendRequestAndRemove(Long toRemoveAccountId) {
        getCurrentlyLoggedAccount()
                .getFriendRequests()
                .remove(getAccountWithId(toRemoveAccountId));
    }
    
    public boolean isUsernameUsed(String username) {
        return accountRepository.findByProfileName(username) != null;
    }
    
    @CacheEvict(value = "accounts", allEntries = true)
    public void saveAccount(Account accountToSave) {
        accountRepository.save(accountToSave);
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
    
    public boolean isProfilePhotoSetted(Account acc) {
        return acc.getProfilePhotoId() != -1L;
    }
    
    @CacheEvict(value = "accounts", allEntries = true)
    public boolean deletedPhotoWasProfilephoto(Account acc, Long photoToDeleteId) {
        return acc.getProfilePhotoId().equals(photoToDeleteId);
    }
    
    public boolean requestMakerIsAuthorized(String username) {
        return username.equals(getCurrentlyLoggedUsername());
    }
    
    public String getCurrentlyLoggedUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
    
    public boolean isOwnerOfThePage(String username) {
        return SecurityContextHolder.getContext().getAuthentication().getName().equals(username);
    }
}
