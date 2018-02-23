package com.youzan.pfcase.web.account;

import com.youzan.pfcase.domain.Account;
import com.youzan.pfcase.domain.UserDetails;
import com.youzan.pfcase.service.AccountService;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class AccountHelper {


    @Inject
    protected AccountService accountService;


    public void newAccount(AccountVo form) {
        Account account = new Account();
        BeanUtils.copyProperties(form, account);
        accountService.insertAccount(account);
    }

    public void editAccount(AccountVo form) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        Account account = userDetails.getAccount();

        // does not map "username" to use that of session object
//        beanMapper.map(form, account, "accountExcludeUsername");
        form.setUsername(account.getUsername());
        BeanUtils.copyProperties(form, account);
        accountService.updateAccount(account);

        // reflect new value to session object
//        beanMapper.map(accountService.getAccount(account.getUsername()), account);

    }
}
