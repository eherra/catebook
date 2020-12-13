
package catebook.services;

import catebook.objects.Account;
import catebook.repositories.AccountRepository;
import java.util.ArrayList;
import java.util.List;
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
    
    public List<String> getUsernamesFromFriends(String logged) {
        return accountRepository.findByUsername(logged).getFriends()
                .stream()
                .map(h -> h.getUsername())
                .collect(Collectors 
                .toCollection(ArrayList::new));
    }
}
